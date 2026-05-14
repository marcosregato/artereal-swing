package com.artereal.swing.ui.panels.lojas;

import com.artereal.swing.application.loja.LojaServiceFacade;
import com.artereal.swing.application.loja.LojaResponse;
import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.ui.layout.PadraoLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Painel de tabela para gestão de Lojas - Migrado para Arquitetura Hexagonal
 * Responsável por gerenciar a exibição e interação com a tabela de dados
 */
public class LojasTablePanel extends JPanel {
    
    private static final Logger logger = LoggerFactory.getLogger(LojasTablePanel.class);
    
    private LojaServiceFacade lojaServiceFacade;
    private DefaultTableModel tableModel;
    private JTable lojasTable;
    private LojaSelectionListener selectionListener;
    
    public LojasTablePanel(LojaServiceFacade lojaServiceFacade) {
        this.lojaServiceFacade = lojaServiceFacade;
        initializeComponents();
        setupLayout();
        setupEvents();
    }
    
    /**
     * Inicializa componentes da tabela
     */
    private void initializeComponents() {
        // Criar modelo da tabela
        tableModel = new DefaultTableModel(new Object[]{
            "Código", "Nome", "CNPJ", "Cidade", "Estado"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Criar tabela
        lojasTable = new JTable(tableModel);
        PadraoLayout.configurarTabela(lojasTable);
    }
    
    /**
     * Configura layout do painel
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(PadraoLayout.COR_FUNDO);
        
        // Criar painel com título
        JPanel tablePanel = PadraoLayout.criarGrupoFormulario("🏛️ Lojas Cadastradas");
        
        // Adicionar tabela com scroll
        JScrollPane scrollPane = new JScrollPane(lojasTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
    }
    
    /**
     * Configura eventos da tabela
     */
    private void setupEvents() {
        // Evento de seleção na tabela
        lojasTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && selectionListener != null) {
                    Loja selectedLoja = getSelectedLoja();
                    selectionListener.onLojaSelected(selectedLoja);
                }
            }
        });
        
        // Evento de duplo clique para editar
        lojasTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && selectionListener != null) {
                    Loja selectedLoja = getSelectedLoja();
                    if (selectedLoja != null) {
                        selectionListener.onLojaDoubleClicked(selectedLoja);
                    }
                }
            }
        });
    }
    
    /**
     * Atualiza tabela com lista de lojas
     */
    public void updateTable(List<Loja> lojas) {
        tableModel.setRowCount(0);
        
        for (Loja loja : lojas) {
            Object[] row = {
                loja.getId(),
                loja.getNome(),
                loja.getCnpj().valor(),
                loja.getEndereco().cidade(),
                loja.getEndereco().estado()
            };
            tableModel.addRow(row);
        }
        
        logger.debug("Tabela atualizada com {} lojas", lojas.size());
    }
    
    /**
     * Carrega todas as lojas do banco e atualiza tabela
     */
    public void refreshData() {
        try {
            List<LojaResponse> lojaResponses = lojaServiceFacade.listarTodas();
            // Converter responses para entidades de domínio para compatibilidade
            List<Loja> lojas = lojaResponses.stream()
                .map(response -> lojaServiceFacade.buscarPorId(response.id()).orElse(null))
                .filter(java.util.Objects::nonNull)
                .toList();
            updateTable(lojas);
        } catch (Exception e) {
            logger.error("Erro ao atualizar dados da tabela", e);
        }
    }
    
    /**
     * Busca lojas por nome e atualiza tabela
     */
    public void searchLojas(String searchTerm) {
        try {
            List<LojaResponse> lojaResponses;
            
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                lojaResponses = lojaServiceFacade.listarTodas();
            } else {
                lojaResponses = lojaServiceFacade.buscarPorNome(searchTerm.trim());
            }
            
            // Converter responses para entidades de domínio
            List<Loja> lojas = lojaResponses.stream()
                .map(response -> lojaServiceFacade.buscarPorId(response.id()).orElse(null))
                .filter(java.util.Objects::nonNull)
                .toList();
            updateTable(lojas);
            
        } catch (Exception e) {
            logger.error("Erro ao pesquisar lojas", e);
        }
    }
    
    /**
     * Obtém loja selecionada na tabela
     */
    public Loja getSelectedLoja() {
        int selectedRow = lojasTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                return lojaServiceFacade.buscarPorId(id).orElse(null);
            } catch (Exception e) {
                logger.error("Erro ao carregar loja selecionada", e);
            }
        }
        return null;
    }
    
    /**
     * Seleciona uma loja específica na tabela
     */
    public void selectLoja(Loja loja) {
        if (loja == null || loja.getId() == null) {
            lojasTable.clearSelection();
            return;
        }
        
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Long id = (Long) tableModel.getValueAt(row, 0);
            if (id.equals(loja.getId())) {
                lojasTable.setRowSelectionInterval(row, row);
                lojasTable.scrollRectToVisible(lojasTable.getCellRect(row, 0, true));
                break;
            }
        }
    }
    
    /**
     * Limpa seleção da tabela
     */
    public void clearSelection() {
        lojasTable.clearSelection();
    }
    
    /**
     * Define listener para eventos de seleção
     */
    public void setSelectionListener(LojaSelectionListener listener) {
        this.selectionListener = listener;
    }
    
    /**
     * Interface para eventos de seleção na tabela
     */
    public interface LojaSelectionListener {
        void onLojaSelected(Loja loja);
        void onLojaDoubleClicked(Loja loja);
    }
    
    // Métodos getters para acesso aos componentes
    public JTable getLojasTable() { return lojasTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    
    /**
     * Obtém número de lojas na tabela
     */
    public int getLojasCount() {
        return tableModel.getRowCount();
    }
    
    /**
     * Verifica se há loja selecionada
     */
    public boolean hasSelection() {
        return lojasTable.getSelectedRow() >= 0;
    }
    
    /**
     * Obtém índice da linha selecionada
     */
    public int getSelectedRow() {
        return lojasTable.getSelectedRow();
    }
}
