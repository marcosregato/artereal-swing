package com.artereal.swing.ui.panels.visitantes;

import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de formulário para Controle de Visitantes com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class VisitantesFormPanel extends BaseEnhancedFormPanel {
    
    // Dados Pessoais
    private JTextField nomeField;
    private JTextField dataVisitaField;
    private JTextField grauSecretoField;
    private JTextField lojaOrigemField;
    
    // Contato
    private JTextField telefoneField;
    private JTextField emailField;
    
    // Informações de Visita
    private JTextField numeroCrachaField;
    private JComboBox<String> tipoComboBox;
    
    // Informações Adicionais
    private JTextArea observacoesArea;
    private JTextArea historicoArea;
    
    public VisitantesFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados Pessoais
        nomeField = createTextField(40);
        dataVisitaField = createTextField(12);
        grauSecretoField = createTextField(20);
        lojaOrigemField = createTextField(30);
        
        // Contato
        telefoneField = createTextField(15);
        emailField = createTextField(40);
        
        // Informações de Visita
        numeroCrachaField = createTextField(15);
        tipoComboBox = new JComboBox<>(new String[]{
            "VISITANTE", "CONVIDADO", "IRMAO_VISITANTE", "VISITANTE_ESPECIAL"
        });
        
        // Informações Adicionais
        observacoesArea = createTextArea(3, 50);
        historicoArea = createTextArea(4, 50);
        
        // Aplicar estilização específica
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização específica aos campos
     */
    private void applyFieldStyling() {
        // Dados Pessoais
        applyFieldStyling(nomeField, "nome");
        applyFieldStyling(dataVisitaField, "data");
        applyFieldStyling(grauSecretoField, "texto");
        applyFieldStyling(lojaOrigemField, "texto");
        
        // Contato
        applyFieldStyling(telefoneField, "telefone");
        applyFieldStyling(emailField, "email");
        
        // Informações de Visita
        applyFieldStyling(numeroCrachaField, "texto");
        applyFieldStyling(tipoComboBox, null);
        
        // Informações Adicionais
        // Áreas de texto já são estilizadas pelo BaseEnhancedFormPanel
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "👤 DADOS PESSOAIS",
                new FormSubsection(
                    "🆔 Identificação do Visitante",
                    new FormField("🏷️ Nome:", nomeField, 0, 0, 1, 0.5),
                    new FormField("📅 Data Visita:", dataVisitaField, 0, 1, 1, 0.5)
                ),
                new FormSubsection(
                    "🔐 Informações Maçônicas",
                    new FormField("🔷 Grau Secreto:", grauSecretoField, 1, 0, 1, 0.5),
                    new FormField("🏛️ Loja Origem:", lojaOrigemField, 1, 1, 1, 0.5)
                )
            ),
            new FormSection(
                "📞 CONTATO",
                new FormSubsection(
                    "📱 Informações de Contato",
                    new FormField("📞 Telefone:", telefoneField, 0, 0, 1, 0.4),
                    new FormField("📧 E-mail:", emailField, 0, 1, 1, 0.6)
                )
            ),
            new FormSection(
                "🎫 INFORMAÇÕES DE VISITA",
                new FormSubsection(
                    "📋 Dados da Visita",
                    new FormField("🏷️ Tipo:", tipoComboBox, 0, 0, 1, 0.4),
                    new FormField("🔢 Nº Crachá:", numeroCrachaField, 0, 1, 1, 0.6)
                )
            ),
            new FormSection(
                "📝 INFORMAÇÕES ADICIONAIS",
                new FormSubsection(
                    "📋 Observações",
                    new FormField("", createObservationsPanel(observacoesArea), 0, 0, 2, 1.0)
                ),
                new FormSubsection(
                    "📜 Histórico",
                    new FormField("", createTextAreaPanel("📜 Histórico do Visitante:", historicoArea), 0, 0, 2, 1.0)
                )
            )
        };
        
        addFormGroup(sections);
    }
    
    /**
     * Cria painel de área de texto com título
     */
    private JPanel createTextAreaPanel(String title, JTextArea textArea) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FIELD_LABEL_FONT);
        titleLabel.setForeground(FIELD_LABEL_COLOR);
        container.add(titleLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    @Override
    public void clearForm() {
        // Dados Pessoais
        nomeField.setText("");
        dataVisitaField.setText("");
        grauSecretoField.setText("");
        lojaOrigemField.setText("");
        
        // Contato
        telefoneField.setText("");
        emailField.setText("");
        
        // Informações de Visita
        numeroCrachaField.setText("");
        tipoComboBox.setSelectedIndex(0);
        
        // Informações Adicionais
        observacoesArea.setText("");
        historicoArea.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getNomeField() { return nomeField; }
    public JTextField getDataVisitaField() { return dataVisitaField; }
    public JTextField getGrauSecretoField() { return grauSecretoField; }
    public JTextField getLojaOrigemField() { return lojaOrigemField; }
    public JTextField getTelefoneField() { return telefoneField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getNumeroCrachaField() { return numeroCrachaField; }
    public JComboBox<String> getTipoComboBox() { return tipoComboBox; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
    public JTextArea getHistoricoArea() { return historicoArea; }
}
