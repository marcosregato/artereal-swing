package com.artereal.swing.ui.panels;

import com.artereal.swing.application.loja.LojaServiceFacade;
import com.artereal.swing.infrastructure.persistence.LojaJpaRepository;
import com.artereal.swing.infrastructure.config.ConfiguracaoBanco;
import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.lojas.LojasFormPanel;
import com.artereal.swing.ui.panels.lojas.LojasTablePanel;
import com.artereal.swing.ui.panels.lojas.LojasActionsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Painel principal de gestão de Lojas - Migrado para Arquitetura Hexagonal
 * Layout padrão Header → Busca → Formulário → Tabela
 * 
 * Arquitetura Hexagonal:
 * - LojaServiceFacade: Coordena use cases da camada application
 * - LojaJpaRepository: Adapter de persistência
 * - LojasFormPanel: Gerencia o formulário e seções
 * - LojasTablePanel: Gerencia a tabela de dados
 * - LojasActionsHandler: Gerencia eventos e lógica CRUD
 */
public class LojasPanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(LojasPanel.class);
    
    // Componentes principais
    private LojaServiceFacade lojaServiceFacade;
    private JTextField pesquisarField;
    private JButton pesquisarButton;
    
    // Componentes refatorados
    private LojasFormPanel formPanel;
    private LojasTablePanel tablePanel;
    private LojasActionsHandler actionsHandler;
    
    // Botões de ação
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    
    public LojasPanel() {
        // Inicializar repository e service facade
        LojaJpaRepository lojaRepository = new LojaJpaRepository(ConfiguracaoBanco.criarDataSource());
        lojaServiceFacade = new LojaServiceFacade(lojaRepository);
        
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
        
        logger.info("LojasPanel inicializado com arquitetura refatorada");
    }
    
    /**
     * Inicializa componentes principais
     */
    private void initializeComponents() {
        // Campo de busca
        pesquisarField = new JTextField(20);
        PadraoLayout.estilizarCampoTexto(pesquisarField);
        pesquisarField.setEditable(true);
        pesquisarField.setEnabled(true);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Componentes refatorados
        formPanel = new LojasFormPanel();
        tablePanel = new LojasTablePanel(lojaServiceFacade);
        actionsHandler = new LojasActionsHandler(lojaServiceFacade, formPanel, tablePanel);
        
        // Botões de ação
        initializeActionButtons();
    }
    
    /**
     * Inicializa botões de ação
     */
    private void initializeActionButtons() {
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        
        // Aplicar estilização
        PadraoLayout.aplicarCoresPastelBotoesPrincipais(
            salvarButton, novoButton, editarButton, excluirButton, limparButton);
    }
    
    /**
     * Configura layout principal
     */
    private void setupLayout() {
        // Aplicar estilização padrão
        PadraoLayout.estilizarPainelPrincipal(this);
        setLayout(new BorderLayout());
        
        // 1. Header
        JPanel headerPanel = PadraoLayout.criarHeader(
            "🏛️ Gestão de Lojas", 
            "Cadastro e administração de lojas maçônicas"
        );
        add(headerPanel, BorderLayout.NORTH);
        
        // 2. Painel principal
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Cria painel principal com busca, formulário e tabela
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // 1. Painel de busca
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // 2. Painel de conteúdo com formulário e tabela
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Cria painel de conteúdo com split vertical
     */
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Criar split vertical (60% formulário, 40% tabela)
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT, 
            createFormPanelWithButtons(), 
            tablePanel
        );
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.6);
        
        contentPanel.add(splitPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Cria painel de formulário com botões
     */
    private JPanel createFormPanelWithButtons() {
        JPanel formContainer = PadraoLayout.criarGrupoFormulario("📝 Dados da Loja");
        
        // Adicionar formulário
        formContainer.add(formPanel, BorderLayout.CENTER);
        
        // Adicionar painel de botões
        JPanel buttonsPanel = createButtonsPanel();
        formContainer.add(buttonsPanel, BorderLayout.SOUTH);
        
        return formContainer;
    }
    
    /**
     * Cria painel de botões organizado
     */
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.setBackground(Color.WHITE);
        
        // Adicionar botões em ordem lógica
        buttonsPanel.add(salvarButton);
        buttonsPanel.add(novoButton);
        buttonsPanel.add(editarButton);
        buttonsPanel.add(excluirButton);
        buttonsPanel.add(limparButton);
        
        return buttonsPanel;
    }
    
    /**
     * Configura eventos dos componentes
     */
    private void setupEvents() {
        // Eventos dos botões
        salvarButton.addActionListener(e -> {
            if (actionsHandler.validarFormulario()) {
                actionsHandler.salvarLoja();
            }
        });
        
        novoButton.addActionListener(e -> actionsHandler.limparFormulario());
        editarButton.addActionListener(e -> actionsHandler.carregarLojaSelecionada());
        excluirButton.addActionListener(e -> actionsHandler.excluirLoja());
        limparButton.addActionListener(e -> actionsHandler.limparFormulario());
        
        // Eventos de busca
        pesquisarButton.addActionListener(e -> 
            actionsHandler.pesquisarLojas(pesquisarField.getText().trim()));
        
        pesquisarField.addActionListener(e -> 
            actionsHandler.pesquisarLojas(pesquisarField.getText().trim()));
        
        logger.debug("Eventos configurados para LojasPanel refatorado");
    }
    
    /**
     * Atualiza dados da tabela
     */
    public void refreshData() {
        actionsHandler.refreshData();
    }
    
    /**
     * Obtém estatísticas das lojas
     */
    public String obterEstatisticas() {
        return actionsHandler.obterEstatisticas();
    }
    
    /**
     * Verifica se há alterações não salvas
     */
    public boolean temAlteracoesNaoSalvas() {
        return actionsHandler.temAlteracoesNaoSalvas();
    }
    
    /**
     * Limpa todos os campos
     */
    public void limparTudo() {
        pesquisarField.setText("");
        actionsHandler.limparFormulario();
    }
    
    /**
     * Foca no campo de busca
     */
    public void focarBusca() {
        pesquisarField.requestFocus();
    }
    
    /**
     * Obtém número de lojas na tabela
     */
    public int getQuantidadeLojas() {
        return tablePanel.getLojasCount();
    }
    
    /**
     * Verifica se há loja selecionada
     */
    public boolean temLojaSelecionada() {
        return tablePanel.hasSelection();
    }
    
    // Métodos getters para acesso aos componentes (se necessário)
    public LojasFormPanel getFormPanel() { return formPanel; }
    public LojasTablePanel getTablePanel() { return tablePanel; }
    public LojasActionsHandler getActionsHandler() { return actionsHandler; }
    public JTextField getPesquisarField() { return pesquisarField; }
    
    /**
     * Aplica máscara de CEP (mantida para compatibilidade)
     */
    @SuppressWarnings("unused")
    private void aplicarMascaraCEP(JTextField cepField) {
        // A máscara de CEP agora é tratada no PadraoLayout
        PadraoLayout.estilizarCampoCEP(cepField);
    }
}
