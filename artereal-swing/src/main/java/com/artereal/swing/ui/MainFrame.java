package com.artereal.swing.ui;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.ui.panels.*;
import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Janela principal do sistema ArteReal
 */
public class MainFrame extends JFrame {
    
    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
    private static final String TITLE = "ArteReal - Sistema de Gestão Maçônica";
    
    // Painéis de conteúdo (lazy initialization)
    private DashboardPanel dashboardPanel;
    private CadastroIrmaosPanel cadastroIrmaosPanel;
    private CandidatosPanel candidatosPanel; // Painel mais utilizado
    private UsuariosPanel usuariosPanel;
    private ConfiguracoesPanel configuracoesPanel;
    
    // Painéis menos utilizados - criados sob demanda
    private IrmaosPanel irmaosPanel;
    private LojasPanel lojasPanel;
    private SessoesPanel sessoesPanel;
    private CaixaPanel caixaPanel;
    private BibliotecaPanel bibliotecaPanel;
    private RelatoriosPanel relatoriosPanel;
    private FrequenciaPanel frequenciaPanel;
    private DespesasPanel despesasPanel;
    private VisitantesPanel visitantesPanel;
    private DocumentosPanel documentosPanel;
    private CalendarioPanel calendarioPanel;
    private AfastamentosPanel afastamentosPanel;
    private GaleriaFotosPanel galeriaFotosPanel;
    
    // Componentes da interface
    private JPanel contentPanel;
    private JLabel statusLabel;
    private JLabel userLabel;
    private JMenuBar menuBar;
    
