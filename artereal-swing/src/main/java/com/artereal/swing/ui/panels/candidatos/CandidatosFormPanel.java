package com.artereal.swing.ui.panels.candidatos;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de formulário para gestão de Candidatos e Profanos com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class CandidatosFormPanel extends BaseEnhancedFormPanel {
    
    // Dados Pessoais
    private JTextField codigoField;
    private JTextField nomeField;
    private JTextField dataNascimentoField;
    private JTextField cpfField;
    private JTextField rgField;
    private JTextField estadoCivilField;
    
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
    
    // Profissional
    private JTextField profissaoField;
    private JTextField empresaField;
    private JTextField cargoField;
    private JTextField formacaoField;
    
    // Processo de Candidatura
    private JTextField dataCandidaturaField;
    private JTextField dataAprovacaoField;
    private JTextField dataIniciacaoField;
    private JComboBox<String> statusComboBox;
    private JTextField padrinhoField;
    private JTextField proponenteField;
    
    // Informações Adicionais
    private JTextArea observacoesArea;
    private JTextArea historicoArea;
    
    public CandidatosFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados Pessoais
        codigoField = createTextField(10);
        nomeField = createTextField(50);
        dataNascimentoField = createTextField(12);
        cpfField = createTextField(15);
        rgField = createTextField(15);
        estadoCivilField = createTextField(20);
        
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
        
        // Profissional
        profissaoField = createTextField(40);
        empresaField = createTextField(40);
        cargoField = createTextField(30);
        formacaoField = createTextField(40);
        
        // Processo de Candidatura
        dataCandidaturaField = createTextField(12);
        dataAprovacaoField = createTextField(12);
        dataIniciacaoField = createTextField(12);
        statusComboBox = new JComboBox<>(new String[]{
            "Profano", "Candidato", "Em Análise", "Aprovado", "Rejeitado", "Iniciado"
        });
        padrinhoField = createTextField(40);
        proponenteField = createTextField(40);
        
        // Informações Adicionais
        observacoesArea = createTextArea(3, 50);
        historicoArea = createTextArea(4, 50);
        
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
        PadraoLayout.estilizarCampoData(dataNascimentoField);
        PadraoLayout.estilizarCampoCargoDiretoria(cpfField);
        PadraoLayout.estilizarCampoCargoDiretoria(rgField);
        PadraoLayout.estilizarCampoCargoDiretoria(estadoCivilField);
        
        // Contato
        PadraoLayout.estilizarCampoTelefone(telefoneField);
        PadraoLayout.estilizarCampoCargoDiretoria(celularField);
        applyFieldStyling(emailField, "email");
        
        // Endereço
        applyFieldStyling(enderecoField, "endereco");
        applyFieldStyling(bairroField, "bairro");
        applyFieldStyling(cidadeField, "cidade");
        applyFieldStyling(estadoField, "estado");
        applyFieldStyling(cepField, "cep");
        
        // Profissional
        PadraoLayout.estilizarCampoCargoDiretoria(profissaoField);
        PadraoLayout.estilizarCampoCargoDiretoria(empresaField);
        PadraoLayout.estilizarCampoCargoDiretoria(cargoField);
        PadraoLayout.estilizarCampoCargoDiretoria(formacaoField);
        
        // Processo de Candidatura
        PadraoLayout.estilizarCampoData(dataCandidaturaField);
        PadraoLayout.estilizarCampoData(dataAprovacaoField);
        PadraoLayout.estilizarCampoData(dataIniciacaoField);
        PadraoLayout.estilizarComboBox(statusComboBox);
        PadraoLayout.estilizarCampoCargoDiretoria(padrinhoField);
        PadraoLayout.estilizarCampoCargoDiretoria(proponenteField);
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
                    new FormField("📅 Data Nascimento:", dataNascimentoField, 1, 0, 1, 0.4),
                    new FormField("💑 Estado Civil:", estadoCivilField, 1, 1, 1, 0.6)
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
                    new FormField("📞 Telefone:", telefoneField, 0, 0, 1, 0.4),
                    new FormField("📱 Celular:", celularField, 0, 1, 1, 0.6),
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
                "💼 DADOS PROFISSIONAIS",
                new FormSubsection(
                    "🏢 Informações Profissionais",
                    new FormField("💼 Profissão:", profissaoField, 0, 0, 1, 0.5),
                    new FormField("🏢 Empresa:", empresaField, 0, 1, 1, 0.5),
                    new FormField("👔 Cargo:", cargoField, 1, 0, 1, 0.4),
                    new FormField("🎓 Formação:", formacaoField, 1, 1, 1, 0.6)
                )
            ),
            new FormSection(
                "🔷 PROCESSO DE CANDIDATURA",
                new FormSubsection(
                    "📋 Datas do Processo",
                    new FormField("📅 Data Candidatura:", dataCandidaturaField, 0, 0, 1, 0.4),
                    new FormField("✅ Data Aprovação:", dataAprovacaoField, 0, 1, 1, 0.4),
                    new FormField("🔷 Data Iniciação:", dataIniciacaoField, 0, 2, 1, 0.2)
                ),
                new FormSubsection(
                    "👥 Apoio Maçônico",
                    new FormField("🤝 Padrinho:", padrinhoField, 0, 0, 1, 0.5),
                    new FormField("👤 Proponente:", proponenteField, 0, 1, 1, 0.5)
                ),
                new FormSubsection(
                    "📊 Status",
                    new FormField("📋 Situação:", statusComboBox, 0, 0, 1, 0.5)
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
                    new FormField("", createTextAreaPanel("📜 Histórico do Candidato:", historicoArea), 0, 0, 2, 1.0)
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
        codigoField.setText("");
        nomeField.setText("");
        dataNascimentoField.setText("");
        cpfField.setText("");
        rgField.setText("");
        estadoCivilField.setText("");
        
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
        
        // Profissional
        profissaoField.setText("");
        empresaField.setText("");
        cargoField.setText("");
        formacaoField.setText("");
        
        // Processo de Candidatura
        dataCandidaturaField.setText("");
        dataAprovacaoField.setText("");
        dataIniciacaoField.setText("");
        statusComboBox.setSelectedIndex(0);
        padrinhoField.setText("");
        proponenteField.setText("");
        
        // Informações Adicionais
        observacoesArea.setText("");
        historicoArea.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getNomeField() { return nomeField; }
    public JTextField getDataNascimentoField() { return dataNascimentoField; }
    public JTextField getCpfField() { return cpfField; }
    public JTextField getRgField() { return rgField; }
    public JTextField getEstadoCivilField() { return estadoCivilField; }
    public JTextField getTelefoneField() { return telefoneField; }
    public JTextField getCelularField() { return celularField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getEnderecoField() { return enderecoField; }
    public JTextField getBairroField() { return bairroField; }
    public JTextField getCidadeField() { return cidadeField; }
    public JTextField getEstadoField() { return estadoField; }
    public JTextField getCepField() { return cepField; }
    public JTextField getProfissaoField() { return profissaoField; }
    public JTextField getEmpresaField() { return empresaField; }
    public JTextField getCargoField() { return cargoField; }
    public JTextField getFormacaoField() { return formacaoField; }
    public JTextField getDataCandidaturaField() { return dataCandidaturaField; }
    public JTextField getDataAprovacaoField() { return dataAprovacaoField; }
    public JTextField getDataIniciacaoField() { return dataIniciacaoField; }
    public JComboBox<String> getStatusComboBox() { return statusComboBox; }
    public JTextField getPadrinhoField() { return padrinhoField; }
    public JTextField getProponenteField() { return proponenteField; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
    public JTextArea getHistoricoArea() { return historicoArea; }
}
