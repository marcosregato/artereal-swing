package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        // Aplicar layout padrão usando PadraoLayout
        PadraoLayout.aplicarLayoutPadrao(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📈 Sistema de Relatórios", "Geração e visualização de relatórios");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(new JTextField(20), new JButton("🔍 Buscar"));
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Parâmetros do Relatório");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Período do Relatório
        JPanel periodoPanel = new JPanel(new BorderLayout());
        periodoPanel.setBackground(Color.WHITE);
        periodoPanel.setBorder(BorderFactory.createTitledBorder("📅 Período do Relatório"));
        
        // Layout manual para garantir Data Início e Data Fim na mesma linha
        JPanel periodoContent = new JPanel(new GridLayout(1, 4, 10, 5)); // 1 linha, 4 colunas
        periodoContent.setBackground(Color.WHITE);
        
        // Adicionar componentes na mesma linha
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Início:"));
        JTextField dataInicioField = new JTextField(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        PadraoLayout.estilizarCampoPeriodoRelatorio(dataInicioField);
        dataInicioField.setColumns(12);
        periodoContent.add(dataInicioField);
        
        periodoContent.add(PadraoLayout.criarLabelFormulario("Data Fim:"));
        JTextField dataFimField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        PadraoLayout.estilizarCampoPeriodoRelatorio(dataFimField);
        dataFimField.setColumns(12);
        periodoContent.add(dataFimField);
        
        periodoPanel.add(periodoContent, BorderLayout.CENTER);
        formContainer.add(periodoPanel, BorderLayout.NORTH);
        
        // SEÇÃO 2: Configurações do Relatório
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setBackground(Color.WHITE);
        configPanel.setBorder(BorderFactory.createTitledBorder("⚙️ Configurações do Relatório"));
        
        JPanel configContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        configContent.setBackground(Color.WHITE);
        
        configContent.add(PadraoLayout.criarLabelFormulario("Tipo:"));
        JComboBox<String> tipoComboBox = new JComboBox<>(new String[]{
            "Todos", "Irmãos", "Financeiro", "Sessões", "Biblioteca", "Visitantes", "Eventos", "Documentos"
        });
        PadraoLayout.estilizarComboBox(tipoComboBox);
        tipoComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXX"); // Tamanho ideal
        configContent.add(tipoComboBox);
        
        configContent.add(PadraoLayout.criarLabelFormulario("Formato:"));
        JComboBox<String> formatoComboBox = new JComboBox<>(new String[]{
            "PDF", "Excel", "HTML", "CSV"
        });
        PadraoLayout.estilizarComboBox(formatoComboBox);
        configContent.add(formatoComboBox);
        
        configPanel.add(configContent, BorderLayout.CENTER);
        formContainer.add(configPanel, BorderLayout.CENTER);
        
        // SEÇÃO 3: Seleção de Relatórios
        JPanel relatoriosPanel = new JPanel(new BorderLayout());
        relatoriosPanel.setBackground(Color.WHITE);
        relatoriosPanel.setBorder(BorderFactory.createTitledBorder("📋 Relatórios Disponíveis"));
        
        JPanel relatoriosContent = new JPanel(PadraoLayout.criarLayoutFormulario());
        relatoriosContent.setBackground(Color.WHITE);
        
        relatoriosContent.add(PadraoLayout.criarLabelFormulario("Relatório:"));
        JComboBox<String> relatorioComboBox = new JComboBox<>(new String[]{
            "👥 Relatório de Irmãos - Lista completa de todos os irmãos cadastrados",
            "💰 Relatório Financeiro - Movimentações financeiras e balanço",
            "📝 Relatório de Sessões - Histórico de sessões maçônicas",
            "📚 Relatório da Biblioteca - Livros e empréstimos",
            "🚪 Relatório de Visitantes - Controle de acesso de visitantes",
            "🎉 Relatório de Eventos - Eventos e participações",
            "📄 Relatório de Documentos - Documentos digitais e assinaturas",
            "📊 Relatório Geral - Resumo completo do sistema",
            "⚙️ Relatório Customizado - Parâmetros personalizados"
        });
        PadraoLayout.estilizarComboBox(relatorioComboBox);
        relatorioComboBox.setPrototypeDisplayValue("XXXXXXXXXXXXXXXX"); // Tamanho menor e mais adequado
        relatoriosContent.add(relatorioComboBox);
        
        relatoriosPanel.add(relatoriosContent, BorderLayout.CENTER);
        
        // Painel de botões de ação
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(PadraoLayout.criarBotao("🔍 Gerar Relatório", new Color(40, 167, 69)));
        botoesPanel.add(PadraoLayout.criarBotao("� Enviar por E-mail", new Color(70, 130, 180)));
        botoesPanel.add(PadraoLayout.criarBotao("⚙️ Agendar", new Color(255, 193, 7)));
        
        formContainer.add(relatoriosPanel, BorderLayout.CENTER);
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("📊 Histórico de Relatórios Gerados");
        
        // Tabela de histórico de relatórios
        String[] colunas = {"Data", "Tipo", "Formato", "Status", "Usuário", "Ações"};
        Object[][] dados = {
            {"15/04/2024", "Irmãos", "PDF", "Concluído", "Admin", "Visualizar"},
            {"14/04/2024", "Financeiro", "Excel", "Concluído", "Admin", "Visualizar"},
            {"13/04/2024", "Sessões", "PDF", "Processando", "Admin", "Aguardar"},
            {"12/04/2024", "Biblioteca", "HTML", "Concluído", "João", "Visualizar"},
            {"11/04/2024", "Geral", "PDF", "Concluído", "Admin", "Visualizar"}
        };
        
        DefaultTableModel tableModel = new DefaultTableModel(dados, colunas);
        JTable relatoriosTable = new JTable(tableModel);
        PadraoLayout.configurarTabela(relatoriosTable);
        
        JScrollPane tableScrollPane = new JScrollPane(relatoriosTable);
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (60%), Tabela abaixo (40%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(400);
        verticalSplitPane.setResizeWeight(0.6);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    // Método removido - substituído por PadraoLayout.criarBotao() direto
    
    public void refreshData() {
        revalidate();
        repaint();
    }
}
