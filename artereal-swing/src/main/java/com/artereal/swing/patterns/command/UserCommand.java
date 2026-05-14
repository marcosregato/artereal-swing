package com.artereal.swing.patterns.command;

import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface Command para operações de usuário
 * Implementa o padrão Command para encapsular ações
 */
public interface UserCommand {
    
    Logger logger = LoggerFactory.getLogger(UserCommand.class);
    
    /**
     * Executa o comando
     * 
     * @param usuario Usuário alvo da operação
     * @return true se operação foi executada com sucesso
     */
    boolean execute(Usuario usuario);
    
    /**
     * Desfaz o comando (se aplicável)
     * 
     * @param usuario Usuário alvo da operação
     * @return true se operação foi desfeita com sucesso
     */
    default boolean undo(Usuario usuario) {
        logger.warn("Comando não suporta undo: {}", this.getClass().getSimpleName());
        return false;
    }
    
    /**
     * Verifica se o comando pode ser executado
     * 
     * @param usuario Usuário alvo da verificação
     * @return true se comando pode ser executado
     */
    default boolean canExecute(Usuario usuario) {
        return usuario != null;
    }
    
    /**
     * Obtém descrição do comando
     * 
     * @return Descrição da operação
     */
    String getDescription();
    
    /**
     * Obtém tipo do comando
     * 
     * @return Tipo da operação
     */
    CommandType getType();
    
    /**
     * Tipos de comandos disponíveis
     */
    enum CommandType {
        CREATE("Criar Usuário"),
        UPDATE("Atualizar Usuário"),
        DELETE("Excluir Usuário"),
        ACTIVATE("Ativar Usuário"),
        DEACTIVATE("Desativar Usuário"),
        RESET_PASSWORD("Redefinir Senha"),
        CHANGE_ROLE("Alterar Perfil");
        
        private final String description;
        
        CommandType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
