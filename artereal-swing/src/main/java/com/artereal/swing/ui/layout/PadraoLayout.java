package com.artereal.swing.ui.layout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import javax.swing.border.Border;

/**
 * Classe utilitária com configurações padrão de layout para todas as telas do sistema.
 * Baseada exclusivamente no layout da tela "Gestão de Lojas".
 * Configurações avançadas para formulários complexos e layouts profissionais.
 */
public class PadraoLayout {
    
    // Cores padrão do sistema
    public static final Color COR_PRIMARIA = new Color(70, 130, 180); // Azul principal
    public static final Color COR_HEADER = new Color(25, 25, 112); // Azul marinho maçônico
    public static final Color COR_FUNDO = new Color(245, 245, 250); // Fundo claro
    public static final Color COR_PAINEL = new Color(240, 240, 245); // Painel secundário
    public static final Color COR_BORDA = new Color(200, 200, 210); // Borda cinza
    public static final Color COR_BORDA_GRUPO = new Color(220, 220, 230); // Borda de grupo
    public static final Color COR_TEXTO_TITULO = new Color(70, 130, 180); // Azul título
    public static final Color COR_TEXTO_SUBTITULO = new Color(173, 216, 230); // Azul claro
    public static final Color COR_TEXTO_BRANCO = Color.WHITE;
    public static final Color COR_TEXTO_PESQUISA = new Color(100, 100, 120);
    public static final Color COR_SELECAO_TABELA = new Color(173, 216, 230); // Azul seleção
    public static final Color COR_SELECAO_TEXTO = new Color(25, 84, 123); // Azul escuro
    
    // Cores de botões pastéis
    public static final Color COR_BOTAO_SALVAR = new Color(144, 238, 144); // Verde pastel
    public static final Color COR_BOTAO_NOVO = new Color(173, 216, 230); // Azul pastel
    public static final Color COR_BOTAO_EDITAR = new Color(255, 250, 205); // Amarelo pastel
    public static final Color COR_BOTAO_EXCLUIR = new Color(255, 182, 193); // Rosa pastel
    public static final Color COR_BOTAO_LIMPAR = new Color(240, 240, 240); // Cinza pastel
    public static final Color COR_BOTAO_PESQUISAR = new Color(221, 160, 221); // Lavanda pastel
    public static final Color COR_BOTAO_APROVAR = new Color(255, 250, 205); // Amarelo claro
    public static final Color COR_BOTAO_REJEITAR = new Color(255, 218, 185); // Laranja pastel
    public static final Color COR_BOTAO_INICIAR = new Color(221, 160, 221); // Lavanda
    
    // Fontes padrão
    public static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONTE_GRUPO = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONTE_PESQUISA = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONTE_TABELA = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONTE_HEADER_TABELA = new Font("Segoe UI", Font.BOLD, 12);
    
