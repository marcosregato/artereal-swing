package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.AfastamentoDAO;
import com.artereal.swing.model.Afastamento;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class AfastamentosPanel extends JPanel {
    
    // Componentes da interface
    private JTextField codigoIrmaoField;
    private JTextField descricaoField;
    private JTextField documentoField;
    private JTextField dataInicialField;
    private JTextField dataFinalField;
    private JComboBox<String> motivoComboBox;
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
        // Campos do formulário
        codigoIrmaoField = new JTextField();
        descricaoField = new JTextField();
        documentoField = new JTextField();
        dataInicialField = new JTextField();
        dataFinalField = new JTextField();
        motivoComboBox = new JComboBox<>(new String[]{"LICENCA_MEDICA", "FERIAS", "SUSPENSAO", "OUTROS"});
        
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
        
        // Data atual como padrão
        dataInicialField.setText(java.time.LocalDate.now().toString());
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
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
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados do Afastamento");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados Básicos do Afastamento
        JPanel dadosBasicosPanel = new JPanel(new BorderLayout());
        dadosBasicosPanel.setBackground(Color.WHITE);
        dadosBasicosPanel.setBorder(BorderFactory.createTitledBorder("📅 Dados Básicos do Afastamento"));
        
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Cód. Irmão:"));
        PadraoLayout.estilizarCampoCodigoIrmao(codigoIrmaoField);
        dadosBasicosContent.add(codigoIrmaoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Motivo:"));
        PadraoLayout.estilizarComboBox(motivoComboBox);
        dadosBasicosContent.add(motivoComboBox);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoDescricaoAfastamento(descricaoField);
        dadosBasicosContent.add(descricaoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Documento:"));
        PadraoLayout.estilizarCampoDocumentoAfastamento(documentoField);
        dadosBasicosContent.add(documentoField);
        
        dadosBasicosPanel.add(dadosBasicosContent, BorderLayout.CENTER);
        
        // SEÇÃO 2: Período do Afastamento
        JPanel periodoPanel = new JPanel(new BorderLayout());
        periodoPanel.setBackground(Color.WHITE);
        periodoPanel.setBorder(BorderFactory.createTitledBorder("📅 Período do Afastamento"));
        
        JPanel periodoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        periodoContent.setBackground(Color.WHITE);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Inicial:"));
        PadraoLayout.estilizarCampoData(dataInicialField);
        periodoContent.add(dataInicialField);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Final:"));
        PadraoLayout.estilizarCampoData(dataFinalField);
        periodoContent.add(dataFinalField);
        
        periodoPanel.add(periodoContent, BorderLayout.CENTER);
        
        // Organizar grupos verticalmente
        JPanel formGroups = new JPanel(new BorderLayout());
        formGroups.setBackground(Color.WHITE);
        formGroups.add(dadosBasicosPanel, BorderLayout.NORTH);
        formGroups.add(periodoPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = PadraoLayout.criarPainelBotoesAcao();
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(excluirButton);
        botoesPanel.add(finalizarButton);
        botoesPanel.add(cancelarButton);
        botoesPanel.add(reativarButton);
        botoesPanel.add(relatorioButton);
        
        formContainer.add(formGroups, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(new Color(245, 245, 250));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        try {
            java.util.List<Object[]> estatisticas = afastamentoDAO.getEstatisticas();
            for (Object[] est : estatisticas) {
                String motivo = (String) est[0];
                int quantidade = (Integer) est[1];
                int totalDias = (Integer) est[2];
                int ativos = (Integer) est[3];
                int finalizados = (Integer) est[4];
                
                JPanel statPanel = new JPanel(new BorderLayout());
                statPanel.setBackground(new Color(240, 240, 245));
                statPanel.setBorder(BorderFactory.createEtchedBorder());
                
                Color color = Color.BLACK;
                switch (motivo) {
                    case "LICENCA_MEDICA": color = Color.RED; break;
                    case "FERIAS": color = Color.BLUE; break;
                    case "SUSPENSAO": color = Color.ORANGE; break;
                    default: color = Color.DARK_GRAY; break;
                }
                
                JLabel motivoLabel = new JLabel(motivo.replace("_", " "), SwingConstants.CENTER);
                motivoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                motivoLabel.setForeground(color);
                statPanel.add(motivoLabel, BorderLayout.NORTH);
                
                JLabel detalhesLabel = new JLabel(String.format("%d afastamentos\n%d dias totais\n%d ativos\n%d finalizados", 
                    quantidade, totalDias, ativos, finalizados), SwingConstants.CENTER);
                detalhesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                statPanel.add(detalhesLabel, BorderLayout.CENTER);
                
                statsPanel.add(statPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📋 Registros de Afastamentos");
        PadraoLayout.configurarTabela(afastamentosTable);
        JScrollPane tableScrollPane = new JScrollPane(afastamentosTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Estrutura vertical: Formulário → Estatísticas → Tabela
        JPanel verticalPanel = new JPanel(new BorderLayout());
        verticalPanel.setBackground(PadraoLayout.COR_FUNDO);
        verticalPanel.add(formPanel, BorderLayout.NORTH);
        verticalPanel.add(statsPanel, BorderLayout.CENTER);
        verticalPanel.add(tabelaPanel, BorderLayout.SOUTH);
        
        contentPanel.add(verticalPanel, BorderLayout.CENTER);
        
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
        // Implementar lógica de salvar
    }
    
    private void limparFormulario() {
        // Implementar lógica de limpar
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
