package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.VisitanteDAO;
import com.artereal.swing.model.Visitante;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private JTextField pesquisarField;
    private JButton pesquisarButton;
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
        nomeField = new JTextField();
        dataVisitaField = new JTextField();
        grauSecretoField = new JTextField();
        lojaOrigemField = new JTextField();
        telefoneField = new JTextField();
        emailField = new JTextField();
        observacoesField = new JTextField();
        numeroCrachaField = new JTextField();
        numeroCrachaField.setEditable(false);
        tipoComboBox = new JComboBox<>(new String[]{"VISITANTE", "CONVIDADO", "IRMAO_VISITANTE"});
        historicoArea = new JTextArea(3, 40);
        historicoArea.setLineWrap(true);
        historicoArea.setWrapStyleWord(true);
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        autorizarButton = PadraoLayout.criarBotao("Autorizar", new Color(152, 251, 152)); // Verde menta
        negarButton = PadraoLayout.criarBotao("Negar Autorização", new Color(255, 182, 193)); // Rosa pastel
        gerarCrachaButton = PadraoLayout.criarBotao("Gerar Crachá", new Color(173, 216, 230)); // Azul pastel
        relatorioButton = PadraoLayout.criarBotao("Relatório", new Color(200, 200, 255)); // Azul claro
        imprimirCrachaButton = PadraoLayout.criarBotao("Imprimir Crachá", new Color(255, 250, 205)); // Amarelo pastel
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesUsuarios(salvarButton, novoButton, excluirButton); // Usa método de usuários (3 botões)
        PadraoLayout.aplicarCoresPastelBotao(autorizarButton, "salvar"); // Usa cor de salvar para autorizar
        PadraoLayout.aplicarCoresPastelBotao(negarButton, "excluir"); // Usa cor de excluir para negar
        PadraoLayout.aplicarCoresPastelBotao(gerarCrachaButton, "novo"); // Usa cor de novo para gerar crachá
        PadraoLayout.aplicarCoresPastelBotao(relatorioButton, "novo"); // Usa cor de novo para relatório
        PadraoLayout.aplicarCoresPastelBotao(imprimirCrachaButton, "limpar"); // Usa cor de limpar para imprimir
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Data atual como padrão
        dataVisitaField.setText(LocalDate.now().toString());
        
        visitanteAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("🚪 Controle de Visitantes", "Registro e controle de acesso de visitantes");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(PadraoLayout.COR_PAINEL);
        formPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel esquerdo - Tabela
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(245, 245, 250));
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(new Color(100, 100, 120));
        
        JTextField pesquisarField = new JTextField();
        pesquisarField.setPreferredSize(new Dimension(200, 30));
        pesquisarField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 190)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        JButton pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        pesquisarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 160, 221), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        pesquisarButton.setFocusPainted(false);
        pesquisarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Criar painel de pesquisa
        JLabel pesquisaLabel = PadraoLayout.criarLabelFormulario("🔍 Pesquisar:");
        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pesquisaPanel.setBackground(Color.WHITE);
        pesquisaPanel.add(pesquisaLabel);
        pesquisaPanel.add(pesquisarField);
        pesquisaPanel.add(pesquisarButton);
        
        // Tabela estilizada
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(visitantesTable);
        
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
                JButton listarPendentesButton = PadraoLayout.criarBotao("Listar Pendentes", new Color(255, 250, 205)); // Amarelo pastel
                listarPendentesButton.addActionListener(e -> listarPendentes());
                pendentesPanel.add(listarPendentesButton, BorderLayout.SOUTH);
            }
        } catch (SQLException e) {
            pendentesPanel.add(new JLabel("Erro ao carregar pendentes"));
        }
        
        // Painel de formulário usando BoxLayout vertical
        JPanel visitantesFormPanel = PadraoLayout.criarGrupoFormulario("👤 Dados do Visitante");
        JPanel formContent = new JPanel();
        formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
        formContent.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados do Visitante
        JPanel dadosVisitantePanel = new JPanel(new BorderLayout());
        dadosVisitantePanel.setBackground(Color.WHITE);
        dadosVisitantePanel.setBorder(BorderFactory.createTitledBorder("👤 Dados do Visitante"));
        
        JPanel dadosVisitanteContent = new JPanel();
        dadosVisitanteContent.setLayout(new BoxLayout(dadosVisitanteContent, BoxLayout.Y_AXIS));
        dadosVisitanteContent.setBackground(Color.WHITE);
        
        // Primeira linha: Nome e Data Visita
        JPanel primeiraLinhaVisitantePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaVisitantePanel.setBackground(Color.WHITE);
        
        // Campo Nome
        JPanel nomeVisitantePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nomeVisitantePanel.setBackground(Color.WHITE);
        nomeVisitantePanel.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoNome(nomeField);
        nomeVisitantePanel.add(nomeField);
        
        // Campo Data Visita
        JPanel dataVisitaVisitantePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dataVisitaVisitantePanel.setBackground(Color.WHITE);
        dataVisitaVisitantePanel.add(PadraoLayout.criarLabelFormulario("Data Visita:"));
        PadraoLayout.estilizarCampoData(dataVisitaField);
        dataVisitaVisitantePanel.add(dataVisitaField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaVisitantePanel.add(nomeVisitantePanel);
        primeiraLinhaVisitantePanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaVisitantePanel.add(dataVisitaVisitantePanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        dadosVisitanteContent.add(primeiraLinhaVisitantePanel);
        dadosVisitanteContent.add(Box.createVerticalStrut(5)); // Espaço vertical
        
        // Segunda linha: Telefone e Email
        JPanel segundaLinhaVisitantePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segundaLinhaVisitantePanel.setBackground(Color.WHITE);
        
        // Campo Telefone
        JPanel telefoneVisitantePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        telefoneVisitantePanel.setBackground(Color.WHITE);
        telefoneVisitantePanel.add(PadraoLayout.criarLabelFormulario("Telefone:"));
        PadraoLayout.estilizarCampoTelefone(telefoneField);
        telefoneVisitantePanel.add(telefoneField);
        
        // Campo Email
        JPanel emailVisitantePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        emailVisitantePanel.setBackground(Color.WHITE);
        emailVisitantePanel.add(PadraoLayout.criarLabelFormulario("Email:"));
        PadraoLayout.estilizarCampoEmail(emailField);
        emailVisitantePanel.add(emailField);
        
        // Adicionar os painéis à segunda linha
        segundaLinhaVisitantePanel.add(telefoneVisitantePanel);
        segundaLinhaVisitantePanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        segundaLinhaVisitantePanel.add(emailVisitantePanel);
        
        // Adicionar o painel da segunda linha ao conteúdo
        dadosVisitanteContent.add(segundaLinhaVisitantePanel);
        
        dadosVisitantePanel.add(dadosVisitanteContent, BorderLayout.CENTER);
        formContent.add(dadosVisitantePanel);
        
        // Painel de observações usando PadraoLayout
        JPanel obsPanel = PadraoLayout.criarPainelTextArea("📝 Observações:", new JTextArea(3, 40));
        
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.add(formContent, BorderLayout.NORTH);
        formContainer.add(obsPanel, BorderLayout.CENTER);
        
        visitantesFormPanel.add(formContainer);
        
        // Adicionar campos adicionais ao formulário
        JPanel camposAdicionais = new JPanel(PadraoLayout.criarLayoutFormulario());
        camposAdicionais.setBackground(Color.WHITE);
        
        camposAdicionais.add(PadraoLayout.criarLabelFormulario("Grau Secreto:"));
        PadraoLayout.estilizarCampoTexto(grauSecretoField);
        camposAdicionais.add(grauSecretoField);
        camposAdicionais.add(PadraoLayout.criarLabelFormulario("Loja Origem:"));
        PadraoLayout.estilizarCampoTexto(lojaOrigemField);
        camposAdicionais.add(lojaOrigemField);
        
        // Painel de crachá
        JPanel crachaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        crachaPanel.add(new JLabel("Crachá:"));
        crachaPanel.add(numeroCrachaField);
        crachaPanel.add(gerarCrachaButton);
        crachaPanel.add(imprimirCrachaButton);
        
                
        // Botões de autorização
        JPanel autorizacaoPanel = new JPanel(new FlowLayout());
        autorizacaoPanel.setBorder(BorderFactory.createTitledBorder("Autorização"));
        autorizacaoPanel.add(autorizarButton);
        autorizacaoPanel.add(negarButton);
        autorizacaoPanel.add(relatorioButton);
        
        visitantesFormPanel.add(autorizacaoPanel, BorderLayout.SOUTH);
        
        // Painel de tabela usando PadraoLayout
        JPanel visitantesTabelaPanel = new JPanel(new BorderLayout());
        visitantesTabelaPanel.setBackground(PadraoLayout.COR_PAINEL);
        visitantesTabelaPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de estatísticas
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(pendentesPanel, BorderLayout.SOUTH);
        
        // Tabela estilizada usando PadraoLayout
        PadraoLayout.configurarTabela(visitantesTable);
        JScrollPane tableScrollPane = new JScrollPane(visitantesTable);
        
        visitantesTabelaPanel.add(topPanel, BorderLayout.NORTH);
        visitantesTabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima, Tabela abaixo
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, visitantesFormPanel, visitantesTabelaPanel);
        verticalSplitPane.setDividerLocation(350);
        verticalSplitPane.setResizeWeight(0.4);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
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
