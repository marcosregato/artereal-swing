package com.artereal.swing.ui.panels.despesas;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;

/**
 * Painel de formulário para gestão de Despesas com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class DespesasFormPanel extends BaseEnhancedFormPanel {
    
    // Dados Principais
    private JTextField descricaoField;
    private JTextField valorField;
    private JTextField dataField;
    
    // Classificação
    private JTextField categoriaField;
    private JTextField fornecedorField;
    
    // Documentação
    private JTextField numeroDocumentoField;
    
    public DespesasFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados Principais
        descricaoField = createTextField(40);
        valorField = createTextField(20);
        dataField = createTextField(10);
        
        // Classificação
        categoriaField = createTextField(25);
        fornecedorField = createTextField(30);
        
        // Documentação
        numeroDocumentoField = createTextField(20);
        
        // Aplicar estilização
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados Principais
        PadraoLayout.estilizarCampoDescricao(descricaoField);
        PadraoLayout.estilizarCampoValor(valorField);
        PadraoLayout.estilizarCampoData(dataField);
        
        // Classificação
        PadraoLayout.estilizarCampoTexto(categoriaField);
        PadraoLayout.estilizarCampoTexto(fornecedorField);
        
        // Documentação
        PadraoLayout.estilizarCampoTexto(numeroDocumentoField);
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "💰 DADOS PRINCIPAIS",
                new FormSubsection(
                    "📝 Informações da Despesa",
                    new FormField("🏷️ Descrição:", descricaoField, 0, 0, 1, 0.6),
                    new FormField("💵 Valor:", valorField, 0, 1, 1, 0.4)
                ),
                new FormSubsection(
                    "📅 Data",
                    new FormField("📆 Data:", dataField, 1, 0, 1, 1.0)
                )
            ),
            new FormSection(
                "🏷️ CLASSIFICAÇÃO",
                new FormSubsection(
                    "📋 Categorias",
                    new FormField("📂 Categoria:", categoriaField, 0, 0, 1, 0.5),
                    new FormField("🏪 Fornecedor:", fornecedorField, 0, 1, 1, 0.5)
                )
            ),
            new FormSection(
                "📄 DOCUMENTAÇÃO",
                new FormSubsection(
                    "🔍 Informações",
                    new FormField("📄 Nº Documento:", numeroDocumentoField, 0, 0, 1, 1.0)
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
        // Dados Principais
        descricaoField.setText("");
        valorField.setText("");
        dataField.setText("");
        
        // Classificação
        categoriaField.setText("");
        fornecedorField.setText("");
        
        // Documentação
        numeroDocumentoField.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getDescricaoField() { return descricaoField; }
    public JTextField getValorField() { return valorField; }
    public JTextField getDataField() { return dataField; }
    public JTextField getCategoriaField() { return categoriaField; }
    public JTextField getFornecedorField() { return fornecedorField; }
    public JTextField getNumeroDocumentoField() { return numeroDocumentoField; }
}
