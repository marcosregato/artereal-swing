package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.UsuarioDAO;
import com.artereal.swing.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Painel de gestão de Usuários do Sistema
 */
public class UsuariosPanel extends JPanel {
    
    private UsuarioDAO usuarioDAO;
    private DefaultTableModel tableModel;
    private JTable usuariosTable;
    private JTextField nomeField;
    private JPasswordField senhaField;
    private JCheckBox adminCheckBox;
    private JCheckBox permissaoPagarCheckBox;
    private JCheckBox permissaoReceberCheckBox;
    private JCheckBox permissaoBackupCheckBox;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private Usuario usuarioAtual;
    
    public UsuariosPanel() {
        usuarioDAO = new UsuarioDAO();
        initializeComponents();
        setupLayout();
        setupEvents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Administrador", "Permissões"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usuariosTable = new JTable(tableModel);
        
        // Formulário
        nomeField = new JTextField(20);
        senhaField = new JPasswordField(20);
        adminCheckBox = new JCheckBox("Administrador");
        permissaoPagarCheckBox = new JCheckBox("Pagar");
        permissaoReceberCheckBox = new JCheckBox("Receber");
        permissaoBackupCheckBox = new JCheckBox("Backup");
        
        // Botões
        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");
        
        usuarioAtual = null;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Título
        JLabel titleLabel = new JLabel("Gestão de Usuários", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(nomeField, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(senhaField, gbc);
        
        // Permissões
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Permissões:"), gbc);
        
        JPanel permissoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        permissoesPanel.add(adminCheckBox);
        permissoesPanel.add(permissaoPagarCheckBox);
        permissoesPanel.add(permissaoReceberCheckBox);
        permissoesPanel.add(permissaoBackupCheckBox);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(permissoesPanel, gbc);
        
        // Botões do formulário
        JPanel botoesFormPanel = new JPanel(new FlowLayout());
        botoesFormPanel.add(salvarButton);
        botoesFormPanel.add(novoButton);
        botoesFormPanel.add(excluirButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(botoesFormPanel, gbc);
        
        // Painel da tabela
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Usuários Cadastrados"));
        tabelaPanel.add(new JScrollPane(usuariosTable), BorderLayout.CENTER);
        
        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        splitPane.setDividerLocation(200);
        
        add(titleLabel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        salvarButton.addActionListener(e -> salvarUsuario());
        novoButton.addActionListener(e -> limparFormulario());
        excluirButton.addActionListener(e -> excluirUsuario());
        
        // Seleção na tabela
        usuariosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarUsuarioSelecionado();
            }
        });
    }
    
    private void salvarUsuario() {
        try {
            if (nomeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome do usuário é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Usuario usuario = usuarioAtual != null ? usuarioAtual : new Usuario();
            usuario.setNome(nomeField.getText().trim());
            usuario.setSenha(new String(senhaField.getPassword()));
            usuario.setAdministrador(adminCheckBox.isSelected());
            usuario.setPermissaoPagar(permissaoPagarCheckBox.isSelected());
            usuario.setPermissaoReceber(permissaoReceberCheckBox.isSelected());
            usuario.setPermissaoBackup(permissaoBackupCheckBox.isSelected());
            
            usuarioDAO.save(usuario);
            
            JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            refreshData();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        usuarioAtual = null;
        nomeField.setText("");
        senhaField.setText("");
        adminCheckBox.setSelected(false);
        permissaoPagarCheckBox.setSelected(true);
        permissaoReceberCheckBox.setSelected(true);
        permissaoBackupCheckBox.setSelected(false);
        nomeField.requestFocus();
    }
    
    private void excluirUsuario() {
        if (usuarioAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcao = JOptionPane.showConfirmDialog(this, 
            "Deseja realmente excluir o usuário " + usuarioAtual.getNome() + "?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (opcao == JOptionPane.YES_OPTION) {
            try {
                usuarioDAO.delete(usuarioAtual.getId());
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                refreshData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void carregarUsuarioSelecionado() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                usuarioAtual = usuarioDAO.findById(id);
                if (usuarioAtual != null) {
                    nomeField.setText(usuarioAtual.getNome());
                    senhaField.setText(usuarioAtual.getSenha());
                    adminCheckBox.setSelected(usuarioAtual.isAdministrador());
                    permissaoPagarCheckBox.setSelected(usuarioAtual.isPermissaoPagar());
                    permissaoReceberCheckBox.setSelected(usuarioAtual.isPermissaoReceber());
                    permissaoBackupCheckBox.setSelected(usuarioAtual.isPermissaoBackup());
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        try {
            List<Usuario> usuarios = usuarioDAO.findAll();
            tableModel.setRowCount(0);
            
            for (Usuario usuario : usuarios) {
                Object[] row = {
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.isAdministrador() ? "Sim" : "Não",
                    getPermissoes(usuario)
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getPermissoes(Usuario usuario) {
        StringBuilder permissoes = new StringBuilder();
        if (usuario.isPermissaoPagar()) permissoes.append("Pagar ");
        if (usuario.isPermissaoReceber()) permissoes.append("Receber ");
        if (usuario.isPermissaoBackup()) permissoes.append("Backup ");
        return permissoes.toString().trim();
    }
}
