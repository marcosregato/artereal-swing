package com.artereal.swing.ui.panels.documentos;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de formulário para gestão de Documentos com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class DocumentosFormPanel extends BaseEnhancedFormPanel {

    // Dados Principais
    private JTextField nomeArquivoField;
    private JTextField caminhoArquivoField;
    private JTextField descricaoField;
    private JTextField dataExpiracaoField;
    private JTextField usuarioUploadField;
    private JTextField tamanhoField;
    private JTextField formatoField;
    private JTextArea hashArea;

    // Classificação
    private JComboBox<String> tipoComboBox;

    public DocumentosFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }

    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados Principais
        nomeArquivoField = createTextField(40);
        caminhoArquivoField = createTextField(50);
        descricaoField = createTextField(40);
        dataExpiracaoField = createTextField(8);
        usuarioUploadField = createTextField(20);
        tamanhoField = createTextField(10);
        formatoField = createTextField(6);
        hashArea = createTextArea(2, 40);

        // Classificação
        tipoComboBox = new JComboBox<>(new String[]{"IDENTIDADE", "DIPLOMA", "CERTIFICADO", "FOTO", "OUTRO"});

        // Aplicar estilização
        applyFieldStyling();
    }

    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados Principais
        PadraoLayout.estilizarCampoTexto(nomeArquivoField);
        PadraoLayout.estilizarCampoTexto(caminhoArquivoField);
        PadraoLayout.estilizarCampoDescricao(descricaoField);
        PadraoLayout.estilizarCampoData(dataExpiracaoField);
        PadraoLayout.estilizarCampoTexto(usuarioUploadField);
        PadraoLayout.estilizarCampoTexto(tamanhoField);
        PadraoLayout.estilizarCampoTexto(formatoField);
        
        // Hash area
        hashArea.setEditable(false);
        hashArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10));
        hashArea.setBackground(java.awt.Color.WHITE);
        hashArea.setBorder(PadraoLayout.BORDA_CAMPO);

        // Classificação
        tipoComboBox.setBackground(Color.WHITE);
        tipoComboBox.setBorder(PadraoLayout.BORDA_CAMPO);
    }

    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "📄 DADOS PRINCIPAIS",
                new FormSubsection(
                    "📝 Informações do Arquivo",
                    new FormField("🗂️ Nome Arquivo:", nomeArquivoField, 0, 0, 1, 0.6),
                    new FormField("📂 Caminho:", caminhoArquivoField, 0, 1, 1, 0.4)
                ),
                new FormSubsection(
                    "📄 Detalhes do Documento",
                    new FormField("📋 Descrição:", descricaoField, 1, 0, 1, 1.0),
                    new FormField("📅 Data Expiração:", dataExpiracaoField, 2, 0, 1, 0.5),
                    new FormField("👤 Usuário Upload:", usuarioUploadField, 2, 1, 1, 0.5)
                ),
                new FormSubsection(
                    "📊 Metadados",
                    new FormField("📏 Tamanho:", tamanhoField, 3, 0, 1, 0.3),
                    new FormField("🎯 Formato:", formatoField, 3, 1, 1, 0.3),
                    new FormField("🔐 Hash:", hashArea, 4, 0, 1, 1.0)
                )
            ),
            new FormSection(
                "🏷️ CLASSIFICAÇÃO",
                new FormSubsection(
                    "📋 Categoria do Documento",
                    new FormField("📂 Tipo:", tipoComboBox, 0, 0, 1, 1.0)
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
        nomeArquivoField.setText("");
        caminhoArquivoField.setText("");
        descricaoField.setText("");
        dataExpiracaoField.setText("");
        usuarioUploadField.setText("");
        tamanhoField.setText("");
        formatoField.setText("");
        hashArea.setText("");

        // Classificação
        tipoComboBox.setSelectedIndex(0);
    }

    // Métodos getters para acesso aos campos
    public JTextField getNomeArquivoField() { return nomeArquivoField; }
    public JTextField getCaminhoArquivoField() { return caminhoArquivoField; }
    public JTextField getDescricaoField() { return descricaoField; }
    public JTextField getDataExpiracaoField() { return dataExpiracaoField; }
    public JTextField getUsuarioUploadField() { return usuarioUploadField; }
    public JTextField getTamanhoField() { return tamanhoField; }
    public JTextField getFormatoField() { return formatoField; }
    public JTextArea getHashArea() { return hashArea; }
    public JComboBox<String> getTipoComboBox() { return tipoComboBox; }
}
