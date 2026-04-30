package com.artereal.swing.ui.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Painel do Dashboard com estatísticas do sistema
 */
public class DashboardPanel extends JPanel {
    
    public DashboardPanel() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        // Componentes serão adicionados no setupLayout
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Dashboard - ArteReal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel central com informações
        JPanel centerPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Cards de estatísticas
        centerPanel.add(createStatCard("Irmãos", "5", "Total cadastrados", new Color(70, 130, 180)));
        centerPanel.add(createStatCard("Lojas", "1", "Loja ativa", new Color(60, 179, 113)));
        centerPanel.add(createStatCard("Sessões", "2", "Realizadas este mês", new Color(255, 193, 7)));
        centerPanel.add(createStatCard("Saldo", "R$ 1.250,50", "Caixa atual", new Color(220, 53, 69)));
        centerPanel.add(createStatCard("Livros", "10", "No acervo", new Color(108, 117, 125)));
        centerPanel.add(createStatCard("Empréstimos", "2", "Ativos", new Color(149, 165, 166)));
        
        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(color);
        
        JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    /**
     * Atualiza os dados do dashboard
     */
    public void refreshData() {
        // Implementar atualização dos dados
        revalidate();
        repaint();
    }
}