    public MainFrame() {
        initializeComponents();
        setupLayout();
        setupEvents();
        setupMenuBar();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(TITLE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        
        // Inicia com o dashboard
        showDashboard();
    }
    
    /**
     * Inicializa os componentes da interface (otimizado para performance)
     */
    private void initializeComponents() {
        // Inicializar banco de dados uma vez só
        try {
            DatabaseManager.getInstance().initializeDatabase();
        } catch (SQLException e) {
            logger.error("Erro ao inicializar banco de dados: " + e.getMessage(), e);
        }
        
        // Painel principal de conteúdo
        contentPanel = new JPanel(new CardLayout());
        
        // Inicializa apenas o painel principal (Dashboard) - lazy initialization para outros
        dashboardPanel = new DashboardPanel();
        contentPanel.add(dashboardPanel, "DASHBOARD");
        
        // Barra de status
        statusLabel = new JLabel("Pronto");
        userLabel = new JLabel("Usuário: Administrador");
        
        logger.info("Componentes principais inicializados com sucesso");
    }
    
    /**
     * Configura o layout da janela
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Painel superior com informações do usuário
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topPanel.add(userLabel, BorderLayout.EAST);
        
        // Adiciona componentes à janela
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }
    
    /**
     * Cria a barra de status
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setPreferredSize(new Dimension(0, 25));
        
        JLabel versionLabel = new JLabel("ArteReal v1.0.0 - Swing + SQLite");
        versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        
        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.add(versionLabel, BorderLayout.WEST);
        
        return statusBar;
    }
    
    /**
     * Configura a barra de menus
     */
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        // Menu Cadastros
        JMenu cadastrosMenu = new JMenu("Cadastros");
        cadastrosMenu.setMnemonic('C');
        
        JMenuItem irmaosMenuItem = new JMenuItem("Irmãos");
        irmaosMenuItem.setMnemonic('I');
        irmaosMenuItem.addActionListener(e -> showIrmaos());
        
        JMenuItem lojasMenuItem = new JMenuItem("Lojas");
        lojasMenuItem.setMnemonic('L');
        lojasMenuItem.addActionListener(e -> showLojas());
        
        JMenuItem candidatosMenuItem = new JMenuItem("Candidatos");
        candidatosMenuItem.setMnemonic('C');
        candidatosMenuItem.addActionListener(e -> showCandidatos());
        
        JMenuItem visitantesMenuItem = new JMenuItem("Visitantes");
        visitantesMenuItem.setMnemonic('V');
        visitantesMenuItem.addActionListener(e -> showVisitantes());
        
        cadastrosMenu.add(irmaosMenuItem);
        cadastrosMenu.add(lojasMenuItem);
        cadastrosMenu.add(candidatosMenuItem);
        cadastrosMenu.addSeparator();
        cadastrosMenu.add(visitantesMenuItem);
        
        // Novo item de cadastro
        cadastrosMenu.addSeparator();
        
        JMenuItem cadastroIrmaosMenuItem = new JMenuItem("Cadastro de Irmãos");
        cadastroIrmaosMenuItem.setMnemonic('I');
        cadastroIrmaosMenuItem.addActionListener(e -> showCadastroIrmaos());
        
        cadastrosMenu.add(cadastroIrmaosMenuItem);
        
        // Menu Administrativo
        JMenu administrativoMenu = new JMenu("Administrativo");
        administrativoMenu.setMnemonic('A');
        
        JMenuItem sessoesMenuItem = new JMenuItem("Sessões");
        sessoesMenuItem.setMnemonic('S');
        sessoesMenuItem.addActionListener(e -> showSessoes());
        
        JMenuItem caixaMenuItem = new JMenuItem("Caixa");
        caixaMenuItem.setMnemonic('C');
        caixaMenuItem.addActionListener(e -> showCaixa());
        
        JMenuItem bibliotecaMenuItem = new JMenuItem("Biblioteca");
        bibliotecaMenuItem.setMnemonic('B');
        bibliotecaMenuItem.addActionListener(e -> showBiblioteca());
        
        JMenuItem frequenciaMenuItem = new JMenuItem("Frequência");
        frequenciaMenuItem.setMnemonic('F');
        frequenciaMenuItem.addActionListener(e -> showFrequencia());
        
        JMenuItem afastamentosMenuItem = new JMenuItem("Afastamentos");
        afastamentosMenuItem.setMnemonic('A');
        afastamentosMenuItem.addActionListener(e -> showAfastamentos());
        
        administrativoMenu.add(sessoesMenuItem);
        administrativoMenu.add(caixaMenuItem);
        administrativoMenu.add(bibliotecaMenuItem);
        administrativoMenu.add(frequenciaMenuItem);
        administrativoMenu.addSeparator();
        administrativoMenu.add(afastamentosMenuItem);
        
        // Menu Relatórios
        JMenu relatoriosMenu = new JMenu("Relatórios");
        relatoriosMenu.setMnemonic('R');
        
        JMenuItem gerarRelatoriosMenuItem = new JMenuItem("Gerar Relatórios");
        gerarRelatoriosMenuItem.setMnemonic('G');
        gerarRelatoriosMenuItem.addActionListener(e -> showRelatorios());
        
        relatoriosMenu.add(gerarRelatoriosMenuItem);
        
        // Menu Financeiro
        JMenu financeiroMenu = new JMenu("Financeiro");
        financeiroMenu.setMnemonic('F');
        
        JMenuItem despesasMenuItem = new JMenuItem("Despesas");
        despesasMenuItem.setMnemonic('D');
        despesasMenuItem.addActionListener(e -> showDespesas());
        
        financeiroMenu.add(despesasMenuItem);
        
        // Menu Documental
        JMenu documentalMenu = new JMenu("Documental");
        documentalMenu.setMnemonic('D');
        
        JMenuItem documentosMenuItem = new JMenuItem("Documentos");
        documentosMenuItem.setMnemonic('D');
        documentosMenuItem.addActionListener(e -> showDocumentos());
        
        documentalMenu.add(documentosMenuItem);
        
        JMenuItem galeriaFotosMenuItem = new JMenuItem("Galeria de Fotos");
        galeriaFotosMenuItem.setMnemonic('G');
        galeriaFotosMenuItem.addActionListener(e -> showGaleriaFotos());
        
        documentalMenu.add(galeriaFotosMenuItem);
        
        // Menu Sistema
        JMenu sistemaMenu = new JMenu("Sistema");
        sistemaMenu.setMnemonic('S');
        
        JMenuItem usuariosMenuItem = new JMenuItem("Usuários");
        usuariosMenuItem.setMnemonic('U');
        usuariosMenuItem.addActionListener(e -> showUsuarios());
        
        JMenuItem configuracoesMenuItem = new JMenuItem("Configurações");
        configuracoesMenuItem.setMnemonic('C');
        configuracoesMenuItem.addActionListener(e -> showConfiguracoes());
        
        JMenuItem calendarioMenuItem = new JMenuItem("Calendário");
        calendarioMenuItem.setMnemonic('L');
        calendarioMenuItem.addActionListener(e -> showCalendario());
        
        JMenuItem sairMenuItem = new JMenuItem("Sair");
        sairMenuItem.setMnemonic('S');
        sairMenuItem.addActionListener(e -> System.exit(0));
        
        sistemaMenu.add(usuariosMenuItem);
        sistemaMenu.addSeparator();
        sistemaMenu.add(configuracoesMenuItem);
        sistemaMenu.add(calendarioMenuItem);
        sistemaMenu.addSeparator();
        sistemaMenu.add(sairMenuItem);
        
        // Menu Ajuda
        JMenu ajudaMenu = new JMenu("Ajuda");
        ajudaMenu.setMnemonic('u');
        
        JMenuItem sobreMenuItem = new JMenuItem("Sobre");
        sobreMenuItem.setMnemonic('S');
        sobreMenuItem.addActionListener(e -> showAbout());
        
        ajudaMenu.add(sobreMenuItem);
        
        // Adiciona menus à barra
        menuBar.add(cadastrosMenu);
        menuBar.add(administrativoMenu);
        menuBar.add(financeiroMenu);
        menuBar.add(documentalMenu);
        menuBar.add(relatoriosMenu);
        menuBar.add(sistemaMenu);
        menuBar.add(ajudaMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Configura eventos
     */
    private void setupEvents() {
        // Pode ser expandido conforme necessário
    }
    
    /**
     * Métodos para navegação entre painéis
     */
    public void showDashboard() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DASHBOARD");
        setTitle(TITLE + " - Dashboard");
        statusLabel.setText("Dashboard");
        // Dashboard não precisa de refreshData() pois mostra dados estáticos
        // Se precisar de dados dinâmicos, implementar refreshDataAsync()
    }
    
    public void showIrmaos() {
        if (irmaosPanel == null) {
            logger.info("Criando IrmaosPanel (lazy initialization)");
            statusLabel.setText("Carregando Gestão de Irmãos...");
            
            // Criar painel em background para não bloquear a UI
            SwingUtilities.invokeLater(() -> {
                try {
                    irmaosPanel = new IrmaosPanel();
                    contentPanel.add(irmaosPanel, "IRMAOS");
                    
                    // Mudar para o painel após criado
                    CardLayout cl = (CardLayout) contentPanel.getLayout();
                    cl.show(contentPanel, "IRMAOS");
                    setTitle(TITLE + " - Gestão de Irmãos");
                    statusLabel.setText("Gestão de Irmãos");
                    
                    // Carregar dados de forma assíncrona
                    SwingUtilities.invokeLater(() -> {
                        irmaosPanel.refreshDataAsync();
                        logger.info("IrmaosPanel carregado com sucesso");
                    });
                    
                } catch (Exception e) {
                    logger.error("Erro ao criar IrmaosPanel: " + e.getMessage(), e);
                    statusLabel.setText("Erro ao carregar Gestão de Irmãos");
                }
            });
        } else {
            // Painel já existe, apenas mostrar e atualizar
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "IRMAOS");
            setTitle(TITLE + " - Gestão de Irmãos");
            statusLabel.setText("Gestão de Irmãos");
            
            // Atualizar dados de forma assíncrona
            SwingUtilities.invokeLater(() -> {
                irmaosPanel.refreshDataAsync();
            });
        }
    }
    
