package com.artereal.swing.ui.panels.afastamentos;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;

/**
 * Painel de formulário para gestão de Afastamentos com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class AfastamentosFormPanel extends BaseEnhancedFormPanel {
    
    // Dados do Irmão
    private JTextField codigoIrmaoField;
    private JTextField descricaoField;
    
    // Período do Afastamento
    private JTextField dataInicialField;
    private JTextField dataFinalField;
    
    // Detalhes do Afastamento
    private JComboBox<String> motivoComboBox;
    private JTextField documentoField;
    
    public AfastamentosFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados do Irmão
        codigoIrmaoField = createTextField(15);
        descricaoField = createTextField(40);
        
        // Período do Afastamento
        dataInicialField = createTextField(10);
        dataFinalField = createTextField(10);
        
        // Detalhes do Afastamento
        motivoComboBox = new JComboBox<>(new String[]{
            "DOENÇA", "VIAGEM", "FAMILIAR", "PROFISSIONAL", "ESTUDO", "OUTROS"
        });
        documentoField = createTextField(30);
        
        // Aplicar estilização
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados do Irmão
        PadraoLayout.estilizarCampoCodigo(codigoIrmaoField);
        PadraoLayout.estilizarCampoDescricao(descricaoField);
        
        // Período do Afastamento
        PadraoLayout.estilizarCampoData(dataInicialField);
        PadraoLayout.estilizarCampoData(dataFinalField);
        
        // Detalhes do Afastamento
        PadraoLayout.estilizarComboBox(motivoComboBox);
        PadraoLayout.estilizarCampoTexto(documentoField);
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
                    new FormField("🏷️ Código:", codigoIrmaoField, 0, 0, 1, 0.3),
                    new FormField("📝 Descrição:", descricaoField, 0, 1, 1, 0.7)
                )
            ),
            new FormSection(
                "📅 PERÍODO DO AFASTAMENTO",
                new FormSubsection(
                    "📆 Datas",
                    new FormField("🚀 Início:", dataInicialField, 0, 0, 1, 0.5),
                    new FormField("🏁 Término:", dataFinalField, 0, 1, 1, 0.5)
                )
            ),
            new FormSection(
                "📋 DETALHES DO AFASTAMENTO",
                new FormSubsection(
                    "🔍 Informações",
                    new FormField("📌 Motivo:", motivoComboBox, 0, 0, 1, 0.4),
                    new FormField("📄 Documento:", documentoField, 0, 1, 1, 0.6)
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
        codigoIrmaoField.setText("");
        descricaoField.setText("");
        
        // Período do Afastamento
        dataInicialField.setText("");
        dataFinalField.setText("");
        
        // Detalhes do Afastamento
        motivoComboBox.setSelectedIndex(0);
        documentoField.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoIrmaoField() { return codigoIrmaoField; }
    public JTextField getDescricaoField() { return descricaoField; }
    public JTextField getDataInicialField() { return dataInicialField; }
    public JTextField getDataFinalField() { return dataFinalField; }
    public JComboBox<String> getMotivoComboBox() { return motivoComboBox; }
    public JTextField getDocumentoField() { return documentoField; }
}
