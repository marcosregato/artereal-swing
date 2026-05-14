package com.artereal.swing.ui.panels.sessoes;

import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de formulário para Gestão de Sessões Maçônicas com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class SessoesEnhancedFormPanel extends BaseEnhancedFormPanel {
    
    // Dados Principais
    private JTextField codigoField;
    private JTextField dataHoraField;
    private JTextField localField;
    
    // Diretoria
    private JTextField presidenteField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
    private JTextField oradorField;
    private JTextField temaField;
    
    // Participação
    private JTextField presentesField;
    private JTextField visitantesField;
    
    // Informações Adicionais
    private JTextArea pautaArea;
    private JTextArea observacoesArea;
    
    public SessoesEnhancedFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados Principais
        codigoField = createTextField(12);
        dataHoraField = createTextField(20);
        localField = createTextField(50);
        
        // Diretoria
        presidenteField = createTextField(40);
        secretarioField = createTextField(40);
        tesoureiroField = createTextField(40);
        oradorField = createTextField(40);
        temaField = createTextField(50);
        
        // Participação
        presentesField = createTextField(8);
        visitantesField = createTextField(8);
        
        // Informações Adicionais
        pautaArea = createTextArea(4, 60);
        observacoesArea = createTextArea(3, 60);
        
        // Aplicar estilização específica
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização específica aos campos
     */
    private void applyFieldStyling() {
        // Dados Principais
        applyFieldStyling(codigoField, "codigo");
        applyFieldStyling(dataHoraField, "data");
        applyFieldStyling(localField, "texto");
        
        // Diretoria
        applyFieldStyling(presidenteField, "texto");
        applyFieldStyling(secretarioField, "texto");
        applyFieldStyling(tesoureiroField, "texto");
        applyFieldStyling(oradorField, "texto");
        applyFieldStyling(temaField, "texto");
        
        // Participação
        applyFieldStyling(presentesField, "numero");
        applyFieldStyling(visitantesField, "numero");
        
        // Informações Adicionais
        // Áreas de texto já são estilizadas pelo BaseEnhancedFormPanel
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "🏛️ DADOS PRINCIPAIS",
                new FormSubsection(
                    "📋 Identificação da Sessão",
                    new FormField("🔢 Código:", codigoField, 0, 0, 1, 0.2),
                    new FormField("📅 Data e Hora:", dataHoraField, 0, 1, 1, 0.8)
                ),
                new FormSubsection(
                    "📍 Localização",
                    new FormField("🏛️ Local:", localField, 1, 0, 1, 1.0)
                )
            ),
            new FormSection(
                "👥 DIRETORIA",
                new FormSubsection(
                    "🏛️ Mesa Diretora",
                    new FormField("👑 Presidente:", presidenteField, 0, 0, 1, 0.5),
                    new FormField("📝 Secretário:", secretarioField, 0, 1, 1, 0.5)
                ),
                new FormSubsection(
                    "📚 Tesouraria",
                    new FormField("💰 Tesoureiro:", tesoureiroField, 1, 0, 1, 0.5),
                    new FormField("👂 Orador:", oradorField, 1, 1, 1, 0.5)
                )
            ),
            new FormSection(
                "📋 DADOS DA SESSÃO",
                new FormSubsection(
                    "🎯 Tema",
                    new FormField("🎯 Tema:", temaField, 0, 0, 2, 1.0)
                ),
                new FormSubsection(
                    "👥 Participação",
                    new FormField("👥 Presentes:", presentesField, 0, 0, 1, 0.3),
                    new FormField("👥 Visitantes:", visitantesField, 0, 1, 1, 0.3)
                )
            ),
            new FormSection(
                "📝 INFORMAÇÕES ADICIONAIS",
                new FormSubsection(
                    "📋 Pauta",
                    new FormField("", createTextAreaPanel("📋 Pauta da Sessão:", pautaArea), 0, 0, 2, 1.0)
                ),
                new FormSubsection(
                    "📝 Observações",
                    new FormField("", createTextAreaPanel("📝 Observações Gerais:", observacoesArea), 0, 0, 2, 1.0)
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
        // Dados Principais
        codigoField.setText("");
        dataHoraField.setText("");
        localField.setText("");
        
        // Diretoria
        presidenteField.setText("");
        secretarioField.setText("");
        tesoureiroField.setText("");
        oradorField.setText("");
        temaField.setText("");
        
        // Participação
        presentesField.setText("");
        visitantesField.setText("");
        
        // Informações Adicionais
        pautaArea.setText("");
        observacoesArea.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getDataHoraField() { return dataHoraField; }
    public JTextField getLocalField() { return localField; }
    public JTextField getPresidenteField() { return presidenteField; }
    public JTextField getSecretarioField() { return secretarioField; }
    public JTextField getTesoureiroField() { return tesoureiroField; }
    public JTextField getOradorField() { return oradorField; }
    public JTextField getTemaField() { return temaField; }
    public JTextField getPresentesField() { return presentesField; }
    public JTextField getVisitantesField() { return visitantesField; }
    public JTextArea getPautaArea() { return pautaArea; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
}
