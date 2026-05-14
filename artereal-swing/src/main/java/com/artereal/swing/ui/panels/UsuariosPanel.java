package com.artereal.swing.ui.panels;

import com.artereal.swing.dao.UsuarioDAO;
import com.artereal.swing.model.Usuario;
import com.artereal.swing.ui.layout.PadraoLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private JTextField pesquisarField;
    private JButton pesquisarButton;
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
        nomeField = new JTextField();
        senhaField = new JPasswordField();
        adminCheckBox = new JCheckBox("Administrador");
        permissaoPagarCheckBox = new JCheckBox("Pagar");
        permissaoReceberCheckBox = new JCheckBox("Receber");
        permissaoBackupCheckBox = new JCheckBox("Backup");
        
        // Botões usando PadraoLayout com cores pastéis
        salvarButton = PadraoLayout.criarBotaoSalvar();
        novoButton = PadraoLayout.criarBotaoNovo();
        excluirButton = PadraoLayout.criarBotaoExcluir();
        
        // Pesquisa
        pesquisarField = new JTextField(20);
        pesquisarButton = PadraoLayout.criarBotaoPesquisar();
        
        // Aplicar cores pastéis nos botões usando PadraoLayout
        PadraoLayout.aplicarCoresPastelBotoesUsuarios(salvarButton, novoButton, excluirButton);
        
        usuarioAtual = null;
    }
    
    private void setupLayout() {
        // Aplicar layout padrão usando PadraoLayout
        // Aplicar estilização padrão ao painel principal
        PadraoLayout.estilizarPainelPrincipal(this);
        
        // Header estilizado usando PadraoLayout
        JPanel headerPanel = PadraoLayout.criarHeader("👤 Gestão de Usuários", "Administração de contas e permissões");
        add(headerPanel, BorderLayout.NORTH);
        
        // Painel principal usando PadraoLayout
        JPanel mainPanel = PadraoLayout.criarPainelPrincipal();
        
        // Painel de busca no topo
        JPanel buscaPanel = PadraoLayout.criarPainelPesquisa(pesquisarField, pesquisarButton);
        mainPanel.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do conteúdo com split vertical (Formulário acima, Tabela abaixo)
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PadraoLayout.COR_FUNDO);
        
        // Painel de formulário usando PadraoLayout
        JPanel formPanel = PadraoLayout.criarGrupoFormulario("📝 Dados do Usuário");
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        
        // SEÇÃO 1: Dados do Usuario
        JPanel dadosUsuarioPanel = new JPanel(new BorderLayout());
        dadosUsuarioPanel.setBackground(Color.WHITE);
        dadosUsuarioPanel.setBorder(BorderFactory.createTitledBorder("👤 Dados do Usuario"));
        
        JPanel dadosUsuarioContent = new JPanel();
        dadosUsuarioContent.setLayout(new BoxLayout(dadosUsuarioContent, BoxLayout.Y_AXIS));
        dadosUsuarioContent.setBackground(Color.WHITE);
        
        // Primeira linha: Nome e Senha
        JPanel primeiraLinhaUsuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        primeiraLinhaUsuarioPanel.setBackground(Color.WHITE);
        
        // Campo Nome
        JPanel nomeUsuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nomeUsuarioPanel.setBackground(Color.WHITE);
        nomeUsuarioPanel.add(PadraoLayout.criarLabelFormulario("Nome:"));
        PadraoLayout.estilizarCampoNomeUsuario(nomeField);
        nomeUsuarioPanel.add(nomeField);
        
        // Campo Senha
        JPanel senhaUsuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        senhaUsuarioPanel.setBackground(Color.WHITE);
        senhaUsuarioPanel.add(PadraoLayout.criarLabelFormulario("Senha:"));
        PadraoLayout.estilizarCampoSenha(senhaField);
        senhaUsuarioPanel.add(senhaField);
        
        // Adicionar os painéis à primeira linha
        primeiraLinhaUsuarioPanel.add(nomeUsuarioPanel);
        primeiraLinhaUsuarioPanel.add(Box.createHorizontalStrut(20)); // Espaço entre os campos
        primeiraLinhaUsuarioPanel.add(senhaUsuarioPanel);
        
        // Adicionar o painel da primeira linha ao conteúdo
        dadosUsuarioContent.add(primeiraLinhaUsuarioPanel);
        
        dadosUsuarioPanel.add(dadosUsuarioContent, BorderLayout.CENTER);
        formContainer.add(dadosUsuarioPanel, BorderLayout.NORTH);
        
        // SEÇÃO 2: Permissões do Usuário
        JPanel permissoesUsuarioPanel = new JPanel(new BorderLayout());
        permissoesUsuarioPanel.setBackground(Color.WHITE);
        permissoesUsuarioPanel.setBorder(BorderFactory.createTitledBorder("🔐 Permissões do Usuário"));
        
        JPanel permissoesContent = new JPanel(new BorderLayout());
        permissoesContent.setBackground(Color.WHITE);
        
        permissoesContent.add(PadraoLayout.criarLabelFormulario("Permissões:"), BorderLayout.NORTH);
        
        JPanel permissoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        permissoesPanel.setBackground(Color.WHITE);
        permissoesPanel.add(adminCheckBox);
        permissoesPanel.add(permissaoPagarCheckBox);
        permissoesPanel.add(permissaoReceberCheckBox);
        permissoesPanel.add(permissaoBackupCheckBox);
        
        permissoesContent.add(permissoesPanel, BorderLayout.CENTER);
        permissoesUsuarioPanel.add(permissoesContent, BorderLayout.CENTER);
        formContainer.add(permissoesUsuarioPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botoesPanel.setBackground(Color.WHITE);
        botoesPanel.add(salvarButton);
        botoesPanel.add(novoButton);
        botoesPanel.add(excluirButton);
        
        formContainer.add(botoesPanel, BorderLayout.SOUTH);
        
        formPanel.add(formContainer, BorderLayout.CENTER);
        
        // Painel de tabela usando PadraoLayout
        JPanel tabelaPanel = PadraoLayout.criarGrupoFormulario("👥 Usuários Cadastrados");
        PadraoLayout.configurarTabela(usuariosTable);
        JScrollPane tableScrollPane = new JScrollPane(usuariosTable);
        
        tabelaPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Split vertical: Formulário acima (50%), Tabela abaixo (50%)
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tabelaPanel);
        verticalSplitPane.setDividerLocation(300);
        verticalSplitPane.setResizeWeight(0.5);
        
        contentPanel.add(verticalSplitPane, BorderLayout.CENTER);
        
        // Adicionar contentPanel ao mainPanel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
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
