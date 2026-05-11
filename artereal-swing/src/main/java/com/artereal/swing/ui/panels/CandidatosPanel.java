package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.CandidatoDAO;
import com.artereal.swing.model.Candidato;
import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.candidatos.CandidatosFormPanel;

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
    private CandidatosFormPanel candidatosFormPanel;
    
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
        PadraoLayout.estilizarCampoTexto(pesquisarField);
        pesquisarField.setEditable(true);
        pesquisarField.setEnabled(true);
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
        
        // Inicializar formulário otimizado
        candidatosFormPanel = new CandidatosFormPanel();
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
        
        // Painel de formulário otimizado usando CandidatosFormPanel
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados do Candidato - UX Otimizada");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Usar o formulário otimizado
        formContainer.add(candidatosFormPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(novoButton);
        botoesPanel.add(salvarButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(aprovarButton);
        botoesPanel.add(rejeitarButton);
        botoesPanel.add(iniciarButton);
        
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
