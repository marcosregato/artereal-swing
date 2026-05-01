package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;
import com.artereal.swing.ui.layout.PadraoLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel para gestão de candidatos e profanos
 * Layout padrão: Header → Busca → Formulário → Tabela
 */
public class CadastroIrmaosPanel extends BasePanel<Irmao> {
    private static final Logger logger = LoggerFactory.getLogger(CadastroIrmaosPanel.class);
    
    private final IrmaoDAO irmaoDAO;
    
    // Componentes do formulário
    private JTextField nomeField;
    private JTextField nascimentoField;
    private JTextField estadoCivilField;
    private JTextField naturalField;
    private JTextField identidadeField;
    private JTextField tipoSanguineoField;
    private JTextField cargoLojaField;
    private JTextField grauField;
    private JTextField cargoGrandeLojaField;
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField telefoneField;
    private JTextField empresaField;
    private JTextField telefoneEmpresaField;
    private JTextField enderecoEmpresaField;
    private JTextField registroGrandeLojaField;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton limparButton;
    private JButton pesquisarButton;
    private JTextField pesquisarField;
    
    // Tabela
    private JTable irmaosTable;
    private DefaultTableModel tableModel;
    
    // Estado
    private Irmao irmaoAtual;
    
    public CadastroIrmaosPanel() {
        super(); // Chamar construtor da classe base
        this.irmaoDAO = new IrmaoDAO();
        // Não chamar initializeComponents() novamente, pois já foi chamado no super()
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    protected void initializeComponents() {
        // Campos do formulário
        nomeField = new JTextField(30);
        nascimentoField = new JTextField(15);
        estadoCivilField = new JTextField(15);
        naturalField = new JTextField(20);
        identidadeField = new JTextField(15);
        tipoSanguineoField = new JTextField(10);
        cargoLojaField = new JTextField(20);
        grauField = new JTextField(15);
        cargoGrandeLojaField = new JTextField(20);
        enderecoField = new JTextField(40);
        bairroField = new JTextField(20);
        cidadeField = new JTextField(20);
        estadoField = new JTextField(10);
        telefoneField = new JTextField(15);
        empresaField = new JTextField(30);
        telefoneEmpresaField = new JTextField(15);
        enderecoEmpresaField = new JTextField(40);
        registroGrandeLojaField = new JTextField(20);
        
        // Botões usando PadraoLayout
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarField = new JTextField(20);
        
        // Tabela
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Data Nascimento", "Estado Civil", "Telefone", "Cargo"},
            0
        );
        irmaosTable = new JTable(tableModel);
    }
    
    protected void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("👥 Gestão de Candidato e Profano", "Cadastro e administração de candidatos e profanos");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = createFormPanel();
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = createTablePanel();
        
