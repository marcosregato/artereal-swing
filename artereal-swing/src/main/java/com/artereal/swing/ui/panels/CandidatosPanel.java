package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CandidatoDAO;
import com.artereal.swing.model.Candidato;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de Gestão de Candidatos e Profanos
 */
public class CandidatosPanel extends JPanel {
    
    private CandidatoDAO candidatoDAO;
    private DefaultTableModel tableModel;
    private JTable candidatosTable;
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField enderecoField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField profissaoField;
    private JTextField foneField;
    private JTextArea informacoesArea;
    private JComboBox<String> statusComboBox;
    private JTextField pesquisarField;
    private JButton pesquisarButton;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton aprovarButton;
    private JButton rejeitarButton;
    private JButton iniciarButton;
    private Candidato candidatoAtual;
    
    public CandidatosPanel() {
        candidatoDAO = new CandidatoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Cidade/Estado", "Profissão", "Status", "Data Cadastro"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        candidatosTable = new JTable(tableModel);
        
        // Formulário
        codigoField = new JTextField(10);
        nomeField = new JTextField(30);
        enderecoField = new JTextField(40);
        cidadeField = new JTextField(20);
        estadoField = new JTextField(10);
        profissaoField = new JTextField(25);
        foneField = new JTextField(15);
        informacoesArea = new JTextArea(4, 30);
        informacoesArea.setLineWrap(true);
        informacoesArea.setWrapStyleWord(true);
        
        statusComboBox = new JComboBox<>(new String[]{"CANDIDATO", "APROVADO", "REJEITADO", "INICIADO"});
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        aprovarButton = PadraoLayout.criarBotao("Aprovar", PadraoLayout.COR_BOTAO_APROVAR);
        rejeitarButton = PadraoLayout.criarBotao("Rejeitar", PadraoLayout.COR_BOTAO_REJEITAR);
        iniciarButton = PadraoLayout.criarBotao("Iniciar", PadraoLayout.COR_BOTAO_INICIAR);
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesCandidatos(salvarButton, novoButton, excluirButton, aprovarButton, rejeitarButton, iniciarButton);
        
        candidatoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("👥 Gestão de Candidatos e Profanos", "Controle de admissões e iniciações maçônicas");
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
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados do Candidato");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Formulário usando BoxLayout vertical para organizar JPanel um de baixo do outro
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados do Candidato
        JPanel dadosCandidatoPanel = new JPanel(new BorderLayout());
        dadosCandidatoPanel.setBackground(Color.WHITE);
        dadosCandidatoPanel.setBorder(BorderFactory.createTitledBorder("👤 Dados do Candidato"));
        
        JPanel dadosCandidatoContent = new JPanel();
        dadosCandidatoContent.setLayout(new BoxLayout(dadosCandidatoContent, BoxLayout.Y_AXIS));
        dadosCandidatoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Código e Nome
        JPanel primeiraLinhaCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaCandidatoPanel.setBackground(Color.WHITE);
        
        // Campo Código
        JPanel codigoCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        codigoCandidatoPanel.setBackground(Color.WHITE);
        codigoCandidatoPanel.add(PadraoLayout.criarLabelFormularioCodigo("Código:"));
        PadraoLayout.estilizarCampoCodigo(codigoField);
        codigoCandidatoPanel.add(codigoField);
        
        // Campo Nome
        JPanel nomeCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nomeCandidatoPanel.setBackground(Color.WHITE);
        nomeCandidatoPanel.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoTexto(nomeField);
        nomeCandidatoPanel.add(nomeField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaCandidatoPanel.add(codigoCandidatoPanel);
        primeiraLinhaCandidatoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaCandidatoPanel.add(nomeCandidatoPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        dadosCandidatoContent.add(primeiraLinhaCandidatoPanel);
        dadosCandidatoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Profissão e Status
        JPanel segundaLinhaCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaCandidatoPanel.setBackground(Color.WHITE);
        
        // Campo Profissão
        JPanel profissaoCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        profissaoCandidatoPanel.setBackground(Color.WHITE);
        profissaoCandidatoPanel.add(PadraoLayout.criarLabelFormulario("Profissão:"));
        PadraoLayout.estilizarCampoTexto(profissaoField);
        profissaoCandidatoPanel.add(profissaoField);
        
        // Campo Status
        JPanel statusCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusCandidatoPanel.setBackground(Color.WHITE);
        statusCandidatoPanel.add(PadraoLayout.criarLabelFormulario("Status:"));
        PadraoLayout.estilizarComboBox(statusComboBox);
        statusCandidatoPanel.add(statusComboBox);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaCandidatoPanel.add(profissaoCandidatoPanel);
        segundaLinhaCandidatoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaCandidatoPanel.add(statusCandidatoPanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        dadosCandidatoContent.add(segundaLinhaCandidatoPanel);
        
        dadosCandidatoPanel.add(dadosCandidatoContent, BorderLayout.CENTER);
        formContent.add(dadosCandidatoPanel);
        
        // SEÇÃO 2: Endereço
        JPanel enderecoCandidatoPanel = new JPanel(new BorderLayout());
        enderecoCandidatoPanel.setBackground(Color.WHITE);
        enderecoCandidatoPanel.setBorder(BorderFactory.createTitledBorder("📍 Endereço"));
        
        JPanel enderecoCandidatoContent = new JPanel();
        enderecoCandidatoContent.setLayout(new BoxLayout(enderecoCandidatoContent, BoxLayout.Y_AXIS));
        enderecoCandidatoContent.setBackground(Color.WHITE);
        
        // Primeira linha: Endereço (ocupa linha inteira)
        JPanel enderecoPrincipalCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        enderecoPrincipalCandidatoPanel.setBackground(Color.WHITE);
        enderecoPrincipalCandidatoPanel.add(PadraoLayout.criarLabelFormulario("Endereço:"));
        PadraoLayout.estilizarCampoTexto(enderecoField);
        enderecoPrincipalCandidatoPanel.add(enderecoField);
        
        // Adicionar o painel do Endereço ao conteúdo
        enderecoCandidatoContent.add(enderecoPrincipalCandidatoPanel);
        enderecoCandidatoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Cidade e Estado
        JPanel segundaLinhaEnderecoCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaEnderecoCandidatoPanel.setBackground(Color.WHITE);
        
        // Campo Cidade
        JPanel cidadeCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cidadeCandidatoPanel.setBackground(Color.WHITE);
        cidadeCandidatoPanel.add(PadraoLayout.criarLabelFormulario("Cidade:"));
        PadraoLayout.estilizarCampoTexto(cidadeField);
        cidadeCandidatoPanel.add(cidadeField);
        
        // Campo Estado
        JPanel estadoCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        estadoCandidatoPanel.setBackground(Color.WHITE);
        estadoCandidatoPanel.add(PadraoLayout.criarLabelFormulario("Estado:"));
        PadraoLayout.estilizarCampoTexto(estadoField);
        estadoCandidatoPanel.add(estadoField);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaEnderecoCandidatoPanel.add(cidadeCandidatoPanel);
        segundaLinhaEnderecoCandidatoPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaEnderecoCandidatoPanel.add(estadoCandidatoPanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        enderecoCandidatoContent.add(segundaLinhaEnderecoCandidatoPanel);
        enderecoCandidatoContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Terceira linha: Telefone
        JPanel terceiraLinhaEnderecoCandidatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        terceiraLinhaEnderecoCandidatoPanel.setBackground(Color.WHITE);
        terceiraLinhaEnderecoCandidatoPanel.add(PadraoLayout.criarLabelFormulario("Telefone:"));
        PadraoLayout.estilizarCampoTexto(foneField);
        terceiraLinhaEnderecoCandidatoPanel.add(foneField);
        
        // Adicionar o painel da terceira linha ao conteúdo
        enderecoCandidatoContent.add(terceiraLinhaEnderecoCandidatoPanel);
        
        enderecoCandidatoPanel.add(enderecoCandidatoContent, BorderLayout.CENTER);
        formContent.add(enderecoCandidatoPanel);
        
        // Informações adicionais
        formContent.add(PadraoLayout.criarLabelFormulario("Informações:"));
        informacoesArea.setRows(3);
        informacoesArea.setColumns(30);
        formContent.add(informacoesArea);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(aprovarButton);
        botoesPanel.add(rejeitarButton);
        botoesPanel.add(iniciarButton);
        
        formContainer.add(formContent, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
                
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("👥 Candidatos Cadastrados");
        PadraoLayout.configurarTabela(candidatosTable);
        JScrollPane tableScrollPane = new JScrollPane(candidatosTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (60%), Tabela abaixo (40%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(400);
        verticalSplitPane.setResizeWeight(0.6);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarCandidato());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirCandidato());
        aprovarButton.addActionListener(e -> aprovarCandidato());
        rejeitarButton.addActionListener(e -> rejeitarCandidato());
        iniciarButton.addActionListener(e -> iniciarCandidato());
        
        // Seleção na tabela
        candidatosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarCandidatoSelecionado();
            }
        });
    }
    
    private void salvarCandidato() {
        try {
            Candidato candidato = new Candidato();
            if (candidatoAtual != null) {
                candidato.setId(candidatoAtual.getId());
            }
            
            candidato.setNome(nomeField.getText().trim());
            candidato.setEndereco(enderecoField.getText().trim());
            candidato.setCidade(cidadeField.getText().trim());
            candidato.setEstado(estadoField.getText().trim());
            candidato.setProfissao(profissaoField.getText().trim());
            candidato.setFoneResidencial(foneField.getText().trim());
            candidato.setInformacoes(informacoesArea.getText().trim());
            candidato.setStatus((String) statusComboBox.getSelectedItem());
            
            candidatoDAO.save(candidato);
            
            JOptionPane.showMessageDialog(this, "Candidato salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        candidatoAtual = null;
        codigoField.setText("");
        nomeField.setText("");
        enderecoField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        profissaoField.setText("");
        foneField.setText("");
        informacoesArea.setText("");
        statusComboBox.setSelectedItem("CANDIDATO");
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o candidato " + candidatoAtual.getNome() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.delete(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void aprovarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para aprovar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            candidatoDAO.aprovar(candidatoAtual.getId());
            JOptionPane.showMessageDialog(this, "Candidato aprovado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarCandidatoSelecionado();
            refreshData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao aprovar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejeitarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para rejeitar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente rejeitar o candidato " + candidatoAtual.getNome() + "?", 
            "Confirmar Rejeição", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.rejeitar(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato rejeitado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarCandidatoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao rejeitar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void iniciarCandidato() {
        if (candidatoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um candidato para iniciar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente iniciar o candidato " + candidatoAtual.getNome() + " como irmão?", 
            "Confirmar Iniciação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                candidatoDAO.iniciar(candidatoAtual.getId());
                JOptionPane.showMessageDialog(this, "Candidato iniciado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarCandidatoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao iniciar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void carregarCandidatoSelecionado() {
        int selectedRow = candidatosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                candidatoAtual = candidatoDAO.findById(id);
                if (candidatoAtual != null) {
                    codigoField.setText(candidatoAtual.getId() != null ? candidatoAtual.getId().toString() : "");
                    nomeField.setText(candidatoAtual.getNome());
                    enderecoField.setText(candidatoAtual.getEndereco());
                    cidadeField.setText(candidatoAtual.getCidade());
                    estadoField.setText(candidatoAtual.getEstado());
                    profissaoField.setText(candidatoAtual.getProfissao());
                    foneField.setText(candidatoAtual.getFoneResidencial());
                    informacoesArea.setText(candidatoAtual.getInformacoes());
                    statusComboBox.setSelectedItem(candidatoAtual.getStatus());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar candidato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
        
    private void atualizarBotoesAcao() {
        String status = (String) statusComboBox.getSelectedItem();
        
        // Habilitar/desabilitar botões baseado no status
        aprovarButton.setEnabled("CANDIDATO".equals(status));
        rejeitarButton.setEnabled("CANDIDATO".equals(status) || "APROVADO".equals(status));
        iniciarButton.setEnabled("APROVADO".equals(status));
    }
    
    public void refreshData() {
        try {
            List<Candidato> candidatos = candidatoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Candidato candidato : candidatos) {
                Object[] row = {
                    candidato.getId(),
                    candidato.getNome(),
                    candidato.getCidade() + "/" + candidato.getEstado(),
                    candidato.getProfissao(),
                    candidato.getStatus(),
                    candidato.getDataCadastro() != null ? candidato.getDataCadastro().toString() : ""
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar candidatos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