    public void showLojas() {
        if (lojasPanel == null) {
            lojasPanel = new LojasPanel();
            contentPanel.add(lojasPanel, "LOJAS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "LOJAS");
        setTitle(TITLE + " - Lojas");
        statusLabel.setText("Gestão de Lojas");
        lojasPanel.refreshData();
    }
    
    public void showSessoes() {
        if (sessoesPanel == null) {
            sessoesPanel = new SessoesPanel();
            contentPanel.add(sessoesPanel, "SESSOES");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "SESSOES");
        setTitle(TITLE + " - Sessões");
        statusLabel.setText("Gestão de Sessões");
        sessoesPanel.refreshData();
    }
    
    public void showCaixa() {
        if (caixaPanel == null) {
            caixaPanel = new CaixaPanel();
            contentPanel.add(caixaPanel, "CAIXA");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "CAIXA");
        setTitle(TITLE + " - Caixa");
        statusLabel.setText("Gestão Financeira");
        caixaPanel.refreshData();
    }
    
    public void showBiblioteca() {
        if (bibliotecaPanel == null) {
            bibliotecaPanel = new BibliotecaPanel();
            contentPanel.add(bibliotecaPanel, "BIBLIOTECA");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "BIBLIOTECA");
        setTitle(TITLE + " - Biblioteca");
        statusLabel.setText("Gestão de Biblioteca");
        bibliotecaPanel.refreshData();
    }
    
    public void showRelatorios() {
        if (relatoriosPanel == null) {
            relatoriosPanel = new RelatoriosPanel();
            contentPanel.add(relatoriosPanel, "RELATORIOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "RELATORIOS");
        setTitle(TITLE + " - Relatórios");
        statusLabel.setText("Relatórios");
        relatoriosPanel.refreshData();
    }
    
    public void showUsuarios() {
        if (usuariosPanel == null) {
            usuariosPanel = new UsuariosPanel();
            contentPanel.add(usuariosPanel, "USUARIOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "USUARIOS");
        setTitle(TITLE + " - Usuários");
        statusLabel.setText("Gestão de Usuários");
        usuariosPanel.refreshData();
    }
    
    public void showFrequencia() {
        if (frequenciaPanel == null) {
            frequenciaPanel = new FrequenciaPanel();
            contentPanel.add(frequenciaPanel, "FREQUENCIA");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "FREQUENCIA");
        setTitle(TITLE + " - Frequência");
        statusLabel.setText("Controle de Frequência");
        frequenciaPanel.refreshData();
    }
    
    public void showCandidatos() {
        if (candidatosPanel == null) {
            candidatosPanel = new CandidatosPanel();
            contentPanel.add(candidatosPanel, "CANDIDATOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "CANDIDATOS");
        setTitle(TITLE + " - Candidatos");
        statusLabel.setText("Gestão de Candidatos");
        candidatosPanel.refreshData();
    }
    
    public void showConfiguracoes() {
        if (configuracoesPanel == null) {
            configuracoesPanel = new ConfiguracoesPanel();
            contentPanel.add(configuracoesPanel, "CONFIGURACOES");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "CONFIGURACOES");
        setTitle(TITLE + " - Configurações");
        statusLabel.setText("Configurações Globais");
        configuracoesPanel.refreshData();
    }
    
    public void showDespesas() {
        if (despesasPanel == null) {
            logger.info("Criando DespesasPanel (lazy initialization)");
            statusLabel.setText("Carregando Gestão de Despesas...");
            despesasPanel = new DespesasPanel();
            contentPanel.add(despesasPanel, "DESPESAS");
        }
        
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DESPESAS");
        setTitle(TITLE + " - Despesas");
        statusLabel.setText("Gestão de Despesas");
        
        if (despesasPanel != null) {
            despesasPanel.refreshData();
        }
    }
    
    public void showVisitantes() {
        if (visitantesPanel == null) {
            visitantesPanel = new VisitantesPanel();
            contentPanel.add(visitantesPanel, "VISITANTES");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "VISITANTES");
        setTitle(TITLE + " - Visitantes");
        statusLabel.setText("Controle de Visitantes");
        visitantesPanel.refreshData();
    }
    
    public void showDocumentos() {
        if (documentosPanel == null) {
            documentosPanel = new DocumentosPanel();
            contentPanel.add(documentosPanel, "DOCUMENTOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DOCUMENTOS");
        setTitle(TITLE + " - Documentos");
        statusLabel.setText("Gestão Documental");
        documentosPanel.refreshData();
    }
    
    public void showCalendario() {
        if (calendarioPanel == null) {
            calendarioPanel = new CalendarioPanel();
            contentPanel.add(calendarioPanel, "CALENDARIO");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "CALENDARIO");
        setTitle(TITLE + " - Calendário");
        statusLabel.setText("Calendário Maçônico");
        calendarioPanel.refreshData();
    }
    
    public void showAfastamentos() {
        if (afastamentosPanel == null) {
            afastamentosPanel = new AfastamentosPanel();
            contentPanel.add(afastamentosPanel, "AFASTAMENTOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "AFASTAMENTOS");
        setTitle(TITLE + " - Afastamentos");
        statusLabel.setText("Gestão de Afastamentos");
        afastamentosPanel.refreshData();
    }
    
    public void showGaleriaFotos() {
        if (galeriaFotosPanel == null) {
            galeriaFotosPanel = new GaleriaFotosPanel();
            contentPanel.add(galeriaFotosPanel, "GALERIA_FOTOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "GALERIA_FOTOS");
        setTitle(TITLE + " - Galeria de Fotos");
        statusLabel.setText("Gestão de Fotos");
        galeriaFotosPanel.refreshData();
    }
    
    public void showCadastroIrmaos() {
        // Lazy initialization - cria o painel apenas quando necessário
        if (cadastroIrmaosPanel == null) {
            logger.info("Criando CadastroIrmaosPanel (lazy initialization)");
            statusLabel.setText("Carregando Cadastro de Irmãos...");
            
            // Criar painel em background para não bloquear a UI
            SwingUtilities.invokeLater(() -> {
                try {
                    cadastroIrmaosPanel = new CadastroIrmaosPanel();
                    contentPanel.add(cadastroIrmaosPanel, "CADASTRO_IRMAOS");
                    
                    // Mudar para o painel após criado
                    CardLayout cl = (CardLayout) contentPanel.getLayout();
                    cl.show(contentPanel, "CADASTRO_IRMAOS");
                    setTitle(TITLE + " - Cadastro de Irmãos");
                    statusLabel.setText("Cadastro de Irmãos Maçons");
                    
                    // Carregar dados de forma assíncrona
                    SwingUtilities.invokeLater(() -> {
                        cadastroIrmaosPanel.refreshData();
                        logger.info("CadastroIrmaosPanel carregado com sucesso");
                    });
                    
                } catch (Exception e) {
                    logger.error("Erro ao criar CadastroIrmaosPanel: " + e.getMessage(), e);
                    statusLabel.setText("Erro ao carregar Cadastro de Irmãos");
                }
            });
        } else {
            // Painel já existe, apenas mostrar e atualizar
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "CADASTRO_IRMAOS");
            setTitle(TITLE + " - Cadastro de Irmãos");
            statusLabel.setText("Cadastro de Irmãos Maçons");
            
            // Atualizar dados de forma assíncrona
            SwingUtilities.invokeLater(() -> {
                cadastroIrmaosPanel.refreshData();
            });
        }
    }
    
    /**
     * Exibe diálogo sobre
     */
    private void showAbout() {
        String aboutText = """
            <html>
            <center>
                <h2><font color='#2c3e50'>🏛️ ArteReal - Sistema de Gestão Maçônica</font></h2>
                <p><b><font color='#34495e' size='4'>Versão 2.0.0</font></b></p>
                <p><font color='#7f8c8d'>Sistema desktop completo para gestão administrativa de lojas maçônicas</font></p>
                <p><font color='#95a5a6' size='2'>Desenvolvido com Java 21, Swing e SQLite</font></p>
                <br>
                <p><b><font color='#2c3e50'>🔧 Tecnologias:</font></b></p>
                <p><font color='#34495e' size='2'>• Java 21 • Swing • SQLite • Maven</font></p>
                <p><font color='#34495e' size='2'>• Material Design • Layout Responsivo</font></p>
                <br>
                <p><b><font color='#2c3e50'>📋 Funcionalidades Principais:</font></b></p>
                <p><font color='#34495e' size='2'>• Gestão de Irmãos e Lojas Maçônicas</font></p>
                <p><font color='#34495e' size='2'>• Controle de Visitantes e Acesso</font></p>
                <p><font color='#34495e' size='2'>• Gestão Financeira e Cheques</font></p>
                <p><font color='#34495e' size='2'>• Documentos Digitais e Assinaturas</font></p>
                <p><font color='#34495e' size='2'>• Calendário Maçônico e Sessões</font></p>
                <p><font color='#34495e' size='2'>• Gestão de Afastamentos e Frequência</font></p>
                <p><font color='#34495e' size='2'>• Biblioteca e Empréstimos</font></p>
                <p><font color='#34495e' size='2'>• Candidatos e Profanos</font></p>
                <p><font color='#34495e' size='2'>• Configurações Globais</font></p>
                <br>
                <p><b><font color='#2c3e50'>🎯 Recursos Avançados:</font></b></p>
                <p><font color='#34495e' size='2'>• Interface Moderna com Material Design</font></p>
                <p><font color='#34495e' size='2'>• Relatórios Detalhados</font></p>
                <p><font color='#34495e' size='2'>• Backup Automático</font></p>
                <p><font color='#34495e' size='2'>• Segurança e Controle de Acesso</font></p>
                <br>
                <p><font color='#95a5a6' size='2'><b>© 2024 - ArteReal Masonic Lodge Management System</b></font></p>
                <p><font color='#bdc3c7' size='1'>Desenvolvido com ❤️ para a comunidade maçônica</font></p>
            </center>
            </html>
            """;
        
        JOptionPane.showMessageDialog(this, 
            aboutText, 
            "Sobre o ArteReal", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Define o usuário logado
     */
    public void setUsuarioLogado(Usuario usuario) {
        userLabel.setText("Usuário: " + usuario.getNome());
    }
}
