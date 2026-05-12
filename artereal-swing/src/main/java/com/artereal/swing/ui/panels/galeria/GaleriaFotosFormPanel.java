package com.artereal.swing.ui.panels.galeria;

import com.artereal.swing.ui.layout.PadraoLayout;
import com.artereal.swing.ui.panels.forms.BaseEnhancedFormPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de formulário para gestão de Galeria de Fotos com UX otimizada
 * Estrutura organizada em grupos lógicos para melhor usabilidade
 */
public class GaleriaFotosFormPanel extends BaseEnhancedFormPanel {

    // Dados da Foto
    private JTextField descricaoField;
    private JTextField caminhoField;
    private JComboBox<String> categoriaComboBox;

    public GaleriaFotosFormPanel() {
        super();
        initializeFields();
        setupFormStructure();
    }

    /**
     * Inicializa todos os campos do formulário
     */
    private void initializeFields() {
        // Dados da Foto
        descricaoField = createTextField(40);
        caminhoField = createTextField(50);
        categoriaComboBox = new JComboBox<>(new String[]{
            "IRMAO", "LOJA", "SESSAO", "EVENTO", "DOCUMENTO", "OUTRA"
        });

        // Aplicar estilização
        applyFieldStyling();
    }

    /**
     * Aplica estilização aos campos
     */
    private void applyFieldStyling() {
        // Dados da Foto
        PadraoLayout.estilizarCampoDescricaoFoto(descricaoField);
        PadraoLayout.estilizarCampoCaminhoFoto(caminhoField);
        caminhoField.setEditable(false);

        // Categoria
        categoriaComboBox.setBackground(Color.WHITE);
        categoriaComboBox.setBorder(PadraoLayout.BORDA_CAMPO);
    }

    /**
     * Configura estrutura do formulário com grupos lógicos
     */
    private void setupFormStructure() {
        FormSection[] sections = {
            new FormSection(
                "📸 DADOS DA FOTO",
                new FormSubsection(
                    "📝 Informações Principais",
                    new FormField("📋 Descrição:", descricaoField, 0, 0, 1, 1.0),
                    new FormField("📂 Categoria:", categoriaComboBox, 1, 0, 1, 1.0)
                ),
                new FormSubsection(
                    "📁 Arquivo da Foto",
                    new FormField("🗂️ Caminho:", caminhoField, 2, 0, 1, 1.0)
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
        // Dados da Foto
        descricaoField.setText("");
        caminhoField.setText("");
        categoriaComboBox.setSelectedIndex(0);
    }

    // Métodos getters para acesso aos campos
    public JTextField getDescricaoField() { return descricaoField; }
    public JTextField getCaminhoField() { return caminhoField; }
    public JComboBox<String> getCategoriaComboBox() { return categoriaComboBox; }
}
