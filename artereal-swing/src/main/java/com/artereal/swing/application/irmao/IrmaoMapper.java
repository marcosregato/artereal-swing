package com.artereal.swing.application.irmao;

import com.artereal.swing.domain.irmao.Irmao;
import java.time.format.DateTimeFormatter;

/**
 * Mapper para conversão entre entidades de domínio e DTOs
 */
public class IrmaoMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public static IrmaoResponse toResponse(Irmao irmao) {
        if (irmao == null) {
            return null;
        }
        
        return new IrmaoResponse(
            irmao.getId(),
            irmao.getNome(),
            irmao.getCpf(),
            irmao.getTelefone(),
            irmao.getEmail(),
            irmao.getStatus().name(),
            irmao.getLojaId(),
            formatarData(irmao.getDataCriacao()),
            formatarData(irmao.getDataAtualizacao())
        );
    }
    
    private static String formatarData(java.time.LocalDateTime data) {
        if (data == null) {
            return null;
        }
        return data.format(FORMATTER);
    }
}
