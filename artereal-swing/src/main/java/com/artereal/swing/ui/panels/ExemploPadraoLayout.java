package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Exemplo completo de como usar a classe PadraoLayout
 * Este painel demonstra todas as funcionalidades da classe PadraoLayout
 */
public class ExemploPadraoLayout extends JPanel {
    
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JTextField campoPesquisa;
    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextArea areaObservacoes;
    private JComboBox<String> comboStatus;
    
    private JButton botaoSalvar;
    private JButton botaoNovo;
    private JButton botaoEditar;
    private JButton botaoExcluir;
    private JButton botaoLimpar;
    private JButton botaoPesquisar;
    
    public ExemploPadraoLayout() {
        inicializarComponentes();
        setupLayout();
        setupEvents();
    }
    
    private void inicializarComponentes() {
        // Inicializar componentes
        campoPesquisa = new JTextField();
        campoCodigo = new JTextField();
        campoNome = new JTextField();
        campoEmail = new JTextField();
        areaObservacoes = new JTextArea(3, 20);
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Pendente"});
        
        botaoSalvar = PadraoLayout.criarBotao("Salvar", PadraoLayout.COR_BOTAO_SALVAR);
        botaoNovo = PadraoLayout.criarBotao("Novo", PadraoLayout.COR_BOTAO_NOVO);
        botaoEditar = PadraoLayout.criarBotao("Editar", PadraoLayout.COR_BOTAO_EDITAR);
        botaoExcluir = PadraoLayout.criarBotao("Excluir", PadraoLayout.COR_BOTAO_EXCLUIR);
        botaoLimpar = PadraoLayout.criarBotao("Limpar", PadraoLayout.COR_BOTAO_LIMPAR);
        botaoPesquisar = PadraoLayout.criarBotao("Buscar", PadraoLayout.COR_BOTAO_PESQUISAR);
        
        // Configurar tabela
        tableModel = new DefaultTableModel(new Object[]{"Código", "Nome", "Email", "Status"}, 0);
        tabela = new JTable(tableModel);
        PadraoLayout.configurarTabela(tabela);
        
        // Configurar campos
        campoCodigo.setEditable(false);
        campoCodigo.setBackground(PadraoLayout.COR_PAINEL);
        areaObservacoes.setLineWrap(true);
        areaObservacoes.setWrapStyleWord(true);
    }
    
