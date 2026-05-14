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
    
    /**
     * Classe interna para criar bordas arredondadas
     */
    public static class RoundBorder implements Border {
        private Color color;
        private int radius;
        private int thickness;
        
        public RoundBorder(Color color, int radius, int thickness) {
            this.color = color;
            this.radius = radius;
            this.thickness = thickness;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
    
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
    
    // Cores de botões otimizadas - MELHORES CORES para UX/UI
    public static final Color COR_BOTAO_SALVAR = new Color(144, 238, 144); // Verde pastel
    public static final Color COR_BOTAO_NOVO = new Color(173, 216, 230); // Azul pastel
    public static final Color COR_BOTAO_EDITAR = new Color(255, 250, 205); // Amarelo pastel
    public static final Color COR_BOTAO_EXCLUIR = new Color(255, 182, 193); // Rosa pastel
    public static final Color COR_BOTAO_LIMPAR = new Color(240, 240, 240); // Cinza claro
    public static final Color COR_BOTAO_PESQUISAR = new Color(221, 160, 221); // Roxo pastel
    public static final Color COR_BOTAO_APROVAR = new Color(0, 150, 136); // Verde-azul Material Design
    public static final Color COR_BOTAO_REJEITAR = new Color(239, 83, 80); // Vermelho claro Material Design
    public static final Color COR_BOTAO_INICIAR = new Color(255, 167, 38); // Âmbar Material Design
    
    // Cores de texto para botões - letras escuras para melhor legibilidade
    public static final Color COR_TEXTO_BOTAO_SALVAR = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_NOVO = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_EDITAR = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_EXCLUIR = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_LIMPAR = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_PESQUISAR = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_APROVAR = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_REJEITAR = new Color(0, 0, 0); // Preto
    public static final Color COR_TEXTO_BOTAO_INICIAR = new Color(0, 0, 0); // Preto
    
    // Cores de fundo ideais para cards - MELHORES CORES UX/UI
    public static final Color COR_CARD_PRIMARIO = new Color(255, 255, 255); // Branco puro
    public static final Color COR_CARD_SECUNDARIO = new Color(248, 249, 250); // Cinza muito claro
    public static final Color COR_CARD_INFO = new Color(227, 242, 253); // Azul muito claro
    public static final Color COR_CARD_SUCESSO = new Color(232, 245, 233); // Verde muito claro
    public static final Color COR_CARD_ALERTA = new Color(255, 248, 225); // Laranja muito claro
    public static final Color COR_CARD_PERIGO = new Color(255, 235, 238); // Vermelho muito claro
    public static final Color COR_CARD_DESTAQUE = new Color(237, 231, 246); // Roxo muito claro
    public static final Color COR_CARD_NEUTRO = new Color(245, 245, 245); // Cinza neutro claro
    public static final Color COR_CARD_ESCURO = new Color(33, 37, 41); // Cinza escuro elegante
    
    // Cores de texto para cards
    public static final Color COR_TEXTO_CARD_PRIMARIO = new Color(33, 37, 41); // Cinza escuro
    public static final Color COR_TEXTO_CARD_SECUNDARIO = new Color(108, 117, 125); // Cinza médio
    public static final Color COR_TEXTO_CARD_CLARO = Color.WHITE; // Branco para cards escuros
    public static final Color COR_TEXTO_CARD_TITULO = new Color(52, 58, 64); // Cinza escuro para títulos
    
    // Cores de borda para cards
    public static final Color COR_BORDA_CARD = new Color(222, 226, 230); // Cinza claro suave
    public static final Color COR_BORDA_CARD_SUAVIZADA = new Color(233, 236, 239); // Cinza muito suave
    public static final Color COR_BORDA_CARD_ESCURA = new Color(173, 181, 189); // Cinza médio
    
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
    public static final int ESPACAMENTO_CAMPOS_CODIGO = 8; // Diminuído para distância mais compacta entre JLabel e JTextField
    public static final int ESPACAMENTO_BOTOES = 10;
    public static final int ESPACAMENTO_GRUPO = 10;
    public static final int ESPACAMENTO_GRID = 10; // LojasPanel
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
        botaoPesquisa.setForeground(Color.BLACK);
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
     * Aplica cores pastéis em botões existentes - MÉTODO PRINCIPAL
     * Centraliza toda a lógica de cores pastéis do sistema
     */
    public static void aplicarCoresPastelBotao(JButton button, String tipoBotao) {
        switch (tipoBotao.toLowerCase()) {
            case "salvar":
                button.setBackground(COR_BOTAO_SALVAR);
                button.setForeground(COR_TEXTO_BOTAO_SALVAR);
                break;
            case "novo":
                button.setBackground(COR_BOTAO_NOVO);
                button.setForeground(COR_TEXTO_BOTAO_NOVO);
                break;
            case "editar":
                button.setBackground(COR_BOTAO_EDITAR);
                button.setForeground(COR_TEXTO_BOTAO_EDITAR);
                break;
            case "excluir":
                button.setBackground(COR_BOTAO_EXCLUIR);
                button.setForeground(COR_TEXTO_BOTAO_EXCLUIR);
                break;
            case "limpar":
                button.setBackground(COR_BOTAO_LIMPAR);
                button.setForeground(COR_TEXTO_BOTAO_LIMPAR);
                break;
            case "pesquisar":
                button.setBackground(COR_BOTAO_PESQUISAR);
                button.setForeground(COR_TEXTO_BOTAO_PESQUISAR);
                break;
            case "aprovar":
                button.setBackground(COR_BOTAO_APROVAR);
                button.setForeground(COR_TEXTO_BOTAO_APROVAR);
                break;
            case "rejeitar":
                button.setBackground(COR_BOTAO_REJEITAR);
                button.setForeground(COR_TEXTO_BOTAO_REJEITAR);
                break;
            case "iniciar":
                button.setBackground(COR_BOTAO_INICIAR);
                button.setForeground(COR_TEXTO_BOTAO_INICIAR);
                break;
        }
        
        // Aplicar estilo padrão a todos os botões com bordas arredondadas
        button.setFont(FONTE_BOTAO);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(button.getBackground().darker(), 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 16, 8, 16),
                new RoundBorder(button.getBackground().darker(), 10, 1)
            )
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Aplica cores pastéis em um conjunto de botões principais
     * Simplifica a aplicação em múltiplos painéis
     */
    public static void aplicarCoresPastelBotoesPrincipais(JButton salvar, JButton novo, JButton editar, JButton excluir, JButton limpar) {
        aplicarCoresPastelBotao(salvar, "salvar");
        aplicarCoresPastelBotao(novo, "novo");
        aplicarCoresPastelBotao(editar, "editar");
        aplicarCoresPastelBotao(excluir, "excluir");
        aplicarCoresPastelBotao(limpar, "limpar");
    }
    
    /**
     * Aplica cores pastéis em botões de candidatos/profanos
     */
    public static void aplicarCoresPastelBotoesCandidatos(JButton salvar, JButton novo, JButton excluir, JButton aprovar, JButton rejeitar, JButton iniciar) {
        aplicarCoresPastelBotao(salvar, "salvar");
        aplicarCoresPastelBotao(novo, "novo");
        aplicarCoresPastelBotao(excluir, "excluir");
        aplicarCoresPastelBotao(aprovar, "aprovar");
        aplicarCoresPastelBotao(rejeitar, "rejeitar");
        aplicarCoresPastelBotao(iniciar, "iniciar");
    }
    
    /**
     * Aplica cores pastéis em botões de usuários
     */
    public static void aplicarCoresPastelBotoesUsuarios(JButton salvar, JButton novo, JButton excluir) {
        aplicarCoresPastelBotao(salvar, "salvar");
        aplicarCoresPastelBotao(novo, "novo");
        aplicarCoresPastelBotao(excluir, "excluir");
    }
    
    /**
     * Aplica cores pastéis em botões de configurações
     */
    public static void aplicarCoresPastelBotoesConfiguracoes(JButton salvar, JButton novo, JButton excluir, JButton restaurar) {
        aplicarCoresPastelBotao(salvar, "salvar");
        aplicarCoresPastelBotao(novo, "novo");
        aplicarCoresPastelBotao(excluir, "excluir");
        aplicarCoresPastelBotao(restaurar, "editar"); // Usa cor de editar para restaurar
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
        return new GridLayout(0, COLUNAS_FORMULARIO, ESPACAMENTO_CAMPOS, ESPACAMENTO_GRID);
    }
    
    /**
     * Cria um GridBagLayout para formulários com alinhamento correto
     * Resolve problema de formulários tortos causados pelo GridLayout
     */
    public static GridBagLayout criarLayoutFormularioAlinhado() {
        return new GridBagLayout();
    }
    
    /**
     * Cria GridBagConstraints para componentes de formulário alinhados
     */
    public static GridBagConstraints criarConstraintsFormulario(int linha, int coluna) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        if (coluna == 0) { // Labels
            gbc.gridx = 0;
            gbc.gridy = linha;
            gbc.weightx = 0.0;
            gbc.gridwidth = 1;
        } else { // Campos
            gbc.gridx = 1;
            gbc.gridy = linha;
            gbc.weightx = 1.0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
        }
        
        return gbc;
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
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_GRUPO);
        label.setForeground(COR_TEXTO_TITULO);
        return label;
    }
    
    /**
     * Cria um label estilizado para formulários de código com espaçamento específico
     */
    public static JLabel criarLabelFormularioCodigo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_GRUPO);
        label.setForeground(COR_TEXTO_TITULO);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, ESPACAMENTO_CAMPOS_CODIGO));
        return label;
    }
    
    /**
     * Aplica estilização padrão a um campo de texto
     */
    public static void estilizarCampoTexto(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
    }
    
    /**
     * Aplica estilização padrão a um campo de CEP com tamanho de 8 caracteres
     */
    public static void estilizarCampoCEP(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para CEP
    }
    
    /**
     * Aplica estilização padrão a um campo de Código com tamanho de 10 caracteres e espaçamento específico
     */
    public static void estilizarCampoCodigo(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Código
        campo.setBorder(BorderFactory.createCompoundBorder(
            BORDA_CAMPO,
            BorderFactory.createEmptyBorder(0, ESPACAMENTO_CAMPOS_CODIGO, 0, 0)
        ));
    }
    
    /**
     * Aplica estilização padrão a um campo de Nome com tamanho de 40 caracteres
     */
    public static void estilizarCampoNome(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Nome
    }
    
        
    /**
     * Aplica estilização padrão a um campo de Endereço com tamanho de 40 caracteres
     */
    public static void estilizarCampoEndereco(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Endereço
    }
    
    /**
     * Aplica estilização padrão a um campo de Cidade com tamanho de 25 caracteres
     */
    public static void estilizarCampoCidade(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Cidade
    }
    
    /**
     * Aplica estilização padrão a um campo de Estado com tamanho de 15 caracteres
     */
    public static void estilizarCampoEstado(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Estado
    }
    
    /**
     * Aplica estilização padrão a um campo de Bairro com tamanho de 20 caracteres
     */
    public static void estilizarCampoBairro(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Bairro
    }
    
    /**
     * Aplica estilização padrão a um campo de Email com tamanho de 30 caracteres
     */
    public static void estilizarCampoEmail(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Email
    }
    
    /**
     * Aplica estilização padrão a um campo de Descrição com tamanho de 40 caracteres
     */
    public static void estilizarCampoDescricao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Descrição
    }
    
    /**
     * Aplica estilização padrão a um campo de Valor com tamanho de 20 caracteres
     */
    public static void estilizarCampoValor(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Valor
    }
    
    /**
     * Aplica estilização padrão a um campo de CPF com tamanho de 14 caracteres (com máscara)
     */
    public static void estilizarCampoCPF(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para CPF
    }
    
    /**
     * Aplica estilização padrão a um campo de RG com tamanho de 12 caracteres
     */
    public static void estilizarCampoRG(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para RG
    }
    
    /**
     * Aplica estilização padrão a um campo de CNPJ com tamanho de 18 caracteres (com máscara)
     */
    public static void estilizarCampoCNPJ(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para CNPJ
    }
    
    /**
     * Aplica estilização padrão a um campo de Telefone com tamanho de 15 caracteres (com máscara)
     */
    public static void estilizarCampoTelefone(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Telefone
    }
    
    /**
     * Aplica estilização padrão a um campo de Celular com tamanho de 15 caracteres (com máscara)
     */
    public static void estilizarCampoCelular(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Celular
    }
    
    /**
     * Aplica estilização padrão a um campo de Data com tamanho de 8 caracteres (formato xx/xx/xxxx)
     */
    public static void estilizarCampoData(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(8); // Tamanho ideal para Data (formato xx/xx/xxxx)
    }
    
    /**
     * Aplica estilização padrão a um campo de Hora com tamanho de 8 caracteres (com máscara)
     */
    public static void estilizarCampoHora(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Hora
    }
    
    /**
     * Aplica estilização padrão a um campo de Placa com tamanho de 8 caracteres (com máscara)
     */
    public static void estilizarCampoPlaca(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Placa
    }
    
    /**
     * Aplica estilização padrão a um campo de Estado Civil com tamanho de 15 caracteres
     */
    public static void estilizarCampoEstadoCivil(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Estado Civil
    }
    
    /**
     * Aplica estilização padrão a um campo de Naturalidade com tamanho de 30 caracteres
     */
    public static void estilizarCampoNaturalidade(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Naturalidade
    }
    
    /**
     * Aplica estilização padrão a um campo de Tipo Sanguíneo com tamanho de 8 caracteres
     */
    public static void estilizarCampoTipoSanguineo(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Tipo Sanguíneo
    }
    
    /**
     * Aplica estilização padrão a um campo de Cargo Maçônico com tamanho de 35 caracteres
     */
    public static void estilizarCampoCargoMaconico(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Cargo Maçônico
    }
    
    /**
     * Aplica estilização padrão a um campo de Grau Maçônico com tamanho ideal de 12 caracteres
     */
    public static void estilizarCampoGrauMaconico(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Grau Maçônico
    }
    
    /**
     * Aplica estilização padrão a um campo de Registro Maçônico com tamanho de 20 caracteres
     */
    public static void estilizarCampoRegistroMaconico(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Registro Maçônico
    }
    
    /**
     * Aplica estilização padrão a um campo de Número Loja com tamanho de 10 caracteres
     */
    public static void estilizarCampoNumeroLoja(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Número Loja
    }
    
    /**
     * Aplica estilização padrão a um campo de Ritual Maçônico com tamanho de 30 caracteres
     */
    public static void estilizarCampoRitalMasonico(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Ritual Maçônico
    }
    
    /**
     * Aplica estilização padrão a um campo de Potência Maçônica com tamanho de 25 caracteres
     */
    public static void estilizarCampoPotenciaMasonica(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho específico para Potência Maçônica
    }
    
    /**
     * Aplica estilização padrão a um campo de Cargo Diretoria com tamanho de 40 caracteres
     */
    public static void estilizarCampoCargoDiretoria(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Cargo Diretoria
    }
    
    /**
     * Aplica estilização padrão a um campo de Data e Hora com tamanho de 20 caracteres
     */
    public static void estilizarCampoDataHora(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(20); // Tamanho específico para Data e Hora (DD/MM/YYYY HH:MM)
    }
    
    /**
     * Aplica estilização padrão a um campo de Local Sessão com tamanho de 50 caracteres
     */
    public static void estilizarCampoLocalSessao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(50); // Tamanho específico para Local Sessão
    }
    
    /**
     * Aplica estilização padrão a um campo de Cargo Sessão com tamanho de 40 caracteres
     */
    public static void estilizarCampoCargoSessao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(40); // Tamanho específico para Cargo Sessão
    }
    
    /**
     * Aplica estilização padrão a um campo de Número Participantes com tamanho de 8 caracteres
     */
    public static void estilizarCampoNumeroParticipantes(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(8); // Tamanho específico para Número Participantes
    }
    
    /**
     * Aplica estilização padrão a um campo de Número Documento com tamanho de 20 caracteres
     */
    public static void estilizarCampoNumeroDocumento(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Número Documento
    }
    
    /**
     * Aplica estilização padrão a um campo de Valor Financeiro com tamanho de 25 caracteres
     */
    public static void estilizarCampoValorFinanceiro(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho específico para Valor Financeiro (R$ 0.000,00)
    }
    
    /**
     * Aplica estilização padrão a um campo de ISBN com tamanho de 20 caracteres
     */
    public static void estilizarCampoISBN(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para ISBN (978-0-000-00000-0)
    }
    
    /**
     * Aplica estilização padrão a um campo de Ano Publicação com tamanho de 10 caracteres
     */
    public static void estilizarCampoAnoPublicacao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(10); // Tamanho específico para Ano Publicação
    }
    
    /**
     * Aplica estilização padrão a um campo de Categoria Biblioteca com tamanho de 30 caracteres
     */
    public static void estilizarCampoCategoriaBiblioteca(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Categoria Biblioteca
    }
    
    /**
     * Aplica estilização padrão a um campo de Localização Biblioteca com tamanho de 25 caracteres
     */
    public static void estilizarCampoLocalizacaoBiblioteca(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho específico para Localização Biblioteca
    }
    
    /**
     * Aplica estilização padrão a um campo de Nome Irmão com tamanho de 40 caracteres
     */
    public static void estilizarCampoNomeIrmao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Nome Irmão
    }
    
    /**
     * Aplica estilização padrão a um campo de Grau Maçônico com tamanho de 25 caracteres
     */
    public static void estilizarCampoGrauMasonicoFrequencia(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho específico para Grau Maçônico
    }
    
    /**
     * Aplica estilização padrão a um campo de Contagem Frequência com tamanho de 8 caracteres
     */
    public static void estilizarCampoContagemFrequencia(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Contagem Frequência
    }
    
    /**
     * Aplica estilização padrão a um campo de Nome Arquivo com tamanho de 20 caracteres
     */
    public static void estilizarCampoNomeArquivo(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Nome Arquivo
    }
    
    /**
     * Aplica estilização padrão a um campo de Caminho Arquivo com tamanho de 60 caracteres
     */
    public static void estilizarCampoCaminhoArquivo(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Caminho Arquivo
    }
    
    /**
     * Aplica estilização padrão a um campo de Usuário Upload com tamanho de 40 caracteres
     */
    public static void estilizarCampoUsuarioUpload(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Usuário Upload
    }
    
    /**
     * Aplica estilização padrão a um campo de Formato Arquivo com tamanho de 6 caracteres
     */
    public static void estilizarCampoFormatoArquivo(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(6); // Tamanho ideal para Formato Arquivo (PDF, JPG, DOC)
    }
    
    /**
     * Aplica estilização padrão a um campo de Descrição Foto com tamanho de 50 caracteres
     */
    public static void estilizarCampoDescricaoFoto(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(50); // Tamanho específico para Descrição Foto
    }
    
    /**
     * Aplica estilização padrão a um campo de Caminho Foto com tamanho de 60 caracteres
     */
    public static void estilizarCampoCaminhoFoto(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(60); // Tamanho específico para Caminho Foto
    }
    
        
    /**
     * Aplica estilização padrão a um campo de Nome Usuário com tamanho de 40 caracteres
     */
    public static void estilizarCampoNomeUsuario(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Nome Usuário
    }
    
    /**
     * Aplica estilização padrão a um campo de Senha com tamanho de 25 caracteres
     */
    public static void estilizarCampoSenha(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho específico para Senha
    }
    
    /**
     * Aplica estilização padrão a um campo de Chave Configuração com tamanho de 30 caracteres
     */
    public static void estilizarCampoChaveConfiguracao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Chave Configuração
    }
    
    /**
     * Aplica estilização padrão a um campo de Valor Configuração com tamanho de 50 caracteres
     */
    public static void estilizarCampoValorConfiguracao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Valor Configuração
    }
    
    /**
     * Aplica estilização padrão a um campo de Descrição Evento com tamanho de 50 caracteres
     */
    public static void estilizarCampoDescricaoEvento(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Descrição Evento
    }
    
    /**
     * Aplica estilização padrão a um campo de Data Evento com tamanho de 12 caracteres
     */
    public static void estilizarCampoDataEvento(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho específico para Data Evento (DD/MM/YYYY)
    }
    
    /**
     * Aplica estilização padrão a um campo de Local Evento com tamanho de 40 caracteres
     */
    public static void estilizarCampoLocalEvento(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(40); // Tamanho específico para Local Evento
    }
    
    /**
     * Aplica estilização padrão a um campo de Código Irmão com tamanho de 10 caracteres
     */
    public static void estilizarCampoCodigoIrmao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(10); // Tamanho específico para Código Irmão
    }
    
    /**
     * Aplica estilização padrão a um campo de Descrição Afastamento com tamanho de 50 caracteres
     */
    public static void estilizarCampoDescricaoAfastamento(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Descrição Afastamento
    }
    
    /**
     * Aplica estilização padrão a um campo de Documento Afastamento com tamanho de 25 caracteres
     */
    public static void estilizarCampoDocumentoAfastamento(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho específico para Documento Afastamento
    }
    
    /**
     * Aplica estilização padrão a um campo de Leitor (Biblioteca)
     */
    public static void estilizarCampoLeitor(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Leitor
    }
    
    /**
     * Aplica estilização padrão a um campo de Data Previsão (Biblioteca)
     */
    public static void estilizarCampoDataPrevisao(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Data Previsão
    }
    
    /**
     * Aplica estilização padrão a um campo de Responsável (Biblioteca)
     */
    public static void estilizarCampoResponsavel(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Responsável
    }
    
    /**
     * Aplica estilização padrão a um campo de Período Relatório
     */
    public static void estilizarCampoPeriodoRelatorio(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Período Relatório
    }
    
    /**
     * Aplica estilização padrão a um campo de Pesquisa (Geral)
     */
    public static void estilizarCampoPesquisa(JTextField campo) {
        campo.setBorder(BORDA_CAMPO);
        campo.setBackground(Color.WHITE);
        campo.setColumns(12); // Tamanho ideal para Pesquisa
    }
    
    /**
     * Aplica estilização padrão a um combo box
     */
    public static void estilizarComboBox(JComboBox<?> combo) {
        combo.setBorder(BORDA_CAMPO);
        combo.setBackground(Color.WHITE);
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
    
    /**
     * Aplica estilização padrão a um painel principal
     */
    public static void estilizarPainelPrincipal(JPanel painel) {
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BORDA_PAINEL);
        painel.setLayout(new BorderLayout());
    }
    
    /**
     * Aplica estilização padrão a um painel de conteúdo
     */
    public static void estilizarPainelConteudo(JPanel painel) {
        painel.setBackground(COR_PAINEL);
        painel.setBorder(BORDA_CONTEUDO);
    }
}
