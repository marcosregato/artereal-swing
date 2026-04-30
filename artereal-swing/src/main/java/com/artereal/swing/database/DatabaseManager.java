package com.artereal.swing.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Gerenciador do banco de dados SQLite - Versão Corrigida
 */
public class DatabaseManager {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {}
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Inicializa o banco de dados
     */
    public void initializeDatabase() throws SQLException {
        String dbPath = System.getProperty("user.home") + File.separator + ".artereal";
        File dbDir = new File(dbPath);
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        
        String dbFile = dbPath + File.separator + "artereal.db";
        String url = "jdbc:sqlite:" + dbFile;
        
        logger.info("Inicializando banco de dados SQLite: {}", dbFile);
        
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(false);
        
        createTables();
        insertInitialData();
        
        logger.info("Banco de dados inicializado com sucesso");
    }
    
    /**
     * Obtém conexão com o banco de dados
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeDatabase();
        }
        return connection;
    }
    
    /**
     * Fecha a conexão com o banco de dados
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.debug("Conexão com banco de dados fechada");
        }
    }
    
    /**
     * Cria todas as tabelas do sistema
     */
    private void createTables() throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            
            // Tabela de Irmãos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS irmao (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    nascimento TEXT,
                    estado_civil TEXT,
                    natural TEXT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    endereco TEXT,
                    bairro TEXT,
                    cidade TEXT,
                    estado TEXT,
                    telefone TEXT,
                    email TEXT,
                    rito TEXT,
                    dia_reuniao TEXT,
                    potencia TEXT,
                    numero_loja TEXT,
                    veneravel TEXT,
                    ativa INTEGER DEFAULT 1,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Sessões
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS sessao (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    senha TEXT NOT NULL,
                    administrador INTEGER DEFAULT 0,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
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
            
            // Tabela de Sessões
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS sessao (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    tipo TEXT NOT NULL,
                    data_hora TEXT NOT NULL,
                    local TEXT,
                    presidente TEXT,
                    secretario TEXT,
                    tesoureiro TEXT,
                    orador TEXT,
                    tema TEXT,
                    pauta TEXT,
                    observacoes TEXT,
                    status TEXT DEFAULT 'PROGRAMADA',
                    quantidade_presentes INTEGER DEFAULT 0,
                    quantidade_visitantes INTEGER DEFAULT 0,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Caixa
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS caixa (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    tipo TEXT NOT NULL,
                    categoria TEXT,
                    descricao TEXT NOT NULL,
                    valor REAL NOT NULL,
                    data_movimentacao TEXT NOT NULL,
                    responsavel TEXT,
                    forma_pagamento TEXT,
                    numero_documento TEXT,
                    status TEXT DEFAULT 'PENDENTE',
                    observacoes TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Biblioteca
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS biblioteca (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    tipo TEXT NOT NULL,
                    titulo TEXT NOT NULL,
                    autor TEXT,
                    isbn TEXT,
                    editora TEXT,
                    ano_publicacao TEXT,
                    categoria TEXT,
                    localizacao TEXT,
                    status TEXT DEFAULT 'DISPONIVEL',
                    nome_leitor TEXT,
                    data_emprestimo TEXT,
                    data_devolucao_prevista TEXT,
                    data_devolucao_real TEXT,
                    responsavel_emprestimo TEXT,
                    multa REAL DEFAULT 0,
                    observacoes TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Tabela de Lojas
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS loja (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    numero TEXT NOT NULL,
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
            
            connection.commit();
            logger.info("Tabelas criadas com sucesso");
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
    
    /**
     * Insere dados iniciais para demonstração
     */
    private void insertInitialData() throws SQLException {
        String[] inserts = {
            
            // Configurações iniciais
            """
            INSERT OR IGNORE INTO configuracao (chave, valor, descricao, tipo, categoria) VALUES
                ('NOME_LOJA', 'Loja Simbólica ArteReal', 'Nome da loja', 'STRING', 'SISTEMA'),
                ('NUMERO_LOJA', '123', 'Número da loja', 'STRING', 'SISTEMA'),
                ('RITO', 'Escocês Antigo e Aceito', 'Rito maçônico', 'STRING', 'SISTEMA'),
                ('POTENCIA', 'Grande Oriente do Brasil', 'Potência maçônica', 'STRING', 'SISTEMA'),
                ('MOEDA_PADRAO', 'BRL', 'Moeda padrão do sistema', 'STRING', 'FINANCEIRO'),
                ('DIAS_AVISO_VENCIMENTO', '7', 'Dias para aviso de vencimento', 'NUMBER', 'FINANCEIRO'),
                ('PERCENTUAL_MINIMO_FREQUENCIA', '75', 'Percentual mínimo de frequência', 'NUMBER', 'SISTEMA'),
                ('BACKUP_AUTOMATICO', 'true', 'Backup automático habilitado', 'BOOLEAN', 'SISTEMA')
            """,
            
            // Usuário administrador inicial
            """
            INSERT OR IGNORE INTO usuario (nome, senha, administrador, data_inicio, permissao_pagar, permissao_receber, permissao_backup)
            VALUES ('admin', 'admin123', 1, CURRENT_TIMESTAMP, 1, 1, 1)
            """,
            
            // Sessões iniciais
            """
            INSERT OR IGNORE INTO sessao (data, tipo, descricao, pauta, realizada)
            VALUES 
                ('2024-01-12', 'Sessão Magna', 'Sessão de inauguração do ano maçônico', 
                 'Abertura dos trabalhos, eleição da nova administração', 1),
                ('2024-01-26', 'Sessão Branca', 'Sessão de estudos maçônicos', 
                 'Estudos sobre simbolismo maçônico', 1)
            """,
            
            // Eventos calendário iniciais
            """
            INSERT OR IGNORE INTO calendario (descricao, data_informe, tipo_evento, status, local, horario) VALUES
                ('Sessão Magna de Inauguração', '2024-01-12', 'SESSAO_MAGNA', 'REALIZADO', 'Templo ArteReal', '20:00'),
                ('Sessão Branca de Estudos', '2024-01-26', 'SESSAO_BRANCA', 'REALIZADO', 'Templo ArteReal', '20:00'),
                ('Sessão de Eleição', '2024-12-05', 'SESSAO_ELEICAO', 'PROGRAMADO', 'Templo ArteReal', '20:00')
            """
        };
        
        try (Statement stmt = getConnection().createStatement()) {
            for (String sql : inserts) {
                stmt.execute(sql);
            }
            connection.commit();
            logger.info("Dados iniciais inseridos com sucesso");
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
    
    /**
     * Verifica se o banco de dados existe
     */
    public boolean databaseExists() {
        String dbPath = System.getProperty("user.home") + File.separator + ".artereal" + File.separator + "artereal.db";
        return new File(dbPath).exists();
    }
}
