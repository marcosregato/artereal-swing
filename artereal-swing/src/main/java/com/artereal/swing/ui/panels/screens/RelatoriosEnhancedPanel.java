package com.artereal.swing.ui.panels.screens;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Painel de Relatórios otimizado com UX moderna
 */
public class RelatoriosEnhancedPanel extends BaseEnhancedScreenPanel {
    
    // Campos de filtro
    private JComboBox<String> tipoRelatorioCombo;
    private JTextField dataInicioField;
    private JTextField dataFimField;
    private JComboBox<String> formatoCombo;
    private JCheckBox incluirGraficosCheckBox;
    
    public RelatoriosEnhancedPanel() {
        super();
        initializeFields();
        setupRelatoriosSections();
    }
    
    /**
     * Inicializa campos de relatório
     */
    private void initializeFields() {
        tipoRelatorioCombo = new JComboBox<>(new String[]{
            "Relatório Geral", "Relatório de Irmãos", "Relatório Financeiro", 
            "Relatório de Sessões", "Relatório de Biblioteca", "Relatório de Frequência"
        });
        dataInicioField = new JTextField(12);
        dataFimField = new JTextField(12);
        formatoCombo = new JComboBox<>(new String[]{"PDF", "Excel", "HTML"});
        incluirGraficosCheckBox = new JCheckBox("Incluir gráficos");
        
        // Estilização
        dataInicioField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        dataFimField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Valores padrão
        incluirGraficosCheckBox.setSelected(true);
        formatoCombo.setSelectedItem("PDF");
        tipoRelatorioCombo.setSelectedItem("Relatório Geral");
    }
    
    /**
     * Configura seções de relatórios
     */
    private void setupRelatoriosSections() {
        // Seção 1: Relatórios Disponíveis
        addSection(new ScreenSection(
            "📊 RELATÓRIOS DISPONÍVEIS",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("📋", "Geral", "Completo", "Visão geral do sistema", this::gerarRelatorioGeral),
            new ScreenCard("👥", "Irmãos", "45", "Membros e dados", this::gerarRelatorioIrmaos),
            new ScreenCard("💰", "Financeiro", "R$ 2.450", "Movimentações", this::gerarRelatorioFinanceiro),
            new ScreenCard("📝", "Sessões", "156", "Atividades", this::gerarRelatorioSessoes)
        ));
        
        // Seção 2: Relatórios Específicos
        addSection(new ScreenSection(
            "📈 RELATÓRIOS ESPECÍFICOS",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("📚", "Biblioteca", "234", "Livros e empréstimos", this::gerarRelatorioBiblioteca),
            new ScreenCard("📅", "Frequência", "85%", "Presença", this::gerarRelatorioFrequencia),
            new ScreenCard("🏢", "Lojas", "8", "Unidades", this::gerarRelatorioLojas),
            new ScreenCard("📊", "Estatísticas", "Detalhado", "Análises", this::gerarRelatorioEstatisticas)
        ));
        
        // Seção 3: Configurações
        addSection(new ScreenSection(
            "⚙️ CONFIGURAÇÕES DO RELATÓRIO",
            ScreenSection.LayoutType.FORM,
            new ScreenCard("", "Personalizar opções de geração", "")
        ));
        
        // Seção 4: Ações
        addSection(new ScreenSection(
            "⚡ AÇÕES RÁPIDAS",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("🔄", "Atualizar", "Dados", null, this::atualizarDados),
            new ScreenCard("📤", "Exportar", "Tudo", null, this::exportarTudo),
            new ScreenCard("📧", "Enviar", "Email", null, this::enviarEmail),
            new ScreenCard("📋", "Agendar", "Relatório", null, this::agendarRelatorio)
        ));
    }
    
    @Override
    protected JPanel createFormLayout(ScreenSection section) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BACKGROUND);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo de Relatório
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📊 Tipo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(tipoRelatorioCombo, gbc);
        
        // Período
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📅 Data Início:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(dataInicioField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📅 Data Fim:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(dataFimField, gbc);
        
        // Formato
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formPanel.add(new JLabel("📄 Formato:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(formatoCombo, gbc);
        
        // Opções
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        formPanel.add(new JLabel("⚙️ Opções:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        formPanel.add(incluirGraficosCheckBox, gbc);
        
        // Botão Gerar
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JButton gerarButton = createActionButton("Gerar Relatório", "📊");
        gerarButton.addActionListener(e -> gerarRelatorioPersonalizado());
        formPanel.add(gerarButton, gbc);
        
        return formPanel;
    }
    
    /**
     * Ações dos cards
     */
    private void gerarRelatorioGeral(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório Geral...", 
            "Relatório Geral", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioIrmaos(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório de Irmãos...", 
            "Relatório de Irmãos", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioFinanceiro(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório Financeiro...", 
            "Relatório Financeiro", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioSessoes(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório de Sessões...", 
            "Relatório de Sessões", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioBiblioteca(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório de Biblioteca...", 
            "Relatório de Biblioteca", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioFrequencia(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório de Frequência...", 
            "Relatório de Frequência", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioLojas(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório de Lojas...", 
            "Relatório de Lojas", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioEstatisticas(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Gerando Relatório de Estatísticas...", 
            "Relatório de Estatísticas", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorioPersonalizado() {
        String tipo = (String) tipoRelatorioCombo.getSelectedItem();
        String formato = (String) formatoCombo.getSelectedItem();
        String dataInicio = dataInicioField.getText();
        String dataFim = dataFimField.getText();
        boolean incluirGraficos = incluirGraficosCheckBox.isSelected();
        
        JOptionPane.showMessageDialog(this, 
            "Gerando relatório personalizado:\n" +
            "Tipo: " + tipo + "\n" +
            "Período: " + dataInicio + " a " + dataFim + "\n" +
            "Formato: " + formato + "\n" +
            "Gráficos: " + (incluirGraficos ? "Sim" : "Não"), 
            "Relatório Personalizado", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void atualizarDados(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Atualizando dados para relatórios...", 
            "Atualizar", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportarTudo(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Exportando todos os relatórios...", 
            "Exportar Tudo", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void enviarEmail(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Enviando relatórios por e-mail...", 
            "Enviar E-mail", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void agendarRelatorio(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Agendando geração automática...", 
            "Agendar Relatório", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected String getScreenTitle() {
        return "📊 Relatórios";
    }
    
    @Override
    protected String getScreenSubtitle() {
        return "Gere e visualize relatórios do sistema ArteReal";
    }
    
    @Override
    protected JPanel createHeaderActions() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(HEADER_COLOR);
        
        JButton previewButton = createActionButton("Visualizar", "👁️");
        previewButton.addActionListener(e -> visualizarRelatorio());
        
        JButton scheduleButton = createActionButton("Agendar", "📅");
        scheduleButton.addActionListener(e -> abrirAgendamento());
        
        actionsPanel.add(previewButton);
        actionsPanel.add(scheduleButton);
        
        return actionsPanel;
    }
    
    /**
     * Visualiza relatório
     */
    private void visualizarRelatorio() {
        JOptionPane.showMessageDialog(this, 
            "Abrindo visualização de relatório...", 
            "Visualizar", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Abre agendamento
     */
    private void abrirAgendamento() {
        JOptionPane.showMessageDialog(this, 
            "Abrindo agenda de relatórios...", 
            "Agendamento", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
