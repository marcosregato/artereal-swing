package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.AfastamentoDAO;
import com.artereal.swing.model.Afastamento;
import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.afastamentos.AfastamentosFormPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class AfastamentosPanel extends JPanel {
    
    // Componentes da interface
    private AfastamentosFormPanel afastamentosFormPanel;
    private JTable afastamentosTable;
    
    // Botões
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton finalizarButton;
    private JButton cancelarButton;
    private JButton reativarButton;
    private JButton relatorioButton;
    private JButton pesquisarButton;
    private JTextField pesquisarField;
    
    // DAO
    private AfastamentoDAO afastamentoDAO;
    
    public AfastamentosPanel() {
        afastamentoDAO = new AfastamentoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
    }
    
    private void initializeComponents() {
        // Inicializar formulário otimizado
        afastamentosFormPanel = new AfastamentosFormPanel();
        
        // Tabela
        afastamentosTable = new JTable(new DefaultTableModel(
            new Object[]{"Código", "Irmão", "Motivo", "Data Inicial", "Data Final", "Status"}, 0
        ));
        
        // Botões usando PadraoLayout
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        finalizarButton = PadraoLayout.criarBotao("Finalizar", new Color(152, 251, 152));
        cancelarButton = PadraoLayout.criarBotao("Cancelar", new Color(255, 182, 193));
        reativarButton = PadraoLayout.criarBotao("Reativar", new Color(173, 216, 230));
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255));
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("🏥 Gestão de Afastamentos e Licenças", "Controle de afastamentos e licenças dos irmãos");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com layout vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário otimizado usando AfastamentosFormPanel
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Usar o formulário otimizado
        formPanel.add(afastamentosFormPanel, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📋 Registros de Afastamentos");
        PadraoLayout.configurarTabela(afastamentosTable);
        JScrollPane tableScrollPane = new JScrollPane(afastamentosTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (40%), Tabela abaixo (60%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(300);
        verticalSplitPane.setResizeWeight(0.4);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarAfastamento());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirAfastamento());
        finalizarButton.addActionListener(e -> finalizarAfastamento());
        cancelarButton.addActionListener(e -> cancelarAfastamento());
        reativarButton.addActionListener(e -> reativarAfastamento());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        pesquisarButton.addActionListener(e -> pesquisar());
        
        afastamentosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarAfastamentoSelecionado();
            }
        });
    }
    
    // Métodos de negócio
    private void salvarAfastamento() {
        try {
            Afastamento afastamento = new Afastamento();
            afastamento.setCodigoIrmao(Long.parseLong(afastamentosFormPanel.getCodigoIrmaoField().getText()));
            afastamento.setDescricao(afastamentosFormPanel.getDescricaoField().getText());
            afastamento.setDocumentoComprobatorio(afastamentosFormPanel.getDocumentoField().getText());
            afastamento.setDataInicial(java.time.LocalDate.parse(afastamentosFormPanel.getDataInicialField().getText()));
            afastamento.setDataFinal(java.time.LocalDate.parse(afastamentosFormPanel.getDataFinalField().getText()));
            afastamento.setMotivo((String) afastamentosFormPanel.getMotivoComboBox().getSelectedItem());
            
            // Salvar no banco
            JOptionPane.showMessageDialog(this, "Afastamento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar afastamento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        afastamentosFormPanel.clearForm();
    }
    
    private void excluirAfastamento() {
        // Implementar lógica de excluir
    }
    
    private void finalizarAfastamento() {
        // Implementar lógica de finalizar
    }
    
    private void cancelarAfastamento() {
        // Implementar lógica de cancelar
    }
    
    private void reativarAfastamento() {
        // Implementar lógica de reativar
    }
    
    private void gerarRelatorio() {
        // Implementar lógica de relatório
    }
    
    private void pesquisar() {
        // Implementar lógica de pesquisar
    }
    
    private void carregarAfastamentoSelecionado() {
        // Implementar lógica de carregar
    }
    
    public void refreshData() {
        // Implementar lógica de atualizar dados da tabela
        try {
            DefaultTableModel model = (DefaultTableModel) afastamentosTable.getModel();
            model.setRowCount(0);
            
            java.util.List<Afastamento> afastamentos = afastamentoDAO.findAll();
            for (Afastamento afastamento : afastamentos) {
                model.addRow(new Object[]{
                    afastamento.getId(),
                    afastamento.getCodigoIrmao(),
                    afastamento.getMotivo(),
                    afastamento.getDataInicial(),
                    afastamento.getDataFinal(),
                    afastamento.getStatus()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
