package com.artereal.swing.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Serviço para envio de e-mails (versão simulada)
 * 
 * Esta é uma implementação simplificada que simula o envio de e-mails
 * sem depender de bibliotecas externas como JavaMail.
 * 
 * Para uso em desenvolvimento e testes. Em produção, substitua por
 * uma implementação real usando JavaMail API.
 */
public class EmailService {
    
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());
    
    // Configurações simuladas
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "noreply@artereal.com";
    private static final String SMTP_PASSWORD = "senha_simulada";
    
    // Lista para armazenar e-mails enviados (para testes)
    private static final List<EmailMessage> emailsEnviados = new ArrayList<>();
    
    /**
     * Envia e-mail de texto simples
     * 
     * @param to Destinatário
     * @param subject Assunto
     * @param body Corpo do e-mail
     * @return true se enviado com sucesso, false caso contrário
     */
    public boolean sendEmail(String to, String subject, String body) {
        return sendEmail(to, subject, body, null);
    }
    
    /**
     * Envia e-mail com anexos
     * 
     * @param to Destinatário
     * @param subject Assunto
     * @param body Corpo do e-mail
     * @param attachments Lista de arquivos para anexar (opcional)
     * @return true se enviado com sucesso, false caso contrário
     */
    public boolean sendEmail(String to, String subject, String body, List<File> attachments) {
        try {
            logger.info("EmailService - Iniciando envio de e-mail para: " + to);
            
            // Simulação de configuração SMTP
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            
            // Simulação de autenticação
            logger.info("EmailService - Autenticando com servidor SMTP...");
            simulateAuthentication();
            
            // Simulação de criação da mensagem
            EmailMessage email = new EmailMessage();
            email.setTo(to);
            email.setSubject(subject);
            email.setBody(body);
            email.setFrom(SMTP_USERNAME);
            email.setTimestamp(System.currentTimeMillis());
            
            // Simulação de anexos
            if (attachments != null && !attachments.isEmpty()) {
                List<String> attachmentNames = new ArrayList<>();
                for (File file : attachments) {
                    if (file.exists()) {
                        attachmentNames.add(file.getName());
                        logger.info("EmailService - Anexo adicionado: " + file.getName());
                    }
                }
                email.setAttachments(attachmentNames);
            }
            
            // Simulação de envio
            logger.info("EmailService - Enviando mensagem...");
            simulateSend(email);
            
            // Armazena e-mail enviado para verificação posterior
            emailsEnviados.add(email);
            
            logger.info("EmailService - E-mail enviado com sucesso para: " + to);
            return true;
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "EmailService - Erro ao enviar e-mail: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    /**
     * Envia e-mail HTML
     * 
     * @param to Destinatário
     * @param subject Assunto
     * @param htmlBody Corpo HTML do e-mail
     * @return true se enviado com sucesso, false caso contrário
     */
    public boolean sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            logger.info("EmailService - Enviando e-mail HTML para: " + to);
            
            EmailMessage email = new EmailMessage();
            email.setTo(to);
            email.setSubject(subject);
            email.setBody(htmlBody);
            email.setFrom(SMTP_USERNAME);
            email.setTimestamp(System.currentTimeMillis());
            email.setHtml(true);
            
            simulateSend(email);
            emailsEnviados.add(email);
            
            logger.info("EmailService - E-mail HTML enviado com sucesso para: " + to);
            return true;
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "EmailService - Erro ao enviar e-mail HTML: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    /**
     * Verifica se o serviço de e-mail está configurado
     * 
     * @return true se configurado, false caso contrário
     */
    public boolean isConfigured() {
        return SMTP_USERNAME != null && !SMTP_USERNAME.isEmpty() && 
               SMTP_PASSWORD != null && !SMTP_PASSWORD.isEmpty();
    }
    
    /**
     * Obtém a lista de e-mails enviados (para testes)
     * 
     * @return Lista de e-mails enviados
     */
    public static List<EmailMessage> getEmailsEnviados() {
        return new ArrayList<>(emailsEnviados);
    }
    
    /**
     * Limpa a lista de e-mails enviados (para testes)
     */
    public static void clearEmailsEnviados() {
        emailsEnviados.clear();
    }
    
    /**
     * Verifica se um e-mail foi enviado para um destinatário específico
     * 
     * @param to Destinatário
     * @return true se encontrado, false caso contrário
     */
    public boolean hasEmailSentTo(String to) {
        return emailsEnviados.stream()
                .anyMatch(email -> email.getTo().equals(to));
    }
    
    /**
     * Obtém o último e-mail enviado para um destinatário
     * 
     * @param to Destinatário
     * @return E-mail encontrado ou null
     */
    public EmailMessage getLastEmailSentTo(String to) {
        return emailsEnviados.stream()
                .filter(email -> email.getTo().equals(to))
                .reduce((first, second) -> second)
                .orElse(null);
    }
    
    // Métodos de simulação privados
    
    private void simulateAuthentication() {
        // Simula tempo de autenticação
        try {
            Thread.sleep(100);
            logger.info("EmailService - Autenticação bem-sucedida");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void simulateSend(EmailMessage email) {
        // Simula tempo de envio
        try {
            Thread.sleep(200);
            logger.info("EmailService - Simulação de envio concluída");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Classe interna para representar um e-mail enviado
     */
    public static class EmailMessage {
        private String to;
        private String from;
        private String subject;
        private String body;
        private boolean isHtml;
        private long timestamp;
        private List<String> attachments;
        
        public EmailMessage() {
            this.attachments = new ArrayList<>();
        }
        
        // Getters e Setters
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        
        public boolean isHtml() { return isHtml; }
        public void setHtml(boolean html) { isHtml = html; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public List<String> getAttachments() { return attachments; }
        public void setAttachments(List<String> attachments) { this.attachments = attachments; }
        
        @Override
        public String toString() {
            return String.format("EmailMessage{to='%s', subject='%s', timestamp=%d}", 
                               to, subject, timestamp);
        }
    }
    
    /**
     * Métodos de configuração (para uso futuro)
     */
    
    public void setSmtpHost(String host) {
        logger.info("EmailService - SMTP host configurado: " + host);
    }
    
    public void setSmtpPort(String port) {
        logger.info("EmailService - SMTP port configurado: " + port);
    }
    
    public void setCredentials(String username, String password) {
        logger.info("EmailService - Credenciais configuradas para: " + username);
    }
    
    /**
     * Testa a conexão com o servidor SMTP
     * 
     * @return true se conexão bem-sucedida, false caso contrário
     */
    public boolean testConnection() {
        try {
            logger.info("EmailService - Testando conexão com servidor SMTP...");
            simulateAuthentication();
            logger.info("EmailService - Conexão testada com sucesso");
            return true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "EmailService - Erro ao testar conexão: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    /**
     * Valida as configurações de e-mail
     * 
     * @return true se configurado corretamente, false caso contrário
     */
    public boolean validarConfiguracoes() {
        return isConfigured();
    }
    
    /**
     * Obtém o e-mail do remetente
     * 
     * @return E-mail do remetente
     */
    public String getEmailRemetente() {
        return SMTP_USERNAME;
    }
    
    /**
     * Envia e-mail com um único anexo
     * 
     * @param to Destinatário
     * @param subject Assunto
     * @param body Corpo do e-mail
     * @param attachment Arquivo para anexar
     * @return true se enviado com sucesso, false caso contrário
     */
    public boolean enviarEmailComAnexo(String to, String subject, String body, File attachment) {
        List<File> attachments = new ArrayList<>();
        if (attachment != null) {
            attachments.add(attachment);
        }
        return sendEmail(to, subject, body, attachments);
    }
    
    /**
     * Envia e-mail (método em português para compatibilidade)
     * 
     * @param to Destinatário
     * @param subject Assunto
     * @param body Corpo do e-mail
     * @return true se enviado com sucesso, false caso contrário
     */
    public boolean enviarEmail(String to, String subject, String body) {
        return sendEmail(to, subject, body);
    }
}
