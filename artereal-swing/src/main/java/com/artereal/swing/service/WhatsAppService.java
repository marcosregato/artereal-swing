package com.artereal.swing.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Serviço para envio de mensagens via WhatsApp Web
 */
public class WhatsAppService {
    
    private static final Logger logger = Logger.getLogger(WhatsAppService.class.getName());
    
    /**
     * Envia uma mensagem via WhatsApp Web
     * 
     * @param numeroTelefone Número de telefone com DDD (ex: 5511999998888)
     * @param mensagem Mensagem a ser enviada
     * @return true se abriu o WhatsApp Web com sucesso, false caso contrário
     */
    public static boolean enviarMensagemWhatsApp(String numeroTelefone, String mensagem) {
        logger.info("WhatsAppService - Iniciando envio via WhatsApp Web para: " + numeroTelefone);
        
        try {
            // Limpar e formatar número de telefone
            String numeroLimpo = limparNumeroTelefone(numeroTelefone);
            
            if (numeroLimpo.isEmpty() || numeroLimpo.length() < 10) {
                logger.warning("WhatsAppService - Número de telefone inválido: " + numeroTelefone);
                return false;
            }
            
            // Codificar mensagem para URL
            String mensagemCodificada = URLEncoder.encode(mensagem, StandardCharsets.UTF_8);
            
            // Criar URL do WhatsApp Web
            String whatsappUrl = "https://web.whatsapp.com/send?phone=" + numeroLimpo + "&text=" + mensagemCodificada;
            
            logger.info("WhatsAppService - Abrindo WhatsApp Web...");
            
            // Abrir WhatsApp Web no navegador padrão
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(whatsappUrl));
                logger.info("WhatsAppService - WhatsApp Web aberto com sucesso");
                return true;
            } else {
                logger.warning("WhatsAppService - Desktop não suporta abertura de navegador");
                return false;
            }
            
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "WhatsAppService - Erro de IO ao abrir WhatsApp Web: " + ex.getMessage(), ex);
            return false;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "WhatsAppService - Erro ao enviar mensagem WhatsApp: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    /**
     * Envia um documento via WhatsApp Web
     * 
     * @param numeroTelefone Número de telefone com DDD
     * @param nomeDocumento Nome do documento
     * @param tipoDocumento Tipo do documento
     * @param descricao Descrição do documento
     * @return true se abriu o WhatsApp Web com sucesso, false caso contrário
     */
    public static boolean enviarDocumentoWhatsApp(String numeroTelefone, String nomeDocumento, String tipoDocumento, String descricao) {
        logger.info("WhatsAppService - Enviando documento via WhatsApp: " + nomeDocumento);
        
        try {
            // Construir mensagem sobre o documento
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("📄 *Documento ArteReal*\n\n");
            mensagem.append("*Nome:* ").append(nomeDocumento).append("\n");
            mensagem.append("*Tipo:* ").append(tipoDocumento).append("\n");
            
            if (descricao != null && !descricao.trim().isEmpty()) {
                mensagem.append("*Descrição:* ").append(descricao).append("\n");
            }
            
            mensagem.append("\n📎 *Anexo:* O documento será anexado manualmente\n");
            mensagem.append("\n_Enviado pelo Sistema ArteReal v2.0.0_");
            
            return enviarMensagemWhatsApp(numeroTelefone, mensagem.toString());
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "WhatsAppService - Erro ao enviar documento WhatsApp: " + ex.getMessage(), ex);
            return false;
        }
    }
    
    /**
     * Formata e valida um número de telefone
     * 
     * @param numero Número de telefone
     * @return Número formatado ou vazio se inválido
     */
    public static String formatarNumeroTelefone(String numero) {
        if (numero == null) {
            return "";
        }
        
        String numeroLimpo = limparNumeroTelefone(numero);
        
        if (numeroLimpo.length() < 10 || numeroLimpo.length() > 13) {
            return "";
        }
        
        // Adicionar código do Brasil se não tiver
        if (!numeroLimpo.startsWith("55") && numeroLimpo.length() == 10 || numeroLimpo.length() == 11) {
            numeroLimpo = "55" + numeroLimpo;
        }
        
        return numeroLimpo;
    }
    
    /**
     * Limpa um número de telefone removendo caracteres não numéricos
     * 
     * @param numero Número de telefone
     * @return Número limpo
     */
    private static String limparNumeroTelefone(String numero) {
        if (numero == null) {
            return "";
        }
        
        return numero.replaceAll("[^0-9]", "");
    }
    
    /**
     * Valida se um número de telefone é válido para WhatsApp
     * 
     * @param numero Número de telefone
     * @return true se válido, false caso contrário
     */
    public static boolean validarNumeroTelefone(String numero) {
        String numeroFormatado = formatarNumeroTelefone(numero);
        return !numeroFormatado.isEmpty() && 
               numeroFormatado.length() >= 12 && 
               numeroFormatado.length() <= 13;
    }
    
    /**
     * Gera uma mensagem de erro para WhatsApp
     * 
     * @param erro Mensagem de erro
     * @return Mensagem formatada para WhatsApp
     */
    public static String gerarMensagemErro(String erro) {
        return "❌ *Erro ao enviar documento*\n\n" +
               "*Descrição:* " + erro + "\n\n" +
               "Por favor, tente novamente ou contate o suporte.\n\n" +
               "_Sistema ArteReal v2.0.0_";
    }
    
    /**
     * Gera uma mensagem de sucesso para WhatsApp
     * 
     * @param nomeDocumento Nome do documento
     * @return Mensagem formatada para WhatsApp
     */
    public static String gerarMensagemSucesso(String nomeDocumento) {
        return "✅ *Documento enviado com sucesso!*\n\n" +
               "*Documento:* " + nomeDocumento + "\n\n" +
               "O documento foi processado e está disponível.\n\n" +
               "_Sistema ArteReal v2.0.0_";
    }
}
