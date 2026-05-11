package com.artereal.swing.ui;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.ui.panels.*;
import com.artereal.swing.ui.panels.screens.*;
import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

/**
 * Janela principal otimizada do sistema ArteReal com UX moderna
 * Menu organizado com agrupamento lógico e painéis otimizados
 */
public class MainFrameEnhanced extends JFrame {
    
    private static final Logger logger = LoggerFactory.getLogger(MainFrameEnhanced.class);
    private static final String TITLE = "🏛️ ArteReal - Sistema de Gestão Maçônica v2.2.0";
    
    // Painéis otimizados (enhanced)
    private DashboardEnhancedPanel dashboardEnhancedPanel;
    private ConfiguracoesEnhancedPanel configuracoesEnhancedPanel;
    private RelatoriosEnhancedPanel relatoriosEnhancedPanel;
    
    // Painéis de formulários otimizados
    private com.artereal.swing.ui.panels.lojas.LojasFormPanel lojasFormPanel;
    private com.artereal.swing.ui.panels.irmaos.IrmaosFormPanel irmaosFormPanel;
    private com.artereal.swing.ui.panels.sessoes.SessoesFormPanel sessoesFormPanel;
    private com.artereal.swing.ui.panels.biblioteca.BibliotecaFormPanel bibliotecaFormPanel;
    
    // Painéis tradicionais (manter compatibilidade)
    private CadastroIrmaosPanel cadastroIrmaosPanel;
    private CandidatosPanel candidatosPanel;
    private UsuariosPanel usuariosPanel;
    private CaixaPanel caixaPanel;
    private FrequenciaPanel frequenciaPanel;
    private DespesasPanel despesasPanel;
    private VisitantesPanel visitantesPanel;
    private DocumentosPanel documentosPanel;
    private CalendarioPanel calendarioPanel;
    private AfastamentosPanel afastamentosPanel;
    private GaleriaFotosPanel galeriaFotosPanel;
    
    // Componentes da interface
    private JPanel contentPanel;
    private JPanel sidePanel;
    private JLabel statusLabel;
    private JLabel userLabel;
    private CardLayout cardLayout;
    
    public MainFrameEnhanced() {
        initializeComponents();
        setupLayout();
        setupMenu();
        loadInitialPanel();
    }
    
    /**
     * Inicializa componentes principais
     */
    private void initializeComponents() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        // Layout principal
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Painel lateral de navegação
        sidePanel = createSidePanel();
        
