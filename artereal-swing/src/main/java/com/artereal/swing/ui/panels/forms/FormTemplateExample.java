package com.artereal.swing.ui.panels.forms;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Template de exemplo para criar novos formulários usando BaseEnhancedFormPanel
 * Este template demonstra como aplicar os padrões de UX otimizada do sistema
 */
public class FormTemplateExample extends BaseEnhancedFormPanel {
    
    // Campos de exemplo - adaptar conforme necessidade
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField descricaoField;
    private JTextField dataField;
    private JTextField statusField;
    private JTextArea observacoesArea;
    
    public FormTemplateExample() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Campos principais
        codigoField = createTextField(10);
        nomeField = createTextField(40);
        descricaoField = createTextField(50);
        dataField = createTextField(12);
        statusField = createTextField(20);
        
        // Campo de observações
        observacoesArea = createTextArea(3, 50);
        
        // Aplicar estilização
        applyFieldStyling(codigoField, "codigo");
        applyFieldStyling(nomeField, "nome");
        PadraoLayout.estilizarCampoCargoDiretoria(descricaoField);
        PadraoLayout.estilizarCampoData(dataField);
        PadraoLayout.estilizarCampoCargoDiretoria(statusField);
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     * Siga este padrão para novos formulários:
     * 1. Dados Principais (identificação + informações básicas)
     * 2. Dados Específicos (informações particulares do domínio)
     * 3. Status e Controle (situação atual + datas)
     * 4. Informações Adicionais (observações + notas)
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            // GRUPO 1: DADOS PRINCIPAIS
            new FormSection(
                "📋 DADOS PRINCIPAIS",
                new FormSubsection(
                    "🆔 Identificação",
                    new FormField("🔢 Código:", codigoField, 0, 0, 1, 0.3),
                    new FormField("📛 Nome:", nomeField, 0, 1, 1, 0.7),
                    new FormField("📅 Data:", dataField, 1, 0, 1, 0.4),
                    new FormField("📋 Status:", statusField, 1, 1, 1, 0.6)
                ),
                new FormSubsection(
                    "📝 Descrição",
                    new FormField("📄 Descrição:", descricaoField, 0, 0, 2, 1.0)
                )
            ),
            
            // GRUPO 2: DADOS ESPECÍFICOS (adaptar conforme domínio)
            new FormSection(
                "🎯 DADOS ESPECÍFICOS",
                new FormSubsection(
                    "📂 Categoria",
                    // Adicionar campos específicos do seu domínio aqui
                    new FormField("🏷️ Categoria:", createTextField(30), 0, 0, 1, 0.5),
                    new FormField("🎯 Tipo:", createTextField(30), 0, 1, 1, 0.5)
                )
            ),
            
            // GRUPO 3: STATUS E CONTROLE
            new FormSection(
                "📊 STATUS E CONTROLE",
                new FormSubsection(
                    "🔄 Situação Atual",
                    new FormField("📋 Situação:", createTextField(25), 0, 0, 1, 0.5),
                    new FormField("📅 Última Atualização:", createTextField(12), 0, 1, 1, 0.5)
                )
            ),
            
            // GRUPO 4: INFORMAÇÕES ADICIONAIS
            new FormSection(
                "📝 INFORMAÇÕES ADICIONAIS",
                new FormSubsection(
                    "📋 Observações",
                    new FormField("", createObservationsPanel(observacoesArea), 0, 0, 2, 1.0)
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
        codigoField.setText("");
        nomeField.setText("");
        descricaoField.setText("");
        dataField.setText("");
        statusField.setText("");
        observacoesArea.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getNomeField() { return nomeField; }
    public JTextField getDescricaoField() { return descricaoField; }
    public JTextField getDataField() { return dataField; }
    public JTextField getStatusField() { return statusField; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
    
    /**
     * GUIA DE USO - COMO CRIAR NOVOS FORMULÁRIOS:
     * 
     * 1. HERDE DE BaseEnhancedFormPanel:
     *    public class SeuFormPanel extends BaseEnhancedFormPanel
     * 
     * 2. DECLARE SEUS CAMPOS:
     *    private JTextField seuCampoField;
     * 
     * 3. INICIALIZE OS CAMPOS:
     *    seuCampoField = createTextField(colunas);
     *    applyFieldStyling(seuCampoField, "tipo");
     * 
     * 4. USE A ESTRUTURA PADRÃO:
     *    - GRUPO 1: DADOS PRINCIPAIS (identificação + básicas)
     *    - GRUPO 2: DADOS ESPECÍFICOS (particularidades do domínio)
     *    - GRUPO 3: STATUS E CONTROLE (situação + datas)
     *    - GRUPO 4: INFORMAÇÕES ADICIONAIS (observações)
     * 
     * 5. USE ÍCONES CONTEXTUAIS:
     *    🔢 Código, 📛 Nome, 📅 Data, 📋 Status
     *    🏠 Endereço, 📞 Telefone, 📧 Email
     *    🔷 Maçônico, 👥 Pessoas, 📚 Conteúdo
     *    💰 Financeiro, 📊 Estatísticas
     * 
     * 6. USE PESOS DINÂMICOS:
     *    - Campos principais: 0.7
     *    - Campos secundários: 0.5
     *    - Campos complementares: 0.3
     *    - Linhas completas: colSpan=2, weight=1.0
     * 
     * 7. IMPLEMENTE clearForm():
     *    Limpe todos os campos para reutilização
     * 
     * 8. FORNEÇA GETTERS:
     *    Para acesso aos campos de fora do formulário
     */
}
