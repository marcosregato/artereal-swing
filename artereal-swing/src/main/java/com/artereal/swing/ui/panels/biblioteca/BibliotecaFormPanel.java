package com.artereal.swing.ui.panels.biblioteca;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;

/**
 * Painel de formulário para gestão de Biblioteca com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class BibliotecaFormPanel extends BaseEnhancedFormPanel {
    
    // Dados do Livro
    private JTextField codigoField;
    private JTextField tituloField;
    private JTextField autorField;
    private JTextField isbnField;
    private JTextField editoraField;
    private JTextField anoField;
    private JTextField edicaoField;
    
    // Classificação
    private JTextField categoriaField;
    private JTextField assuntoField;
    private JTextField grauField;
    private JTextField localizacaoField;
    
    // Status
    private JTextField statusField;
    private JTextField dataAquisicaoField;
    private JTextField dataBaixaField;
    private JTextField motivoBaixaField;
    
    // Empréstimo
    private JTextField irmaoEmprestimoField;
    private JTextField dataEmprestimoField;
    private JTextField dataDevolucaoField;
    private JTextArea observacoesArea;
    
    public BibliotecaFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados do Livro
        codigoField = createTextField(10);
        tituloField = createTextField(50);
        autorField = createTextField(40);
        isbnField = createTextField(20);
        editoraField = createTextField(30);
        anoField = createTextField(8);
        edicaoField = createTextField(10);
        
        // Classificação
        categoriaField = createTextField(30);
        assuntoField = createTextField(40);
        grauField = createTextField(20);
        localizacaoField = createTextField(20);
        
        // Status
        statusField = createTextField(20);
        dataAquisicaoField = createTextField(12);
        dataBaixaField = createTextField(12);
        motivoBaixaField = createTextField(40);
        
        // Empréstimo
        irmaoEmprestimoField = createTextField(40);
        dataEmprestimoField = createTextField(12);
        dataDevolucaoField = createTextField(12);
        observacoesArea = createTextArea(3, 50);
        
        // Aplicar estilização
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados do Livro
        applyFieldStyling(codigoField, "codigo");
        applyFieldStyling(tituloField, "nome");
        PadraoLayout.estilizarCampoCargoDiretoria(autorField);
        PadraoLayout.estilizarCampoCargoDiretoria(isbnField);
        PadraoLayout.estilizarCampoCargoDiretoria(editoraField);
        PadraoLayout.estilizarCampoCargoDiretoria(anoField);
        PadraoLayout.estilizarCampoCargoDiretoria(edicaoField);
        
        // Classificação
        PadraoLayout.estilizarCampoCargoDiretoria(categoriaField);
        PadraoLayout.estilizarCampoCargoDiretoria(assuntoField);
        PadraoLayout.estilizarCampoCargoDiretoria(grauField);
        PadraoLayout.estilizarCampoCargoDiretoria(localizacaoField);
        
        // Status
        PadraoLayout.estilizarCampoCargoDiretoria(statusField);
        PadraoLayout.estilizarCampoData(dataAquisicaoField);
        PadraoLayout.estilizarCampoData(dataBaixaField);
        PadraoLayout.estilizarCampoCargoDiretoria(motivoBaixaField);
        
        // Empréstimo
        PadraoLayout.estilizarCampoCargoDiretoria(irmaoEmprestimoField);
        PadraoLayout.estilizarCampoData(dataEmprestimoField);
        PadraoLayout.estilizarCampoData(dataDevolucaoField);
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "📚 DADOS DO LIVRO",
                new FormSubsection(
                    "🆔 Identificação",
                    new FormField("🔢 Código:", codigoField, 0, 0, 1, 0.3),
                    new FormField("📖 Título:", tituloField, 0, 1, 1, 0.7),
                    new FormField("✍️ Autor:", autorField, 1, 0, 1, 0.6),
                    new FormField("🔖 ISBN:", isbnField, 1, 1, 1, 0.4)
                ),
                new FormSubsection(
                    "📄 Publicação",
                    new FormField("🏢 Editora:", editoraField, 0, 0, 1, 0.4),
                    new FormField("📅 Ano:", anoField, 0, 1, 1, 0.3),
                    new FormField("📋 Edição:", edicaoField, 0, 2, 1, 0.3)
                )
            ),
            new FormSection(
                "🏷️ CLASSIFICAÇÃO",
                new FormSubsection(
                    "📂 Categorização",
                    new FormField("📁 Categoria:", categoriaField, 0, 0, 1, 0.4),
                    new FormField("🎯 Assunto:", assuntoField, 0, 1, 1, 0.6),
                    new FormField("⭐ Grau:", grauField, 1, 0, 1, 0.5),
                    new FormField("📍 Localização:", localizacaoField, 1, 1, 1, 0.5)
                )
            ),
            new FormSection(
                "📊 STATUS",
                new FormSubsection(
                    "🔄 Situação Atual",
                    new FormField("📋 Status:", statusField, 0, 0, 1, 0.5),
                    new FormField("📅 Data Aquisição:", dataAquisicaoField, 0, 1, 1, 0.5)
                ),
                new FormSubsection(
                    "❌ Baixa",
                    new FormField("📅 Data Baixa:", dataBaixaField, 0, 0, 1, 0.4),
                    new FormField("📝 Motivo:", motivoBaixaField, 0, 1, 1, 0.6)
                )
            ),
            new FormSection(
                "📚 EMPRÉSTIMO",
                new FormSubsection(
                    "👤 Empréstimo Atual",
                    new FormField("👥 Irmão:", irmaoEmprestimoField, 0, 0, 1, 0.6),
                    new FormField("📅 Data Empréstimo:", dataEmprestimoField, 0, 1, 1, 0.4),
                    new FormField("📅 Devolução Prevista:", dataDevolucaoField, 1, 0, 1, 0.4)
                )
            ),
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
        // Dados do Livro
        codigoField.setText("");
        tituloField.setText("");
        autorField.setText("");
        isbnField.setText("");
        editoraField.setText("");
        anoField.setText("");
        edicaoField.setText("");
        
        // Classificação
        categoriaField.setText("");
        assuntoField.setText("");
        grauField.setText("");
        localizacaoField.setText("");
        
        // Status
        statusField.setText("");
        dataAquisicaoField.setText("");
        dataBaixaField.setText("");
        motivoBaixaField.setText("");
        
        // Empréstimo
        irmaoEmprestimoField.setText("");
        dataEmprestimoField.setText("");
        dataDevolucaoField.setText("");
        observacoesArea.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getTituloField() { return tituloField; }
    public JTextField getAutorField() { return autorField; }
    public JTextField getIsbnField() { return isbnField; }
    public JTextField getEditoraField() { return editoraField; }
    public JTextField getAnoField() { return anoField; }
    public JTextField getEdicaoField() { return edicaoField; }
    public JTextField getCategoriaField() { return categoriaField; }
    public JTextField getAssuntoField() { return assuntoField; }
    public JTextField getGrauField() { return grauField; }
    public JTextField getLocalizacaoField() { return localizacaoField; }
    public JTextField getStatusField() { return statusField; }
    public JTextField getDataAquisicaoField() { return dataAquisicaoField; }
    public JTextField getDataBaixaField() { return dataBaixaField; }
    public JTextField getMotivoBaixaField() { return motivoBaixaField; }
    public JTextField getIrmaoEmprestimoField() { return irmaoEmprestimoField; }
    public JTextField getDataEmprestimoField() { return dataEmprestimoField; }
    public JTextField getDataDevolucaoField() { return dataDevolucaoField; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
}
