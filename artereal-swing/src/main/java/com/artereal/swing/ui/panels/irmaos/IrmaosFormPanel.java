package com.artereal.swing.ui.panels.irmaos;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;

/**
 * Painel de formulário para gestão de Irmãos com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class IrmaosFormPanel extends BaseEnhancedFormPanel {
    
    // Dados Pessoais
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField apelidoField;
    private JTextField dataNascimentoField;
    private JTextField cpfField;
    private JTextField rgField;
    
    // Contato
    private JTextField telefoneField;
    private JTextField celularField;
    private JTextField emailField;
    
    // Endereço
    private JTextField enderecoField;
    private JTextField bairroField;
    private JTextField cidadeField;
    private JTextField estadoField;
    private JTextField cepField;
    
    // Dados Maçônicos
    private JTextField dataIniciacaoField;
    private JTextField dataElevacaoField;
    private JTextField grauField;
    private JTextField lojaField;
    private JTextField cargoField;
    
    // Informações Adicionais
    private JTextArea observacoesArea;
    
    public IrmaosFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados Pessoais
        codigoField = createTextField(12);
        nomeField = createTextField(40);
        apelidoField = createTextField(30);
        dataNascimentoField = createTextField(10);
        cpfField = createTextField(15);
        rgField = createTextField(15);
        
        // Contato
        telefoneField = createTextField(15);
        celularField = createTextField(15);
        emailField = createTextField(40);
        
        // Endereço
        enderecoField = createTextField(50);
        bairroField = createTextField(30);
        cidadeField = createTextField(30);
        estadoField = createTextField(20);
        cepField = createTextField(10);
        
        // Dados Maçônicos
        dataIniciacaoField = createTextField(10);
        dataElevacaoField = createTextField(10);
        grauField = createTextField(20);
        lojaField = createTextField(30);
        cargoField = createTextField(30);
        
        // Informações Adicionais
        observacoesArea = createTextArea(3, 40);
        
        // Aplicar estilização
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados Pessoais
        applyFieldStyling(codigoField, "codigo");
        applyFieldStyling(nomeField, "nome");
        PadraoLayout.estilizarCampoCargoDiretoria(apelidoField);
        PadraoLayout.estilizarCampoData(dataNascimentoField);
        PadraoLayout.estilizarCampoCargoDiretoria(cpfField);
        PadraoLayout.estilizarCampoCargoDiretoria(rgField);
        
        // Contato
        applyFieldStyling(telefoneField, "telefone");
        PadraoLayout.estilizarCampoCargoDiretoria(celularField);
        applyFieldStyling(emailField, "email");
        
        // Endereço
        applyFieldStyling(enderecoField, "endereco");
        applyFieldStyling(bairroField, "bairro");
        applyFieldStyling(cidadeField, "cidade");
        applyFieldStyling(estadoField, "estado");
        applyFieldStyling(cepField, "cep");
        
        // Dados Maçônicos
        PadraoLayout.estilizarCampoData(dataIniciacaoField);
        PadraoLayout.estilizarCampoData(dataElevacaoField);
        PadraoLayout.estilizarCampoCargoDiretoria(grauField);
        PadraoLayout.estilizarCampoCargoDiretoria(lojaField);
        PadraoLayout.estilizarCampoCargoDiretoria(cargoField);
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "👤 DADOS PESSOAIS",
                new FormSubsection(
                    "🆔 Identificação",
                    new FormField("🔢 Código:", codigoField, 0, 0, 1, 0.3),
                    new FormField("📛 Nome Completo:", nomeField, 0, 1, 1, 0.7),
                    new FormField("🏷️ Apelido:", apelidoField, 1, 0, 1, 0.5),
                    new FormField("📅 Data Nascimento:", dataNascimentoField, 1, 1, 1, 0.5)
                ),
                new FormSubsection(
                    "📋 Documentos",
                    new FormField("🆔 CPF:", cpfField, 0, 0, 1, 0.5),
                    new FormField("📄 RG:", rgField, 0, 1, 1, 0.5)
                )
            ),
            new FormSection(
                "📞 CONTATO",
                new FormSubsection(
                    "📱 Informações de Contato",
                    new FormField("📞 Telefone:", telefoneField, 0, 0, 1, 0.5),
                    new FormField("📱 Celular:", celularField, 0, 1, 1, 0.5),
                    new FormField("📧 E-mail:", emailField, 1, 0, 2, 1.0)
                )
            ),
            new FormSection(
                "📍 ENDEREÇO",
                new FormSubsection(
                    "🏠 Localização",
                    new FormField("🏠 Endereço:", enderecoField, 0, 0, 2, 1.0),
                    new FormField("🏘️ Bairro:", bairroField, 1, 0, 1, 0.4),
                    new FormField("🏙️ Cidade:", cidadeField, 1, 1, 1, 0.6),
                    new FormField("🗺️ Estado:", estadoField, 2, 0, 1, 0.3),
                    new FormField("📮 CEP:", cepField, 2, 1, 1, 0.7)
                )
            ),
            new FormSection(
                "🔷 DADOS MAÇÔNICOS",
                new FormSubsection(
                    "📜 Informações Maçônicas",
                    new FormField("📅 Data Iniciação:", dataIniciacaoField, 0, 0, 1, 0.5),
                    new FormField("📅 Data Elevação:", dataElevacaoField, 0, 1, 1, 0.5),
                    new FormField("⭐ Grau:", grauField, 1, 0, 1, 0.4),
                    new FormField("🏛️ Loja:", lojaField, 1, 1, 1, 0.6)
                ),
                new FormSubsection(
                    "👔 Cargo Atual",
                    new FormField("🎯 Cargo:", cargoField, 0, 0, 2, 1.0)
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
        // Dados Pessoais
        codigoField.setText("");
        nomeField.setText("");
        apelidoField.setText("");
        dataNascimentoField.setText("");
        cpfField.setText("");
        rgField.setText("");
        
        // Contato
        telefoneField.setText("");
        celularField.setText("");
        emailField.setText("");
        
        // Endereço
        enderecoField.setText("");
        bairroField.setText("");
        cidadeField.setText("");
        estadoField.setText("");
        cepField.setText("");
        
        // Dados Maçônicos
        dataIniciacaoField.setText("");
        dataElevacaoField.setText("");
        grauField.setText("");
        lojaField.setText("");
        cargoField.setText("");
        
        // Informações Adicionais
        observacoesArea.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getNomeField() { return nomeField; }
    public JTextField getApelidoField() { return apelidoField; }
    public JTextField getDataNascimentoField() { return dataNascimentoField; }
    public JTextField getCpfField() { return cpfField; }
    public JTextField getRgField() { return rgField; }
    public JTextField getTelefoneField() { return telefoneField; }
    public JTextField getCelularField() { return celularField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getEnderecoField() { return enderecoField; }
    public JTextField getBairroField() { return bairroField; }
    public JTextField getCidadeField() { return cidadeField; }
    public JTextField getEstadoField() { return estadoField; }
    public JTextField getCepField() { return cepField; }
    public JTextField getDataIniciacaoField() { return dataIniciacaoField; }
    public JTextField getDataElevacaoField() { return dataElevacaoField; }
    public JTextField getGrauField() { return grauField; }
    public JTextField getLojaField() { return lojaField; }
    public JTextField getCargoField() { return cargoField; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
}
