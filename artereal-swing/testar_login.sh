#!/bin/bash

echo "🔍 Testando login do Sistema ArteReal..."
echo ""

# Compilar o projeto
cd /home/marcos/Documentos/ARTEREAL/artereal-swing
mvn compile -q

# Criar um pequeno programa Java para testar o login
cat > TestLogin.java << 'EOF'
import com.artereal.swing.dao.UsuarioDAO;
import com.artereal.swing.database.DatabaseManager;

public class TestLogin {
    public static void main(String[] args) {
        try {
            System.out.println("🔧 Inicializando banco de dados...");
            DatabaseManager.getInstance().initializeDatabase();
            
            System.out.println("🔍 Testando autenticação...");
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            
            // Testar login com Administrador/admin123
            var usuario = usuarioDAO.authenticate("Administrador", "admin123");
            
            if (usuario != null) {
                System.out.println("✅ Login bem-sucedido!");
                System.out.println("   Usuário: " + usuario.getNome());
                System.out.println("   Administrador: " + (usuario.isAdministrador() ? "Sim" : "Não"));
            } else {
                System.out.println("❌ Falha no login!");
                
                // Listar todos os usuários
                var usuarios = usuarioDAO.findAll();
                System.out.println("📋 Usuários cadastrados:");
                for (var u : usuarios) {
                    System.out.println("   - " + u.getNome() + " (admin: " + u.isAdministrador() + ")");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
EOF

# Compilar e executar o teste
javac -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" TestLogin.java
java -cp ".:target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" TestLogin

# Limpar
rm -f TestLogin.java TestLogin.class

echo ""
echo "🔚 Teste concluído"
