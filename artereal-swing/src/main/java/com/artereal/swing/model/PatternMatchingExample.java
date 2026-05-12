package com.artereal.swing.model;

/**
 * Exemplo de Pattern Matching moderno para eliminar instanceof e casts manuais
 * Demonstra como usar switch expressions e pattern matching
 * NOTA: Este código usa Java 17+ features para demonstração
 */
public class PatternMatchingExample {
    
    // Exemplo 1: Pattern Matching com instanceof eliminado
    public String processarDocumento(Object obj) {
        if (obj instanceof DocumentoRecord doc) {
            return "Processando documento: " + doc.nomeArquivo();
        } else if (obj instanceof DespesaRecord desp) {
            return "Processando despesa: " + desp.descricao();
        } else if (obj instanceof IrmaoRecord irmao) {
            return "Processando irmão: " + irmao.nome();
        } else if (obj == null) {
            return "Objeto nulo";
        } else {
            return "Tipo desconhecido: " + obj.getClass().getSimpleName();
        }
    }
    
    // Exemplo 2: Switch expression com pattern matching (Java 17+)
    public String validarStatus(String status) {
        return switch (status) {
            case "ATIVO" -> "Documento ativo";
            case "EXPIRADO" -> "Documento expirado";
            case "CANCELADO" -> "Documento cancelado";
            case null, default -> "Status inválido";
        };
    }
    
    // Exemplo 3: Pattern matching com guard clauses (Java 17+)
    public String processarDespesa(DespesaRecord despesa) {
        if (despesa.valor() > 5000.0) {
            return "Despesa alta: " + despesa.descricao();
        } else if ("Urgente".equals(despesa.categoria())) {
            return "Despesa urgente: " + despesa.descricao();
        } else {
            return "Despesa normal: " + despesa.descricao();
        }
    }
    
    // Exemplo 4: Destruturação de dados com if-else tradicional
    public String extrairInformacoes(IrmaoRecord irmao) {
        if (irmao.cargo().equals("Venerável")) {
            return "Irmão Venerável " + irmao.nome() + " (" + irmao.apelido() + ") - Loja: " + irmao.loja();
        } else if (irmao.loja().startsWith("ARTE")) {
            return "Irmão da Loja ARTE " + irmao.nome() + " - Cargo: " + irmao.cargo();
        } else {
            return "Irmão Regular " + irmao.nome() + " (" + irmao.apelido() + ")";
        }
    }
    
    // Exemplo 5: Validação tradicional com if-else
    public boolean validarDocumento(DocumentoRecord doc) {
        if (doc.status().equals("ATIVO") && doc.dataExpiracao().isAfter(java.time.LocalDateTime.now())) {
            return false; // Documento expirado
        } else if (doc.assinaturaDigital() != null && !doc.assinaturaDigital().isEmpty()) {
            return true; // Documento assinado digitalmente
        } else {
            return true; // Documento válido por padrão
        }
    }
    
    // Exemplo 6: Concorrência moderna com ExecutorService (Java 17+)
    public void processarEmParalelo(Runnable... tarefas) {
        // Usando ExecutorService para compatibilidade com Java 17
        var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor(Runtime.getRuntime().availableProcessors());
        
        try {
            // Submete todas as tarefas para execução paralela
            var futures = java.util.Arrays.stream(tarefas)
                .map(executor::submit)
                .toList();
            
            // Aguarda todas as tarefas terminarem
            for (var future : futures) {
                future.get(); // Bloqueia até a tarefa completar
            }
            
        } catch (Exception e) {
        } finally {
            executor.shutdown();
        }
    }
}