        // Split vertical: Formulário acima (60%), Tabela abaixo (40%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(400);
        verticalSplitPane.setResizeWeight(0.6);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    @Override
    protected JPanel createFormPanel() {
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados do Irmão");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoNome(nomeField); // Usando método específico
        dadosBasicosContent.add(nomeField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Data Nascimento:"));
        PadraoLayout.estilizarCampoData(nascimentoField); // Usando método otimizado para data
        dadosBasicosContent.add(nascimentoField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Estado Civil:"));
        PadraoLayout.estilizarCampoEstadoCivil(estadoCivilField); // Usando método específico
        dadosBasicosContent.add(estadoCivilField, PadraoLayout.criarConstraintsFormulario(2, 1));
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Naturalidade:"));
        PadraoLayout.estilizarCampoNaturalidade(naturalField); // Usando método específico
        dadosBasicosContent.add(naturalField, PadraoLayout.criarConstraintsFormulario(3, 1));
        
        dadosBasicosPanel.add(dadosBasicosContent, BorderLayout.CENTER);
        
        // Grupo 2: Documentação
        JPanel docsPanel = createFormGroup("📄 Documentação");
        JPanel docsContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        docsContent.setBackground(Color.WHITE);
        
        docsContent.add(PadraoLayout.criarLabelFormulario("Identidade:"));
        PadraoLayout.estilizarCampoRG(identidadeField); // Usando método específico
        docsContent.add(identidadeField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        docsContent.add(PadraoLayout.criarLabelFormulario("Tipo Sanguíneo:"));
        PadraoLayout.estilizarCampoTipoSanguineo(tipoSanguineoField); // Usando método específico
        docsContent.add(tipoSanguineoField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        docsContent.add(PadraoLayout.criarLabelFormulario("Registro Grande Loja:"));
        PadraoLayout.estilizarCampoRegistroMaconico(registroGrandeLojaField); // Usando método específico
        docsContent.add(registroGrandeLojaField, PadraoLayout.criarConstraintsFormulario(2, 1));
        
        docsPanel.add(docsContent, BorderLayout.CENTER);
        
        // Grupo 3: Contato
        JPanel contatoPanel = createFormGroup("📞 Contato");
        JPanel contatoContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        contatoContent.setBackground(Color.WHITE);
        
        contatoContent.add(PadraoLayout.criarLabelFormulario("Telefone:"));
        PadraoLayout.estilizarCampoTelefone(telefoneField); // Usando método específico
        contatoContent.add(telefoneField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        contatoContent.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoEndereco(enderecoField); // Usando método específico
        contatoContent.add(enderecoField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        contatoContent.add(PadraoLayout.criarLabelFormulario("Bairro:"));
        PadraoLayout.estilizarCampoBairro(bairroField); // Usando método específico
        contatoContent.add(bairroField, PadraoLayout.criarConstraintsFormulario(2, 1));
        
        contatoContent.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoCidade(cidadeField); // Usando método específico
        contatoContent.add(cidadeField, PadraoLayout.criarConstraintsFormulario(3, 1));
        
        contatoContent.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoEstado(estadoField); // Usando método específico
        contatoContent.add(estadoField, PadraoLayout.criarConstraintsFormulario(4, 1));
        
        contatoPanel.add(contatoContent, BorderLayout.CENTER);
        
        // Grupo 4: Informações Profissionais
        JPanel profPanel = createFormGroup("💼 Informações Profissionais");
        JPanel profContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        profContent.setBackground(Color.WHITE);
        
        profContent.add(PadraoLayout.criarLabelFormulario("Empresa:"));
        PadraoLayout.estilizarCampoNome(empresaField); // Usando método específico
        profContent.add(empresaField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        profContent.add(PadraoLayout.criarLabelFormulario("Telefone Empresa:"));
        PadraoLayout.estilizarCampoTelefone(telefoneEmpresaField); // Usando método específico
        profContent.add(telefoneEmpresaField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        profContent.add(PadraoLayout.criarLabelFormulario("Endereço Empresa:"));
        PadraoLayout.estilizarCampoEndereco(enderecoEmpresaField); // Usando método específico
        profContent.add(enderecoEmpresaField, PadraoLayout.criarConstraintsFormulario(2, 1));
        
        profPanel.add(profContent, BorderLayout.CENTER);
        
        // Grupo 5: Informações Maçônicas
        JPanel masonPanel = createFormGroup("🔷 Informações Maçônicas");
        JPanel masonContent = new JPanel(PadraoLayout.criarLayoutFormularioAlinhado());
        masonContent.setBackground(Color.WHITE);
        
        masonContent.add(PadraoLayout.criarLabelFormulario("Cargo na Loja:"));
        PadraoLayout.estilizarCampoCargoMaconico(cargoLojaField); // Usando método específico
        masonContent.add(cargoLojaField, PadraoLayout.criarConstraintsFormulario(0, 1));
        
        masonContent.add(PadraoLayout.criarLabelFormulario("Grau:"));
        PadraoLayout.estilizarCampoGrauMaconico(grauField); // Usando método específico
        masonContent.add(grauField, PadraoLayout.criarConstraintsFormulario(1, 1));
        
        masonContent.add(PadraoLayout.criarLabelFormulario("Cargo Grande Loja:"));
        PadraoLayout.estilizarCampoCargoMaconico(cargoGrandeLojaField); // Usando método específico
        masonContent.add(cargoGrandeLojaField, PadraoLayout.criarConstraintsFormulario(2, 1));
        
        masonPanel.add(masonContent, BorderLayout.CENTER);
        
        // Organizar todos os grupos verticalmente
        JPanel allGroups = new JPanel(new BorderLayout());
        allGroups.setBackground(Color.WHITE);
        
        JPanel topGroups = new JPanel(new BorderLayout());
        topGroups.setBackground(Color.WHITE);
        topGroups.add(dadosBasicosPanel, BorderLayout.NORTH);
        topGroups.add(docsPanel, BorderLayout.CENTER);
        
        JPanel bottomGroups = new JPanel(new BorderLayout());
        bottomGroups.setBackground(Color.WHITE);
        bottomGroups.add(contatoPanel, BorderLayout.NORTH);
        
        JPanel profMasonGroups = new JPanel(new BorderLayout());
        profMasonGroups.setBackground(Color.WHITE);
        profMasonGroups.add(profPanel, BorderLayout.NORTH);
        profMasonGroups.add(masonPanel, BorderLayout.CENTER);
        
        bottomGroups.add(profMasonGroups, BorderLayout.CENTER);
        
        allGroups.add(topGroups, BorderLayout.NORTH);
        allGroups.add(bottomGroups, BorderLayout.CENTER);
        
        // Adicionar scroll ao formulário
        JScrollPane formScroll = new JScrollPane(allGroups);
        formScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(limparButton);
        
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        return formPanel;
    }
    
    @Override
    protected JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Split vertical: Formulário acima (70%), Tabela abaixo (30%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tablePanel);
        verticalSplitPane.setDividerLocation(400);
        verticalSplitPane.setResizeWeight(0.7);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private JPanel createFormGroup(String title) {
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBackground(Color.WHITE);
        groupPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        groupPanel.add(titleLabel, BorderLayout.NORTH);
        return groupPanel;
    }
    
    protected void setupEvents() {
        // Usando métodos padrão da BasePanel
        salvarButton.addActionListener(e -> performSave());
        novoButton.addActionListener(e -> performNew());
        editarButton.addActionListener(e -> performEdit());
        excluirButton.addActionListener(e -> performDelete());
        limparButton.addActionListener(e -> performClear());
        pesquisarButton.addActionListener(e -> performSearch());
        
        // Seleção na tabela
        irmaosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarIrmaoSelecionado();
            }
        });
    }
    
    public void refreshData() {
        try {
            List<Irmao> irmaos = irmaoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Irmao irmao : irmaos) {
                Object[] row = {
                    irmao.getId(),
                    irmao.getNome(),
                    irmao.getNascimento() != null ? irmao.getNascimento().toString() : "",
                    irmao.getEstadoCivil(),
                    irmao.getTelefone(),
                    irmao.getCargoLoja()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            logger.error("Erro ao carregar dados: " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        
    private void editarIrmao() {
        if (irmaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Carregar dados do irmão selecionado para o formulário
        nomeField.setText(irmaoAtual.getNome());
        nascimentoField.setText(irmaoAtual.getNascimento() != null ? irmaoAtual.getNascimento().toString() : "");
        estadoCivilField.setText(irmaoAtual.getEstadoCivil());
        naturalField.setText(irmaoAtual.getNatural());
        identidadeField.setText(irmaoAtual.getIdentidade());
        tipoSanguineoField.setText(irmaoAtual.getTipoSanguineo());
        cargoLojaField.setText(irmaoAtual.getCargoLoja());
        grauField.setText(irmaoAtual.getGrau());
        cargoGrandeLojaField.setText(irmaoAtual.getCargoGrandeLoja());
        enderecoField.setText(irmaoAtual.getEndereco());
        bairroField.setText(irmaoAtual.getBairro());
        cidadeField.setText(irmaoAtual.getCidade());
        estadoField.setText(irmaoAtual.getEstado());
        telefoneField.setText(irmaoAtual.getTelefone());
        empresaField.setText(irmaoAtual.getEmpresa());
        telefoneEmpresaField.setText(irmaoAtual.getTelefoneEmpresa());
        enderecoEmpresaField.setText(irmaoAtual.getEnderecoEmpresa());
        registroGrandeLojaField.setText(irmaoAtual.getRegistroGrandeLoja());
    }
    
        
    private void carregarIrmaoSelecionado() {
        int selectedRow = irmaosTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                irmaoAtual = irmaoDAO.findById(id);
                if (irmaoAtual != null) {
                    editarIrmao();
                }
            } catch (Exception ex) {
                logger.error("Erro ao carregar irmão selecionado: " + ex.getMessage(), ex);
            }
        }
    }
    
    // Implementação dos métodos abstratos da BasePanel
    
    @Override
    protected String getHeaderTitle() {
        return "👥 Gestão de Candidatos e Profanos";
    }
    
    @Override
    protected String getHeaderDescription() {
        return "Novos cadastros e admissões maçônicas";
    }
    
    @Override
    protected DefaultTableModel createTableModel() {
        return new DefaultTableModel(
            new Object[]{"ID", "Nome", "Data Nascimento", "Estado Civil", "Telefone", "Cargo"},
            0
        );
    }
    
    @Override
    protected List<Irmao> loadData() throws Exception {
        return irmaoDAO.findAll();
    }
    
    @Override
    protected List<Irmao> searchData(String searchTerm) throws Exception {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return irmaoDAO.findAll();
        }
        return irmaoDAO.findByNome(searchTerm.trim());
    }
    
    @Override
    protected JPanel createTablePanel() {
        JPanel panel = PadraoLayout.criarGrupoFormulario("👥 Candidatos e Profanos Cadastrados");
        panel.setLayout(new BorderLayout());
        
        PadraoLayout.configurarTabela(irmaosTable);
        JScrollPane scrollPane = new JScrollPane(irmaosTable);
        scrollPane.setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    @Override
    protected Irmao getSelectedEntity() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) dataTable.getValueAt(selectedRow, 0);
                return irmaoDAO.findById(id);
            } catch (Exception ex) {
                logger.error("Erro ao carregar irmão selecionado: " + ex.getMessage(), ex);
                return null;
            }
        }
        return null;
    }
    
    @Override
    protected void loadEntityToForm(Irmao entity) {
        if (entity != null) {
            nomeField.setText(entity.getNome());
            nascimentoField.setText(entity.getNascimento() != null ? entity.getNascimento().toString() : "");
            estadoCivilField.setText(entity.getEstadoCivil());
            naturalField.setText(entity.getNatural());
            identidadeField.setText(entity.getIdentidade());
            tipoSanguineoField.setText(entity.getTipoSanguineo());
            cargoLojaField.setText(entity.getCargoLoja());
            grauField.setText(entity.getGrau());
            cargoGrandeLojaField.setText(entity.getCargoGrandeLoja());
            enderecoField.setText(entity.getEndereco());
            bairroField.setText(entity.getBairro());
            cidadeField.setText(entity.getCidade());
            estadoField.setText(entity.getEstado());
            telefoneField.setText(entity.getTelefone());
            empresaField.setText(entity.getEmpresa());
            telefoneEmpresaField.setText(entity.getTelefoneEmpresa());
            enderecoEmpresaField.setText(entity.getEnderecoEmpresa());
            registroGrandeLojaField.setText(entity.getRegistroGrandeLoja());
        }
    }
    
    @Override
    protected Irmao getEntityFromForm() throws Exception {
        Irmao irmao = new Irmao();
        
        if (nomeField.getText().trim().isEmpty()) {
            throw new Exception("Nome é obrigatório!");
        }
        
        irmao.setNome(nomeField.getText().trim());
        
        // Data de nascimento
        String nascimentoStr = nascimentoField.getText().trim();
        if (!nascimentoStr.isEmpty()) {
            try {
                irmao.setNascimento(LocalDate.parse(nascimentoStr));
            } catch (Exception e) {
                throw new Exception("Data de nascimento inválida! Use formato YYYY-MM-DD");
            }
        }
        
        irmao.setEstadoCivil(estadoCivilField.getText().trim());
        irmao.setNatural(naturalField.getText().trim());
        irmao.setIdentidade(identidadeField.getText().trim());
        irmao.setTipoSanguineo(tipoSanguineoField.getText().trim());
        irmao.setCargoLoja(cargoLojaField.getText().trim());
        irmao.setGrau(grauField.getText().trim());
        irmao.setCargoGrandeLoja(cargoGrandeLojaField.getText().trim());
        irmao.setEndereco(enderecoField.getText().trim());
        irmao.setBairro(bairroField.getText().trim());
        irmao.setCidade(cidadeField.getText().trim());
        irmao.setEstado(estadoField.getText().trim());
        irmao.setTelefone(telefoneField.getText().trim());
        irmao.setEmpresa(empresaField.getText().trim());
        irmao.setTelefoneEmpresa(telefoneEmpresaField.getText().trim());
        irmao.setEnderecoEmpresa(enderecoEmpresaField.getText().trim());
        irmao.setRegistroGrandeLoja(registroGrandeLojaField.getText().trim());
        
        return irmao;
    }
    
    @Override
    protected void validateEntity(Irmao entity) throws Exception {
        if (entity.getNome() == null || entity.getNome().trim().isEmpty()) {
            throw new Exception("Nome é obrigatório!");
        }
    }
    
    @Override
    protected void saveEntity(Irmao entity) throws Exception {
        irmaoDAO.save(entity);
    }
    
    @Override
    protected void deleteEntity(Irmao entity) throws Exception {
        irmaoDAO.delete(entity.getId());
    }
    
    @Override
    protected void updateTableModel(List<Irmao> data) {
        tableModel.setRowCount(0);
        for (Irmao irmao : data) {
            Object[] row = {
                irmao.getId(),
                irmao.getNome(),
                irmao.getNascimento() != null ? irmao.getNascimento().toString() : "",
                irmao.getEstadoCivil(),
                irmao.getTelefone(),
                irmao.getCargoLoja()
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected void clearFormFields() {
        nomeField.setText("");
        nascimentoField.setText("");
        estadoCivilField.setText("");
        naturalField.setText("");
        identidadeField.setText("");
        tipoSanguineoField.setText("");
        cargoLojaField.setText("");
        grauField.setText("");
        cargoGrandeLojaField.setText("");
        enderecoField.setText("");
        bairroField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        telefoneField.setText("");
        empresaField.setText("");
        telefoneEmpresaField.setText("");
        enderecoEmpresaField.setText("");
        registroGrandeLojaField.setText("");
        
        dataTable.clearSelection();
    }
    
    @Override
    protected void enableFormFields(boolean enabled) {
        nomeField.setEnabled(enabled);
        nascimentoField.setEnabled(enabled);
        estadoCivilField.setEnabled(enabled);
        naturalField.setEnabled(enabled);
        identidadeField.setEnabled(enabled);
        tipoSanguineoField.setEnabled(enabled);
        cargoLojaField.setEnabled(enabled);
        grauField.setEnabled(enabled);
        cargoGrandeLojaField.setEnabled(enabled);
        enderecoField.setEnabled(enabled);
        bairroField.setEnabled(enabled);
        cidadeField.setEnabled(enabled);
        estadoField.setEnabled(enabled);
        telefoneField.setEnabled(enabled);
        empresaField.setEnabled(enabled);
        telefoneEmpresaField.setEnabled(enabled);
        enderecoEmpresaField.setEnabled(enabled);
        registroGrandeLojaField.setEnabled(enabled);
    }
}