        // Status bar
        statusLabel = new JLabel("Pronto");
        userLabel = new JLabel("Usuário: Admin");
    }
    
    /**
     * Configura layout principal
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header com branding
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Conteúdo principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(sidePanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }
    
    /**
     * Cria painel header com branding
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(70, 130, 180));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Logo e título
        JLabel titleLabel = new JLabel("🏛️ ArteReal Masonic Lodge Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel("Sistema Integrado de Gestão Maçônica");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        // Painel de texto
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(new Color(70, 130, 180));
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        header.add(textPanel, BorderLayout.WEST);
        
        // Painel de informações do usuário
        JPanel userInfoPanel = createUserInfoPanel();
        header.add(userInfoPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Cria painel de informações do usuário
     */
    private JPanel createUserInfoPanel() {
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userInfo.setBackground(new Color(70, 130, 180));
        
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Sair");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        logoutButton.setBackground(new Color(200, 50, 50));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> logout());
        
        userInfo.add(userLabel);
        userInfo.add(logoutButton);
        
        return userInfo;
    }
    
    /**
     * Cria painel lateral de navegação
     */
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(new Color(245, 245, 245));
        sidePanel.setPreferredSize(new Dimension(250, 0));
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        
        // Menu lateral
        JPanel menuPanel = createSideMenu();
        sidePanel.add(menuPanel, BorderLayout.CENTER);
        
        return sidePanel;
    }
    
    /**
     * Cria menu lateral com agrupamento lógico
     */
    private JPanel createSideMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(245, 245, 245));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // GRUPO 1: PRINCIPAL
        menuPanel.add(createMenuSection("🏠 PRINCIPAL"));
        menuPanel.add(createMenuItem("📊 Dashboard", "dashboard", this::showDashboard));
        menuPanel.add(Box.createVerticalStrut(15));
        
        // GRUPO 2: GESTÃO DE MEMBROS
        menuPanel.add(createMenuSection("👥 GESTÃO DE MEMBROS"));
        menuPanel.add(createMenuItem("👤 Irmãos", "irmaos", this::showIrmaos));
        menuPanel.add(createMenuItem("📝 Cadastro", "cadastro", this::showCadastro));
        menuPanel.add(createMenuItem("🎯 Candidatos", "candidatos", this::showCandidatos));
        menuPanel.add(createMenuItem("👥 Visitantes", "visitantes", this::showVisitantes));
        menuPanel.add(createMenuItem("📅 Frequência", "frequencia", this::showFrequencia));
        menuPanel.add(createMenuItem("⏸️ Afastamentos", "afastamentos", this::showAfastamentos));
        menuPanel.add(Box.createVerticalStrut(15));
        
        // GRUPO 3: GESTÃO DE LOJA
        menuPanel.add(createMenuSection("🏢 GESTÃO DE LOJA"));
        menuPanel.add(createMenuItem("🏛️ Lojas", "lojas", this::showLojas));
        menuPanel.add(createMenuItem("📝 Sessões", "sessoes", this::showSessoes));
        menuPanel.add(createMenuItem("📚 Biblioteca", "biblioteca", this::showBiblioteca));
        menuPanel.add(createMenuItem("📄 Documentos", "documentos", this::showDocumentos));
        menuPanel.add(createMenuItem("📸 Galeria", "galeria", this::showGaleria));
        menuPanel.add(Box.createVerticalStrut(15));
        
        // GRUPO 4: FINANCEIRO
        menuPanel.add(createMenuSection("💰 FINANCEIRO"));
        menuPanel.add(createMenuItem("💳 Caixa", "caixa", this::showCaixa));
        menuPanel.add(createMenuItem("📊 Despesas", "despesas", this::showDespesas));
        menuPanel.add(Box.createVerticalStrut(15));
        
        // GRUPO 5: ADMINISTRAÇÃO
        menuPanel.add(createMenuSection("⚙️ ADMINISTRAÇÃO"));
        menuPanel.add(createMenuItem("👥 Usuários", "usuarios", this::showUsuarios));
        menuPanel.add(createMenuItem("⚙️ Configurações", "configuracoes", this::showConfiguracoes));
        menuPanel.add(createMenuItem("📊 Relatórios", "relatorios", this::showRelatorios));
        menuPanel.add(createMenuItem("📅 Calendário", "calendario", this::showCalendario));
        
        // Espaçamento final
        menuPanel.add(Box.createVerticalStrut(20));
        
        return menuPanel;
    }
    
    /**
     * Cria título de seção do menu
     */
    private JLabel createMenuSection(String title) {
        JLabel section = new JLabel(title);
        section.setFont(new Font("Segoe UI", Font.BOLD, 12));
        section.setForeground(new Color(70, 130, 180));
        section.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return section;
    }
    
    /**
     * Cria item do menu
     */
    private JButton createMenuItem(String text, String action, java.awt.event.ActionListener listener) {
        JButton item = new JButton(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.setBackground(new Color(245, 245, 245));
        item.setForeground(Color.DARK_GRAY);
        item.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setFocusPainted(false);
        item.setBorderPainted(false);
        item.setContentAreaFilled(false);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(230, 230, 250));
                item.setForeground(new Color(70, 130, 180));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(245, 245, 245));
                item.setForeground(Color.DARK_GRAY);
            }
        });
        
        item.addActionListener(listener);
        return item;
    }
    
    /**
     * Configura menu tradicional (mantido para compatibilidade)
     */
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu arquivoMenu = new JMenu("Arquivo");
        arquivoMenu.setMnemonic(KeyEvent.VK_A);
        
        JMenuItem novoItem = new JMenuItem("Novo", KeyEvent.VK_N);
        JMenuItem abrirItem = new JMenuItem("Abrir", KeyEvent.VK_A);
        JMenuItem salvarItem = new JMenuItem("Salvar", KeyEvent.VK_S);
        JMenuItem sairItem = new JMenuItem("Sair", KeyEvent.VK_R);
        
        arquivoMenu.add(novoItem);
        arquivoMenu.add(abrirItem);
        arquivoMenu.add(salvarItem);
        arquivoMenu.addSeparator();
        arquivoMenu.add(sairItem);
        
        // Menu Editar
        JMenu editarMenu = new JMenu("Editar");
        editarMenu.setMnemonic(KeyEvent.VK_E);
        
        JMenuItem copiarItem = new JMenuItem("Copiar", KeyEvent.VK_C);
        JMenuItem colarItem = new JMenuItem("Colar", KeyEvent.VK_V);
        
        editarMenu.add(copiarItem);
        editarMenu.add(colarItem);
        
        // Menu Ajuda
        JMenu ajudaMenu = new JMenu("Ajuda");
        ajudaMenu.setMnemonic(KeyEvent.VK_U);
        
        JMenuItem sobreItem = new JMenuItem("Sobre", KeyEvent.VK_S);
        JMenuItem ajudaItem = new JMenuItem("Ajuda", KeyEvent.VK_A);
        
        ajudaMenu.add(sobreItem);
        ajudaMenu.add(ajudaItem);
        
        menuBar.add(arquivoMenu);
        menuBar.add(editarMenu);
        menuBar.add(ajudaMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Configura barra de status
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(240, 240, 240));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        statusBar.setPreferredSize(new Dimension(0, 25));
        
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        
        JLabel versionLabel = new JLabel("Versão 2.2.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        versionLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(versionLabel, BorderLayout.EAST);
        
        return statusBar;
    }
    
    /**
     * Carrega painel inicial
     */
    private void loadInitialPanel() {
        showDashboard(null);
    }
    
    // Métodos de navegação
    private void showDashboard(ActionEvent e) {
        if (dashboardEnhancedPanel == null) {
            dashboardEnhancedPanel = new DashboardEnhancedPanel();
            contentPanel.add(dashboardEnhancedPanel, "dashboard");
        }
        cardLayout.show(contentPanel, "dashboard");
        statusLabel.setText("Dashboard - Visão geral do sistema");
    }
    
    private void showIrmaos(ActionEvent e) {
        if (irmaosFormPanel == null) {
            irmaosFormPanel = new com.artereal.swing.ui.panels.irmaos.IrmaosFormPanel();
            contentPanel.add(irmaosFormPanel, "irmaos");
        }
        cardLayout.show(contentPanel, "irmaos");
        statusLabel.setText("Gestão de Irmãos - Formulário otimizado");
    }
    
    private void showCadastro(ActionEvent e) {
        if (cadastroIrmaosPanel == null) {
            cadastroIrmaosPanel = new CadastroIrmaosPanel();
            contentPanel.add(cadastroIrmaosPanel, "cadastro");
        }
        cardLayout.show(contentPanel, "cadastro");
        statusLabel.setText("Cadastro de Irmãos - Formulário tradicional");
    }
    
    private void showCandidatos(ActionEvent e) {
        if (candidatosPanel == null) {
            candidatosPanel = new CandidatosPanel();
            contentPanel.add(candidatosPanel, "candidatos");
        }
        cardLayout.show(contentPanel, "candidatos");
        statusLabel.setText("Gestão de Candidatos");
    }
    
    private void showVisitantes(ActionEvent e) {
        if (visitantesPanel == null) {
            visitantesPanel = new VisitantesPanel();
            contentPanel.add(visitantesPanel, "visitantes");
        }
        cardLayout.show(contentPanel, "visitantes");
        statusLabel.setText("Gestão de Visitantes");
    }
    
    private void showFrequencia(ActionEvent e) {
        if (frequenciaPanel == null) {
            frequenciaPanel = new FrequenciaPanel();
            contentPanel.add(frequenciaPanel, "frequencia");
        }
        cardLayout.show(contentPanel, "frequencia");
        statusLabel.setText("Controle de Frequência");
    }
    
    private void showAfastamentos(ActionEvent e) {
        if (afastamentosPanel == null) {
            afastamentosPanel = new AfastamentosPanel();
            contentPanel.add(afastamentosPanel, "afastamentos");
        }
        cardLayout.show(contentPanel, "afastamentos");
        statusLabel.setText("Gestão de Afastamentos");
    }
    
    private void showLojas(ActionEvent e) {
        if (lojasFormPanel == null) {
            lojasFormPanel = new com.artereal.swing.ui.panels.lojas.LojasFormPanel();
            contentPanel.add(lojasFormPanel, "lojas");
        }
        cardLayout.show(contentPanel, "lojas");
        statusLabel.setText("Gestão de Lojas - Formulário otimizado");
    }
    
    private void showSessoes(ActionEvent e) {
        if (sessoesFormPanel == null) {
            sessoesFormPanel = new com.artereal.swing.ui.panels.sessoes.SessoesFormPanel();
            contentPanel.add(sessoesFormPanel, "sessoes");
        }
        cardLayout.show(contentPanel, "sessoes");
        statusLabel.setText("Gestão de Sessões - Formulário otimizado");
    }
    
    private void showBiblioteca(ActionEvent e) {
        if (bibliotecaFormPanel == null) {
            bibliotecaFormPanel = new com.artereal.swing.ui.panels.biblioteca.BibliotecaFormPanel();
            contentPanel.add(bibliotecaFormPanel, "biblioteca");
        }
        cardLayout.show(contentPanel, "biblioteca");
        statusLabel.setText("Gestão de Biblioteca - Formulário otimizado");
    }
    
    private void showDocumentos(ActionEvent e) {
        if (documentosPanel == null) {
            documentosPanel = new DocumentosPanel();
            contentPanel.add(documentosPanel, "documentos");
        }
        cardLayout.show(contentPanel, "documentos");
        statusLabel.setText("Gestão de Documentos");
    }
    
    private void showGaleria(ActionEvent e) {
        if (galeriaFotosPanel == null) {
            galeriaFotosPanel = new GaleriaFotosPanel();
            contentPanel.add(galeriaFotosPanel, "galeria");
        }
        cardLayout.show(contentPanel, "galeria");
        statusLabel.setText("Galeria de Fotos");
    }
    
    private void showCaixa(ActionEvent e) {
        if (caixaPanel == null) {
            caixaPanel = new CaixaPanel();
            contentPanel.add(caixaPanel, "caixa");
        }
        cardLayout.show(contentPanel, "caixa");
        statusLabel.setText("Gestão de Caixa");
    }
    
    private void showDespesas(ActionEvent e) {
        if (despesasPanel == null) {
            despesasPanel = new DespesasPanel();
            contentPanel.add(despesasPanel, "despesas");
        }
        cardLayout.show(contentPanel, "despesas");
        statusLabel.setText("Controle de Despesas");
    }
    
    private void showUsuarios(ActionEvent e) {
        if (usuariosPanel == null) {
            usuariosPanel = new UsuariosPanel();
            contentPanel.add(usuariosPanel, "usuarios");
        }
        cardLayout.show(contentPanel, "usuarios");
        statusLabel.setText("Gestão de Usuários");
    }
    
    private void showConfiguracoes(ActionEvent e) {
        if (configuracoesEnhancedPanel == null) {
            configuracoesEnhancedPanel = new ConfiguracoesEnhancedPanel();
            contentPanel.add(configuracoesEnhancedPanel, "configuracoes");
        }
        cardLayout.show(contentPanel, "configuracoes");
        statusLabel.setText("Configurações do Sistema - Tela otimizada");
    }
    
    private void showRelatorios(ActionEvent e) {
        if (relatoriosEnhancedPanel == null) {
            relatoriosEnhancedPanel = new RelatoriosEnhancedPanel();
            contentPanel.add(relatoriosEnhancedPanel, "relatorios");
        }
        cardLayout.show(contentPanel, "relatorios");
        statusLabel.setText("Relatórios - Tela otimizada");
    }
    
    private void showCalendario(ActionEvent e) {
        if (calendarioPanel == null) {
            calendarioPanel = new CalendarioPanel();
            contentPanel.add(calendarioPanel, "calendario");
        }
        cardLayout.show(contentPanel, "calendario");
        statusLabel.setText("Calendário de Atividades");
    }
    
    /**
     * Realiza logout
     */
    private void logout() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            // Aqui poderia abrir a tela de login novamente
            System.exit(0);
        }
    }
    
    /**
     * Exibe a janela
     */
    public void showFrame() {
        setVisible(true);
        logger.info("MainFrameEnhanced exibido com sucesso");
    }
}
