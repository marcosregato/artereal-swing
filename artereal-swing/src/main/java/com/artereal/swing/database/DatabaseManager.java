package com.artereal.swing.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Gerenciador do banco de dados PostgreSQL
 */
public class DatabaseManager {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static volatile DatabaseManager instance;
    
    // Connection pool para suportar concorrência
    private final BlockingQueue<Connection> connectionPool = new LinkedBlockingQueue<>(10);
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    
    // Configurações do banco
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    
    private DatabaseManager() {
        // Configurações PostgreSQL (podem vir de system properties)
        this.host = System.getProperty("db.host", "localhost");
        this.port = System.getProperty("db.port", "5432");
        this.database = System.getProperty("db.name", "artereal_db");
        this.username = System.getProperty("db.user", "postgres");
        this.password = System.getProperty("db.password", "postgres");
        
            }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicializa o banco de dados PostgreSQL com retry e delay
     */
    public void initializeDatabase() throws SQLException {
        if (initialized.get()) {
            logger.debug("Banco de dados já inicializado");
            return;
        }
        
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        logger.info("Inicializando banco de dados PostgreSQL: {}", url);
        
        // Limpa conexões pendentes antes de inicializar
        cleanupStaleConnections();
        
        int maxRetries = 3;
        int retryDelay = 2000; // 2 segundos
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Configurações de performance para PostgreSQL
                Properties props = new Properties();
                props.setProperty("user", username);
                props.setProperty("password", password);
                props.setProperty("ssl", "false");
                props.setProperty("prepareThreshold", "3");
                props.setProperty("preparedStatementCacheQueries", "256");
                props.setProperty("preparedStatementCacheSizeMiB", "5");
                props.setProperty("defaultRowFetchSize", "1000");
                
                Connection mainConnection = DriverManager.getConnection(url, props);
                mainConnection.setAutoCommit(false); // Melhor performance para transações
                
                // Adiciona conexão principal ao pool
                connectionPool.offer(mainConnection);
                
                // Cria tabelas e dados iniciais
                createTables();
                insertInitialData();
                
                initialized.set(true);
                logger.info("Banco de dados PostgreSQL inicializado com sucesso (tentativa {})", attempt);
                return; // Sucesso, sai do loop
                
            } catch (SQLException e) {
                logger.warn("Tentativa {} falhou: {}", attempt, e.getMessage());
                
                if (attempt == maxRetries) {
                    logger.error("Erro ao inicializar banco de dados após {} tentativas: {}", maxRetries, e.getMessage());
                    throw e;
                }
                
                // Limpa conexões novamente e espera antes da próxima tentativa
                cleanupStaleConnections();
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Inicialização interrompida", ie);
                }
            }
        }
    }
    
    /**
     * Limpa conexões pendentes no PostgreSQL
     */
    private void cleanupStaleConnections() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port + "/postgres", 
                username, password)) {
            
            try (Statement stmt = conn.createStatement()) {
                // Termina conexões idle por mais de 5 minutos que não são do psql
                String cleanupSql = """
                    SELECT pg_terminate_backend(pid) 
                    FROM pg_stat_activity 
                    WHERE pid <> pg_backend_pid() 
                    AND application_name <> 'psql'
                    AND state = 'idle'
                    AND query_start < now() - interval '5 minutes'
                    """;
                
                stmt.execute(cleanupSql);
                logger.debug("Limpeza de conexões pendentes executada");
            }
        } catch (SQLException e) {
            logger.warn("Não foi possível limpar conexões pendentes: {}", e.getMessage());
        }
    }
    
    /**
     * Obtém conexão do pool (connection pool para concorrência)
     */
    public Connection getConnection() throws SQLException {
        if (!initialized.get()) {
            initializeDatabase();
        }
        
        try {
            // Tenta obter conexão do pool
            Connection conn = connectionPool.poll();
            if (conn != null && !conn.isClosed()) {
                return conn;
            }
        } catch (Exception e) {
            logger.debug("Erro ao obter conexão do pool: {}", e.getMessage());
        }
        
        // Cria nova conexão se o pool estiver vazio
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("ssl", "false");
        props.setProperty("prepareThreshold", "3");
        
        Connection newConn = DriverManager.getConnection(url, props);
        newConn.setAutoCommit(true); // Auto-commit para concorrência
        return newConn;
    }
    
    /**
     * Devolve conexão ao pool para reutilização
     */
    public void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    // Limpa estado da conexão
                    conn.setAutoCommit(false);
                    // Devolve ao pool se não estiver cheio
                    if (!connectionPool.offer(conn)) {
                        // Pool cheio, fecha a conexão
                        conn.close();
                    }
                }
            } catch (SQLException e) {
                logger.debug("Erro ao devolver conexão ao pool: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Fecha todas as conexões do pool
     */
    public void closeConnection() throws SQLException {
        Connection conn;
        while ((conn = connectionPool.poll()) != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.debug("Erro ao fechar conexão do pool: {}", e.getMessage());
            }
        }
        logger.debug("Conexões do pool fechadas");
    }
    
    /**
     * Cria todas as tabelas do sistema com transação otimizada
     */
    private void createTables() throws SQLException {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("ssl", "false");
        props.setProperty("prepareThreshold", "3");
        
        Connection conn = DriverManager.getConnection(url, props);
        try (Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false); // Transação para melhor performance
            
            // Tabela de Irmãos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS irmao (
                    id SERIAL PRIMARY KEY,
                    nome TEXT NOT NULL,
                    nascimento TEXT,
                    estado_civil TEXT,
                    naturalidade TEXT,
                    identidade TEXT,
                    tipo_sanguineo TEXT,
                    cargo_loja TEXT,
                    grau TEXT,
                    endereco TEXT,
                    bairro TEXT,
                    cidade TEXT,
                    estado TEXT,
                    telefone TEXT,
                    empresa TEXT,
                    telefone_empresa TEXT,
                    endereco_empresa TEXT,
                    registro_grande_loja TEXT,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Lojas
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS loja (
                    id SERIAL PRIMARY KEY,
                    nome TEXT NOT NULL,
                    endereco TEXT,
                    bairro TEXT,
                    cidade TEXT,
                    estado TEXT,
                    cep TEXT,
                    telefone TEXT,
                    email TEXT,
                    presidente TEXT,
                    secretario TEXT,
                    tesoureiro TEXT,
                    data_fundacao TEXT,
                    rito TEXT,
                    potencia TEXT,
                    observacoes TEXT,
                    status TEXT DEFAULT 'ATIVA',
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Sessões
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS sessao (
                    id SERIAL PRIMARY KEY,
                    data TEXT NOT NULL,
                    tipo TEXT NOT NULL,
                    descricao TEXT,
                    pauta TEXT,
                    realizada INTEGER DEFAULT 0,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Caixa
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS caixa (
                    id SERIAL PRIMARY KEY,
                    data TEXT NOT NULL,
                    historico TEXT,
                    entrada REAL DEFAULT 0,
                    saida REAL DEFAULT 0,
                    saldo REAL DEFAULT 0,
                    grupo TEXT,
                    lancamento TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Biblioteca
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS biblioteca (
                    id SERIAL PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    assunto TEXT,
                    estoque INTEGER DEFAULT 1,
                    emprestados INTEGER DEFAULT 0,
                    autor TEXT,
                    grau TEXT,
                    isbn TEXT,
                    editora TEXT,
                    ano_publicacao INTEGER,
                    localizacao TEXT,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Empréstimos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS emprestimo (
                    id SERIAL PRIMARY KEY,
                    codigo_livro INTEGER NOT NULL,
                    codigo_irmao INTEGER NOT NULL,
                    data_emprestimo TEXT NOT NULL,
                    data_devolucao TEXT,
                    devolver INTEGER DEFAULT 0,
                    nome_irmao TEXT,
                    titulo TEXT,
                    multa REAL DEFAULT 0,
                    renovacoes INTEGER DEFAULT 0,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Contas a Pagar
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pagar (
                    id SERIAL PRIMARY KEY,
                    fatura TEXT,
                    emissao TEXT NOT NULL,
                    favorecido TEXT,
                    valor REAL NOT NULL,
                    vencimento TEXT NOT NULL,
                    modo TEXT,
                    banco TEXT,
                    pagamento TEXT,
                    valor_pago REAL DEFAULT 0,
                    numero_cheque TEXT,
                    codigo_fornecedor INTEGER,
                    historico TEXT,
                    situacao TEXT,
                    grupo TEXT,
                    lanc_c TEXT,
                    lanc_b TEXT,
                    pago INTEGER DEFAULT 0,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Usuários
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuario (
                    id SERIAL PRIMARY KEY,
                    nome TEXT NOT NULL,
                    senha TEXT NOT NULL,
                    administrador BOOLEAN DEFAULT FALSE,
                    acesso TEXT,
                    data_inicio TEXT,
                    contas TEXT,
                    lancamentos TEXT,
                    classes TEXT,
                    portaria INTEGER DEFAULT 0,
                    dados_usuario TEXT,
                    permissao_backup INTEGER DEFAULT 0,
                    permissao_restaura INTEGER DEFAULT 0,
                    diretorio_servico TEXT,
                    permissao_pagar INTEGER DEFAULT 1,
                    permissao_receber INTEGER DEFAULT 1,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Frequência
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS frequencia (
                    id SERIAL PRIMARY KEY,
                    codigo_irmao INTEGER NOT NULL,
                    nome_irmao TEXT NOT NULL,
                    registro_grande_loja TEXT,
                    numero_licenca TEXT,
                    data_licenca TEXT,
                    data_final_licenca TEXT,
                    grau TEXT,
                    data_instalado TEXT,
                    irregular INTEGER DEFAULT 0,
                    numero_presencas INTEGER DEFAULT 0,
                    numero_faltas INTEGER DEFAULT 0,
                    numero_secoes INTEGER DEFAULT 0,
                    nome_historico TEXT,
                    presenca_diretoria INTEGER DEFAULT 0,
                    secretaria_diretoria INTEGER DEFAULT 0,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Candidatos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS candidato (
                    id SERIAL PRIMARY KEY,
                    nome TEXT NOT NULL,
                    endereco TEXT,
                    numero TEXT,
                    cidade TEXT,
                    estado TEXT,
                    bairro TEXT,
                    fone_residencial TEXT,
                    data_nascimento TEXT,
                    idade INTEGER,
                    estado_civil TEXT,
                    esposa TEXT,
                    profissao TEXT,
                    funcao TEXT,
                    local_trabalho TEXT,
                    onde_exerce TEXT,
                    informacoes TEXT,
                    chanceler TEXT,
                    veneravel TEXT,
                    secretario TEXT,
                    linha_negra TEXT,
                    status TEXT DEFAULT 'CANDIDATO',
                    data_cadastro TEXT NOT NULL,
                    data_status TEXT,
                    observacoes TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Configurações Globais
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS configuracao (
                    id SERIAL PRIMARY KEY,
                    chave TEXT NOT NULL UNIQUE,
                    valor TEXT,
                    descricao TEXT,
                    tipo TEXT DEFAULT 'STRING',
                    categoria TEXT DEFAULT 'SISTEMA',
                    editavel INTEGER DEFAULT 1,
                    visivel INTEGER DEFAULT 1,
                    data_atualizacao TEXT DEFAULT CURRENT_TIMESTAMP,
                    usuario_atualizacao TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Cheques
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cheque (
                    id SERIAL PRIMARY KEY,
                    fatura TEXT,
                    data_emissao TEXT NOT NULL,
                    sacado TEXT NOT NULL,
                    valor REAL NOT NULL,
                    data_vencimento TEXT NOT NULL,
                    modo_pagamento TEXT,
                    banco TEXT,
                    data_pagamento TEXT,
                    valor_pago REAL DEFAULT 0,
                    codigo_cliente INTEGER,
                    situacao TEXT DEFAULT 'ABERTO',
                    grupo TEXT,
                    historico TEXT,
                    lancamento_credito TEXT,
                    lancamento_debito TEXT,
                    numero_nota TEXT,
                    codigo_vendedor INTEGER,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Documentos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS documento (
                    id SERIAL PRIMARY KEY,
                    codigo_irmao INTEGER,
                    nome_arquivo TEXT NOT NULL,
                    caminho_arquivo TEXT,
                    tipo TEXT DEFAULT 'OUTRO',
                    descricao TEXT,
                    data_upload TEXT DEFAULT CURRENT_TIMESTAMP,
                    data_expiracao TEXT,
                    status TEXT DEFAULT 'ATIVO',
                    assinatura_digital TEXT,
                    hash_arquivo TEXT,
                    tamanho_arquivo REAL DEFAULT 0,
                    formato_arquivo TEXT,
                    usuario_upload TEXT,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Visitantes
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS visitante (
                    id SERIAL PRIMARY KEY,
                    nome TEXT NOT NULL,
                    data_visita TEXT NOT NULL,
                    grau_secreto TEXT,
                    historico TEXT,
                    tipo TEXT DEFAULT 'VISITANTE',
                    loja_origem TEXT,
                    telefone TEXT,
                    email TEXT,
                    autorizado_por TEXT,
                    autorizado INTEGER DEFAULT 0,
                    observacoes TEXT,
                    numero_cracha TEXT,
                    data_cadastro TEXT DEFAULT CURRENT_DATE,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Afastamentos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS afastamento (
                    id SERIAL PRIMARY KEY,
                    codigo_irmao INTEGER NOT NULL,
                    data_inicial TEXT NOT NULL,
                    data_final TEXT NOT NULL,
                    descricao TEXT,
                    motivo TEXT DEFAULT 'OUTRO',
                    status TEXT DEFAULT 'ATIVO',
                    documento_comprobatorio TEXT,
                    data_cadastro TEXT DEFAULT CURRENT_DATE,
                    usuario_cadastro TEXT,
                    observacoes TEXT,
                    afeta_frequencia INTEGER DEFAULT 1,
                    dias_afastamento INTEGER DEFAULT 0,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Calendário Maçônico
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS calendario (
                    id SERIAL PRIMARY KEY,
                    descricao TEXT NOT NULL,
                    codigo_irmao INTEGER,
                    informe TEXT,
                    data_informe TEXT NOT NULL,
                    assinatura_informe TEXT,
                    tipo_evento TEXT DEFAULT 'CERIMONIA',
                    status TEXT DEFAULT 'PROGRAMADO',
                    local TEXT,
                    horario TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Fotos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS foto (
                    id SERIAL PRIMARY KEY,
                    titulo TEXT NOT NULL,
                    descricao TEXT,
                    caminho_arquivo TEXT,
                    nome_arquivo TEXT,
                    categoria TEXT DEFAULT 'OUTRA',
                    evento TEXT,
                    data_foto TEXT,
                    data_upload TEXT DEFAULT CURRENT_TIMESTAMP,
                    usuario_upload TEXT,
                    tags TEXT,
                    tamanho_arquivo REAL DEFAULT 0,
                    formato_arquivo TEXT,
                    ativo INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Despesas
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS despesas (
                    id SERIAL PRIMARY KEY,
                    descricao TEXT NOT NULL,
                    valor REAL NOT NULL,
                    data TEXT NOT NULL,
                    categoria TEXT DEFAULT 'Geral',
                    fornecedor TEXT DEFAULT 'Não informado',
                    numero_documento TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            conn.commit(); // Commit da transação
            logger.info("Tabelas criadas com sucesso (transação)");
        } catch (SQLException e) {
            conn.rollback(); // Rollback em caso de erro
            logger.error("Erro ao criar tabelas: {}", e.getMessage());
            throw e;
        } finally {
            // Single connection - não precisa devolver ao pool
        }
    }
    
    /**
     * Insere dados iniciais para demonstração com transação otimizada
     */
    private void insertInitialData() throws SQLException {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("ssl", "false");
        props.setProperty("prepareThreshold", "3");
        
        Connection conn = DriverManager.getConnection(url, props);
        try (Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false); // Transação para melhor performance
            
            // Configurações iniciais
            stmt.execute("""
            INSERT INTO configuracao (chave, valor, descricao, tipo, categoria) VALUES
                ('NOME_LOJA', 'Loja Simbólica ArteReal', 'Nome da loja', 'STRING', 'SISTEMA'),
                ('NUMERO_LOJA', '123', 'Número da loja', 'STRING', 'SISTEMA'),
                ('RITO', 'Escocês Antigo e Aceito', 'Rito maçônico', 'STRING', 'SISTEMA'),
                ('POTENCIA', 'Grande Oriente do Brasil', 'Potência maçônica', 'STRING', 'SISTEMA'),
                ('MOEDA_PADRAO', 'BRL', 'Moeda padrão do sistema', 'STRING', 'FINANCEIRO'),
                ('DIAS_AVISO_VENCIMENTO', '7', 'Dias para aviso de vencimento', 'NUMBER', 'FINANCEIRO'),
                ('PERCENTUAL_MINIMO_FREQUENCIA', '75', 'Percentual mínimo de frequência', 'NUMBER', 'SISTEMA'),
                ('BACKUP_AUTOMATICO', 'true', 'Backup automático habilitado', 'BOOLEAN', 'SISTEMA')
            ON CONFLICT (chave) DO NOTHING
            """);
            
            // Usuário administrador inicial
            stmt.execute("""
            DELETE FROM usuario WHERE nome = 'Administrador';
            INSERT INTO usuario (nome, senha, administrador, data_inicio, permissao_pagar, permissao_receber, permissao_backup) 
            VALUES ('Administrador', 'admin123', TRUE, CURRENT_TIMESTAMP, 1, 1, 1)
            """);
            
            conn.commit(); // Commit da transação
            logger.info("Dados iniciais inseridos com sucesso (transação)");
        } catch (SQLException e) {
            conn.rollback(); // Rollback em caso de erro
            logger.error("Erro ao inserir dados iniciais: {}", e.getMessage());
            throw e;
        } finally {
            // Single connection - não precisa devolver ao pool
        }
    }
    
    /**
     * Obtém estatísticas das conexões para monitoramento
     */
    public String getConnectionStats() {
        try {
            int poolSize = connectionPool.size();
            int activeConnections = 0;
            
            // Conta conexões ativas no pool
            for (Connection conn : connectionPool) {
                if (conn != null && !conn.isClosed()) {
                    activeConnections++;
                }
            }
            
            return String.format("Pool: %d/%d conexões, Ativas: %d, Inicializado: %s", 
                    poolSize, 10, activeConnections, initialized.get());
        } catch (Exception e) {
            return String.format("Pool: Erro - %s, Inicializado: %s", e.getMessage(), initialized.get());
        }
    }
}
