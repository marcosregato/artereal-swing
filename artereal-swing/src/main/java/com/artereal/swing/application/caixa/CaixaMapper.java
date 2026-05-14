package com.artereal.swing.application.caixa;

import com.artereal.swing.domain.caixa.Caixa;
import java.time.format.DateTimeFormatter;

/**
 * Mapper para conversão entre entidades de domínio e DTOs
 */
public class CaixaMapper {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public static CaixaResponse toResponse(Caixa caixa) {
        if (caixa == null) {
            return null;
        }
        
        return new CaixaResponse(
            caixa.getId(),
            caixa.getLojaId(),
            caixa.getValor(),
            caixa.getResponsavel(),
            caixa.getStatus().name(),
            caixa.getSaldo(),
            formatarData(caixa.getDataAbertura()),
            formatarData(caixa.getDataFechamento())
        );
    }
    
    private static String formatarData(java.time.LocalDateTime data) {
        if (data == null) {
            return null;
        }
        return data.format(FORMATTER);
    }
}
