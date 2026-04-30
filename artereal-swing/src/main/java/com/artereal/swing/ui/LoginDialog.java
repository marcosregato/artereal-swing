package com.artereal.swing.ui;

import com.artereal.swing.dao.UsuarioDAO;
import com.artereal.swing.model.Usuario;
import com.artereal.swing.ui.components.LogoMaconaria;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

/**
 * Diálogo de Login do Sistema
 */
public class LoginDialog extends JDialog {
    
    private UsuarioDAO usuarioDAO;
    private JTextField usuarioField;
    private JPasswordField senhaField;
    private JButton entrarButton;
    private JButton cancelarButton;
    private Usuario usuarioLogado;
    private boolean autenticado = false;
    
    public LoginDialog(Frame owner) {
        super(owner, "Login - ArteReal", true);
        usuarioDAO = new UsuarioDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setResizable(false);
    }
    
    private void initializeComponents() {
        usuarioField = new JTextField(20);
        senhaField = new JPasswordField(20);
        entrarButton = PadraoLayout.criarBotao("Entrar", new Color(144, 238, 144)); // Verde pastel
        cancelarButton = PadraoLayout.criarBotao("Cancelar", new Color(255, 182, 193)); // Rosa pastel
        
        // Configurar botão Enter como default
        getRootPane().setDefaultButton(entrarButton);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Painel do título com logo
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        // Adicionar logo maçônico usando imagem estática
        BufferedImage logoImage = LogoMaconaria.createLogoImage(48, null);
        ImageIcon logoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        // Textos do título
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Sistema ArteReal", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        
        JLabel subtitleLabel = new JLabel("Gestão Maçônica", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 120));
        
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);
        
        titlePanel.add(logoLabel, BorderLayout.WEST);
        titlePanel.add(textPanel, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(titlePanel, gbc);
        
        // Espaçamento
        gbc.gridy = 2; gbc.insets = new Insets(15, 5, 15, 5);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        
        // Usuário
        gbc.gridy = 3; gbc.gridwidth = 1; gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        mainPanel.add(usuarioField, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        mainPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        mainPanel.add(senhaField, gbc);
        
        // Painel de botões estilizados com cores pastéis
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botoesPanel.setBackground(Color.WHITE);
        
        // Estilizar botões com cores pastéis
        entrarButton.setBackground(new Color(144, 238, 144)); // Verde pastel suave
        entrarButton.setForeground(new Color(34, 89, 34));
        entrarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        entrarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(144, 238, 144), 2),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        entrarButton.setFocusPainted(false);
        entrarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cancelarButton.setBackground(new Color(255, 182, 193)); // Rosa pastel suave
        cancelarButton.setForeground(new Color(180, 82, 92));
        cancelarButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelarButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 182, 193), 2),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        cancelarButton.setFocusPainted(false);
        cancelarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        botoesPanel.add(entrarButton);
        botoesPanel.add(cancelarButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(15, 5, 5, 5);
        mainPanel.add(botoesPanel, gbc);
        
        // Informações
        JLabel infoLabel = new JLabel("<html><small>Usuário padrão: admin<br>Senha padrão: admin123</small></html>", SwingConstants.CENTER);
        infoLabel.setForeground(Color.GRAY);
        gbc.gridy = 6; gbc.insets = new Insets(10, 5, 5, 5);
        mainPanel.add(infoLabel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        entrarButton.addActionListener(e -> realizarLogin());
        cancelarButton.addActionListener(e -> cancelar());
        
        // Allow Enter key in password field to trigger login
        senhaField.addActionListener(e -> realizarLogin());
    }
    
    private void realizarLogin() {
        String usuario = usuarioField.getText().trim();
        String senha = new String(senhaField.getPassword());
        
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o usuário!", "Aviso", JOptionPane.WARNING_MESSAGE);
            usuarioField.requestFocus();
            return;
        }
        
        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe a senha!", "Aviso", JOptionPane.WARNING_MESSAGE);
            senhaField.requestFocus();
            return;
        }
        
        try {
            // Debug: mostrar valores sendo testados
            System.out.println("Tentando login com: usuario='" + usuario + "', senha='" + senha + "'");
            
            usuarioLogado = usuarioDAO.authenticate(usuario.trim(), senha.trim());
            
            if (usuarioLogado != null) {
                autenticado = true;
                JOptionPane.showMessageDialog(this, 
                    "Bem-vindo, " + usuarioLogado.getNome() + "!", 
                    "Login Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Usuário ou senha inválidos!\nUsuário: '" + usuario + "'\nSenha: '" + senha + "'", 
                    "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                senhaField.setText("");
                senhaField.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao autenticar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelar() {
        autenticado = false;
        dispose();
    }
    
    public boolean isAutenticado() {
        return autenticado;
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    /**
     * Exibe o diálogo de login e retorna o resultado
     */
    public static LoginResult showLogin(Frame owner) {
        LoginDialog dialog = new LoginDialog(owner);
        dialog.setVisible(true);
        
        return new LoginResult(dialog.isAutenticado(), dialog.getUsuarioLogado());
    }
    
    /**
     * Classe para encapsular o resultado do login
     */
    public static class LoginResult {
        private final boolean autenticado;
        private final Usuario usuario;
        
        public LoginResult(boolean autenticado, Usuario usuario) {
            this.autenticado = autenticado;
            this.usuario = usuario;
        }
        
        public boolean isAutenticado() {
            return autenticado;
        }
        
        public Usuario getUsuario() {
            return usuario;
        }
    }
}
