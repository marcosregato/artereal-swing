package com.artereal.swing.patterns.factory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Factory Method para criação de componentes UI
 * Implementa o padrão Factory Method para centralizar criação
 */
public class UIComponentFactory {
    
    /**
     * Cria botão com estilo padrão
     * 
     * @param text Texto do botão
     * @param icon Ícone (opcional)
     * @return JButton configurado
     */
    public static JButton createButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        
        // Aplica estilo padrão
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Cria campo de texto com estilo padrão
     * 
     * @param columns Número de colunas
     * @return JTextField configurado
     */
    public static JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        
        // Aplica estilo padrão
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textField.setBackground(Color.WHITE);
        
        return textField;
    }
    
    /**
     * Cria label com estilo padrão
     * 
     * @param text Texto da label
     * @return JLabel configurado
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        
        // Aplica estilo padrão
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(50, 50, 50));
        
        return label;
    }
    
    /**
     * Cria painel com estilo padrão
     * 
     * @param title Título do painel
     * @return JPanel configurado
     */
    public static JPanel createPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            title,
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 12),
            Color.BLACK
        ));
        panel.setBackground(new Color(248, 248, 248));
        
        return panel;
    }
    
    /**
     * Cria tabela com estilo padrão
     * 
     * @param data Dados da tabela
     * @param columns Colunas da tabela
     * @return JTable configurada
     */
    public static JTable createTable(Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns);
        
        // Aplica estilo padrão
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
        
        return table;
    }
    
    /**
     * Cria scroll pane com estilo padrão
     * 
     * @param component Componente a ser adicionado
     * @return JScrollPane configurado
     */
    public static JScrollPane createScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        
        // Aplica estilo padrão
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        return scrollPane;
    }
    
    /**
     * Cria combo box com estilo padrão
     * 
     * @param items Itens do combo
     * @return JComboBox configurado
     */
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        
        // Aplica estilo padrão
        comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));
        
        return comboBox;
    }
    
    /**
     * Cria área de texto com estilo padrão
     * 
     * @param rows Número de linhas
     * @param columns Número de colunas
     * @return JTextArea configurada
     */
    public static JTextArea createTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        
        // Aplica estilo padrão
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        return textArea;
    }
    
    /**
     * Aplica tema escuro aos componentes
     * 
     * @param component Componente a ser estilizado
     */
    public static void applyDarkTheme(Component component) {
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setBackground(new Color(60, 60, 60));
            button.setForeground(Color.WHITE);
        } else if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.setBackground(new Color(45, 45, 45));
        } else if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.setBackground(new Color(60, 60, 60));
            textField.setForeground(Color.WHITE);
            textField.setCaretColor(Color.WHITE);
        }
    }
    
    /**
     * Aplica tema claro aos componentes
     * 
     * @param component Componente a ser estilizado
     */
    public static void applyLightTheme(Component component) {
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setBackground(new Color(240, 240, 240));
            button.setForeground(Color.BLACK);
        } else if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.setBackground(Color.WHITE);
        } else if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            textField.setCaretColor(Color.BLACK);
        }
    }
}
