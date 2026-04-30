import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class test_dao {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        try {
            // Testar SessaoDAO
            System.out.println("=== Testando SessaoDAO ===");
            testSessaoDAO();
            
            // Testar CaixaDAO  
            System.out.println("\n=== Testando CaixaDAO ===");
            testCaixaDAO();
            
            // Testar BibliotecaDAO
            System.out.println("\n=== Testando BibliotecaDAO ===");
            testBibliotecaDAO();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void testSessaoDAO() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:/home/marcos/.artereal/artereal.db");
        String sql = "SELECT * FROM sessao ORDER BY data DESC LIMIT 3";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("Sessão " + count + ":");
                System.out.println("  ID: " + rs.getLong("id"));
                System.out.println("  Tipo: " + rs.getString("tipo"));
                
                String dataStr = rs.getString("data");
                System.out.println("  Data (bruta): " + dataStr);
                
                if (dataStr != null && !dataStr.isEmpty()) {
                    try {
                        LocalDateTime dataHora;
                        if (dataStr.contains(" ")) {
                            dataHora = LocalDateTime.parse(dataStr, formatter);
                        } else {
                            dataHora = LocalDateTime.parse(dataStr + " 00:00:00", formatter);
                        }
                        System.out.println("  Data (parseada): " + dataHora);
                    } catch (Exception e) {
                        System.out.println("  ERRO ao converter data: " + e.getMessage());
                    }
                }
                
                System.out.println("  Descrição: " + rs.getString("descricao"));
                System.out.println("  Presença: " + rs.getInt("presenca"));
                System.out.println("  Realizada: " + rs.getBoolean("realizada"));
                System.out.println();
            }
        }
        conn.close();
    }
    
    private static void testCaixaDAO() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:/home/marcos/.artereal/artereal.db");
        String sql = "SELECT * FROM caixa ORDER BY data DESC LIMIT 3";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("Caixa " + count + ":");
                System.out.println("  ID: " + rs.getLong("id"));
                
                String dataStr = rs.getString("data");
                System.out.println("  Data (bruta): " + dataStr);
                
                if (dataStr != null && !dataStr.isEmpty()) {
                    try {
                        LocalDateTime dataMov;
                        if (dataStr.contains(" ")) {
                            dataMov = LocalDateTime.parse(dataStr, formatter);
                        } else {
                            dataMov = LocalDateTime.parse(dataStr + " 00:00:00", formatter);
                        }
                        System.out.println("  Data (parseada): " + dataMov);
                    } catch (Exception e) {
                        System.out.println("  ERRO ao converter data: " + e.getMessage());
                    }
                }
                
                System.out.println("  Descrição: " + rs.getString("descricao"));
                System.out.println("  Entrada: " + rs.getBigDecimal("entrada"));
                System.out.println("  Saída: " + rs.getBigDecimal("saida"));
                System.out.println();
            }
        }
        conn.close();
    }
    
    private static void testBibliotecaDAO() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:/home/marcos/.artereal/artereal.db");
        String sql = "SELECT * FROM biblioteca ORDER BY titulo LIMIT 3";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("Biblioteca " + count + ":");
                System.out.println("  ID: " + rs.getLong("id"));
                System.out.println("  Título: " + rs.getString("titulo"));
                System.out.println("  Autor: " + rs.getString("autor"));
                System.out.println("  Assunto: " + rs.getString("assunto"));
                System.out.println("  Estoque: " + rs.getInt("estoque"));
                System.out.println("  Emprestados: " + rs.getInt("emprestados"));
                System.out.println("  Ativo: " + rs.getBoolean("ativo"));
                System.out.println();
            }
        }
        conn.close();
    }
}
