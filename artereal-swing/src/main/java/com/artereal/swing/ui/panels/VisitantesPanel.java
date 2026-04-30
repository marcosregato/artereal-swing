package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.VisitanteDAO;
import com.artereal.swing.model.Visitante;
import com.artereal.swing.ui.components.LogoMaconaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Painel de Controle de Visitantes
 */
public class VisitantesPanel extends JPanel {
    
    private VisitanteDAO visitanteDAO;
    private DefaultTableModel tableModel;
    private JTable visitantesTable;
    private JTextField nomeField;
    private JTextField dataVisitaField;
    private JTextField grauSecretoField;
    private JTextField lojaOrigemField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JTextField observacoesField;
    private JTextField numeroCrachaField;
    private JComboBox<String> tipoComboBox;
    private JTextArea historicoArea;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JButton autorizarButton;
    private JButton negarButton;
    private JButton gerarCrachaButton;
    private JButton relatorioButton;
    private JButton imprimirCrachaButton;
    private Visitante visitanteAtual;
    
    public VisitantesPanel() {
        visitanteDAO = new VisitanteDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo", "Data Visita", "Loja Origem", "Autorizado", "Crachá", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        visitantesTable = new JTable(tableModel);
        
        // Formulário
        nomeField = new JTextField(40);
        dataVisitaField = new JTextField(12);
        grauSecretoField = new JTextField(20);
        lojaOrigemField = new JTextField(30);
        telefoneField = new JTextField(20);
        emailField = new JTextField(30);
        observacoesField = new JTextField(50);
        numeroCrachaField = new JTextField(15);
        numeroCrachaField.setEditable(false);
        tipoComboBox = new JComboBox<>(new String[]{"VISITANTE", "CONVIDADO", "IRMAO_VISITANTE"});
        historicoArea = new JTextArea(3, 40);
        historicoArea.setLineWrap(true);
        historicoArea.setWrapStyleWord(true);
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");
        autorizarButton = new JButton("Autorizar");
        negarButton = new JButton("Negar Autorização");
        gerarCrachaButton = new JButton("Gerar Crachá");
        relatorioButton = new JButton("Relatório");
        imprimirCrachaButton = new JButton("Imprimir Crachá");
        
        // Data atual como padrão
        dataVisitaField.setText(LocalDate.now().toString());
        
        visitanteAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        
        // Header estilizado com logo
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 112)); // Azul marinho maçônico
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🚪 Controle de Visitantes", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Gestão de acesso e autorizações para loja", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(173, 216, 230));
        
        // Logo maçônico
        JLabel logoLabel = LogoMaconaria.createLogoLabel(32);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        titleContainer.setBackground(new Color(25, 25, 112));
        titleContainer.add(titleLabel);
        titleContainer.add(subtitleLabel);
        
        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setBackground(new Color(25, 25, 112));
        headerContent.add(logoLabel, BorderLayout.WEST);
        headerContent.add(titleContainer, BorderLayout.CENTER);
        
        headerPanel.add(headerContent, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Painel esquerdo - Tabela
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(245, 245, 250));
        
        // Painel de pesquisa
        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pesquisaPanel.setBackground(new Color(240, 240, 245));
        pesquisaPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(100, 100, 120));
        
        JTextField pesquisarField = new JTextField();
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        JButton pesquisarButton = new JButton("Buscar");
        pesquisarButton.setBackground(new Color(221, 160, 221)); // Lila pastel suave
        pesquisarButton.setForeground(new Color(102, 51, 153));
        pesquisarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pesquisarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 160, 221), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        pesquisarButton.setFocusPainted(false);
        pesquisarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pesquisaPanel.add(searchLabel);
        pesquisaPanel.add(pesquisarField);
        pesquisaPanel.add(pesquisarButton);
        
        // Tabela estilizada
        visitantesTable.setRowHeight(25);
        visitantesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        visitantesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        visitantesTable.getTableHeader().setBackground(new Color(70, 130, 180));
        visitantesTable.getTableHeader().setForeground(Color.WHITE);
        visitantesTable.setSelectionBackground(new Color(173, 216, 230));
        visitantesTable.setSelectionForeground(new Color(25, 84, 123));
        
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tabelaPanel.add(new JScrollPane(visitantesTable), BorderLayout.CENTER);
        
        leftPanel.add(pesquisaPanel, BorderLayout.NORTH);
        leftPanel.add(tabelaPanel, BorderLayout.CENTER);
        
        // Painel direito - Formulário
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(245, 245, 250));
        
        // Título do formulário
        JPanel formTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formTitlePanel.setBackground(new Color(70, 130, 180));
        formTitlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel formTitle = new JLabel("📝 Dados do Visitante");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(Color.WHITE);
        formTitlePanel.add(formTitle);
        rightPanel.add(formTitlePanel, BorderLayout.NORTH);
        
        // Painel de estatísticas melhorado
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(new Color(240, 240, 245));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        try {
            List<Object[]> estatisticas = visitanteDAO.getEstatisticas();
            for (Object[] est : estatisticas) {
                String tipo = (String) est[0];
                int quantidade = (Integer) est[1];
                int autorizados = (Integer) est[2];
                int pendentes = (Integer) est[3];
                
                JPanel statPanel = new JPanel(new BorderLayout());
                statPanel.setBorder(BorderFactory.createEtchedBorder());
                
                Color color = Color.BLACK;
                switch (tipo) {
                    case "VISITANTE": color = Color.BLUE; break;
                    case "CONVIDADO": color = Color.GREEN; break;
                    case "IRMAO_VISITANTE": color = Color.MAGENTA; break;
                }
                
                JLabel tipoLabel = new JLabel(tipo.replace("_", " "), SwingConstants.CENTER);
                tipoLabel.setForeground(color);
                statPanel.add(tipoLabel, BorderLayout.NORTH);
                
                JLabel detalhesLabel = new JLabel(String.format("%d total\n%d autorizados\n%d pendentes", quantidade, autorizados, pendentes), SwingConstants.CENTER);
                detalhesLabel.setFont(new Font("Arial", Font.PLAIN, 11));
                statPanel.add(detalhesLabel, BorderLayout.CENTER);
                
                statsPanel.add(statPanel);
            }
        } catch (SQLException e) {
            statsPanel.add(new JLabel("Erro ao carregar estatísticas"));
        }
        
        // Painel de pendentes
        JPanel pendentesPanel = new JPanel(new BorderLayout());
        pendentesPanel.setBorder(BorderFactory.createTitledBorder("Autorizações Pendentes"));
        
        try {
            int pendentes = visitanteDAO.countPendentes();
            JLabel pendentesLabel = new JLabel(String.format("⚠ %d visitantes aguardando autorização", pendentes), SwingConstants.CENTER);
            pendentesLabel.setForeground(pendentes > 0 ? Color.RED : Color.GREEN);
            pendentesLabel.setFont(new Font("Arial", Font.BOLD, 14));
            pendentesPanel.add(pendentesLabel, BorderLayout.CENTER);
            
            if (pendentes > 0) {
                JButton listarPendentesButton = new JButton("Listar Pendentes");
                listarPendentesButton.addActionListener(e -> listarPendentes());
                pendentesPanel.add(listarPendentesButton, BorderLayout.SOUTH);
            }
        } catch (SQLException e) {
            pendentesPanel.add(new JLabel("Erro ao carregar pendentes"));
        }
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Visitante"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(nomeField, gbc);
        
        // Tipo e Data
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel tipoDataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipoDataPanel.add(tipoComboBox);
        tipoDataPanel.add(new JLabel("Data Visita:"));
        tipoDataPanel.add(dataVisitaField);
        formPanel.add(tipoDataPanel, gbc);
        
        // Grau Secreto e Loja Origem
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Grau Secreto:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel grauLojaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        grauLojaPanel.add(grauSecretoField);
        grauLojaPanel.add(new JLabel("Loja Origem:"));
        grauLojaPanel.add(lojaOrigemField);
        formPanel.add(grauLojaPanel, gbc);
        
        // Telefone e Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Contato:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel contatoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contatoPanel.add(telefoneField);
        contatoPanel.add(new JLabel("Email:"));
        contatoPanel.add(emailField);
        formPanel.add(contatoPanel, gbc);
        
        // Número Crachá
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Crachá:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        JPanel crachaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        crachaPanel.add(numeroCrachaField);
        crachaPanel.add(gerarCrachaButton);
        crachaPanel.add(imprimirCrachaButton);
        formPanel.add(crachaPanel, gbc);
        
        // Histórico
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Histórico:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(new JScrollPane(historicoArea), gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(observacoesField, gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Botões de autorização
        JPanel autorizacaoPanel = new JPanel(new FlowLayout());
        autorizacaoPanel.setBorder(BorderFactory.createTitledBorder("Autorização"));
        autorizacaoPanel.add(autorizarButton);
        autorizacaoPanel.add(negarButton);
        autorizacaoPanel.add(relatorioButton);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(autorizacaoPanel, gbc);
        
        // Painel da tabela (já foi criado anteriormente)
        // Layout principal
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        mainSplitPane.setDividerLocation(400);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(mainSplitPane, BorderLayout.CENTER);
        centerPanel.add(pendentesPanel, BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarVisitante());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirVisitante());
        autorizarButton.addActionListener(e -> autorizarVisitante());
        negarButton.addActionListener(e -> negarAutorizacao());
        gerarCrachaButton.addActionListener(e -> gerarCracha());
        relatorioButton.addActionListener(e -> gerarRelatorio());
        imprimirCrachaButton.addActionListener(e -> imprimirCracha());
        
        // Seleção na tabela
        visitantesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarVisitanteSelecionado();
            }
        });
        
        // Mudança de tipo
        tipoComboBox.addActionListener(e -> atualizarCamposPorTipo());
    }
    
    private void salvarVisitante() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome do visitante é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Visitante visitante = visitanteAtual != null ? visitanteAtual : new Visitante();
            visitante.setNome(nomeField.getText().trim());
            visitante.setDataVisita(LocalDate.parse(dataVisitaField.getText().trim()));
            visitante.setGrauSecreto(grauSecretoField.getText().trim());
            visitante.setLojaOrigem(lojaOrigemField.getText().trim());
            visitante.setTelefone(telefoneField.getText().trim());
            visitante.setEmail(emailField.getText().trim());
            visitante.setObservacoes(observacoesField.getText().trim());
            visitante.setNumeroCracha(numeroCrachaField.getText().trim());
            visitante.setTipo((String) tipoComboBox.getSelectedItem());
            visitante.setHistorico(historicoArea.getText().trim());
            
            visitanteDAO.save(visitante);
            
            JOptionPane.showMessageDialog(this, "Visitante salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar visitante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        visitanteAtual = null;
        nomeField.setText("");
        dataVisitaField.setText(LocalDate.now().toString());
        grauSecretoField.setText("");
        lojaOrigemField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        observacoesField.setText("");
        numeroCrachaField.setText("");
        historicoArea.setText("");
        tipoComboBox.setSelectedItem("VISITANTE");
        nomeField.requestFocus();
        atualizarBotoesAcao();
    }
    
    private void excluirVisitante() {
        if (visitanteAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um visitante para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o visitante " + visitanteAtual.getNome() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                visitanteDAO.delete(visitanteAtual.getId());
                JOptionPane.showMessageDialog(this, "Visitante excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir visitante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void autorizarVisitante() {
        if (visitanteAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um visitante para autorizar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!visitanteAtual.necessitaAutorizacao()) {
            JOptionPane.showMessageDialog(this, "Este visitante não necessita de autorização!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String autorizador = JOptionPane.showInputDialog(this, "Autorizado por:");
        if (autorizador != null && !autorizador.trim().isEmpty()) {
            try {
                visitanteDAO.autorizar(visitanteAtual.getId(), autorizador.trim());
                JOptionPane.showMessageDialog(this, "Visitante autorizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarVisitanteSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao autorizar visitante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void negarAutorizacao() {
        if (visitanteAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um visitante para negar autorização!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!visitanteAtual.necessitaAutorizacao()) {
            JOptionPane.showMessageDialog(this, "Este visitante não necessita de autorização!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente negar autorização para " + visitanteAtual.getNome() + "?", 
            "Confirmar Negação", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                visitanteDAO.negarAutorizacao(visitanteAtual.getId());
                JOptionPane.showMessageDialog(this, "Autorização negada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarVisitanteSelecionado();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao negar autorização: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void gerarCracha() {
        try {
            String numeroCracha = visitanteDAO.gerarNumeroCracha();
            numeroCrachaField.setText(numeroCracha);
            JOptionPane.showMessageDialog(this, "Número de crachá gerado: " + numeroCracha, "Crachá Gerado", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar crachá: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void imprimirCracha() {
        if (visitanteAtual == null || numeroCrachaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um visitante e gere um número de crachá!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String crachaText = String.format(
            "VISITANTE\n" +
            "=============\n" +
            "Loja ArteReal\n" +
            "=============\n\n" +
            "Nome: %s\n" +
            "Tipo: %s\n" +
            "Data: %s\n" +
            "Crachá: %s\n\n" +
            "Acesso autorizado",
            visitanteAtual.getNome(),
            visitanteAtual.getTipo().replace("_", " "),
            visitanteAtual.getDataVisita(),
            numeroCrachaField.getText()
        );
        
        JTextArea textArea = new JTextArea(crachaText);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Visualização do Crachá", JOptionPane.INFORMATION_MESSAGE);
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja imprimir este crachá?", "Imprimir Crachá", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Funcionalidade de impressão em desenvolvimento!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void gerarRelatorio() {
        try {
            List<Visitante> visitantes = visitanteDAO.findAll();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("RELATÓRIO DE VISITANTES\n");
            relatorio.append("=====================\n\n");
            
            int total = visitantes.size();
            int autorizados = 0;
            int pendentes = 0;
            
            for (Visitante visitante : visitantes) {
                relatorio.append(String.format("Nome: %s\n", visitante.getNome()));
                relatorio.append(String.format("Tipo: %s\n", visitante.getTipo().replace("_", " ")));
                relatorio.append(String.format("Data Visita: %s\n", visitante.getDataVisita()));
                relatorio.append(String.format("Loja Origem: %s\n", visitante.getLojaOrigem()));
                relatorio.append(String.format("Telefone: %s\n", visitante.getTelefone()));
                relatorio.append(String.format("Status: %s\n", visitante.getStatusFormatado()));
                if (visitante.getNumeroCracha() != null) {
                    relatorio.append(String.format("Crachá: %s\n", visitante.getNumeroCracha()));
                }
                relatorio.append("------------------------\n");
                
                if (visitante.isAutorizado()) {
                    autorizados++;
                } else if (visitante.necessitaAutorizacao()) {
                    pendentes++;
                }
            }
            
            relatorio.append("\nRESUMO\n");
            relatorio.append("======\n");
            relatorio.append(String.format("Total de Visitantes: %d\n", total));
            relatorio.append(String.format("Autorizados: %d\n", autorizados));
            relatorio.append(String.format("Pendentes: %d\n", pendentes));
            relatorio.append(String.format("Regulares: %d\n", total - autorizados - pendentes));
            
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Relatório de Visitantes", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void listarPendentes() {
        try {
            List<Visitante> pendentes = visitanteDAO.findPendentes();
            if (pendentes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Não há visitantes pendentes de autorização!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            StringBuilder lista = new StringBuilder();
            lista.append("VISITANTES PENDENTES DE AUTORIZAÇÃO\n");
            lista.append("===================================\n\n");
            
            for (Visitante visitante : pendentes) {
                lista.append(String.format("• %s - %s (%s)\n", 
                    visitante.getNome(), 
                    visitante.getTipo().replace("_", " "),
                    visitante.getDataVisita()));
            }
            
            JTextArea textArea = new JTextArea(lista.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Visitantes Pendentes", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar pendentes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarVisitanteSelecionado() {
        int selectedRow = visitantesTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                visitanteAtual = visitanteDAO.findById(id);
                if (visitanteAtual != null) {
                    nomeField.setText(visitanteAtual.getNome());
                    dataVisitaField.setText(visitanteAtual.getDataVisita().toString());
                    grauSecretoField.setText(visitanteAtual.getGrauSecreto());
                    lojaOrigemField.setText(visitanteAtual.getLojaOrigem());
                    telefoneField.setText(visitanteAtual.getTelefone());
                    emailField.setText(visitanteAtual.getEmail());
                    observacoesField.setText(visitanteAtual.getObservacoes());
                    numeroCrachaField.setText(visitanteAtual.getNumeroCracha());
                    tipoComboBox.setSelectedItem(visitanteAtual.getTipo());
                    historicoArea.setText(visitanteAtual.getHistorico());
                    atualizarBotoesAcao();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar visitante: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarBotoesAcao() {
        boolean temVisitante = visitanteAtual != null;
        boolean necessitaAutorizacao = temVisitante && visitanteAtual.necessitaAutorizacao();
        
        autorizarButton.setEnabled(necessitaAutorizacao);
        negarButton.setEnabled(necessitaAutorizacao);
        imprimirCrachaButton.setEnabled(temVisitante && visitanteAtual.getNumeroCracha() != null);
    }
    
    private void atualizarCamposPorTipo() {
        String tipo = (String) tipoComboBox.getSelectedItem();
        boolean isIrmaoVisitante = "IRMAO_VISITANTE".equals(tipo);
        
        grauSecretoField.setEnabled(isIrmaoVisitante);
        lojaOrigemField.setEnabled(isIrmaoVisitante);
        
        if (!isIrmaoVisitante) {
            grauSecretoField.setText("");
            lojaOrigemField.setText("");
        }
    }
    
    public void refreshData() {
        try {
            List<Visitante> visitantes = visitanteDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Visitante visitante : visitantes) {
                Object[] row = {
                    visitante.getId(),
                    visitante.getNome(),
                    visitante.getTipo().replace("_", " "),
                    visitante.getDataVisita(),
                    visitante.getLojaOrigem(),
                    visitante.isAutorizado() ? "Sim" : "Não",
                    visitante.getNumeroCracha(),
                    visitante.getStatusFormatado()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar visitantes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
