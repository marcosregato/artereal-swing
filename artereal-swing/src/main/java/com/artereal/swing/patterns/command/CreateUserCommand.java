package com.artereal.swing.patterns.command;

import com.artereal.swing.dao.UsuarioDAO;
import com.artereal.swing.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Comando concreto para criar usuário
 * Implementa UserCommand com lógica específica de criação
 */
public class CreateUserCommand implements UserCommand {
    
    private static final Logger logger = LoggerFactory.getLogger(CreateUserCommand.class);
    
    private final UsuarioDAO usuarioDAO;
    private final Usuario novoUsuario;
    
    /**
     * Construtor
     * 
     * @param usuarioDAO DAO para persistência
     * @param novoUsuario Novo usuário a ser criado
     */
    public CreateUserCommand(UsuarioDAO usuarioDAO, Usuario novoUsuario) {
        this.usuarioDAO = usuarioDAO;
        this.novoUsuario = novoUsuario;
    }
    
    @Override
    public boolean execute(Usuario usuario) {
        if (!canExecute(usuario)) {
            return false;
        }
        
        try {
            // Validações específicas para criação
            if (novoUsuario.getNome() == null || novoUsuario.getNome().trim().isEmpty()) {
                logger.error("Nome do usuário não pode ser vazio");
                return false;
            }
            
            if (novoUsuario.getSenha() == null || novoUsuario.getSenha().length() < 6) {
                logger.error("Senha deve ter pelo menos 6 caracteres");
                return false;
            }
            
            // Define data de criação e status administrador
            novoUsuario.setDataInicio(java.time.LocalDateTime.now());
            novoUsuario.setAdministrador(false); // Por padrão, novos usuários não são admin
            
            // Salva no banco
            usuarioDAO.save(novoUsuario);
            
            logger.info("Usuário criado com sucesso: {}", novoUsuario.getNome());
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao criar usuário: " + novoUsuario.getNome(), e);
            return false;
        }
    }
    
    @Override
    public boolean undo(Usuario usuario) {
        try {
            // Para desfazer criação, verificamos se o usuário existe e o excluímos
            List<Usuario> usuarios = usuarioDAO.findByNome(novoUsuario.getNome());
            Usuario usuarioExistente = usuarios != null && !usuarios.isEmpty() ? usuarios.get(0) : null;
            if (usuarioExistente != null) {
                usuarioDAO.delete(usuarioExistente.getId());
                logger.info("Criação de usuário desfeita: {}", novoUsuario.getNome());
                return true;
            }
            return false;
            
        } catch (Exception e) {
            logger.error("Erro ao desfazer criação de usuário: " + novoUsuario.getNome(), e);
            return false;
        }
    }
    
    @Override
    public boolean canExecute(Usuario usuario) {
        // Verifica se usuário executor tem permissão para criar usuários
        if (usuario == null) {
            return false;
        }
        
        // Apenas administradores podem criar usuários
        return usuario.isAdministrador();
    }
    
    @Override
    public String getDescription() {
        return "Criar usuário: " + (novoUsuario != null ? novoUsuario.getNome() : "desconhecido");
    }
    
    @Override
    public CommandType getType() {
        return CommandType.CREATE;
    }
    
    /**
     * Obtém o usuário que será criado
     * 
     * @return Novo usuário
     */
    public Usuario getNovoUsuario() {
        return novoUsuario;
    }
}
