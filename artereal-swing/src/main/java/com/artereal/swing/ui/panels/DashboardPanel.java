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
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📊 Dashboard", "Estatísticas e informações do sistema ArteReal");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal com margens usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel central com informações usando PadraoLayout - 100% CONFORMIDADE
        JPanel centerPanel = new JPanel(new GridLayout(2, 3, PadraoLayout.ESPACAMENTO_GRUPO, PadraoLayout.ESPACAMENTO_GRUPO));
        centerPanel.setBackground(PadraoLayout.COR_PAINEL);
        centerPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        // Cards de estatísticas usando cores pastéis consistentes com PadraoLayout
        centerPanel.add(createStatCard("👥 Irmãos", "5", "Total cadastrados", PadraoLayout.COR_PRIMARIA));
        centerPanel.add(createStatCard("🏢 Lojas", "1", "Loja ativa", PadraoLayout.COR_BOTAO_SALVAR));
        centerPanel.add(createStatCard("📝 Sessões", "2", "Realizadas este mês", PadraoLayout.COR_BOTAO_APROVAR));
        centerPanel.add(createStatCard("💰 Saldo", "R$ 1.250,50", "Caixa atual", PadraoLayout.COR_BOTAO_EXCLUIR));
        centerPanel.add(createStatCard("📚 Livros", "10", "No acervo", PadraoLayout.COR_BOTAO_LIMPAR));
        centerPanel.add(createStatCard("📖 Empréstimos", "2", "Ativos", PadraoLayout.COR_BOTAO_PESQUISAR));
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String title, String value, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PadraoLayout.COR_BORDA_CARD_SUAVIZADA, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Usar cores de fundo ideais para cards baseado na cor do ícone/título
        Color fundoCard = PadraoLayout.COR_CARD_PRIMARIO; // Branco padrão
        
        // Ajustar fundo baseado na cor do card para melhor harmonia
        if (color.equals(PadraoLayout.COR_BOTAO_SALVAR)) {
            fundoCard = PadraoLayout.COR_CARD_SUCESSO; // Verde muito claro
        } else if (color.equals(PadraoLayout.COR_BOTAO_EXCLUIR)) {
            fundoCard = PadraoLayout.COR_CARD_PERIGO; // Vermelho muito claro
        } else if (color.equals(PadraoLayout.COR_BOTAO_NOVO)) {
            fundoCard = PadraoLayout.COR_CARD_INFO; // Azul muito claro
        } else if (color.equals(PadraoLayout.COR_BOTAO_EDITAR)) {
            fundoCard = PadraoLayout.COR_CARD_ALERTA; // Laranja muito claro
        }
        
        card.setBackground(fundoCard);
        card.setOpaque(true);
        
        JLabel titleLabel = PadraoLayout.criarLabelFormulario(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(PadraoLayout.COR_TEXTO_CARD_TITULO);
        
        JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(PadraoLayout.COR_TEXTO_CARD_SECUNDARIO);
        
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
