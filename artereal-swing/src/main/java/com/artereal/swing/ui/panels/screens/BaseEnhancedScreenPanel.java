package com.artereal.swing.ui.panels.screens;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Painel de tela base com UX otimizada para todas as telas do sistema
 * Fornece estrutura consistente para dashboards, configurações, relatórios, etc.
 */
public abstract class BaseEnhancedScreenPanel extends JPanel {
    
    protected static final Color HEADER_COLOR = new Color(70, 130, 180);
    protected static final Color SECTION_TITLE_COLOR = new Color(70, 130, 180);
    protected static final Color CARD_BACKGROUND = Color.WHITE;
    protected static final Color CARD_BORDER_COLOR = new Color(200, 200, 200);
    protected static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    protected static final Font SECTION_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 14);
    protected static final Font CARD_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 12);
    
    protected JPanel mainContent;
    protected JPanel headerPanel;
    protected JScrollPane scrollPane;
    
    public BaseEnhancedScreenPanel() {
        initializeComponents();
        setupLayout();
    }
    
    /**
     * Inicializa componentes básicos
     */
    private void initializeComponents() {
        // Header
        headerPanel = createHeaderPanel();
        
        // Conteúdo principal
        mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(245, 245, 245));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Scroll pane
        scrollPane = new JScrollPane(mainContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }
    
    /**
     * Configura layout principal
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Cria painel header com título e subtítulo
     */
    protected JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Título principal
        JLabel titleLabel = new JLabel(getScreenTitle());
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setIconTextGap(10);
        
        // Subtítulo
        JLabel subtitleLabel = new JLabel(getScreenSubtitle());
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        
        // Painel de texto
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(HEADER_COLOR);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        header.add(textPanel, BorderLayout.WEST);
        
        // Painel de ações (opcional)
        JPanel actionsPanel = createHeaderActions();
        if (actionsPanel != null) {
            header.add(actionsPanel, BorderLayout.EAST);
        }
        
        return header;
    }
    
    /**
     * Cria painel de ações no header (sobrecarregar se necessário)
     */
    protected JPanel createHeaderActions() {
        return null; // Implementar nas subclasses se necessário
    }
    
    /**
     * Adiciona seção ao conteúdo principal
     */
    protected void addSection(ScreenSection section) {
        JPanel sectionPanel = createSectionPanel(section);
        mainContent.add(sectionPanel);
        mainContent.add(Box.createVerticalStrut(20));
    }
    
    /**
     * Cria painel de seção
     */
    protected JPanel createSectionPanel(ScreenSection section) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(245, 245, 245));
        
        // Título da seção
        JLabel titleLabel = new JLabel(section.getTitle());
        titleLabel.setFont(SECTION_TITLE_FONT);
        titleLabel.setForeground(SECTION_TITLE_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Conteúdo da seção
        JPanel contentPanel = createSectionContent(section);
        
        container.add(titleLabel, BorderLayout.NORTH);
        container.add(contentPanel, BorderLayout.CENTER);
        
        return container;
    }
    
    /**
     * Cria conteúdo da seção (grid de cards ou layout customizado)
     */
    protected JPanel createSectionContent(ScreenSection section) {
        if (section.getLayoutType() == ScreenSection.LayoutType.CARDS) {
            return createCardsLayout(section);
        } else if (section.getLayoutType() == ScreenSection.LayoutType.TABLE) {
            return createTableLayout(section);
        } else if (section.getLayoutType() == ScreenSection.LayoutType.FORM) {
            return createFormLayout(section);
        } else {
            return createCustomLayout(section);
        }
    }
    
    /**
     * Cria layout em grade de cards
     */
    protected JPanel createCardsLayout(ScreenSection section) {
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        cardsPanel.setBackground(new Color(245, 245, 245));
        
        for (ScreenCard card : section.getCards()) {
            JPanel cardPanel = createCard(card);
            cardsPanel.add(cardPanel);
        }
        
        return cardsPanel;
    }
    
    /**
     * Cria painel de card individual
     */
    protected JPanel createCard(ScreenCard card) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(250, 120));
        cardPanel.setBackground(CARD_BACKGROUND);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Ícone e título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(CARD_BACKGROUND);
        
        JLabel iconLabel = new JLabel(card.getIcon());
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(card.getTitle());
        titleLabel.setFont(CARD_TITLE_FONT);
        titleLabel.setForeground(SECTION_TITLE_COLOR);
        
        titlePanel.add(iconLabel, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Valor/descrição
        JLabel valueLabel = new JLabel(card.getValue());
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.DARK_GRAY);
        
        // Descrição (se existir)
        if (card.getDescription() != null && !card.getDescription().isEmpty()) {
            JLabel descLabel = new JLabel(card.getDescription());
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            descLabel.setForeground(Color.GRAY);
            
            JPanel valuePanel = new JPanel(new BorderLayout());
            valuePanel.setBackground(CARD_BACKGROUND);
            valuePanel.add(valueLabel, BorderLayout.NORTH);
            valuePanel.add(descLabel, BorderLayout.CENTER);
            
            cardPanel.add(titlePanel, BorderLayout.NORTH);
            cardPanel.add(valuePanel, BorderLayout.CENTER);
        } else {
            cardPanel.add(titlePanel, BorderLayout.NORTH);
            cardPanel.add(valueLabel, BorderLayout.CENTER);
        }
        
        // Action listener (se existir)
        if (card.getActionListener() != null) {
            cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    card.getActionListener().actionPerformed(new java.awt.event.ActionEvent(e.getSource(), 0, ""));
                }
            });
        }
        
        return cardPanel;
    }
    
    /**
     * Cria layout de tabela (sobrecarregar nas subclasses)
     */
    protected JPanel createTableLayout(ScreenSection section) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BACKGROUND);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Implementar nas subclasses específicas
        JLabel placeholder = new JLabel("Tabela - Implementar na subclasse");
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        tablePanel.add(placeholder, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * Cria layout de formulário (sobrecarregar nas subclasses)
     */
    protected JPanel createFormLayout(ScreenSection section) {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(CARD_BACKGROUND);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Implementar nas subclasses específicas
        JLabel placeholder = new JLabel("Formulário - Implementar na subclasse");
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(placeholder, BorderLayout.CENTER);
        
        return formPanel;
    }
    
    /**
     * Cria layout customizado (sobrecarregar nas subclasses)
     */
    protected JPanel createCustomLayout(ScreenSection section) {
        JPanel customPanel = new JPanel(new BorderLayout());
        customPanel.setBackground(CARD_BACKGROUND);
        customPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Implementar nas subclasses específicas
        JLabel placeholder = new JLabel("Layout Customizado - Implementar na subclasse");
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        customPanel.add(placeholder, BorderLayout.CENTER);
        
        return customPanel;
    }
    
    /**
     * Cria botão de ação padrão
     */
    protected JButton createActionButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(HEADER_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(50, 110, 160));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(HEADER_COLOR);
            }
        });
        
        return button;
    }
    
    /**
     * Métodos abstratos para implementação nas subclasses
     */
    protected abstract String getScreenTitle();
    protected abstract String getScreenSubtitle();
    
    /**
     * Classes de definição de estrutura de tela
     */
    public static class ScreenSection {
        private final String title;
        private final LayoutType layoutType;
        private final ScreenCard[] cards;
        
        public ScreenSection(String title, LayoutType layoutType, ScreenCard... cards) {
            this.title = title;
            this.layoutType = layoutType;
            this.cards = cards;
        }
        
        public String getTitle() { return title; }
        public LayoutType getLayoutType() { return layoutType; }
        public ScreenCard[] getCards() { return cards; }
        
        public enum LayoutType {
            CARDS, TABLE, FORM, CUSTOM
        }
    }
    
    public static class ScreenCard {
        private final String icon;
        private final String title;
        private final String value;
        private final String description;
        private final java.awt.event.ActionListener actionListener;
        
        public ScreenCard(String icon, String title, String value) {
            this(icon, title, value, null, null);
        }
        
        public ScreenCard(String icon, String title, String value, String description) {
            this(icon, title, value, description, null);
        }
        
        public ScreenCard(String icon, String title, String value, String description, java.awt.event.ActionListener actionListener) {
            this.icon = icon;
            this.title = title;
            this.value = value;
            this.description = description;
            this.actionListener = actionListener;
        }
        
        public String getIcon() { return icon; }
        public String getTitle() { return title; }
        public String getValue() { return value; }
        public String getDescription() { return description; }
        public java.awt.event.ActionListener getActionListener() { return actionListener; }
    }
}
