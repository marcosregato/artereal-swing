package com.artereal.swing.ui.panels.screens;

import com.artereal.swing.ui.panels.screens.BaseEnhancedScreenPanel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Dashboard otimizado com UX moderna e cards informativos
 */
public class DashboardEnhancedPanel extends BaseEnhancedScreenPanel {
    
    public DashboardEnhancedPanel() {
        super();
        setupDashboardSections();
    }
    
    /**
     * Configura seções do dashboard
     */
    private void setupDashboardSections() {
        // Seção 1: Estatísticas Principais
        addSection(new ScreenSection(
            "📊 ESTATÍSTICAS PRINCIPAIS",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("👥", "Irmãos", "45", "Membros ativos"),
            new ScreenCard("🏢", "Lojas", "8", "Lojas registradas"),
            new ScreenCard("📝", "Sessões", "156", "Sessões realizadas"),
            new ScreenCard("📚", "Livros", "234", "Livros na biblioteca")
        ));
        
        // Seção 2: Atividades Recentes
        addSection(new ScreenSection(
            "🔄 ATIVIDADES RECENTES",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("📅", "Última Sessão", "15/05/2024", "Sessão Ordinária"),
            new ScreenCard("👤", "Novo Irmão", "João Silva", "Iniciado em 10/05"),
            new ScreenCard("📚", "Empréstimo", "3 livros", "Em andamento"),
            new ScreenCard("💰", "Saldo", "R$ 2.450,00", "Caixa geral")
        ));
        
        // Seção 3: Ações Rápidas
        addSection(new ScreenSection(
            "⚡ AÇÕES RÁPIDAS",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("➕", "Novo Irmão", "Cadastrar", null, this::cadastrarIrmao),
            new ScreenCard("📝", "Nova Sessão", "Registrar", null, this::registrarSessao),
            new ScreenCard("📚", "Empréstimo", "Gerenciar", null, this::gerenciarEmprestimo),
            new ScreenCard("📊", "Relatório", "Gerar", null, this::gerarRelatorio)
        ));
        
        // Seção 4: Informações do Sistema
        addSection(new ScreenSection(
            "ℹ️ INFORMAÇÕES DO SISTEMA",
            ScreenSection.LayoutType.CARDS,
            new ScreenCard("🔧", "Versão", "2.2.0", "Sistema ArteReal"),
            new ScreenCard("💾", "Backup", "Ontem", "Último backup"),
            new ScreenCard("👤", "Usuário", "Admin", "Logado como"),
            new ScreenCard("🕐", "Horário", "23:02", "Hora atual")
        ));
    }
    
    /**
     * Ações dos cards
     */
    private void cadastrarIrmao(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Abrir cadastro de novo irmão...", 
            "Ação Rápida", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void registrarSessao(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Abrir registro de nova sessão...", 
            "Ação Rápida", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerenciarEmprestimo(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Abrir gerenciamento de empréstimos...", 
            "Ação Rápida", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarRelatorio(ActionEvent e) {
        JOptionPane.showMessageDialog(this, 
            "Abrir gerador de relatórios...", 
            "Ação Rápida", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected String getScreenTitle() {
        return "📊 Dashboard";
    }
    
    @Override
    protected String getScreenSubtitle() {
        return "Visão geral do sistema ArteReal Masonic Lodge Management";
    }
    
    @Override
    protected JPanel createHeaderActions() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(HEADER_COLOR);
        
        JButton refreshButton = createActionButton("Atualizar", "🔄");
        refreshButton.addActionListener(e -> refreshDashboard());
        
        JButton configButton = createActionButton("Config", "⚙️");
        configButton.addActionListener(e -> openConfig());
        
        actionsPanel.add(refreshButton);
        actionsPanel.add(configButton);
        
        return actionsPanel;
    }
    
    /**
     * Atualiza dados do dashboard
     */
    private void refreshDashboard() {
        // Limpar conteúdo atual
        mainContent.removeAll();
        
        // Recarregar seções (simulado)
        setupDashboardSections();
        
        // Atualizar UI
        mainContent.revalidate();
        mainContent.repaint();
        
        JOptionPane.showMessageDialog(this, 
            "Dashboard atualizado com sucesso!", 
            "Atualização", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Abre configurações
     */
    private void openConfig() {
        JOptionPane.showMessageDialog(this, 
            "Abrir tela de configurações...", 
            "Configurações", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
