package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.AfastamentoDAO;
import com.artereal.swing.model.Afastamento;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel de Gestão de Afastamentos e Licenças
 */
public class AfastamentosPanel extends JPanel {
    
    private AfastamentoDAO afastamentoDAO;
    private DefaultTableModel tableModel;
    private JTable afastamentosTable;
    private JTextField codigoIrmaoField;
    private JTextField dataInicialField;
    private JTextField dataFinalField;
    private JTextField descricaoField;
    private JTextField documentoField;
    private JTextField usuarioCadastroField;
    private JTextArea observacoesArea;
    private JComboBox<String> motivoComboBox;
    private JCheckBox afetaFrequenciaCheckBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton finalizarButton;
    private JButton cancelarButton;
    private JButton reativarButton;
    private JButton relatorioButton;
    private Afastamento afastamentoAtual;
    
    public AfastamentosPanel() {
        afastamentoDAO = new AfastamentoDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Cód. Irmão", "Descrição", "Motivo", "Período", "Dias", "Status", "Afeta Frequência"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        afastamentosTable = new JTable(tableModel);
        
        // Formulário
        codigoIrmaoField = new JTextField(10);
        dataInicialField = new JTextField(12);
        dataFinalField = new JTextField(12);
        descricaoField = new JTextField(40);
        documentoField = new JTextField(30);
        usuarioCadastroField = new JTextField(20);
        observacoesArea = new JTextArea(3, 40);
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        motivoComboBox = new JComboBox<>(new String[]{
            "LICENCA_MEDICA", "FERIAS", "SUSPENSAO", 
            "AFASTAMENTO_TEMPORARIO", "LICENCA_MATERNIDADE", "OUTRO"
        });
        afetaFrequenciaCheckBox = new JCheckBox("Afeta Frequência");
        afetaFrequenciaCheckBox.setSelected(true);
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        finalizarButton = PadraoLayout.criarBotao("Finalizar", new Color(152, 251, 152)); // Verde menta
        cancelarButton = PadraoLayout.criarBotao("Cancelar", new Color(255, 182, 193)); // Rosa pastel
        reativarButton = PadraoLayout.criarBotao("Reativar", new Color(173, 216, 230)); // Azul pastel
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255)); // Azul claro
        
        // Data atual como padrão
        dataInicialField.setText(LocalDate.now().toString());
        
        afastamentoAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO EM LOTE
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("🏥 Gestão de Afastamentos e Licenças", "Controle de afastamentos e licenças dos irmãos");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JSplitPane splitPane = PadraoLayout.criarSplitPaneVertical(null, null);
        
        // Painel esquerdo - Tabela e estatísticas usando PadraoLayout - CORREÇÃO EM LOTE
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PadraoLayout.COR_PAINEL);
        leftPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de pesquisa usando PadraoLayout - CORREÇÃO EM LOTE
        JPanel pesquisaPanel = new JPanel(new BorderLayout());
        pesquisaPanel.setBackground(PadraoLayout.COR_PAINEL);
        pesquisaPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchContainer.setBackground(new Color(245, 245, 250));
        
        JLabel searchLabel = PadraoLayout.criarLabelFormulario("🔍 Pesquisar:");
        
        JTextField pesquisarField = new JTextField();
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        PadraoLayout.estilizarCampoTexto(pesquisarField);
        
        JButton pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        pesquisarButton.setFocusPainted(false);
        
        searchContainer.add(searchLabel);
        searchContainer.add(pesquisarField);
        searchContainer.add(pesquisarButton);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        // Painel de estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(new Color(245, 245, 250));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        try {
            List<Object[]> estatisticas = afastamentoDAO.getEstatisticas();
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
        
        // Painel de alertas
        JPanel alertasPanel = new JPanel(new BorderLayout());
        alertasPanel.setBackground(new Color(245, 245, 250));
        alertasPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        try {
            List<Afastamento> vencidos = afastamentoDAO.findVencidos();
            List<Afastamento> emAndamento = afastamentoDAO.findEmAndamento();
            
            JPanel alertasContent = new JPanel(new GridLayout(2, 1));
            alertasContent.setBackground(new Color(245, 245, 250));
            
            // Vencidos
            JPanel vencidosPanel = new JPanel(new BorderLayout());
            vencidosPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            JLabel vencidosLabel = new JLabel(String.format("⚠ %d Afastamentos Vencidos", vencidos.size()), SwingConstants.CENTER);
            vencidosLabel.setForeground(Color.RED);
            vencidosPanel.add(vencidosLabel, BorderLayout.CENTER);
            alertasContent.add(vencidosPanel);
            
            // Em andamento
            JPanel andamentoPanel = new JPanel(new BorderLayout());
            andamentoPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            JLabel andamentoLabel = new JLabel(String.format("📋 %d Afastamentos em Andamento", emAndamento.size()), SwingConstants.CENTER);
            andamentoLabel.setForeground(Color.BLUE);
            andamentoPanel.add(andamentoLabel, BorderLayout.CENTER);
            alertasContent.add(andamentoPanel);
            
            alertasPanel.add(alertasContent, BorderLayout.CENTER);
        } catch (SQLException e) {
            alertasPanel.add(new JLabel("Erro ao carregar alertas"));
        }
        
        // Tabela estilizada
        JScrollPane tableScrollPane = new JScrollPane(afastamentosTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210)));
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(afastamentosTable);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(alertasPanel, BorderLayout.SOUTH);
        topPanel.add(tableScrollPane, BorderLayout.SOUTH);
        
        leftPanel.add(topPanel, BorderLayout.CENTER);
        
        // Painel direito - Formulário (padrão SessoesPanel)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        // Título do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(new Color(245, 245, 250));
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Dados do Afastamento", SwingConstants.LEFT);
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setForeground(new Color(70, 130, 180));
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formularioPanel = new JPanel(new BorderLayout());
        formularioPanel.setBackground(Color.WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel formTitle = new JLabel("📝 Dados do Afastamento");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(new Color(70, 130, 180));
        
        // Container principal para todos os grupos
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel dadosBasicosPanel = createFormGroup("📅 Dados Básicos");
        JPanel dadosBasicosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        dadosBasicosContent.setBackground(Color.WHITE);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Cód. Irmão:"));
        PadraoLayout.estilizarCampoTexto(codigoIrmaoField);
        dadosBasicosContent.add(codigoIrmaoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Motivo:"));
        PadraoLayout.estilizarComboBox(motivoComboBox);
        dadosBasicosContent.add(motivoComboBox);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Descrição:"));
        PadraoLayout.estilizarCampoTexto(descricaoField);
        dadosBasicosContent.add(descricaoField);
        
        dadosBasicosContent.add(PadraoLayout.criarLabelFormulario("Documento:"));
        PadraoLayout.estilizarCampoTexto(documentoField);
        dadosBasicosContent.add(documentoField);
        
        dadosBasicosPanel.add(dadosBasicosContent);
        
        // Grupo 2: Período do Afastamento
        JPanel periodoPanel = createFormGroup("📅 Período do Afastamento");
        JPanel periodoContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        periodoContent.setBackground(Color.WHITE);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Inicial:"));
        PadraoLayout.estilizarCampoTexto(dataInicialField);
        periodoContent.add(dataInicialField);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Final:"));
        PadraoLayout.estilizarCampoTexto(dataFinalField);
        periodoContent.add(dataFinalField);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Usuário Cadastro:"));
        PadraoLayout.estilizarCampoTexto(usuarioCadastroField);
        periodoContent.add(usuarioCadastroField);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Afeta Frequência:"));
        periodoContent.add(afetaFrequenciaCheckBox);
        
        periodoPanel.add(periodoContent);
        // Grupo 3: Observações usando PadraoLayout
        JPanel observacoesPanel = PadraoLayout.criarGrupoFormulario("📝 Observações");
        JPanel observacoesContent = PadraoLayout.criarPainelTextArea("Observações:", observacoesArea);
        observacoesPanel.add(observacoesContent);
        
        // Organizar grupos verticalmente usando PadraoLayout
        JPanel allGroups = PadraoLayout.criarFormularioMultiplosGrupos(
            dadosBasicosPanel, periodoPanel, observacoesPanel
        );
        
        // Adicionar scroll ao formulário usando PadraoLayout
        JScrollPane formScroll = PadraoLayout.criarFormularioComScroll(allGroups);
        
        formContainer.add(formScroll, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        botoesPanel.add(createStyledButton("Salvar", new Color(144, 238, 144)));
        botoesPanel.add(createStyledButton("Novo", new Color(173, 216, 230)));
        botoesPanel.add(createStyledButton("Excluir", new Color(255, 182, 193)));
        botoesPanel.add(createStyledButton("Finalizar", new Color(255, 250, 205)));
        botoesPanel.add(createStyledButton("Cancelar", new Color(255, 218, 185)));
        botoesPanel.add(createStyledButton("Reativar", new Color(240, 240, 240)));
        botoesPanel.add(createStyledButton("Relatório", new Color(211, 211, 211)));
        
        formularioPanel.add(formTitle, BorderLayout.NORTH);
        formularioPanel.add(formContainer, BorderLayout.CENTER);
        formularioPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        rightPanel.add(formularioPanel, BorderLayout.CENTER);
        
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
        salvarButton.addActionListener(e -> salvarAfastamento());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirAfastamento());
        finalizarButton.addActionListener(e -> finalizarAfastamento());
        cancelarButton.addActionListener(e -> cancelarAfastamento());
        reativarButton.addActionListener(e -> reativarAfastamento());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        
        // Seleção na tabela
        afastamentosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarAfastamentoSelecionado();
            }
        });
        
        // Mudança de datas para calcular dias
        dataInicialField.addActionListener(e -> calcularDias());
        dataFinalField.addActionListener(e -> calcularDias());
    }
    
    private void salvarAfastamento() {
        try {
            if (codigoIrmaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código do irmão é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (descricaoField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Descrição é obrigatória!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (dataInicialField.getText().trim().isEmpty() || dataFinalField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Período (data inicial e final) é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Afastamento afastamento = afastamentoAtual != null ? afastamentoAtual : new Afastamento();
            afastamento.setCodigoIrmao(Long.parseLong(codigoIrmaoField.getText().trim()));
            afastamento.setDataInicial(LocalDate.parse(dataInicialField.getText().trim()));
            afastamento.setDataFinal(LocalDate.parse(dataFinalField.getText().trim()));
            afastamento.setDescricao(descricaoField.getText().trim());
            afastamento.setMotivo((String) motivoComboBox.getSelectedItem());
            afastamento.setDocumentoComprobatorio(documentoField.getText().trim());
            afastamento.setUsuarioCadastro(usuarioCadastroField.getText().trim());
            afastamento.setObservacoes(observacoesArea.getText().trim());
            afastamento.setAfetaFrequencia(afetaFrequenciaCheckBox.isSelected());
            
            // Calcular dias automaticamente
            afastamento.setDiasAfastamento(afastamento.calcularDias());
            
            afastamentoDAO.save(afastamento);
            
            JOptionPane.showMessageDialog(this, "Afastamento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        afastamentoAtual = null;
        codigoIrmaoField.setText("");
        dataInicialField.setText(LocalDate.now().toString());
        dataFinalField.setText("");
        descricaoField.setText("");
        documentoField.setText("");
        usuarioCadastroField.setText("");
        observacoesArea.setText("");
        motivoComboBox.setSelectedItem("OUTRO");
        afetaFrequenciaCheckBox.setSelected(true);
        codigoIrmaoField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir este afastamento?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.delete(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void finalizarAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para finalizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!afastamentoAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Apenas afastamentos ativos podem ser finalizados!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente finalizar este afastamento?", 
            "Confirmar Finalização", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.finalizar(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento finalizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAfastamentoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao finalizar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para cancelar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!afastamentoAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Apenas afastamentos ativos podem ser cancelados!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente cancelar este afastamento?", 
            "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.cancelar(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento cancelado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAfastamentoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cancelar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void reativarAfastamento() {
        if (afastamentoAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um afastamento para reativar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (afastamentoAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Afastamento já está ativo!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente reativar este afastamento?", 
            "Confirmar Reativação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                afastamentoDAO.reativar(afastamentoAtual.getId());
                JOptionPane.showMessageDialog(this, "Afastamento reativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAfastamentoSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao reativar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void gerarRelatorio() {
        try {
            List<Afastamento> afastamentos = afastamentoDAO.findAll();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("RELATÓRIO DE AFASTAMENTOS\n");
            relatorio.append("========================\n\n");
            
            int totalAfastamentos = afastamentos.size();
            int totalDias = 0;
            int ativos = 0;
            int finalizados = 0;
            int cancelados = 0;
            
            for (Afastamento afastamento : afastamentos) {
                relatorio.append(String.format("Irmão: %d\n", afastamento.getCodigoIrmao()));
                relatorio.append(String.format("Descrição: %s\n", afastamento.getDescricao()));
                relatorio.append(String.format("Motivo: %s\n", afastamento.getMotivoFormatado()));
                relatorio.append(String.format("Período: %s\n", afastamento.getPeriodoFormatado()));
                relatorio.append(String.format("Dias: %d\n", afastamento.getDiasAfastamento()));
                relatorio.append(String.format("Status: %s\n", afastamento.getStatusFormatado()));
                relatorio.append(String.format("Afeta Frequência: %s\n", afastamento.isAfetaFrequencia() ? "Sim" : "Não"));
                
                if (afastamento.getUsuarioCadastro() != null) {
                    relatorio.append(String.format("Cadastro por: %s\n", afastamento.getUsuarioCadastro()));
                }
                
                relatorio.append("------------------------\n");
                
                totalDias += afastamento.getDiasAfastamento();
                if (afastamento.isAtivo()) ativos++;
                else if (afastamento.isFinalizado()) finalizados++;
                else if (afastamento.isCancelado()) cancelados++;
            }
            
            relatorio.append("\nRESUMO\n");
            relatorio.append("======\n");
            relatorio.append(String.format("Total de Afastamentos: %d\n", totalAfastamentos));
            relatorio.append(String.format("Total de Dias: %d\n", totalDias));
            relatorio.append(String.format("Ativos: %d\n", ativos));
            relatorio.append(String.format("Finalizados: %d\n", finalizados));
            relatorio.append(String.format("Cancelados: %d\n", cancelados));
            relatorio.append(String.format("Média de Dias: %.1f\n", totalAfastamentos > 0 ? (double) totalDias / totalAfastamentos : 0));
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório de Afastamentos", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarAfastamentoSelecionado() {
        int selectedRow = afastamentosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                afastamentoAtual = afastamentoDAO.findById(id);
                if (afastamentoAtual != null) {
                    codigoIrmaoField.setText(String.valueOf(afastamentoAtual.getCodigoIrmao()));
                    dataInicialField.setText(afastamentoAtual.getDataInicial().toString());
                    dataFinalField.setText(afastamentoAtual.getDataFinal().toString());
                    descricaoField.setText(afastamentoAtual.getDescricao());
                    motivoComboBox.setSelectedItem(afastamentoAtual.getMotivo());
                    documentoField.setText(afastamentoAtual.getDocumentoComprobatorio());
                    usuarioCadastroField.setText(afastamentoAtual.getUsuarioCadastro());
                    observacoesArea.setText(afastamentoAtual.getObservacoes());
                    afetaFrequenciaCheckBox.setSelected(afastamentoAtual.isAfetaFrequencia());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar afastamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void calcularDias() {
        try {
            String dataInicialStr = dataInicialField.getText().trim();
            String dataFinalStr = dataFinalField.getText().trim();
            
            if (!dataInicialStr.isEmpty() && !dataFinalStr.isEmpty()) {
                LocalDate dataInicial = LocalDate.parse(dataInicialStr);
                LocalDate dataFinal = LocalDate.parse(dataFinalStr);
                
                if (dataFinal.isBefore(dataInicial)) {
                    JOptionPane.showMessageDialog(this, "Data final deve ser posterior à data inicial!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int dias = (int) java.time.temporal.ChronoUnit.DAYS.between(dataInicial, dataFinal) + 1;
                JOptionPane.showMessageDialog(this, String.format("Período de %d dias", dias), "Cálculo de Dias", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            // Ignora erros de parsing
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temAfastamento = afastamentoAtual != null;
        boolean ativo = temAfastamento && afastamentoAtual.isAtivo();
        
        finalizarButton.setEnabled(ativo);
        cancelarButton.setEnabled(ativo);
        reativarButton.setEnabled(temAfastamento && !ativo);
    }
    
    public void refreshData() {
        try {
            List<Afastamento> afastamentos = afastamentoDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Afastamento afastamento : afastamentos) {
                Object[] row = {
                    afastamento.getId(),
                    afastamento.getCodigoIrmao(),
                    afastamento.getDescricao(),
                    afastamento.getMotivoFormatado(),
                    afastamento.getPeriodoFormatado(),
                    afastamento.getDiasAfastamento(),
                    afastamento.getStatusFormatado(),
                    afastamento.isAfetaFrequencia() ? "Sim" : "Não"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar afastamentos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
