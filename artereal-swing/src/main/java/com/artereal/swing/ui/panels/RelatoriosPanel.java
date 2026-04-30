package com.artereal.swing.ui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de Relatórios
 */
public class RelatoriosPanel extends JPanel {
    
    public RelatoriosPanel() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        // Componentes serão adicionados no setupLayout
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Relatórios", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        
        JButton irmaosButton = new JButton("Relatório de Irmãos");
        JButton financeiroButton = new JButton("Relatório Financeiro");
        JButton sessoesButton = new JButton("Relatório de Sessões");
        JButton bibliotecaButton = new JButton("Relatório da Biblioteca");
        JButton geralButton = new JButton("Relatório Geral");
        JButton customButton = new JButton("Relatório Customizado");
        
        buttonPanel.add(irmaosButton);
        buttonPanel.add(financeiroButton);
        buttonPanel.add(sessoesButton);
        buttonPanel.add(bibliotecaButton);
        buttonPanel.add(geralButton);
        buttonPanel.add(customButton);
        
        JTextArea infoArea = new JTextArea("Selecione um relatório para gerar...");
        infoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    public void refreshData() {
        revalidate();
        repaint();
    }
}
