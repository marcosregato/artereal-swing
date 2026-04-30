package com.artereal.swing.ui;

import com.artereal.swing.ui.panels.*;
import com.artereal.swing.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Janela principal do sistema ArteReal
 */
public class MainFrame extends JFrame {
    
    private static final String TITLE = "ArteReal - Sistema de Gestão Maçônica";
    
    // Painéis de conteúdo
    private DashboardPanel dashboardPanel;
    private IrmaosPanel irmaosPanel;
    private LojasPanel lojasPanel;
    private SessoesPanel sessoesPanel;
    private CaixaPanel caixaPanel;
    private BibliotecaPanel bibliotecaPanel;
    private RelatoriosPanel relatoriosPanel;
    // Novos painéis
    private UsuariosPanel usuariosPanel;
    private FrequenciaPanel frequenciaPanel;
    private CandidatosPanel candidatosPanel;
    private ConfiguracoesPanel configuracoesPanel;
    private ChequesPanel chequesPanel;
    private VisitantesPanel visitantesPanel;
    private DocumentosPanel documentosPanel;
    private CalendarioPanel calendarioPanel;
    private AfastamentosPanel afastamentosPanel;
    private GaleriaFotosPanel galeriaFotosPanel;
    
    // Novo painel de cadastro
    private CadastroIrmaosPanel cadastroIrmaosPanel;
    
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
     * Inicializa os componentes da interface
     */
    private void initializeComponents() {
        // Painel principal de conteúdo
        contentPanel = new JPanel(new CardLayout());
        
        // Inicializa apenas o painel principal (Dashboard)
        dashboardPanel = new DashboardPanel();
        
        // Outros painéis serão criados sob demanda (lazy initialization)
        
        // Adiciona apenas o painel dashboard inicialmente
        contentPanel.add(dashboardPanel, "DASHBOARD");
        
        // Barra de status
        statusLabel = new JLabel("Pronto");
        userLabel = new JLabel("Usuário: Administrador");
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
        
        JMenuItem chequesMenuItem = new JMenuItem("Cheques");
        chequesMenuItem.setMnemonic('C');
        chequesMenuItem.addActionListener(e -> showCheques());
        
        financeiroMenu.add(chequesMenuItem);
        
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
        sairMenuItem.addActionListener(e -> sair());
        
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
        dashboardPanel.refreshData();
    }
    
    public void showIrmaos() {
        if (irmaosPanel == null) {
            irmaosPanel = new IrmaosPanel();
            contentPanel.add(irmaosPanel, "IRMAOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "IRMAOS");
        setTitle(TITLE + " - Gestão de Irmãos");
        statusLabel.setText("Gestão de Irmãos");
        irmaosPanel.refreshData();
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
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "RELATORIOS");
        setTitle(TITLE + " - Relatórios");
        statusLabel.setText("Relatórios");
        relatoriosPanel.refreshData();
    }
    
    public void showUsuarios() {
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
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "CONFIGURACOES");
        setTitle(TITLE + " - Configurações");
        statusLabel.setText("Configurações Globais");
        configuracoesPanel.refreshData();
    }
    
    public void showCheques() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "CHEQUES");
        setTitle(TITLE + " - Cheques");
        statusLabel.setText("Gestão de Cheques");
        chequesPanel.refreshData();
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
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DOCUMENTOS");
        setTitle(TITLE + " - Documentos");
        statusLabel.setText("Gestão Documental");
        documentosPanel.refreshData();
    }
    
    public void showCalendario() {
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
        if (cadastroIrmaosPanel == null) {
            cadastroIrmaosPanel = new CadastroIrmaosPanel();
            contentPanel.add(cadastroIrmaosPanel, "CADASTRO_IRMAOS");
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "CADASTRO_IRMAOS");
        setTitle(TITLE + " - Cadastro de Irmãos");
        statusLabel.setText("Cadastro de Irmãos Maçons");
        cadastroIrmaosPanel.refreshData();
    }
    
    /**
     * Exibe diálogo sobre
     */
    private void showAbout() {
        String aboutText = """
            <html>
            <center>
                <h2>ArteReal - Sistema de Gestão Maçônica</h2>
                <p>Versão 1.0.0</p>
                <p>Sistema desktop para gestão administrativa de lojas maçônicas</p>
                <p>Desenvolvido com Java Swing e SQLite</p>
                <br>
                <p><b>Tecnologias:</b></p>
                <p>Java 17 • Swing • SQLite</p>
                <br>
                <p><b>Funcionalidades:</b></p>
                <p>Gestão de Irmãos • Lojas • Sessões • Caixa • Biblioteca</p>
                <p>Usuários • Frequência • Candidatos</p>
                <br>
                <p><small>© 2024 - ArteReal Masonic Lodge Management System</small></p>
            </center>
            </html>
            """;
        
        JOptionPane.showMessageDialog(this, 
            aboutText, 
            "Sobre o ArteReal", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método para sair do sistema
     */
    private void sair() {
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente sair do sistema?", 
            "Sair", 
            JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    /**
     * Atualiza a barra de status
     */
    public void setStatus(String message) {
        statusLabel.setText(message);
    }
    
    /**
     * Define o usuário logado
     */
    public void setUsuarioLogado(Usuario usuario) {
        userLabel.setText("Usuário: " + usuario.getNome());
    }
}
