package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.IrmaoDAO;
import com.artereal.swing.model.Irmao;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel de cadastro de Irmãos Maçons
 */
public class CadastroIrmaosPanel extends JPanel {
    
    private IrmaoDAO irmaoDAO;
    private DefaultTableModel tableModel;
    private JTable irmaosTable;
    
    // Formulário
    private JTextField codigoField;
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
    
    private Irmao irmaoAtual;
    
    public CadastroIrmaosPanel() {
        irmaoDAO = new IrmaoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Nome", "Telefone", "Grau", "Cargo Loja", "Cidade"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        irmaosTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        codigoField.setEditable(false);
        nomeField = new JTextField(40);
        nascimentoField = new JTextField(10);
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
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        editarButton = PadraoLayout.criarBotaoEditar();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        limparButton = PadraoLayout.criarBotaoLimpar();
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarField = new JTextField(20);
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("� Cadastro de Irmãos", "Novos cadastros e admissões maçônicas");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.6);
        
        // Painel esquerdo - Tabela
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de pesquisa
        JPanel pesquisaPanel = new JPanel(new BorderLayout());
        pesquisaPanel.setBackground(new Color(245, 245, 250));
        pesquisaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchContainer.setBackground(new Color(245, 245, 250));
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(100, 100, 120));
        
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        pesquisarButton.setBackground(new Color(70, 130, 180));
        pesquisarButton.setForeground(Color.WHITE);
        pesquisarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pesquisarButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        pesquisarButton.setFocusPainted(false);
        
        searchContainer.add(searchLabel);
        searchContainer.add(pesquisarField);
        searchContainer.add(pesquisarButton);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(irmaosTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(irmaosTable);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        leftPanel.add(topPanel, BorderLayout.CENTER);
        
        // Painel direito - Formulário (padrão SessoesPanel)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Dados do Irmão", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(70, 130, 180));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("📝 Dados do Irmão");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Código:"));
        PadraoLayout.estilizarCampoTexto(codigoField);
        dadosBasicosContent.add(codigoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoTexto(nomeField);
        dadosBasicosContent.add(nomeField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Nascimento:"));
        PadraoLayout.estilizarCampoTexto(nascimentoField);
        dadosBasicosContent.add(nascimentoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Estado Civil:"));
        PadraoLayout.estilizarCampoTexto(estadoCivilField);
        dadosBasicosContent.add(estadoCivilField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Naturalidade:"));
        PadraoLayout.estilizarCampoTexto(naturalField);
        dadosBasicosContent.add(naturalField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Identidade:"));
        PadraoLayout.estilizarCampoTexto(identidadeField);
        dadosBasicosContent.add(identidadeField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Tipo Sanguíneo:"));
        PadraoLayout.estilizarCampoTexto(tipoSanguineoField);
        dadosBasicosContent.add(tipoSanguineoField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Dados Maçônicos
        JPanel dadosMasonicosPanel = createFormGroup("🔱 Dados Maçônicos");
        JPanel dadosMasonicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosMasonicosContent.setBackground(Color.WHITE);
        
        dadosMasonicosContent.add(PadraoLayout.criarLabelFormulario("Cargo na Loja:"));
        PadraoLayout.estilizarCampoTexto(cargoLojaField);
        dadosMasonicosContent.add(cargoLojaField);
        
        dadosMasonicosContent.add(PadraoLayout.criarLabelFormulario("Grau:"));
        PadraoLayout.estilizarCampoTexto(grauField);
        dadosMasonicosContent.add(grauField);
        
        dadosMasonicosContent.add(PadraoLayout.criarLabelFormulario("Cargo Grande Loja:"));
        PadraoLayout.estilizarCampoTexto(cargoGrandeLojaField);
        dadosMasonicosContent.add(cargoGrandeLojaField);
        
        dadosMasonicosContent.add(PadraoLayout.criarLabelFormulario("Registro Grande Loja:"));
        PadraoLayout.estilizarCampoTexto(registroGrandeLojaField);
        dadosMasonicosContent.add(registroGrandeLojaField);
        
        dadosMasonicosPanel.add(dadosMasonicosContent);
        
        // Grupo 3: Endereço e Contato
        JPanel enderecoContatoPanel = createFormGroup("📍 Endereço e Contato");
        JPanel enderecoContatoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        enderecoContatoContent.setBackground(Color.WHITE);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoTexto(enderecoField);
        enderecoContatoContent.add(enderecoField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Bairro:"));
        PadraoLayout.estilizarCampoTexto(bairroField);
        enderecoContatoContent.add(bairroField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoTexto(cidadeField);
        enderecoContatoContent.add(cidadeField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoTexto(estadoField);
        enderecoContatoContent.add(estadoField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Telefone:"));
        PadraoLayout.estilizarCampoTexto(telefoneField);
        enderecoContatoContent.add(telefoneField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Empresa:"));
        PadraoLayout.estilizarCampoTexto(empresaField);
        enderecoContatoContent.add(empresaField);
        
        enderecoContatoContent.add(PadraoLayout.criarLabelFormulario("Telefone Empresa:"));
        PadraoLayout.estilizarCampoTexto(telefoneEmpresaField);
        enderecoContatoContent.add(telefoneEmpresaField);
        
        enderecoContatoPanel.add(enderecoContatoContent);
        
        // Grupo 4: Informações Adicionais
        JPanel infoPanel = createFormGroup("📝 Informações Adicionais");
        JPanel infoContent = new JPanel(new BorderLayout());
        infoContent.setBackground(Color.WHITE);
        
        // Painel de informações adicionais
        JPanel textAreasPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        textAreasPanel.setBackground(Color.WHITE);
        
        JPanel obsPanel = new JPanel(new BorderLayout());
        obsPanel.setBackground(Color.WHITE);
        obsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        obsPanel.add(new JLabel("📄 Endereço Empresa:"), BorderLayout.NORTH);
        obsPanel.add(new JScrollPane(enderecoEmpresaField), BorderLayout.CENTER);
        
        textAreasPanel.add(obsPanel);
        infoContent.add(textAreasPanel, BorderLayout.CENTER);
        infoPanel.add(infoContent);
        
        // Organizar grupos verticalmente (padrão SessoesPanel)
        JPanel groupsPanel = new JPanel(new BorderLayout());
        groupsPanel.setBackground(Color.WHITE);
        
        JPanel topGroups = new JPanel(new BorderLayout());
        topGroups.setBackground(Color.WHITE);
        topGroups.add(dadosBasicosPanel, BorderLayout.NORTH);
        topGroups.add(dadosMasonicosPanel, BorderLayout.CENTER);
        
        JPanel middleGroups = new JPanel(new BorderLayout());
        middleGroups.setBackground(Color.WHITE);
        middleGroups.add(enderecoContatoPanel, BorderLayout.NORTH);
        middleGroups.add(infoPanel, BorderLayout.CENTER);
        
        JPanel allGroups = new JPanel(new BorderLayout());
        allGroups.setBackground(Color.WHITE);
        allGroups.add(topGroups, BorderLayout.NORTH);
        allGroups.add(middleGroups, BorderLayout.CENTER);
        
        // Adicionar scroll ao formulário para garantir visibilidade
        JScrollPane formScroll = new JScrollPane(allGroups);
        formScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Editar", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Limpar", new Color(240, 240, 240)));
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
        // Adicionar tabela ao painel esquerdo
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabelaPanel.add(topPanel, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        return PadraoLayout.criarBotao(text, bgColor);
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
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarIrmao());
        novoButton.addActionListener(e -> limparFormulario());
        editarButton.addActionListener(e -> editarIrmao());
        excluirButton.addActionListener(e -> excluirIrmao());
        limparButton.addActionListener(e -> limparFormulario());
        pesquisarButton.addActionListener(e -> pesquisarIrmaos());
        
        // Seleção na tabela
        irmaosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarIrmaoSelecionado();
            }
        });
    }
    
    private void salvarIrmao() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Irmao irmao = irmaoAtual != null ? irmaoAtual : new Irmao();
            irmao.setNome(nomeField.getText().trim());
            
            // Data de nascimento
            String nascimentoStr = nascimentoField.getText().trim();
            if (!nascimentoStr.isEmpty()) {
                try {
                    irmao.setNascimento(LocalDate.parse(nascimentoStr));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data de nascimento inválida! Use formato YYYY-MM-DD", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
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
            
            irmaoDAO.save(irmao);
            
            JOptionPane.showMessageDialog(this, "Irmão salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar irmão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarIrmao() {
        if (irmaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // O formulário já está preenchido com os dados do irmão selecionado
        nomeField.requestFocus();
    }
    
    private void excluirIrmao() {
        if (irmaoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um irmão para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o irmão " + irmaoAtual.getNome() + "?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION);
            
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                irmaoDAO.delete(irmaoAtual.getId());
                JOptionPane.showMessageDialog(this, "Irmão excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir irmão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparFormulario() {
        irmaoAtual = null;
        codigoField.setText("");
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
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void pesquisarIrmaos() {
        try {
            String termo = pesquisarField.getText().trim();
            if (termo.isEmpty()) {
                refreshData();
                return;
            }
            
            List<Irmao> irmaos = irmaoDAO.findAll(); // Simplificado - poderia ter método específico
            tableModel.setRowCount(0);
            
            for (Irmao irmao : irmaos) {
                if (irmao.getNome().toLowerCase().contains(termo.toLowerCase()) ||
                    irmao.getEndereco().toLowerCase().contains(termo.toLowerCase())) {
                    Object[] row = {
                        irmao.getId(),
                        irmao.getNome(),
                        irmao.getTelefone(),
                        irmao.getGrau(),
                        irmao.getCargoLoja(),
                        irmao.getCidade()
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarIrmaoSelecionado() {
        int selectedRow = irmaosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                irmaoAtual = irmaoDAO.findById(id);
                if (irmaoAtual != null) {
                    codigoField.setText(String.valueOf(irmaoAtual.getId()));
                    nomeField.setText(irmaoAtual.getNome());
                    
                    // Data de nascimento
                    if (irmaoAtual.getNascimento() != null) {
                        nascimentoField.setText(irmaoAtual.getNascimento().toString());
                    }
                    
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
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar irmão: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temIrmao = irmaoAtual != null;
        editarButton.setEnabled(temIrmao);
        excluirButton.setEnabled(temIrmao);
    }
    
    public void refreshData() {
        try {
            List<Irmao> irmaos = irmaoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Irmao irmao : irmaos) {
                Object[] row = {
                    irmao.getId(),
                    irmao.getNome(),
                    irmao.getTelefone(),
                    irmao.getGrau(),
                    irmao.getCargoLoja(),
                    irmao.getCidade()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar irmãos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
