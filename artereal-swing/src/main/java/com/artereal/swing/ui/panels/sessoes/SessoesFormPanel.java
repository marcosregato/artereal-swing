package com.artereal.swing.ui.panels.sessoes;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de formulário para gestão de Sessões com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class SessoesFormPanel extends BaseEnhancedFormPanel {
    
    // Dados da Sessão
    private JTextField codigoField;
    private JTextField tipoField;
    private JTextField dataField;
    private JTextField horaField;
    private JTextField localField;
    
    // Participantes
    private JTextField presidenteField;
    private JTextField oradorField;
    private JTextField secretarioField;
    private JTextField tesoureiroField;
    private JTextField mestreCerimoniasField;
    
    // Conteúdo da Sessão
    private JTextField temaField;
    private JTextArea ordemDiaArea;
    private JTextArea ataArea;
    private JTextArea observacoesArea;
    
    // Informações Adicionais
    private JTextField numeroIrmaosField;
    private JTextField numeroVisitantesField;
    private JTextField duracaoField;
    
    public SessoesFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }
    
    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados da Sessão
        codigoField = createTextField(10);
        tipoField = createTextField(20);
        dataField = createTextField(12);
        horaField = createTextField(8);
        localField = createTextField(40);
        
        // Participantes
        presidenteField = createTextField(40);
        oradorField = createTextField(40);
        secretarioField = createTextField(40);
        tesoureiroField = createTextField(40);
        mestreCerimoniasField = createTextField(40);
        
        // Conteúdo da Sessão
        temaField = createTextField(50);
        ordemDiaArea = createTextArea(4, 50);
        ataArea = createTextArea(6, 50);
        observacoesArea = createTextArea(3, 50);
        
        // Informações Adicionais
        numeroIrmaosField = createTextField(8);
        numeroVisitantesField = createTextField(8);
        duracaoField = createTextField(8);
        
        // Aplicar estilização
        applyFieldStyling();
    }
    
    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados da Sessão
        applyFieldStyling(codigoField, "codigo");
        PadraoLayout.estilizarCampoCargoDiretoria(tipoField);
        PadraoLayout.estilizarCampoData(dataField);
        PadraoLayout.estilizarCampoCargoDiretoria(horaField);
        applyFieldStyling(localField, "endereco");
        
        // Participantes
        PadraoLayout.estilizarCampoCargoDiretoria(presidenteField);
        PadraoLayout.estilizarCampoCargoDiretoria(oradorField);
        PadraoLayout.estilizarCampoCargoDiretoria(secretarioField);
        PadraoLayout.estilizarCampoCargoDiretoria(tesoureiroField);
        PadraoLayout.estilizarCampoCargoDiretoria(mestreCerimoniasField);
        
        // Conteúdo da Sessão
        PadraoLayout.estilizarCampoNome(temaField);
        
        // Informações Adicionais
        PadraoLayout.estilizarCampoCargoDiretoria(numeroIrmaosField);
        PadraoLayout.estilizarCampoCargoDiretoria(numeroVisitantesField);
        PadraoLayout.estilizarCampoCargoDiretoria(duracaoField);
    }
    
    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "📋 DADOS DA SESSÃO",
                new FormSubsection(
                    "🆔 Identificação",
                    new FormField("🔢 Código:", codigoField, 0, 0, 1, 0.3),
                    new FormField("📛 Tipo:", tipoField, 0, 1, 1, 0.7),
                    new FormField("📅 Data:", dataField, 1, 0, 1, 0.5),
                    new FormField("⏰ Hora:", horaField, 1, 1, 1, 0.5)
                ),
                new FormSubsection(
                    "📍 Local",
                    new FormField("🏠 Local:", localField, 0, 0, 2, 1.0)
                )
            ),
            new FormSection(
                "👥 PARTICIPANTES",
                new FormSubsection(
                    "👔 Diretoria da Sessão",
                    new FormField("👑 Presidente:", presidenteField, 0, 0, 2, 1.0),
                    new FormField("🎤 Orador:", oradorField, 1, 0, 2, 1.0),
                    new FormField("📝 Secretário:", secretarioField, 2, 0, 2, 1.0),
                    new FormField("💰 Tesoureiro:", tesoureiroField, 3, 0, 2, 1.0),
                    new FormField("🎭 Mestre de Cerimônias:", mestreCerimoniasField, 4, 0, 2, 1.0)
                )
            ),
            new FormSection(
                "📚 CONTEÚDO DA SESSÃO",
                new FormSubsection(
                    "🎯 Tema",
                    new FormField("📋 Tema da Sessão:", temaField, 0, 0, 2, 1.0)
                ),
                new FormSubsection(
                    "📝 Ordem do Dia",
                    new FormField("", createTextAreaPanel("📋 Ordem do Dia:", ordemDiaArea), 0, 0, 2, 1.0)
                ),
                new FormSubsection(
                    "📄 Ata",
                    new FormField("", createTextAreaPanel("📄 Ata da Sessão:", ataArea), 0, 0, 2, 1.0)
                )
            ),
            new FormSection(
                "📊 ESTATÍSTICAS",
                new FormSubsection(
                    "🔢 Controle de Presença",
                    new FormField("👥 Nº Irmãos:", numeroIrmaosField, 0, 0, 1, 0.4),
                    new FormField("👤 Nº Visitantes:", numeroVisitantesField, 0, 1, 1, 0.4),
                    new FormField("⏱️ Duração:", duracaoField, 0, 2, 1, 0.2)
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
        // Dados da Sessão
        codigoField.setText("");
        tipoField.setText("");
        dataField.setText("");
        horaField.setText("");
        localField.setText("");
        
        // Participantes
        presidenteField.setText("");
        oradorField.setText("");
        secretarioField.setText("");
        tesoureiroField.setText("");
        mestreCerimoniasField.setText("");
        
        // Conteúdo da Sessão
        temaField.setText("");
        ordemDiaArea.setText("");
        ataArea.setText("");
        observacoesArea.setText("");
        
        // Informações Adicionais
        numeroIrmaosField.setText("");
        numeroVisitantesField.setText("");
        duracaoField.setText("");
    }
    
    // Métodos getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getTipoField() { return tipoField; }
    public JTextField getDataField() { return dataField; }
    public JTextField getHoraField() { return horaField; }
    public JTextField getLocalField() { return localField; }
    public JTextField getPresidenteField() { return presidenteField; }
    public JTextField getOradorField() { return oradorField; }
    public JTextField getSecretarioField() { return secretarioField; }
    public JTextField getTesoureiroField() { return tesoureiroField; }
    public JTextField getMestreCerimoniasField() { return mestreCerimoniasField; }
    public JTextField getTemaField() { return temaField; }
    public JTextArea getOrdemDiaArea() { return ordemDiaArea; }
    public JTextArea getAtaArea() { return ataArea; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
    public JTextField getNumeroIrmaosField() { return numeroIrmaosField; }
    public JTextField getNumeroVisitantesField() { return numeroVisitantesField; }
    public JTextField getDuracaoField() { return duracaoField; }
}
