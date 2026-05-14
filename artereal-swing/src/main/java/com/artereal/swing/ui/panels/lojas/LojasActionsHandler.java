package com.artereal.swing.ui.panels.lojas;

import com.artereal.swing.application.loja.LojaServiceFacade;
import com.artereal.swing.domain.loja.Loja;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.List;

/**
 * Handler para ações CRUD e eventos da tela de Lojas - Migrado para Arquitetura Hexagonal
 * Responsável por centralizar toda a lógica de negócio e interação com o usuário
 */
public class LojasActionsHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(LojasActionsHandler.class);
    
    private LojaServiceFacade lojaServiceFacade;
    private LojasFormPanel formPanel;
    private LojasTablePanel tablePanel;
    private Loja lojaAtual;
    
    public LojasActionsHandler(LojaServiceFacade lojaServiceFacade, LojasFormPanel formPanel, LojasTablePanel tablePanel) {
        this.lojaServiceFacade = lojaServiceFacade;
        this.formPanel = formPanel;
        this.tablePanel = tablePanel;
        
        setupTableEvents();
    }
    
    /**
     * Configura eventos da tabela
     */
    private void setupTableEvents() {
        tablePanel.setSelectionListener(new LojasTablePanel.LojaSelectionListener() {
            @Override
            public void onLojaSelected(Loja loja) {
                carregarLojaParaFormulario(loja);
            }
            
            @Override
            public void onLojaDoubleClicked(Loja loja) {
                carregarLojaParaFormulario(loja);
            }
        });
    }
    
    /**
     * Salva loja no banco de dados
     */
    public void salvarLoja() {
        try {
            Loja loja = criarLojaDoFormulario();
            
            if (loja.getId() == null) {
                // Nova loja
                lojaDAO.save(loja);
                showSuccessMessage("Loja cadastrada com sucesso!");
                logger.info("Nova loja cadastrada: {}", loja.getNome());
            } else {
                // Atualização
                lojaDAO.save(loja);
                showSuccessMessage("Loja atualizada com sucesso!");
                logger.info("Loja atualizada: {}", loja.getNome());
            }
            
            limparFormulario();
            tablePanel.refreshData();
            
        } catch (Exception e) {
            logger.error("Erro ao salvar loja", e);
            showErrorMessage("Erro ao salvar loja: " + e.getMessage());
        }
    }
    
    /**
     * Exclui loja selecionada
     */
    public void excluirLoja() {
        Loja selectedLoja = tablePanel.getSelectedLoja();
        
        if (selectedLoja == null) {
            showWarningMessage("Selecione uma loja para excluir.");
            return;
        }
        
        try {
            String nome = selectedLoja.getNome();
            
            if (showConfirmation("Deseja realmente excluir a loja \"" + nome + "\"?")) {
                lojaDAO.delete(selectedLoja.getId());
                showSuccessMessage("Loja excluída com sucesso!");
                logger.info("Loja excluída: {}", nome);
                
                limparFormulario();
                tablePanel.refreshData();
            }
            
        } catch (Exception e) {
            logger.error("Erro ao excluir loja", e);
            showErrorMessage("Erro ao excluir loja: " + e.getMessage());
        }
    }
    
    /**
     * Carrega loja selecionada da tabela para o formulário
     */
    public void carregarLojaSelecionada() {
        Loja selectedLoja = tablePanel.getSelectedLoja();
        carregarLojaParaFormulario(selectedLoja);
    }
    
    /**
     * Pesquisa lojas por nome
     */
    public void pesquisarLojas(String searchTerm) {
        tablePanel.searchLojas(searchTerm);
    }
    
    /**
     * Limpa formulário
     */
    public void limparFormulario() {
        formPanel.clearForm();
        lojaAtual = null;
        tablePanel.clearSelection();
        logger.debug("Formulário limpo");
    }
    
    /**
     * Atualiza dados da tabela
     */
    public void refreshData() {
        tablePanel.refreshData();
    }
    
    /**
     * Cria objeto Loja a partir do formulário
     */
    private Loja criarLojaDoFormulario() {
        Loja loja = new Loja();
        
        if (lojaAtual != null) {
            loja.setId(lojaAtual.getId());
        }
        
        loja.setNome(formPanel.getNomeField().getText().trim());
        loja.setNumero(formPanel.getNumeroField().getText().trim());
        loja.setEndereco(formPanel.getEnderecoField().getText().trim());
        loja.setBairro(formPanel.getBairroField().getText().trim());
        loja.setCidade(formPanel.getCidadeField().getText().trim());
        loja.setEstado(formPanel.getEstadoField().getText().trim());
        loja.setCep(formPanel.getCepField().getText().trim());
        loja.setTelefone(formPanel.getTelefoneField().getText().trim());
        loja.setEmail(formPanel.getEmailField().getText().trim());
        loja.setDataFundacao(formPanel.getDataFundacaoField().getText().trim());
        loja.setPresidente(formPanel.getPresidenteField().getText().trim());
        loja.setSecretario(formPanel.getSecretarioField().getText().trim());
        loja.setTesoureiro(formPanel.getTesoureiroField().getText().trim());
        loja.setObservacoes(formPanel.getObservacoesArea().getText().trim());
        
        return loja;
    }
    
    /**
     * Carrega dados da loja para o formulário
     */
    private void carregarLojaParaFormulario(Loja loja) {
        if (loja == null) {
            limparFormulario();
            return;
        }
        
        lojaAtual = loja;
        
        formPanel.getNomeField().setText(loja.getNome() != null ? loja.getNome() : "");
        formPanel.getNumeroField().setText(loja.getNumero() != null ? loja.getNumero() : "");
        formPanel.getEnderecoField().setText(loja.getEndereco() != null ? loja.getEndereco() : "");
        formPanel.getBairroField().setText(loja.getBairro() != null ? loja.getBairro() : "");
        formPanel.getCidadeField().setText(loja.getCidade() != null ? loja.getCidade() : "");
        formPanel.getEstadoField().setText(loja.getEstado() != null ? loja.getEstado() : "");
        formPanel.getCepField().setText(loja.getCep() != null ? loja.getCep() : "");
        formPanel.getTelefoneField().setText(loja.getTelefone() != null ? loja.getTelefone() : "");
        formPanel.getEmailField().setText(loja.getEmail() != null ? loja.getEmail() : "");
        formPanel.getDataFundacaoField().setText(loja.getDataFundacao() != null ? loja.getDataFundacao() : "");
        formPanel.getPresidenteField().setText(loja.getPresidente() != null ? loja.getPresidente() : "");
        formPanel.getSecretarioField().setText(loja.getSecretario() != null ? loja.getSecretario() : "");
        formPanel.getTesoureiroField().setText(loja.getTesoureiro() != null ? loja.getTesoureiro() : "");
        formPanel.getObservacoesArea().setText(loja.getObservacoes() != null ? loja.getObservacoes() : "");
        
        logger.debug("Loja carregada no formulário: {}", loja.getNome());
    }
    
    /**
     * Valida formulário antes de salvar
     */
    public boolean validarFormulario() {
        String nome = formPanel.getNomeField().getText().trim();
        
        if (nome.isEmpty()) {
            showWarningMessage("O campo Nome é obrigatório.");
            formPanel.getNomeField().requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Verifica se há alterações não salvas
     */
    public boolean temAlteracoesNaoSalvas() {
        if (lojaAtual == null) {
            // Verificar se formulário está preenchido (nova loja não salva)
            return !formPanel.getNomeField().getText().trim().isEmpty();
        }
        
        // Comparar dados atuais com dados do formulário
        Loja lojaForm = criarLojaDoFormulario();
        
        return !lojaForm.getNome().equals(lojaAtual.getNome()) ||
               !lojaForm.getNumero().equals(lojaAtual.getNumero()) ||
               !lojaForm.getEndereco().equals(lojaAtual.getEndereco()) ||
               !lojaForm.getCidade().equals(lojaAtual.getCidade()) ||
               !lojaForm.getTelefone().equals(lojaAtual.getTelefone());
    }
    
    /**
     * Mostra mensagem de sucesso
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(formPanel, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Mostra mensagem de erro
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(formPanel, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Mostra mensagem de aviso
     */
    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(formPanel, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Mostra mensagem de confirmação
     */
    private boolean showConfirmation(String message) {
        return JOptionPane.showConfirmDialog(formPanel, message, "Confirmação", 
                                      JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    /**
     * Obtém estatísticas das lojas
     */
    public String obterEstatisticas() {
        try {
            List<Loja> lojas = lojaDAO.findAll();
            int total = lojas.size();
            
            long comTelefone = lojas.stream()
                .filter(l -> l.getTelefone() != null && !l.getTelefone().trim().isEmpty())
                .count();
            
            long comEmail = lojas.stream()
                .filter(l -> l.getEmail() != null && !l.getEmail().trim().isEmpty())
                .count();
            
            return String.format("Total: %d | Com Telefone: %d | Com E-mail: %d", 
                               total, comTelefone, comEmail);
            
        } catch (Exception e) {
            logger.error("Erro ao obter estatísticas", e);
            return "Erro ao carregar estatísticas";
        }
    }
    
    /**
     * Obtém loja atualmente carregada no formulário
     */
    public Loja getLojaAtual() {
        return lojaAtual;
    }
    
    /**
     * Verifica se formulário está em modo de edição
     */
    public boolean isModoEdicao() {
        return lojaAtual != null;
    }
    
    /**
     * Exporta dados da tabela para formato CSV (implementação futura)
     */
    public void exportarParaCSV() {
        // TODO: Implementar exportação CSV
        showWarningMessage("Funcionalidade de exportação ainda não implementada.");
    }
}
