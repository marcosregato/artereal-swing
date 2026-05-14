package com.artereal.swing.ui.panels.relatorios;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Painel de formulário para gestão de Relatórios com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class RelatoriosFormPanel extends BaseEnhancedFormPanel {

    // Período do Relatório
    private JTextField dataInicioField;
    private JTextField dataFimField;

    // Configurações do Relatório
    private JComboBox<String> tipoComboBox;
    private JComboBox<String> formatoComboBox;

    public RelatoriosFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }

    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Período do Relatório
        dataInicioField = createTextField(12);
        dataFimField = createTextField(12);

        // Configurações do Relatório
        tipoComboBox = new JComboBox<>(new String[]{
            "Todos", "Irmãos", "Financeiro", "Sessões", "Biblioteca", "Visitantes", "Eventos", "Documentos"
        });
        formatoComboBox = new JComboBox<>(new String[]{
            "PDF", "Excel", "HTML", "CSV"
        });

        // Aplicar estilização
        applyFieldStyling();
    }

    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Período do Relatório
        PadraoLayout.estilizarCampoPeriodoRelatorio(dataInicioField);
        PadraoLayout.estilizarCampoPeriodoRelatorio(dataFimField);

        // Configurações do Relatório
        tipoComboBox.setBackground(Color.WHITE);
        tipoComboBox.setBorder(PadraoLayout.BORDA_CAMPO);
        formatoComboBox.setBackground(Color.WHITE);
        formatoComboBox.setBorder(PadraoLayout.BORDA_CAMPO);
    }

    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "📅 PERÍODO DO RELATÓRIO",
                new FormSubsection(
                    "📅 Intervalo de Datas",
                    new FormField("🗓️ Data Início:", dataInicioField, 0, 0, 1, 0.5),
                    new FormField("🗓️ Data Fim:", dataFimField, 0, 1, 1, 0.5)
                )
            ),
            new FormSection(
                "⚙️ CONFIGURAÇÕES DO RELATÓRIO",
                new FormSubsection(
                    "📋 Tipo e Formato",
                    new FormField("📂 Tipo:", tipoComboBox, 0, 0, 1, 0.5),
                    new FormField("📄 Formato:", formatoComboBox, 0, 1, 1, 0.5)
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
        // Período do Relatório
        dataInicioField.setText("");
        dataFimField.setText("");

        // Configurações do Relatório
        tipoComboBox.setSelectedIndex(0);
        formatoComboBox.setSelectedIndex(0);
    }

    /**
     * Preenche os campos com valores padrão
     */
    public void setDefaultValues() {
        dataInicioField.setText(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dataFimField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tipoComboBox.setSelectedIndex(0);
        formatoComboBox.setSelectedIndex(0);
    }

    // Métodos getters para acesso aos campos
    public JTextField getDataInicioField() { return dataInicioField; }
    public JTextField getDataFimField() { return dataFimField; }
    public JComboBox<String> getTipoComboBox() { return tipoComboBox; }
    public JComboBox<String> getFormatoComboBox() { return formatoComboBox; }
}
