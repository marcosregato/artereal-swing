package com.artereal.swing.patterns.command;

import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * Invoker para comandos de usuário
 * Implementa o padrão Command para gerenciar execução e undo
 */
public class UserCommandInvoker {
    
    private static final Logger logger = LoggerFactory.getLogger(UserCommandInvoker.class);
    private final Stack<UserCommand> commandHistory;
    private final Stack<UserCommand> undoHistory;
    
    /**
     * Construtor
     */
    public UserCommandInvoker() {
        this.commandHistory = new Stack<>();
        this.undoHistory = new Stack<>();
    }
    
    /**
     * Executa um comando
     * 
     * @param command Comando a ser executado
     * @param usuario Usuário alvo
     * @return true se executado com sucesso
     */
    public boolean executeCommand(UserCommand command, Usuario usuario) {
        if (command == null || usuario == null) {
            logger.warn("Comando ou usuário nulo - execução cancelada");
            return false;
        }
        
        if (!command.canExecute(usuario)) {
            logger.warn("Comando não pode ser executado: {} para usuário: {}", 
                      command.getDescription(), usuario.getNome());
            return false;
        }
        
        try {
            boolean success = command.execute(usuario);
            
            if (success) {
                commandHistory.push(command);
                undoHistory.clear(); // Limpa histórico de undo ao executar novo comando
                logger.info("Comando executado com sucesso: {} para usuário: {}", 
                          command.getDescription(), usuario.getNome());
            } else {
                logger.error("Falha ao executar comando: {} para usuário: {}", 
                           command.getDescription(), usuario.getNome());
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("Erro ao executar comando: " + command.getDescription(), e);
            return false;
        }
    }
    
    /**
     * Desfaz o último comando executado
     * 
     * @param usuario Usuário alvo
     * @return true se desfeito com sucesso
     */
    public boolean undoLastCommand(Usuario usuario) {
        if (commandHistory.isEmpty()) {
            logger.warn("Não há comandos para desfazer");
            return false;
        }
        
        UserCommand lastCommand = commandHistory.peek();
        
        try {
            boolean success = lastCommand.undo(usuario);
            
            if (success) {
                undoHistory.push(commandHistory.pop());
                logger.info("Comando desfeito com sucesso: {} para usuário: {}", 
                          lastCommand.getDescription(), usuario.getNome());
            } else {
                logger.error("Falha ao desfazer comando: {} para usuário: {}", 
                           lastCommand.getDescription(), usuario.getNome());
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("Erro ao desfazer comando: " + lastCommand.getDescription(), e);
            return false;
        }
    }
    
    /**
     * Refaz o último comando desfeito
     * 
     * @param usuario Usuário alvo
     * @return true if refeito com sucesso
     */
    public boolean redoLastCommand(Usuario usuario) {
        if (undoHistory.isEmpty()) {
            logger.warn("Não há comandos para refazer");
            return false;
        }
        
        UserCommand commandToRedo = undoHistory.peek();
        
        try {
            boolean success = commandToRedo.execute(usuario);
            
            if (success) {
                commandHistory.push(undoHistory.pop());
                logger.info("Comando refeito com sucesso: {} para usuário: {}", 
                          commandToRedo.getDescription(), usuario.getNome());
            } else {
                logger.error("Falha ao refazer comando: {} para usuário: {}", 
                           commandToRedo.getDescription(), usuario.getNome());
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("Erro ao refazer comando: " + commandToRedo.getDescription(), e);
            return false;
        }
    }
    
    /**
     * Limpa histórico de comandos
     */
    public void clearHistory() {
        commandHistory.clear();
        undoHistory.clear();
        logger.info("Histórico de comandos limpo");
    }
    
    /**
     * Retorna quantidade de comandos executados
     * 
     * @return Número de comandos no histórico
     */
    public int getCommandCount() {
        return commandHistory.size();
    }
    
    /**
     * Retorna quantidade de comandos disponíveis para undo
     * 
     * @return Número de comandos que podem ser desfeitos
     */
    public int getUndoCount() {
        return commandHistory.size();
    }
    
    /**
     * Retorna quantidade de comandos disponíveis para redo
     * 
     * @return Número de comandos que podem ser refeitos
     */
    public int getRedoCount() {
        return undoHistory.size();
    }
    
    /**
     * Obtém descrição do último comando executado
     * 
     * @return Descrição do último comando ou null se não houver
     */
    public String getLastCommandDescription() {
        if (commandHistory.isEmpty()) {
            return null;
        }
        return commandHistory.peek().getDescription();
    }
}