    private void setupLayout() {
        // Aplicar layout padrão ao painel principal
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Criar header usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader(
            "📋 Exemplo de Layout Padrão", 
            "Demonstração do uso da classe PadraoLayout"
        );
        add(headerPanel, BorderLayout.NORTH);
        
        // Criar painel de pesquisa usando PadraoLayout
        JPanel pesquisaPanel = PadraoLayout.criarPainelPesquisa(campoPesquisa, botaoPesquisar);
        
                
        // Painel esquerdo com pesquisa e tabela
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PadraoLayout.COR_FUNDO);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PadraoLayout.COR_FUNDO);
        topPanel.add(pesquisaPanel, BorderLayout.NORTH);
        topPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        
        leftPanel.add(topPanel, BorderLayout.CENTER);
        
        // Painel direito com formulário
        JPanel rightPanel = criarFormularioPadrao();
        
        // Criar split pane principal usando PadraoLayout
        JSplitPane splitPane = PadraoLayout.criarSplitPaneVertical(leftPanel, rightPanel);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel criarFormularioPadrao() {
        // Header do formulário
        JPanel formHeaderPanel = new JPanel(new BorderLayout());
        formHeaderPanel.setBackground(PadraoLayout.COR_PAINEL);
        formHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel formTitleLabel = new JLabel("📝 Dados do Registro", SwingConstants.LEFT);
        formTitleLabel.setFont(PadraoLayout.FONTE_TITULO);
        formTitleLabel.setForeground(PadraoLayout.COR_TEXTO_TITULO);
        
        formHeaderPanel.add(formTitleLabel, BorderLayout.CENTER);
        
        // Container principal do formulário
        JPanel mainFormContainer = new JPanel(new BorderLayout());
        mainFormContainer.setBackground(Color.WHITE);
        
        // Grupo 1: Dados Básicos
        JPanel basicosPanel = PadraoLayout.criarGrupoFormulario("📅 Dados Básicos");
        JPanel basicosContent = new JPanel(PadraoLayout.criarLayoutGrupo());
        basicosContent.setBackground(Color.WHITE);
        
        basicosContent.add(new JLabel("Código:"));
        basicosContent.add(campoCodigo);
        basicosContent.add(new JLabel("Nome:"));
        basicosContent.add(campoNome);
        basicosContent.add(new JLabel("Email:"));
        basicosContent.add(campoEmail);
        basicosContent.add(new JLabel("Status:"));
        basicosContent.add(comboStatus);
        
        basicosPanel.add(basicosContent);
        
        // Grupo 2: Observações
        JPanel observacoesPanel = PadraoLayout.criarGrupoFormulario("📝 Observações");
        JPanel observacoesContent = new JPanel(new BorderLayout());
        observacoesContent.setBackground(Color.WHITE);
        
        areaObservacoes.setBorder(PadraoLayout.BORDA_CAMPO);
        observacoesContent.add(new JLabel("Observações:"), BorderLayout.NORTH);
        observacoesContent.add(new JScrollPane(areaObservacoes), BorderLayout.CENTER);
        
        observacoesPanel.add(observacoesContent);
        
        // Organizar grupos verticalmente
        JPanel layoutGroupsPanel = new JPanel(new BorderLayout());
        layoutGroupsPanel.setBackground(Color.WHITE);
        
        JPanel upperGroups = new JPanel(new BorderLayout());
        upperGroups.setBackground(Color.WHITE);
        upperGroups.add(basicosPanel, BorderLayout.NORTH);
        
        JPanel combinedGroups = new JPanel(new BorderLayout());
        combinedGroups.setBackground(Color.WHITE);
        combinedGroups.add(upperGroups, BorderLayout.NORTH);
        combinedGroups.add(observacoesPanel, BorderLayout.CENTER);
        
        // Adicionar scroll ao formulário
        JScrollPane formScrollPane = new JScrollPane(combinedGroups);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        mainFormContainer.add(formScrollPane, BorderLayout.CENTER);
        
        // Painel de botões usando PadraoLayout
        JPanel botoesPanel = PadraoLayout.criarPainelBotoes(
            botaoSalvar, botaoNovo, botaoEditar, botaoExcluir, botaoLimpar
        );
        
        // Montar painel direito completo
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        
        rightPanel.add(formHeaderPanel, BorderLayout.NORTH);
        rightPanel.add(mainFormContainer, BorderLayout.CENTER);
        rightPanel.add(botoesPanel, BorderLayout.SOUTH);
        
        return rightPanel;
    }
    
    private void setupEvents() {
        botaoSalvar.addActionListener(e -> salvarRegistro());
        botaoNovo.addActionListener(e -> limparFormulario());
        botaoEditar.addActionListener(e -> editarRegistro());
        botaoExcluir.addActionListener(e -> excluirRegistro());
        botaoLimpar.addActionListener(e -> limparFormulario());
        botaoPesquisar.addActionListener(e -> pesquisarRegistros());
        
        // Seleção na tabela
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() >= 0) {
                carregarRegistroSelecionado();
            }
        });
    }
    
    private void salvarRegistro() {
        // Lógica para salvar registro
        JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editarRegistro() {
        // Lógica para editar registro
        JOptionPane.showMessageDialog(this, "Registro em modo de edição", "Editar", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void excluirRegistro() {
        // Lógica para excluir registro
        int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este registro?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Registro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoEmail.setText("");
        areaObservacoes.setText("");
        comboStatus.setSelectedIndex(0);
    }
    
    private void pesquisarRegistros() {
        // Lógica para pesquisar registros
        String termo = campoPesquisa.getText().trim();
        JOptionPane.showMessageDialog(this, "Pesquisando por: " + termo, "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void carregarRegistroSelecionado() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow >= 0) {
            // Carregar dados do registro selecionado
            campoCodigo.setText("001");
            campoNome.setText("Exemplo Nome");
            campoEmail.setText("exemplo@email.com");
            areaObservacoes.setText("Observações do registro selecionado");
            comboStatus.setSelectedItem("Ativo");
        }
    }
    
    // Método principal para teste
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Exemplo PadraoLayout");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            
            frame.add(new ExemploPadraoLayout());
            frame.setVisible(true);
        });
    }
}
