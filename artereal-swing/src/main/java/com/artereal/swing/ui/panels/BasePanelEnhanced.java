package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.patterns.factory.UIComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Classe base aprimorada para todos os Panels do sistema.
 * Implementa estrutura Header → Busca → Formulário → Tabela
 * Se não houver dados, exibe painel informativo com instruções
 */
public abstract class BasePanelEnhanced<T> extends JPanel {
    
    protected static final Logger logger = LoggerFactory.getLogger(BasePanelEnhanced.class);
    
    // Componentes comuns
    protected JTable dataTable;
    protected DefaultTableModel tableModel;
    protected JTextField pesquisarField;
    protected JButton pesquisarButton;
    protected JButton novoButton;
    protected JButton editarButton;
    protected JButton excluirButton;
    protected JButton salvarButton;
    protected JButton limparButton;
    
    // Layout components
    protected JPanel headerPanel;
    protected JPanel contentPanel;
    protected JPanel filterPanel;
    protected JPanel tablePanel;
    protected JPanel formPanel;
    protected JPanel emptyStatePanel; // Painel para quando não há dados
    
    // Estados da UI
    private enum UIState {
        LOADING, EMPTY, DATA, SEARCHING
    }
    
    private UIState currentState = UIState.EMPTY;
    
    /**
     * Construtor base
     */
    public BasePanelEnhanced() {
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    /**
     * Inicializa componentes comuns usando Factory Pattern
     */
    protected void initializeComponents() {
        // Aplicar layout padrão
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Inicializar tabela
        tableModel = createTableModel();
        dataTable = new JTable(tableModel);
        
        // Configurar tabela
        PadraoLayout.configurarTabela(dataTable);
        
        // Inicializar campos de busca usando Factory
        pesquisarField = UIComponentFactory.createTextField(20);
        
        // Inicializar botões usando Factory
        pesquisarButton = UIComponentFactory.createButton("🔍 Buscar", null);
        novoButton = UIComponentFactory.createButton("➕ Novo", null);
        editarButton = UIComponentFactory.createButton("✏️ Editar", null);
        excluirButton = UIComponentFactory.createButton("🗑️ Excluir", null);
        salvarButton = UIComponentFactory.createButton("💾 Salvar", null);
        limparButton = UIComponentFactory.createButton("🧹 Limpar", null);
        
        // Inicializar painéis
        headerPanel = createHeaderPanel();
        filterPanel = createFilterPanel();
        tablePanel = createTablePanel();
        formPanel = createFormPanel();
        contentPanel = createContentPanel();
        emptyStatePanel = createEmptyStatePanel();
    }
    
    /**
     * Configura o layout principal
     */
    protected void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header
        add(headerPanel, BorderLayout.NORTH);
        
        // Conteúdo principal
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura eventos comuns
     */
    protected void setupEvents() {
        // Evento de busca
        pesquisarButton.addActionListener(e -> performSearch());
        pesquisarField.addActionListener(e -> performSearch());
        
        // Eventos dos botões
        novoButton.addActionListener(e -> performNew());
        editarButton.addActionListener(e -> performEdit());
        excluirButton.addActionListener(e -> performDelete());
        salvarButton.addActionListener(e -> performSave());
        limparButton.addActionListener(e -> performClear());
        
        // Evento de seleção na tabela
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                performTableSelection();
            }
        });
        
        // Evento de Enter no campo de busca
        pesquisarField.addActionListener(e -> performSearch());
    }
    
    /**
     * Cria painel de header
     */
    protected JPanel createHeaderPanel() {
        return PadraoLayout.criarHeader(getHeaderTitle(), getHeaderDescription());
    }
    
    /**
     * Cria painel de filtros
     */
    protected JPanel createFilterPanel() {
        JPanel panel = UIComponentFactory.createPanel("🔍 Filtros de Busca");
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        panel.add(UIComponentFactory.createLabel("Buscar:"));
        panel.add(pesquisarField);
        panel.add(new JLabel()); // Espaço
        panel.add(pesquisarButton);
        
        return panel;
    }
    
    /**
     * Cria painel de tabela
     */
    protected JPanel createTablePanel() {
        JPanel panel = UIComponentFactory.createPanel("📊 Registros Encontrados");
        panel.setLayout(new BorderLayout());
        panel.add(UIComponentFactory.createScrollPane(dataTable), BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Cria painel de formulário
     */
    protected JPanel createFormPanel() {
        return UIComponentFactory.createPanel("📝 Formulário de Dados");
    }
    
    /**
     * Cria painel de conteúdo principal
     */
    protected JPanel createContentPanel() {
        JPanel mainContent = new JPanel(new BorderLayout());
        
        // Painel esquerdo: formulário
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Painel direito: tabela
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Divide o espaço
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.3); // 30% formulário, 70% tabela
        
        mainContent.add(splitPane, BorderLayout.CENTER);
        
        return mainContent;
    }
    
    /**
     * Cria painel para estado vazio com instruções detalhadas
     */
    protected JPanel createEmptyStatePanel() {
        JPanel panel = UIComponentFactory.createPanel("📋 Informações");
        panel.setLayout(new BorderLayout());
        
        // Título principal
        JLabel titleLabel = UIComponentFactory.createLabel("🔍 Nenhum registro encontrado");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        // Painel de instruções
        JPanel instructionsPanel = createInstructionsPanel();
        
        // Painel de ações rápidas
        JPanel actionsPanel = createQuickActionsPanel();
        
        // Organização vertical
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(instructionsPanel, BorderLayout.CENTER);
        centerPanel.add(actionsPanel, BorderLayout.SOUTH);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria painel de instruções detalhadas
     */
    protected JPanel createInstructionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String instructions = getInstructionsText();
        JTextArea instructionsArea = UIComponentFactory.createTextArea(6, 20);
        instructionsArea.setText(instructions);
        instructionsArea.setEditable(false);
        instructionsArea.setOpaque(false);
        instructionsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(UIComponentFactory.createLabel("📝 Instruções:"), BorderLayout.NORTH);
        panel.add(instructionsArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria painel de ações rápidas
     */
    protected JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Botão de novo registro
        JButton quickNewButton = UIComponentFactory.createButton("➕ Adicionar Novo", null);
        quickNewButton.addActionListener(e -> performNew());
        
        // Botão de importar (se aplicável)
        if (supportsImport()) {
            JButton importButton = UIComponentFactory.createButton("📥 Importar Dados", null);
            importButton.addActionListener(e -> performImport());
            panel.add(importButton);
        }
        
        panel.add(quickNewButton);
        
        return panel;
    }
    
    /**
     * Template Method para refresh de dados com estados
     */
    public final void refreshData() {
        setState(UIState.LOADING);
        
        try {
            // Carrega dados do DAO
            List<T> data = loadData();
            
            // Atualiza tabela
            updateTable(data);
            
            // Atualiza estado baseado nos dados
            if (data.isEmpty()) {
                setState(UIState.EMPTY);
            } else {
                setState(UIState.DATA);
            }
            
            logger.info("Dados atualizados: {} registros encontrados", data.size());
            
        } catch (Exception e) {
            setState(UIState.EMPTY);
            logger.error("Erro ao carregar dados", e);
            showError("Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    /**
     * Atualiza o estado da UI
     */
    protected void setState(UIState newState) {
        if (currentState != newState) {
            currentState = newState;
            onStateChanged(newState);
        }
    }
    
    /**
     * Chamado quando o estado da UI muda
     */
    protected void onStateChanged(UIState newState) {
        // Implementação padrão - pode ser sobrescrito
        logger.debug("Estado da UI alterado para: {}", newState);
        
        // Atualiza visibilidade dos componentes baseado no estado
        updateContentVisibility();
    }
    
    /**
     * Atualiza a visibilidade dos painéis baseado no estado atual
     */
    protected void updateContentVisibility() {
        // Remove todos os componentes
        contentPanel.removeAll();
        
        switch (currentState) {
            case LOADING:
                // Mostra painel de carregamento
                contentPanel.add(createLoadingPanel());
                break;
                
            case EMPTY:
                // Mostra painel vazio
                contentPanel.add(emptyStatePanel);
                break;
                
            case DATA:
            case SEARCHING:
                // Mostra conteúdo principal (formulário + tabela)
                contentPanel.add(createContentPanel());
                break;
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    /**
     * Cria painel de carregamento
     */
    protected JPanel createLoadingPanel() {
        JPanel panel = UIComponentFactory.createPanel("Carregando...");
        panel.setLayout(new BorderLayout());
        
        // Spinner de carregamento
        JSpinner spinner = new JSpinner();
        spinner.setEnabled(false);
        
        // Label de status
        JLabel loadingLabel = UIComponentFactory.createLabel("🔄 Carregando dados...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(loadingLabel, BorderLayout.CENTER);
        panel.add(spinner, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Atualiza a tabela com novos dados
     */
    protected void updateTable(List<T> data) {
        tableModel.setRowCount(0);
        
        for (T item : data) {
            Object[] row = mapEntityToRow(item);
            tableModel.addRow(row);
        }
    }
    
    /**
     * Mostra mensagem de erro
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Mostra mensagem de sucesso
     */
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método abstrato para busca de dados - deve ser implementado pelas subclasses
     * 
     * @param searchTerm Termo de busca
     * @return Lista de entidades filtradas
     */
    protected abstract List<T> searchData(String searchTerm) throws Exception;
    
    /**
     * Mostra mensagem de confirmação
     */
    protected boolean showConfirmation(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirmação", 
                                      JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    /**
     * Realiza seleção na tabela e carrega dados no formulário
     */
    protected void performTableSelection() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Lógica para carregar dados do item selecionado
            T selectedEntity = getEntityFromTable(selectedRow);
            if (selectedEntity != null) {
                loadEntityToForm(selectedEntity);
                setState(UIState.DATA);
                logger.info("Item selecionado: {}", selectedEntity.getClass().getSimpleName());
            }
        } else {
            clearForm();
            setState(UIState.EMPTY);
        }
    }
    
    /**
     * Obtém entidade da tabela a partir da linha selecionada
     * Deve ser implementado pelas subclasses
     */
    protected abstract T getEntityFromTable(int row);
    
    /**
     * Carrega dados da entidade no formulário
     * Deve ser implementado pelas subclasses
     */
    protected abstract void loadEntityToForm(T entity);
    
    /**
     * Limpa formulário (implementação padrão)
     */
    protected void clearForm() {
        // Implementação padrão vazia - pode ser sobrescrita
        logger.debug("Formulário limpo");
    }
    
        
    /**
     * Realiza busca com feedback visual
     */
    protected void performSearch() {
        String searchTerm = pesquisarField.getText().trim();
        if (!searchTerm.isEmpty()) {
            setState(UIState.SEARCHING);
            
            try {
                List<T> filteredData = searchData(searchTerm);
                updateTable(filteredData);
                
                if (filteredData.isEmpty()) {
                    setState(UIState.EMPTY);
                } else {
                    setState(UIState.DATA);
                }
                
                logger.info("Busca realizada: '{}' - {} resultados", searchTerm, filteredData.size());
            } catch (Exception e) {
                setState(UIState.EMPTY);
                logger.error("Erro na busca", e);
                showError("Erro na busca: " + e.getMessage());
            }
        }
    }
    
    // Métodos abstratos para serem implementados pelas subclasses
    
    /**
     * Carrega dados do DAO
     */
    protected abstract List<T> loadData() throws Exception;
    
    /**
     * Cria model para tabela
     */
    protected abstract DefaultTableModel createTableModel();
    
    /**
     * Mapeia entidade para linha da tabela
     */
    protected abstract Object[] mapEntityToRow(T entity);
    
    /**
     * Obtém título do header
     */
    protected abstract String getHeaderTitle();
    
    /**
     * Obtém descrição do header
     */
    protected abstract String getHeaderDescription();
    
    /**
     * Obtém texto de instruções personalizado
     */
    protected abstract String getInstructionsText();
    
    /**
     * Verifica se suporta importação de dados
     */
    protected boolean supportsImport() {
        return false; // Implementação padrão
    }
    
    // Métodos de ação (podem ser sobrescritos)
    
    /**
     * Ação de novo registro
     */
    protected void performNew() {
        clearForm();
        onNew();
    }
    
    /**
     * Ação de editar registro
     */
    protected void performEdit() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow >= 0) {
            onEdit(selectedRow);
        } else {
            showWarning("Selecione um registro para editar");
        }
    }
    
    /**
     * Ação de excluir registro
     */
    protected void performDelete() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow >= 0) {
            if (showConfirmation("Deseja realmente excluir este registro?")) {
                onDelete(selectedRow);
            }
        } else {
            showWarning("Selecione um registro para excluir");
        }
    }
    
    /**
     * Ação de salvar registro
     */
    protected void performSave() {
        onSave();
    }
    
    /**
     * Ação de limpar formulário
     */
    protected void performClear() {
        clearForm();
        onClear();
    }
    
    /**
     * Ação de importar dados (se suportado)
     */
    protected void performImport() {
        // Implementação padrão vazia
        logger.info("Importação de dados solicitada");
    }
    
    /**
     * Mostra mensagem de aviso
     */
    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    
    // Métodos de ciclo de vida (podem ser sobrescritos)
    
        
    // Métodos abstratos para ações (devem ser implementados)
    
    /**
     * Ação de novo registro
     */
    protected abstract void onNew();
    
    /**
     * Ação de editar registro
     */
    protected abstract void onEdit(int selectedRow);
    
    /**
     * Ação de excluir registro
     */
    protected abstract void onDelete(int selectedRow);
    
    /**
     * Ação de salvar registro
     */
    protected abstract void onSave();
    
    /**
     * Ação de limpar formulário
     */
    protected abstract void onClear();
}
