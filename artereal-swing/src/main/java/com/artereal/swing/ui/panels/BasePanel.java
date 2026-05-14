package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Classe base para todos os Panels do sistema.
 * Elimina código duplicado e fornece métodos comuns para operações de UI.
 * 
 * @param <T> Tipo da entidade gerenciada pelo panel
 */
public abstract class BasePanel<T> extends JPanel {
    
    protected static final Logger logger = LoggerFactory.getLogger(BasePanel.class);
    
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
    
    /**
     * Construtor base
     */
    public BasePanel() {
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    /**
     * Inicializa componentes comuns
     */
    protected void initializeComponents() {
        // Aplicar layout padrão
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Inicializar tabela
        tableModel = createTableModel();
        dataTable = new JTable(tableModel);
        
        // Configurar tabela
        PadraoLayout.configurarTabela(dataTable);
        
        // Inicializar campos de busca
        pesquisarField = new JTextField(20);
        PadraoLayout.estilizarCampoTexto(pesquisarField);
        
        // Inicializar botões
        pesquisarButton = PadraoLayout.criarBotao("🔍 Buscar", new Color(70, 130, 180));
        novoButton = PadraoLayout.criarBotao("➕ Novo", new Color(60, 179, 113));
        editarButton = PadraoLayout.criarBotao("✏️ Editar", new Color(255, 193, 7));
        excluirButton = PadraoLayout.criarBotao("🗑️ Excluir", new Color(220, 53, 69));
        salvarButton = PadraoLayout.criarBotao("💾 Salvar", new Color(40, 167, 69));
        limparButton = PadraoLayout.criarBotao("🧹 Limpar", new Color(108, 117, 125));
        
        // Inicializar painéis
        headerPanel = createHeaderPanel();
        filterPanel = createFilterPanel();
        tablePanel = createTablePanel();
        formPanel = createFormPanel();
        contentPanel = createContentPanel();
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
        JPanel panel = PadraoLayout.criarGrupoFormulario("🔍 Filtros de Busca");
        JPanel content = new JPanel(PadraoLayout.criarLayoutFormulario());
        content.setBackground(Color.WHITE);
        
        content.add(PadraoLayout.criarLabelFormulario("Buscar:"));
        content.add(pesquisarField);
        content.add(new JLabel()); // Espaço vazio
        content.add(pesquisarButton);
        
        panel.add(content);
        return panel;
    }
    
    /**
     * Cria painel de tabela
     */
    protected JPanel createTablePanel() {
        JPanel panel = PadraoLayout.criarGrupoFormulario("📊 Registros Encontrados");
        panel.setLayout(new BorderLayout());
        
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria painel de formulário
     */
    protected JPanel createFormPanel() {
        return PadraoLayout.criarGrupoFormulario("📝 Dados do Registro");
    }
    
    /**
     * Cria painel de conteúdo principal
     */
    protected JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filterPanel, BorderLayout.NORTH);
        topPanel.add(tablePanel, BorderLayout.CENTER);
        
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(formPanel);
        
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Atualiza dados da tabela
     */
    protected void refreshData() {
        try {
            List<T> data = loadData();
            updateTableModel(data);
            logger.debug("Dados atualizados: {} registros", data.size());
        } catch (Exception e) {
            logger.error("Erro ao atualizar dados", e);
            showError("Erro ao carregar dados: " + e.getMessage());
        }
    }
    
    /**
     * Realiza busca
     */
    protected void performSearch() {
        String searchTerm = pesquisarField.getText().trim();
        try {
            List<T> results = searchData(searchTerm);
            updateTableModel(results);
            logger.debug("Busca realizada: {} resultados", results.size());
        } catch (Exception e) {
            logger.error("Erro na busca", e);
            showError("Erro na busca: " + e.getMessage());
        }
    }
    
    /**
     * Realiza ação de novo registro
     */
    protected void performNew() {
        clearForm();
        enableForm(true);
        setFormMode(FormMode.INSERT);
        logger.debug("Modo de inserção ativado");
    }
    
    /**
     * Realiza ação de edição
     */
    protected void performEdit() {
        T selected = getSelectedEntity();
        if (selected != null) {
            loadEntityToForm(selected);
            enableForm(true);
            setFormMode(FormMode.UPDATE);
            logger.debug("Modo de edição ativado");
        } else {
            showWarning("Selecione um registro para editar");
        }
    }
    
    /**
     * Realiza ação de exclusão
     */
    protected void performDelete() {
        T selected = getSelectedEntity();
        if (selected != null) {
            int result = showConfirm("Deseja realmente excluir este registro?");
            if (result == JOptionPane.YES_OPTION) {
                try {
                    deleteEntity(selected);
                    refreshData();
                    clearForm();
                    showInfo("Registro excluído com sucesso");
                    logger.debug("Registro excluído com sucesso");
                } catch (Exception e) {
                    logger.error("Erro ao excluir", e);
                    showError("Erro ao excluir: " + e.getMessage());
                }
            }
        } else {
            showWarning("Selecione um registro para excluir");
        }
    }
    
    /**
     * Realiza ação de salvar
     */
    protected void performSave() {
        try {
            T entity = getEntityFromForm();
            validateEntity(entity);
            
            saveEntity(entity);
            refreshData();
            clearForm();
            enableForm(false);
            showInfo("Registro salvo com sucesso");
            logger.debug("Registro salvo com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao salvar", e);
            showError("Erro ao salvar: " + e.getMessage());
        }
    }
    
    /**
     * Realiza ação de limpar formulário
     */
    protected void performClear() {
        clearForm();
        enableForm(false);
        setFormMode(FormMode.VIEW);
        logger.debug("Formulário limpo");
    }
    
    /**
     * Realiza ação quando seleciona item na tabela
     */
    protected void performTableSelection() {
        T selected = getSelectedEntity();
        if (selected != null) {
            loadEntityToForm(selected);
            setFormMode(FormMode.VIEW);
        }
    }
    
    /**
     * Habilita/desabilita formulário
     */
    protected void enableForm(boolean enabled) {
        salvarButton.setEnabled(enabled);
        limparButton.setEnabled(enabled);
        enableFormFields(enabled);
    }
    
    /**
     * Limpa formulário
     */
    protected void clearForm() {
        clearFormFields();
    }
    
    /**
     * Define modo do formulário
     */
    protected void setFormMode(FormMode mode) {
        // Implementação específica em cada panel
    }
    
    // Métodos utilitários
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    
    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected int showConfirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirmação", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    
    // Métodos abstratos que devem ser implementados pelas classes filhas
    
    /**
     * Obtém título do header
     */
    protected abstract String getHeaderTitle();
    
    /**
     * Obtém descrição do header
     */
    protected abstract String getHeaderDescription();
    
    /**
     * Cria table model específico
     */
    protected abstract DefaultTableModel createTableModel();
    
    /**
     * Carrega dados do banco
     */
    protected abstract List<T> loadData() throws Exception;
    
    /**
     * Busca dados
     */
    protected abstract List<T> searchData(String searchTerm) throws Exception;
    
    /**
     * Obtém entidade selecionada
     */
    protected abstract T getSelectedEntity();
    
    /**
     * Carrega entidade no formulário
     */
    protected abstract void loadEntityToForm(T entity);
    
    /**
     * Obtém entidade do formulário
     */
    protected abstract T getEntityFromForm() throws Exception;
    
    /**
     * Valida entidade
     */
    protected abstract void validateEntity(T entity) throws Exception;
    
    /**
     * Salva entidade
     */
    protected abstract void saveEntity(T entity) throws Exception;
    
    /**
     * Exclui entidade
     */
    protected abstract void deleteEntity(T entity) throws Exception;
    
    /**
     * Atualiza table model
     */
    protected abstract void updateTableModel(List<T> data);
    
    /**
     * Limpa campos do formulário
     */
    protected abstract void clearFormFields();
    
    /**
     * Habilita/desabilita campos do formulário
     */
    protected abstract void enableFormFields(boolean enabled);
    
    /**
     * Modos do formulário
     */
    protected enum FormMode {
        VIEW, INSERT, UPDATE
    }
}
