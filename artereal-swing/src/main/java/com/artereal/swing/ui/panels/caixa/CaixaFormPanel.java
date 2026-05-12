package com.artereal.swing.ui.panels.caixa;

import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Painel de formulário otimizado para Controle Financeiro (Caixa)
 * Extende BaseEnhancedFormPanel para UX consistente
 */
public class CaixaFormPanel extends BaseEnhancedFormPanel {
    
    // Campos do formulário
    private JTextField codigoField;
    private JTextField dataField;
    private JTextField descricaoField;
    private JTextField valorField;
    private JTextField numeroDocumentoField;
    private JTextArea observacoesArea;
    
    // ComboBox
    private JComboBox<String> tipoCombo;
    private JComboBox<String> categoriaCombo;
    private JComboBox<String> formaPagamentoCombo;
    private JComboBox<String> statusCombo;
    
    public CaixaFormPanel() {
        initializeFields();
        setupFormSections();
    }
    
    private void initializeFields() {
        // Inicializar campos
        codigoField = createTextField(10);
        dataField = createTextField(12);
        descricaoField = createTextField(40);
        valorField = createTextField(15);
        numeroDocumentoField = createTextField(25);
        observacoesArea = createTextArea(3, 40);
        
        // Aplicar estilização
        PadraoLayout.estilizarCampoCodigo(codigoField);
        PadraoLayout.estilizarCampoData(dataField);
        PadraoLayout.estilizarCampoDescricao(descricaoField);
        PadraoLayout.estilizarCampoValorFinanceiro(valorField);
        PadraoLayout.estilizarCampoNumeroDocumento(numeroDocumentoField);
        
        // ComboBox
        tipoCombo = new JComboBox<>(new String[]{"Receita", "Despesa"});
        categoriaCombo = new JComboBox<>(new String[]{
            "Mensalidades", "Doações", "Eventos", "Material", "Aluguel", 
            "Água", "Luz", "Telefone", "Outros"
        });
        formaPagamentoCombo = new JComboBox<>(new String[]{
            "Dinheiro", "Transferência", "Cheque", "Cartão", "Pix"
        });
        statusCombo = new JComboBox<>(new String[]{"Pendente", "Processado", "Cancelado"});
        
        // Aplicar estilização aos ComboBox
        PadraoLayout.estilizarComboBox(tipoCombo);
        PadraoLayout.estilizarComboBox(categoriaCombo);
        PadraoLayout.estilizarComboBox(formaPagamentoCombo);
        PadraoLayout.estilizarComboBox(statusCombo);
        
        // Data atual como padrão
        dataField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    private void setupFormSections() {
        FormSection[] sections = {
            new FormSection(
                "💰 DADOS PRINCIPAIS",
                new FormSubsection(
                    "🆔 Identificação da Movimentação",
                    new FormField("🏷️ Código:", codigoField, 0, 0, 1, 0.2),
                    new FormField("📅 Data:", dataField, 0, 1, 1, 0.3),
                    new FormField("🏷️ Tipo:", tipoCombo, 0, 2, 1, 0.25),
                    new FormField("📊 Status:", statusCombo, 0, 3, 1, 0.25)
                ),
                new FormSubsection(
                    "💳 Informações Financeiras",
                    new FormField("💰 Valor:", valorField, 1, 0, 1, 0.3),
                    new FormField("📂 Categoria:", categoriaCombo, 1, 1, 1, 0.35),
                    new FormField("💳 Forma Pagto:", formaPagamentoCombo, 1, 2, 1, 0.35)
                )
            ),
            new FormSection(
                "📋 DETALHES DA MOVIMENTAÇÃO",
                new FormSubsection(
                    "📝 Descrição",
                    new FormField("", createTextAreaPanel("📝 Descrição da Movimentação:", descricaoField), 0, 0, 2, 1.0)
                ),
                new FormSubsection(
                    "📄 Documentação",
                    new FormField("📄 Nº Documento:", numeroDocumentoField, 1, 0, 2, 1.0)
                )
            ),
            new FormSection(
                "📝 INFORMAÇÕES ADICIONAIS",
                new FormSubsection(
                    "📝 Observações",
                    new FormField("", createTextAreaPanel("📝 Observações:", observacoesArea), 0, 0, 2, 1.0)
                )
            )
        };
        
        addFormGroup(sections);
    }
    
    private JPanel createTextAreaPanel(String labelText, JComponent textComponent) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(labelText);
        label.setFont(FIELD_LABEL_FONT);
        label.setForeground(FIELD_LABEL_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        if (textComponent instanceof JTextArea) {
            JScrollPane scrollPane = new JScrollPane(textComponent);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            container.add(scrollPane, BorderLayout.CENTER);
        } else {
            container.add(textComponent, BorderLayout.CENTER);
        }
        
        container.add(label, BorderLayout.NORTH);
        
        return container;
    }
    
    @Override
    public void clearForm() {
        codigoField.setText("");
        dataField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        descricaoField.setText("");
        valorField.setText("");
        numeroDocumentoField.setText("");
        observacoesArea.setText("");
        tipoCombo.setSelectedIndex(0);
        categoriaCombo.setSelectedIndex(0);
        formaPagamentoCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
    }
    
    // Getters para acesso aos campos
    public JTextField getCodigoField() { return codigoField; }
    public JTextField getDataField() { return dataField; }
    public JTextField getDescricaoField() { return descricaoField; }
    public JTextField getValorField() { return valorField; }
    public JTextField getNumeroDocumentoField() { return numeroDocumentoField; }
    public JTextArea getObservacoesArea() { return observacoesArea; }
    public JComboBox<String> getTipoCombo() { return tipoCombo; }
    public JComboBox<String> getCategoriaCombo() { return categoriaCombo; }
    public JComboBox<String> getFormaPagamentoCombo() { return formaPagamentoCombo; }
    public JComboBox<String> getStatusCombo() { return statusCombo; }
}
