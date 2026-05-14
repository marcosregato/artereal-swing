package com.artereal.swing.application.loja;

import com.artereal.swing.domain.loja.Loja;

/**
 * Mapper para conversão entre entidades de domínio e DTOs
 * 
 * Esta classe contém métodos estáticos para converter entre
 * entidades Loja e seus respectivos DTOs (Request/Response).
 */
public class LojaMapper {
    
    /**
     * Converte uma entidade Loja para LojaResponse
     */
    public static LojaResponse toResponse(Loja loja) {
        if (loja == null) {
            return null;
        }
        
        return new LojaResponse(
            loja.getId(),
            loja.getNome(),
            loja.getCnpj().getNumeros(),
            loja.getEndereco().rua(),
            loja.getCidade(),
            loja.getEstado(),
            loja.getStatus().name(),
            loja.getEnderecoCompleto(),
            loja.getIrmaos().size(),
            loja.getCaixas().size(),
            formatarData(loja.getDataCriacao()),
            formatarData(loja.getDataAtualizacao())
        );
    }
    
    /**
     * Converte uma entidade Loja para LojaSummary (resumo)
     */
    public static LojaSummary toSummary(Loja loja) {
        if (loja == null) {
            return null;
        }
        
        return new LojaSummary(
            loja.getId(),
            loja.getNome(),
            loja.getCidade(),
            loja.getEstado(),
            loja.getStatus().name(),
            loja.getQuantidadeIrmaosAtivos(),
            loja.getValorTotalCaixasAbertos()
        );
    }
    
    /**
     * Converte uma lista de Loja para lista de LojaResponse
     */
    public static java.util.List<LojaResponse> toResponseList(java.util.List<Loja> lojas) {
        if (lojas == null) {
            return java.util.Collections.emptyList();
        }
        
        return lojas.stream()
            .map(LojaMapper::toResponse)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Converte uma lista de Loja para lista de LojaSummary
     */
    public static java.util.List<LojaSummary> toSummaryList(java.util.List<Loja> lojas) {
        if (lojas == null) {
            return java.util.Collections.emptyList();
        }
        
        return lojas.stream()
            .map(LojaMapper::toSummary)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Formata data para exibição
     */
    private static String formatarData(java.time.LocalDateTime data) {
        if (data == null) {
            return null;
        }
        
        return data.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    /**
     * Converte uma lista de Loja para lista de LojaResponse com paginação
     */
    public static PaginatedLojaResponse toPaginatedResponse(java.util.List<Loja> lojas, 
                                                        int pagina, 
                                                        int tamanhoPagina, 
                                                        long total) {
        return new PaginatedLojaResponse(
            toResponseList(lojas),
            pagina,
            tamanhoPagina,
            total,
            (int) Math.ceil((double) total / tamanhoPagina)
        );
    }
    
    /**
     * DTO para resumo de loja (usado em listagens)
     */
    public record LojaSummary(
        Long id,
        String nome,
        String cidade,
        String estado,
        String status,
        int quantidadeIrmaosAtivos,
        java.math.BigDecimal valorTotalCaixasAbertos
    ) {
        public boolean isAtiva() {
            return "ATIVA".equals(status);
        }
    }
    
    /**
     * DTO para resposta paginada de lojas
     */
    public record PaginatedLojaResponse(
        java.util.List<LojaResponse> lojas,
        int pagina,
        int tamanhoPagina,
        long total,
        int totalPaginas
    ) {}
}
