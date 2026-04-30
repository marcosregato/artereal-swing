package com.artereal.swing;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.ui.MainFrame;
import com.artereal.swing.ui.LoginDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Aplicação principal ArteReal Swing
 * Sistema desktop para gestão de lojas maçônicas
 */
public class ArteRealSwingApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(ArteRealSwingApplication.class);
    
    public static void main(String[] args) {
        try {
            // Configurar Look and Feel nativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("Não foi possível configurar Look and Feel nativo", e);
        }
        
        // Inicializar banco de dados
        try {
            DatabaseManager.getInstance().initializeDatabase();
            logger.info("Banco de dados inicializado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao inicializar banco de dados", e);
            JOptionPane.showMessageDialog(null, 
                "Erro ao inicializar o banco de dados:\n" + e.getMessage(),
                "Erro - ArteReal", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Iniciar interface gráfica na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Exibir tela de login
                LoginDialog.LoginResult loginResult = LoginDialog.showLogin(null);
                
                if (loginResult.isAutenticado()) {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setUsuarioLogado(loginResult.getUsuario());
                    mainFrame.setVisible(true);
                    logger.info("Aplicação ArteReal iniciada com sucesso - Usuário: {}", loginResult.getUsuario().getNome());
                } else {
                    logger.info("Login cancelado pelo usuário");
                    System.exit(0);
                }
            } catch (Exception e) {
                logger.error("Erro ao iniciar a interface gráfica", e);
                JOptionPane.showMessageDialog(null,
                    "Erro ao iniciar a aplicação:\n" + e.getMessage(),
                    "Erro - ArteReal", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
