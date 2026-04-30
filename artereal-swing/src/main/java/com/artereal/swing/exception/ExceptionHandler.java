package com.artereal.swing.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.Component;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

/**
 * Classe centralizada para tratamento de exceções e logging.
 * Fornece métodos padronizados para tratar diferentes tipos de erros.
 */
public class ExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    
    /**
     * Trata exceção genérica
     */
    public static void handle(Exception e, Component parent) {
        handle(e, parent, "Ocorreu um erro inesperado");
    }
    
    /**
     * Trata exceção com mensagem personalizada
     */
    public static void handle(Exception e, Component parent, String context) {
        logger.error("{}: {}", context, e.getMessage(), e);
        
        String userMessage = getUserFriendlyMessage(e);
        String fullMessage = String.format("%s\n\nDetalhes: %s", context, userMessage);
        
        showErrorDialog(parent, fullMessage, "Erro");
    }
    
    /**
     * Trata exceção de banco de dados
     */
    public static void handleDatabase(Exception e, Component parent) {
        logger.error("Erro de banco de dados: {}", e.getMessage(), e);
        
        String message = "Erro ao acessar o banco de dados.\n\n" +
                       "Verifique se o banco está disponível e tente novamente.\n\n" +
                       "Detalhes: " + e.getMessage();
        
        showErrorDialog(parent, message, "Erro de Banco de Dados");
    }
    
    /**
     * Trata exceção de validação
     */
    public static void handleValidation(Exception e, Component parent) {
        logger.warn("Erro de validação: {}", e.getMessage());
        
        String message = "Erro de validação:\n\n" + e.getMessage();
        
        showWarningDialog(parent, message, "Erro de Validação");
    }
    
    /**
     * Trata exceção de negócio
     */
    public static void handleBusiness(Exception e, Component parent) {
        logger.warn("Erro de negócio: {}", e.getMessage());
        
        String message = "Não foi possível completar a operação:\n\n" + e.getMessage();
        
        showWarningDialog(parent, message, "Operação Inválida");
    }
    
    /**
     * Trata exceção de timeout
     */
    public static void handleTimeout(TimeoutException e, Component parent) {
        logger.warn("Timeout na operação: {}", e.getMessage());
        
        String message = "A operação demorou mais tempo que o esperado.\n\n" +
                       "Tente novamente ou verifique sua conexão.";
        
        showWarningDialog(parent, message, "Timeout");
    }
    
    /**
     * Trata exceção de IO
     */
    public static void handleIO(Exception e, Component parent) {
        logger.error("Erro de I/O: {}", e.getMessage(), e);
        
        String message = "Erro ao acessar arquivo ou recurso:\n\n" + e.getMessage();
        
        showErrorDialog(parent, message, "Erro de Acesso");
    }
    
    /**
     * Trata exceção de segurança
     */
    public static void handleSecurity(Exception e, Component parent) {
        logger.error("Erro de segurança: {}", e.getMessage(), e);
        
        String message = "Erro de segurança ou permissão negada:\n\n" + e.getMessage();
        
        showErrorDialog(parent, message, "Erro de Segurança");
    }
    
    /**
     * Trata exceção de parse/conversão
     */
    public static void handleParse(Exception e, Component parent) {
        logger.warn("Erro de conversão: {}", e.getMessage());
        
        String message = "Erro ao converter dados:\n\n" + 
                       "Verifique o formato dos valores informados.\n\n" +
                       "Detalhes: " + e.getMessage();
        
        showWarningDialog(parent, message, "Erro de Formato");
    }
    
    /**
     * Trata exceção de concorrência
     */
    public static void handleConcurrency(Exception e, Component parent) {
        logger.warn("Erro de concorrência: {}", e.getMessage());
        
        String message = "O registro foi modificado por outro usuário.\n\n" +
                       "Recarregue os dados e tente novamente.";
        
        showWarningDialog(parent, message, "Conflito de Dados");
    }
    
    /**
     * Trata exceção de recurso não encontrado
     */
    public static void handleNotFound(Exception e, Component parent) {
        logger.warn("Recurso não encontrado: {}", e.getMessage());
        
        String message = "Registro não encontrado:\n\n" + e.getMessage();
        
        showWarningDialog(parent, message, "Não Encontrado");
    }
    
    /**
     * Trata exceção de permissão negada
     */
    public static void handlePermission(Exception e, Component parent) {
        logger.warn("Permissão negada: {}", e.getMessage());
        
        String message = "Você não tem permissão para realizar esta operação:\n\n" + e.getMessage();
        
        showWarningDialog(parent, message, "Acesso Negado");
    }
    
    /**
     * Trata exceção de conexão
     */
    public static void handleConnection(Exception e, Component parent) {
        logger.error("Erro de conexão: {}", e.getMessage(), e);
        
        String message = "Erro de conexão:\n\n" +
                       "Verifique sua conexão com a rede e tente novamente.\n\n" +
                       "Detalhes: " + e.getMessage();
        
        showErrorDialog(parent, message, "Erro de Conexão");
    }
    
    /**
     * Trata exceção genérica com opções
     */
    public static int handleWithOptions(Exception e, Component parent, String context, String[] options) {
        logger.error("{}: {}", context, e.getMessage(), e);
        
        String userMessage = getUserFriendlyMessage(e);
        String fullMessage = String.format("%s\n\nDetalhes: %s", context, userMessage);
        
        return showOptionDialog(parent, fullMessage, "Erro", options);
    }
    
    /**
     * Trata exceção e decide se deve continuar
     */
    public static boolean handleAndAskToContinue(Exception e, Component parent, String context) {
        logger.error("{}: {}", context, e.getMessage(), e);
        
        String userMessage = getUserFriendlyMessage(e);
        String fullMessage = String.format("%s\n\nDeseja continuar mesmo assim?\n\nDetalhes: %s", 
                                          context, userMessage);
        
        int result = showOptionDialog(parent, fullMessage, "Erro", 
                                     new String[]{"Continuar", "Cancelar"});
        
        return result == 0;
    }
    
    /**
     * Log de informação
     */
    public static void logInfo(String message) {
        logger.info(message);
    }
    
    /**
     * Log de aviso
     */
    public static void logWarning(String message) {
        logger.warn(message);
    }
    
    /**
     * Log de erro
     */
    public static void logError(String message) {
        logger.error(message);
    }
    
    /**
     * Log de erro com exceção
     */
    public static void logError(String message, Exception e) {
        logger.error(message, e);
    }
    
    /**
     * Log de debug
     */
    public static void logDebug(String message) {
        logger.debug(message);
    }
    
    /**
     * Obtém mensagem amigável para o usuário baseada no tipo de exceção
     */
    private static String getUserFriendlyMessage(Exception e) {
        if (e instanceof SQLException) {
            return "Erro ao acessar o banco de dados";
        } else if (e instanceof NumberFormatException) {
            return "Valor numérico inválido";
        } else if (e instanceof IllegalArgumentException) {
            return e.getMessage();
        } else if (e instanceof TimeoutException) {
            return "Operação excedeu o tempo limite";
        } else if (e instanceof SecurityException) {
            return "Acesso negado";
        } else if (e instanceof NullPointerException) {
            return "Erro interno do sistema";
        } else if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            return e.getMessage();
        } else {
            return "Ocorreu um erro inesperado";
        }
    }
    
    /**
     * Exibe diálogo de erro
     */
    private static void showErrorDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Exibe diálogo de aviso
     */
    private static void showWarningDialog(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Exibe diálogo de opções
     */
    private static int showOptionDialog(Component parent, String message, String title, String[] options) {
        return JOptionPane.showOptionDialog(
            parent,
            message,
            title,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[0]
        );
    }
    
    /**
     * Trata exceção e retorna mensagem para log
     */
    public static String handleForLog(Exception e, String context) {
        String message = String.format("%s: %s", context, e.getMessage());
        
        if (e instanceof SQLException) {
            logger.error("Erro de banco de dados: {}", message, e);
        } else if (e instanceof IllegalArgumentException || e instanceof NumberFormatException) {
            logger.warn("Erro de validação: {}", message);
        } else {
            logger.error("Erro genérico: {}", message, e);
        }
        
        return message;
    }
    
    /**
     * Verifica se exceção é crítica
     */
    public static boolean isCritical(Throwable e) {
        return e instanceof OutOfMemoryError || 
               e instanceof StackOverflowError ||
               e instanceof NoClassDefFoundError ||
               e instanceof NoSuchMethodError ||
               e instanceof NoSuchFieldError;
    }
    
    /**
     * Trata exceção crítica
     */
    public static void handleCritical(Exception e, Component parent) {
        logger.error("ERRO CRÍTICO: {}", e.getMessage(), e);
        
        String message = "Ocorreu um erro crítico no sistema.\n\n" +
                       "Recomenda-se reiniciar a aplicação.\n\n" +
                       "Detalhes: " + e.getMessage();
        
        JOptionPane.showMessageDialog(parent, message, "Erro Crítico", JOptionPane.ERROR_MESSAGE);
        
        // Em caso de erro crítico, sugere reiniciar
        int option = JOptionPane.showConfirmDialog(
            parent,
            "Deseja reiniciar a aplicação?",
            "Reiniciar",
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            System.exit(1);
        }
    }
}
