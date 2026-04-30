package com.artereal.swing.session;

import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gerenciador de sessão do usuário usando padrão Singleton.
 * Controla informações da sessão atual e dados temporários.
 */
public class SessionManager {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static volatile SessionManager instance;
    
    // Dados da sessão
    private Usuario usuarioLogado;
    private LocalDateTime loginTime;
    private LocalDateTime lastActivity;
    private String sessionId;
    
    // Cache de dados temporários da sessão
    private final Map<String, Object> sessionData;
    
    /**
     * Construtor privado para implementar Singleton
     */
    private SessionManager() {
        this.sessionData = new HashMap<>();
        this.sessionId = generateSessionId();
        logger.debug("SessionManager Singleton criado com ID: {}", sessionId);
    }
    
    /**
     * Obtém a instância única do SessionManager (Double-Checked Locking)
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicia a sessão do usuário
     */
    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.loginTime = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
        this.sessionId = generateSessionId();
        
        logger.info("Sessão iniciada para usuário: {} ({})", 
                   usuario.getNome(), usuario.getAcesso());
    }
    
    /**
     * Encerra a sessão do usuário
     */
    public void logout() {
        if (usuarioLogado != null) {
            logger.info("Sessão encerrada para usuário: {}", usuarioLogado.getNome());
        }
        
        this.usuarioLogado = null;
        this.loginTime = null;
        this.lastActivity = null;
        this.sessionData.clear();
        this.sessionId = generateSessionId();
        
        logger.debug("Sessão limpa e resetada");
    }
    
    /**
     * Verifica se há usuário logado
     */
    public boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }
    
    /**
     * Verifica se a sessão está expirada (30 minutos de inatividade)
     */
    public boolean isSessionExpired() {
        if (!isUsuarioLogado()) {
            return true;
        }
        
        if (lastActivity == null) {
            return true;
        }
        
        return lastActivity.plusMinutes(30).isBefore(LocalDateTime.now());
    }
    
    /**
     * Atualiza o tempo da última atividade
     */
    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }
    
    /**
     * Obtém o usuário logado
     */
    public Usuario getUsuarioLogado() {
        updateLastActivity();
        return usuarioLogado;
    }
    
    /**
     * Obtém o ID da sessão
     */
    public String getSessionId() {
        return sessionId;
    }
    
    /**
     * Obtém o horário de login
     */
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    /**
     * Obtém a última atividade
     */
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }
    
    /**
     * Verifica se o usuário tem permissão específica
     */
    public boolean hasPermission(String permissao) {
        if (!isUsuarioLogado()) {
            return false;
        }
        
        return permissao.equals(usuarioLogado.getAcesso()) || 
               "ADMIN".equals(usuarioLogado.getAcesso()) ||
               usuarioLogado.isAdministrador();
    }
    
    /**
     * Verifica se o usuário é administrador
     */
    public boolean isAdmin() {
        return hasPermission("ADMIN") || usuarioLogado.isAdministrador();
    }
    
    /**
     * Armazena dados temporários na sessão
     */
    public void setSessionData(String key, Object value) {
        sessionData.put(key, value);
        logger.debug("Dado armazenado na sessão: {} = {}", key, value.getClass().getSimpleName());
    }
    
    /**
     * Obtém dados temporários da sessão
     */
    @SuppressWarnings("unchecked")
    public <T> T getSessionData(String key, Class<T> type) {
        Object value = sessionData.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Remove dados temporários da sessão
     */
    public void removeSessionData(String key) {
        sessionData.remove(key);
        logger.debug("Dado removido da sessão: {}", key);
    }
    
    /**
     * Limpa todos os dados temporários da sessão
     */
    public void clearSessionData() {
        int size = sessionData.size();
        sessionData.clear();
        logger.debug("Dados temporários da sessão limpos. {} itens removidos.", size);
    }
    
    /**
     * Obtém estatísticas da sessão
     */
    public Map<String, Object> getSessionStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("sessionId", sessionId);
        stats.put("usuarioLogado", usuarioLogado != null ? usuarioLogado.getNome() : null);
        stats.put("permissao", usuarioLogado != null ? usuarioLogado.getAcesso() : null);
        stats.put("loginTime", loginTime);
        stats.put("lastActivity", lastActivity);
        stats.put("sessionDataSize", sessionData.size());
        stats.put("isExpired", isSessionExpired());
        
        return stats;
    }
    
    /**
     * Gera um ID de sessão único
     */
    private String generateSessionId() {
        return "SESSION-" + System.currentTimeMillis() + "-" + 
               Integer.toHexString((int) (Math.random() * 0xFFFF));
    }
    
    /**
     * Valida a sessão atual
     */
    public boolean validateSession() {
        if (!isUsuarioLogado()) {
            logger.warn("Tentativa de validação de sessão sem usuário logado");
            return false;
        }
        
        if (isSessionExpired()) {
            logger.warn("Sessão expirada para usuário: {}", usuarioLogado.getNome());
            logout();
            return false;
        }
        
        updateLastActivity();
        return true;
    }
    
    /**
     * Força a renovação da sessão
     */
    public void renewSession() {
        if (isUsuarioLogado()) {
            this.lastActivity = LocalDateTime.now();
            logger.info("Sessão renovada para usuário: {}", usuarioLogado.getNome());
        }
    }
    
    /**
     * Obtém o tempo de sessão em minutos
     */
    public long getSessionDurationMinutes() {
        if (loginTime == null) {
            return 0;
        }
        
        return java.time.Duration.between(loginTime, LocalDateTime.now()).toMinutes();
    }
    
    /**
     * Obtém o tempo de inatividade em minutos
     */
    public long getInactivityMinutes() {
        if (lastActivity == null) {
            return 0;
        }
        
        return java.time.Duration.between(lastActivity, LocalDateTime.now()).toMinutes();
    }
}
