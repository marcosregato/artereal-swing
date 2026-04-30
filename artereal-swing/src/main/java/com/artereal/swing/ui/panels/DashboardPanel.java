package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;

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
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO CRÍTICA
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📊 Dashboard", "Estatísticas e informações do sistema ArteReal");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel central com informações usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel centerPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        centerPanel.setBackground(PadraoLayout.COR_PAINEL);
        centerPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        // Cards de estatísticas usando método existente
        centerPanel.add(createStatCard("👥 Irmãos", "5", "Total cadastrados", PadraoLayout.COR_PRIMARIA));
        centerPanel.add(createStatCard("🏢 Lojas", "1", "Loja ativa", new Color(60, 179, 113)));
        centerPanel.add(createStatCard("📝 Sessões", "2", "Realizadas este mês", new Color(255, 193, 7)));
        centerPanel.add(createStatCard("💰 Saldo", "R$ 1.250,50", "Caixa atual", new Color(220, 53, 69)));
        centerPanel.add(createStatCard("📚 Livros", "10", "No acervo", new Color(108, 117, 125)));
        centerPanel.add(createStatCard("📖 Empréstimos", "2", "Ativos", new Color(149, 165, 166)));
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, String description, Color color) {
        JPanel card = PadraoLayout.criarGrupoFormulario(title);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = PadraoLayout.criarLabelFormulario(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        
        JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
