package com.artereal.swing.ui.panels;

import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
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
        // Aplicar layout padrão usando PadraoLayout - CORREÇÃO CRÍTICA
        this.setBorder(PadraoLayout.BORDA_PAINEL);
        this.setBackground(PadraoLayout.COR_FUNDO);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("📈 Sistema de Relatórios", "Geração e visualização de relatórios");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PadraoLayout.COR_PAINEL);
        mainPanel.setBorder(PadraoLayout.BORDA_CONTEUDO);
        
        // Painel de filtros usando PadraoLayout - CORREÇÃO CRÍTICA
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtroPanel.setBackground(PadraoLayout.COR_PAINEL);
        filtroPanel.setBorder(PadraoLayout.BORDA_GRUPO);
        
        JLabel dataInicioLabel = PadraoLayout.criarLabelFormulario("📅 Data Início:");
        JTextField dataInicioField = new JTextField(LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        PadraoLayout.estilizarCampoTexto(dataInicioField);
        
        JLabel dataFimLabel = PadraoLayout.criarLabelFormulario("📅 Data Fim:");
        JTextField dataFimField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        PadraoLayout.estilizarCampoTexto(dataFimField);
        
        JButton filtrarButton = PadraoLayout.criarBotao("🔍 Filtrar", PadraoLayout.COR_BOTAO_PESQUISAR);
        
        filtroPanel.add(dataInicioLabel);
        filtroPanel.add(dataInicioField);
        filtroPanel.add(dataFimLabel);
        filtroPanel.add(dataFimField);
        filtroPanel.add(filtrarButton);
        
        // Painel de relatórios
        JPanel relatoriosPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        relatoriosPanel.setBackground(new Color(245, 245, 250));
        relatoriosPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Botões de relatórios usando PadraoLayout - CORREÇÃO FINAL
        JButton irmaosButton = PadraoLayout.criarBotao("👥 Irmãos", PadraoLayout.COR_PRIMARIA);
        JButton financeiroButton = PadraoLayout.criarBotao("💰 Financeiro", PadraoLayout.COR_PRIMARIA);
        JButton sessoesButton = PadraoLayout.criarBotao("📝 Sessões", PadraoLayout.COR_PRIMARIA);
        JButton bibliotecaButton = PadraoLayout.criarBotao("📚 Biblioteca", PadraoLayout.COR_PRIMARIA);
        JButton visitantesButton = PadraoLayout.criarBotao("🚪 Visitantes", PadraoLayout.COR_PRIMARIA);
        JButton eventosButton = PadraoLayout.criarBotao("🎉 Eventos", PadraoLayout.COR_PRIMARIA);
        JButton documentosButton = PadraoLayout.criarBotao("📄 Documentos", PadraoLayout.COR_PRIMARIA);
        JButton geralButton = PadraoLayout.criarBotao("📊 Geral", PadraoLayout.COR_PRIMARIA);
        JButton customButton = PadraoLayout.criarBotao("⚙️ Customizado", PadraoLayout.COR_PRIMARIA);
        
        relatoriosPanel.add(irmaosButton);
        relatoriosPanel.add(financeiroButton);
        relatoriosPanel.add(sessoesButton);
        relatoriosPanel.add(bibliotecaButton);
        relatoriosPanel.add(visitantesButton);
        relatoriosPanel.add(eventosButton);
        relatoriosPanel.add(documentosButton);
        relatoriosPanel.add(geralButton);
        relatoriosPanel.add(customButton);
        
        // Painel de informações usando PadraoLayout
        JPanel infoPanel = PadraoLayout.criarGrupoFormulario("📋 Informações do Relatório");
        
        JTextArea infoArea = new JTextArea("Selecione um relatório para visualizar informações detalhadas...\n\n" +
            "• Relatórios disponíveis em PDF, Excel e HTML\n" +
            "• Filtros por período e categorias\n" +
            "• Exportação automática para e-mail\n" +
            "• Agendamento de relatórios periódicos\n" +
            "• Gráficos e análises estatísticas");
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel infoContent = PadraoLayout.criarPainelTextArea("Informações:", infoArea);
        infoPanel.add(infoContent);
        
        mainPanel.add(filtroPanel, BorderLayout.NORTH);
        mainPanel.add(relatoriosPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    // Método removido - substituído por PadraoLayout.criarBotao() direto
    
    public void refreshData() {
        revalidate();
        repaint();
    }
}
