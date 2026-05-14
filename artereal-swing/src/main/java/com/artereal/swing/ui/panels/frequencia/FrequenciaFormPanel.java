package com.artereal.swing.ui.panels.frequencia;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;

/**
 * Painel de formulário para gestão de Frequência com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class FrequenciaFormPanel extends BaseEnhancedFormPanel {
    
    // Dados do Irmão
    private JTextField nomeField;
    private JTextField grauField;
    
    // Controle de Frequência
    private JTextField presencasField;
    private JTextField faltasField;
    private JTextField totalField;
    private JTextField percentualField;
    
    public FrequenciaFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados do Irmão
        nomeField = createTextField(40);
        grauField = createTextField(20);
        
        // Controle de Frequência
        presencasField = createTextField(10);
        faltasField = createTextField(10);
        totalField = createTextField(10);
        percentualField = createTextField(10);
        
        // Aplicar estilização
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados do Irmão
        applyFieldStyling(nomeField, "nome");
        PadraoLayout.estilizarCampoGrauMasonicoFrequencia(grauField);
        
        // Controle de Frequência
        PadraoLayout.estilizarCampoTexto(presencasField);
        PadraoLayout.estilizarCampoTexto(faltasField);
        PadraoLayout.estilizarCampoTexto(totalField);
        PadraoLayout.estilizarCampoTexto(percentualField);
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "👤 DADOS DO IRMÃO",
                new FormSubsection(
                    "🆔 Identificação",
                    new FormField("👥 Nome:", nomeField, 0, 0, 1, 0.6),
                    new FormField("⭐ Grau:", grauField, 0, 1, 1, 0.4)
                )
            ),
            new FormSection(
                "📊 CONTROLE DE FREQUÊNCIA",
                new FormSubsection(
                    "📈 Estatísticas",
                    new FormField("✅ Presenças:", presencasField, 0, 0, 1, 0.25),
                    new FormField("❌ Faltas:", faltasField, 0, 1, 1, 0.25),
                    new FormField("📝 Total:", totalField, 0, 2, 1, 0.25),
                    new FormField("📊 Percentual:", percentualField, 0, 3, 1, 0.25)
                )
            )
        };
        
        addFormGroup(sections);
    }
    
    /**
     * Limpa todos os campos do formulário
     */
    @Override
    public void clearForm() {
        // Dados do Irmão
        nomeField.setText("");
        grauField.setText("");
        
        // Controle de Frequência
        presencasField.setText("");
        faltasField.setText("");
        totalField.setText("");
        percentualField.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getNomeField() { return nomeField; }
    public JTextField getGrauField() { return grauField; }
    public JTextField getPresencasField() { return presencasField; }
    public JTextField getFaltasField() { return faltasField; }
    public JTextField getTotalField() { return totalField; }
    public JTextField getPercentualField() { return percentualField; }
}