    // Bordas padrão
    public static final Border BORDA_PADRAO = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COR_BORDA, 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );
    
    public static final Border BORDA_GRUPO = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COR_BORDA_GRUPO, 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );
    
    public static final Border BORDA_CAMPO = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(180, 180, 190), 1),
        BorderFactory.createEmptyBorder(5, 8, 5, 8)
    );
    
    public static final Border BORDA_PAINEL = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COR_BORDA, 1),
        BorderFactory.createEmptyBorder(15, 20, 15, 20)
    );
    
    public static final Border BORDA_CONTEUDO = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COR_BORDA, 1),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    );
    
    // Dimensões padrão (baseado LojasPanel)
    public static final int DIVISOR_SPLIT_VERTICAL = 300;
    public static final double PESO_SPLIT_VERTICAL = 0.6;
    public static final int ALTURA_LINHA_TABELA = 25;
    public static final int MARGEM_PADRAO = 10;
    public static final int MARGEM_GRANDE = 20; // LojasPanel
    public static final int ESPACAMENTO_CAMPOS = 10;
    public static final int ESPACAMENTO_BOTOES = 10;
    public static final int ESPACAMENTO_GRUPO = 10;
    public static final int ESPACAMENTO_GRID = 8; // LojasPanel
    public static final int LARGURA_CAMPO_PESQUISA = 200;
    public static final int ALTURA_CAMPO = 30;
    public static final int MARGEM_PAINEL = 20;
    public static final int MARGEM_CONTEUDO = 30;
    
    // Layout de formulários (LojasPanel)
    public static final int COLUNAS_FORMULARIO = 2;
    public static final int LINHAS_TEXTO_AREA = 3;
    
    /**
     * Cria um header padrão com título, subtítulo e logo maçônico
     */
    public static JPanel criarHeader(String titulo, String subtitulo) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COR_HEADER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel(titulo, SwingConstants.LEFT);
        titleLabel.setFont(FONTE_TITULO);
        titleLabel.setForeground(COR_TEXTO_BRANCO);
        
        JLabel subtitleLabel = new JLabel(subtitulo, SwingConstants.LEFT);
        subtitleLabel.setFont(FONTE_SUBTITULO);
        subtitleLabel.setForeground(COR_TEXTO_SUBTITULO);
        
        // Logo maçônico (se disponível)
        try {
            JLabel logoLabel = new JLabel("⚡"); // Placeholder para logo maçônico
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
            logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            
            JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
            titleContainer.setBackground(COR_HEADER);
            titleContainer.add(titleLabel);
            titleContainer.add(subtitleLabel);
            
            JPanel headerContent = new JPanel(new BorderLayout());
            headerContent.setBackground(COR_HEADER);
            headerContent.add(logoLabel, BorderLayout.WEST);
            headerContent.add(titleContainer, BorderLayout.CENTER);
            
            headerPanel.add(headerContent, BorderLayout.CENTER);
        } catch (Exception e) {
            // Fallback sem logo
            JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 5));
            titleContainer.setBackground(COR_HEADER);
            titleContainer.add(titleLabel);
            titleContainer.add(subtitleLabel);
            headerPanel.add(titleContainer, BorderLayout.CENTER);
        }
        
        return headerPanel;
    }
    
    /**
     * Cria um painel de pesquisa padrão
     */
    public static JPanel criarPainelPesquisa(JTextField campoPesquisa, JButton botaoPesquisa) {
        JPanel pesquisaPanel = new JPanel(new BorderLayout());
        pesquisaPanel.setBackground(COR_PAINEL);
        pesquisaPanel.setBorder(BORDA_PAINEL);
        
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchContainer.setBackground(COR_PAINEL);
        
        JLabel searchLabel = new JLabel("🔍 Pesquisar:");
        searchLabel.setFont(FONTE_PESQUISA);
        searchLabel.setForeground(COR_TEXTO_PESQUISA);
        
        campoPesquisa.setPreferredSize(new Dimension(LARGURA_CAMPO_PESQUISA, ALTURA_CAMPO));
        campoPesquisa.setBorder(BORDA_CAMPO);
        
        botaoPesquisa.setBackground(COR_BOTAO_PESQUISAR);
        botaoPesquisa.setForeground(new Color(102, 51, 153));
        botaoPesquisa.setFont(FONTE_BOTAO);
        botaoPesquisa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BOTAO_PESQUISAR, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        botaoPesquisa.setFocusPainted(false);
        botaoPesquisa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchContainer.add(searchLabel);
        searchContainer.add(campoPesquisa);
        searchContainer.add(botaoPesquisa);
        pesquisaPanel.add(searchContainer, BorderLayout.CENTER);
        
        return pesquisaPanel;
    }
    
    /**
     * Cria um grupo de formulário padrão com título
     */
    public static JPanel criarGrupoFormulario(String titulo) {
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBackground(Color.WHITE);
        groupPanel.setBorder(BORDA_GRUPO);
        
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(FONTE_GRUPO);
        titleLabel.setForeground(COR_TEXTO_TITULO);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        groupPanel.add(titleLabel, BorderLayout.NORTH);
        return groupPanel;
    }
    
    /**
     * Cria um botão estilizado padrão
     */
    public static JButton criarBotao(String texto, Color corFundo) {
        JButton button = new JButton(texto);
        button.setBackground(corFundo);
        button.setForeground(Color.BLACK);
        button.setFont(FONTE_BOTAO);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corFundo.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(corFundo.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(corFundo);
            }
        });
        
        return button;
    }
    
    /**
     * Cria um botão SALVAR com cor pastel verde
     */
    public static JButton criarBotaoSalvar() {
        return criarBotao("Salvar", COR_BOTAO_SALVAR);
    }
    
    /**
     * Cria um botão EDITAR com cor pastel amarela
     */
    public static JButton criarBotaoEditar() {
        return criarBotao("Editar", COR_BOTAO_EDITAR);
    }
    
    /**
     * Cria um botão EXCLUIR com cor pastel rosa
     */
    public static JButton criarBotaoExcluir() {
        return criarBotao("Excluir", COR_BOTAO_EXCLUIR);
    }
    
    /**
     * Cria um botão NOVO com cor pastel azul
     */
    public static JButton criarBotaoNovo() {
        return criarBotao("Novo", COR_BOTAO_NOVO);
    }
    
    /**
     * Cria um botão LIMPAR com cor pastel cinza
     */
    public static JButton criarBotaoLimpar() {
        return criarBotao("Limpar", COR_BOTAO_LIMPAR);
    }
    
    /**
     * Cria um botão PESQUISAR com cor pastel lavanda
     */
    public static JButton criarBotaoPesquisar() {
        return criarBotao("Pesquisar", COR_BOTAO_PESQUISAR);
    }
    
    /**
     * Cria um conjunto de botões padrão (Salvar, Editar, Excluir, Novo, Limpar)
     * Todos com cores pastéis consistentes
     */
    public static JPanel criarPainelBotoesAcao() {
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.setBackground(Color.WHITE);
        
        painelBotoes.add(criarBotaoNovo());
        painelBotoes.add(criarBotaoSalvar());
        painelBotoes.add(criarBotaoEditar());
        painelBotoes.add(criarBotaoExcluir());
        painelBotoes.add(criarBotaoLimpar());
        
        return painelBotoes;
    }
    
    /**
     * Configura uma tabela com estilo padrão
     * Colunas com letras pretas e cabeçalho com fundo azul e letras brancas
     */
    public static void configurarTabela(JTable tabela) {
        // Configurações das linhas da tabela
        tabela.setRowHeight(ALTURA_LINHA_TABELA);
        tabela.setFont(FONTE_TABELA);
        tabela.setForeground(Color.BLACK); // Letras pretas nas colunas
        tabela.setBackground(Color.WHITE);  // Fundo branco das células
        
        // Configurações do cabeçalho (linha de nomes das colunas)
        JTableHeader header = tabela.getTableHeader();
        header.setFont(FONTE_HEADER_TABELA);
        header.setBackground(COR_PRIMARIA);      // Fundo azul
        header.setForeground(COR_TEXTO_BRANCO);  // Letras brancas
        
        // Configurações de seleção
        tabela.setSelectionBackground(COR_SELECAO_TABELA);
        tabela.setSelectionForeground(COR_SELECAO_TEXTO);
        
        // Outras configurações de estilo
        tabela.setShowGrid(true);
        tabela.setGridColor(COR_BORDA);
        tabela.setIntercellSpacing(new Dimension(1, 1));
        
        // Configuração do renderer do cabeçalho para garantir o estilo
        header.setDefaultRenderer(new HeaderRenderer());
    }
    
    /**
     * Renderer personalizado para o cabeçalho da tabela
     * Garante fundo azul e letras brancas consistentes
     */
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setBackground(COR_PRIMARIA);      // Fundo azul
            setForeground(COR_TEXTO_BRANCO);  // Letras brancas
            setFont(FONTE_HEADER_TABELA);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COR_BORDA),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            
            return this;
        }
    }
    
    /**
     * Cria um painel de botões padrão com os botões principais
     */
    public static JPanel criarPainelBotoes(JButton... botoes) {
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        for (JButton botao : botoes) {
            botoesPanel.add(botao);
        }
        
        return botoesPanel;
    }
    
    /**
     * Cria um painel de conteúdo com borda padrão
     */
    public static JPanel criarPainelConteudo(Component componente) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(componente, BorderLayout.CENTER);
        contentPanel.setBorder(BORDA_CONTEUDO);
        return contentPanel;
    }
    
    /**
     * Cria um split pane vertical (padrão LojasPanel)
     */
    public static JSplitPane criarSplitPaneVertical(Component topo, Component baixo) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(DIVISOR_SPLIT_VERTICAL);
        splitPane.setResizeWeight(PESO_SPLIT_VERTICAL);
        splitPane.setTopComponent(topo);
        splitPane.setBottomComponent(baixo);
        return splitPane;
    }
    
    /**
     * Cria um painel de formulário com múltiplos grupos organizados verticalmente
     */
    public static JPanel criarFormularioMultiplosGrupos(JPanel... grupos) {
        JPanel allGroups = new JPanel(new BorderLayout());
        allGroups.setBackground(Color.WHITE);
        
        if (grupos.length == 0) return allGroups;
        
        if (grupos.length == 1) {
            allGroups.add(grupos[0], BorderLayout.CENTER);
        } else if (grupos.length == 2) {
            JPanel topGroups = new JPanel(new BorderLayout());
            topGroups.setBackground(Color.WHITE);
            topGroups.add(grupos[0], BorderLayout.NORTH);
            
            JPanel bottomGroups = new JPanel(new BorderLayout());
            bottomGroups.setBackground(Color.WHITE);
            bottomGroups.add(grupos[1], BorderLayout.CENTER);
            
            allGroups.add(topGroups, BorderLayout.NORTH);
            allGroups.add(bottomGroups, BorderLayout.CENTER);
        } else {
            // Organiza em 3 camadas (padrão LojasPanel)
            JPanel topGroups = new JPanel(new BorderLayout());
            topGroups.setBackground(Color.WHITE);
            topGroups.add(grupos[0], BorderLayout.NORTH);
            if (grupos.length > 1) topGroups.add(grupos[1], BorderLayout.CENTER);
            
            JPanel middleGroups = new JPanel(new BorderLayout());
            middleGroups.setBackground(Color.WHITE);
            if (grupos.length > 2) middleGroups.add(grupos[2], BorderLayout.NORTH);
            if (grupos.length > 3) middleGroups.add(grupos[3], BorderLayout.CENTER);
            
            JPanel bottomGroups = new JPanel(new BorderLayout());
            bottomGroups.setBackground(Color.WHITE);
            if (grupos.length > 4) bottomGroups.add(grupos[4], BorderLayout.NORTH);
            if (grupos.length > 5) bottomGroups.add(grupos[5], BorderLayout.CENTER);
            
            allGroups.add(topGroups, BorderLayout.NORTH);
            allGroups.add(middleGroups, BorderLayout.CENTER);
            allGroups.add(bottomGroups, BorderLayout.SOUTH);
        }
        
        return allGroups;
    }
    
    /**
     * Cria um painel de grupo de formulário com scroll (padrão LojasPanel)
     */
    public static JScrollPane criarFormularioComScroll(JPanel formulario) {
        JScrollPane formScroll = new JScrollPane(formulario);
        formScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        return formScroll;
    }
    
    /**
     * Cria um painel para TextArea com label e borda (padrão LojasPanel)
     */
    public static JPanel criarPainelTextArea(String label, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Cria um GridLayout para conteúdo de grupo com espaçamento padrão
     */
    public static GridLayout criarLayoutGrupo() {
        return new GridLayout(0, 2, ESPACAMENTO_GRUPO, 8);
    }
    
    /**
     * Cria um GridLayout para formulários (padrão LojasPanel)
     */
    public static GridLayout criarLayoutFormulario() {
        System.out.println("[PADRAO_LAYOUT] criarLayoutFormulario() chamado - COLUNAS=" + COLUNAS_FORMULARIO + ", ESPACAMENTO_CAMPOS=" + ESPACAMENTO_CAMPOS + ", ESPACAMENTO_GRID=" + ESPACAMENTO_GRID);
        return new GridLayout(0, COLUNAS_FORMULARIO, ESPACAMENTO_CAMPOS, ESPACAMENTO_GRID);
    }
    
    /**
     * Cria um painel principal com margens grandes (padrão LojasPanel)
     */
    public static JPanel criarPainelPrincipal() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COR_FUNDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(MARGEM_GRANDE, MARGEM_CONTEUDO, MARGEM_GRANDE, MARGEM_CONTEUDO));
        return mainPanel;
    }
    
    /**
     * Aplica o layout padrão a um painel principal
     */
    public static void aplicarLayoutPadrao(JPanel painel) {
        painel.setLayout(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(MARGEM_GRANDE, MARGEM_CONTEUDO, MARGEM_GRANDE, MARGEM_CONTEUDO));
    }
    
    /**
     * Cria um label estilizado para formulários
     */
    public static JLabel criarLabelFormulario(String texto) {
        System.out.println("[PADRAO_LAYOUT] criarLabelFormulario() chamado - texto='" + texto + "'");
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_GRUPO);
        label.setForeground(new Color(70, 130, 180));
        System.out.println("[PADRAO_LAYOUT] Label estilizado - Fonte=" + FONTE_GRUPO.getName() + ", Cor=Azul(70,130,180)");
        return label;
    }
    
    /**
     * Aplica estilização padrão a um campo de texto
     */
    public static void estilizarCampoTexto(JTextField campo) {
        System.out.println("[PADRAO_LAYOUT] estilizarCampoTexto() chamado - campo=" + campo.getClass().getSimpleName());
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        System.out.println("[PADRAO_LAYOUT] Campo estilizado - Borda=PADRAO, Background=WHITE");
    }
    
    /**
     * Aplica estilização padrão a um combo box
     */
    public static void estilizarComboBox(JComboBox<?> combo) {
        System.out.println("[PADRAO_LAYOUT] estilizarComboBox() chamado - combo=" + combo.getClass().getSimpleName());
        combo.setBorder(BORDA_CAMPO);
        combo.setBackground(Color.WHITE);
        System.out.println("[PADRAO_LAYOUT] ComboBox estilizado - Borda=PADRAO, Background=WHITE");
    }
    
    // ============ MÉTODOS DO LAYOUTTEMPLATE INTEGRADOS ============
    
    /**
     * Cria um painel de filtros/pesquisa padrão (do LayoutTemplate)
     */
    public static JPanel criarPainelFiltros() {
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtroPanel.setBackground(COR_PAINEL);
        filtroPanel.setBorder(BORDA_PAINEL);
        
        JLabel pesquisaLabel = new JLabel("🔍 Pesquisar:");
        pesquisaLabel.setFont(FONTE_PESQUISA);
        pesquisaLabel.setForeground(COR_TEXTO_TITULO);
        
        JTextField pesquisaField = new JTextField(20);
        pesquisaField.setPreferredSize(new Dimension(LARGURA_CAMPO_PESQUISA, ALTURA_CAMPO));
        pesquisaField.setBorder(BORDA_CAMPO);
        
        JButton pesquisarButton = criarBotaoPesquisar();
        pesquisarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_BOTAO_PESQUISAR, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        pesquisarButton.setFocusPainted(false);
        pesquisarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        filtroPanel.add(pesquisaLabel);
        filtroPanel.add(pesquisaField);
        filtroPanel.add(pesquisarButton);
        
        return filtroPanel;
    }
    
    /**
     * Cria um painel de conteúdo principal com borda (do LayoutTemplate)
     */
    public static JPanel criarPainelConteudo(JComponent mainContent) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COR_FUNDO);
        
        // Painel de conteúdo principal com borda
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setBorder(BORDA_CONTEUDO);
        
        wrapperPanel.add(mainContent, BorderLayout.CENTER);
        contentPanel.add(wrapperPanel, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    /**
     * Cria um painel principal completo com header e conteúdo (do LayoutTemplate)
     */
    public static JPanel criarPainelCompleto(String titulo, String subtitulo, String icone, JComponent conteudoPrincipal) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COR_FUNDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(MARGEM_PADRAO, MARGEM_GRANDE, MARGEM_PADRAO, MARGEM_GRANDE));
        
        // Header estilizado com logo
        JPanel headerPanel = criarHeader(icone + " " + titulo, subtitulo);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel de filtros
        JPanel filtroPanel = criarPainelFiltros();
        mainPanel.add(filtroPanel, BorderLayout.CENTER);
        
        // Painel de conteúdo
        JPanel contentPanel = criarPainelConteudo(conteudoPrincipal);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
        
    /**
     * Cria um botão estilizado com efeito hover (melhorado do LayoutTemplate)
     */
    public static JButton criarBotaoComHover(String texto, Color bgColor) {
        JButton button = criarBotao(texto, bgColor);
        
        // Efeito hover melhorado
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Template abstrato para painéis (do LayoutTemplate)
     * Esta classe pode ser estendida para criar painéis com layout padrão
     */
    public abstract static class TemplatePainel extends JPanel {
        
        protected abstract String getTituloPainel();
        protected abstract String getSubtituloPainel();
        protected abstract String getIconePainel();
        protected abstract JPanel criarConteudoPrincipal();
        
        public TemplatePainel() {
            setupLayout();
        }
        
        protected void setupLayout() {
            aplicarLayoutPadrao(this);
            
            // Header estilizado com logo
            JPanel headerPanel = criarHeader(getIconePainel() + " " + getTituloPainel(), getSubtituloPainel());
            add(headerPanel, BorderLayout.NORTH);
            
            // Painel principal
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(COR_FUNDO);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(MARGEM_PADRAO, MARGEM_GRANDE, MARGEM_PADRAO, MARGEM_GRANDE));
            
            // Painel de filtros
            JPanel filtroPanel = criarPainelFiltros();
            mainPanel.add(filtroPanel, BorderLayout.NORTH);
            
            // Conteúdo específico do painel
            JPanel contentPanel = criarConteudoPrincipal();
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            
            add(mainPanel, BorderLayout.CENTER);
        }
    }
}
