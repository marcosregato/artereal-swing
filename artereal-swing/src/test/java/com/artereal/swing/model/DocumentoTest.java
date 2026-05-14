package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Documento
 */
@DisplayName("Testes Unitários - Documento")
class DocumentoTest {

    private Documento documento;

    @BeforeEach
    void setUp() {
        documento = new Documento();
    }

    @Test
    @DisplayName("Deve criar documento com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Documento novoDocumento = new Documento();

        // Assert
        assertThat(novoDocumento).isNotNull();
        assertThat(novoDocumento.getId()).isNull();
        assertThat(novoDocumento.getCodigoIrmao()).isNull();
        assertThat(novoDocumento.getNomeArquivo()).isNull();
        assertThat(novoDocumento.getCaminhoArquivo()).isNull();
        assertThat(novoDocumento.getTipo()).isNull();
        assertThat(novoDocumento.getDescricao()).isNull();
        assertThat(novoDocumento.getDataUpload()).isNull();
        assertThat(novoDocumento.getDataExpiracao()).isNull();
        assertThat(novoDocumento.getStatus()).isNull();
        assertThat(novoDocumento.isAtivo()).isFalse(); // boolean default is false
    }

    @Test
    @DisplayName("Deve criar documento com construtor parametrizado")
    void testConstrutorParametrizado() {
        // Arrange
        Long codigoIrmao = 123L;
        String nomeArquivo = "identidade.pdf";
        String tipo = "IDENTIDADE";

        // Act
        Documento novoDocumento = new Documento(codigoIrmao, nomeArquivo, tipo);

        // Assert
        assertThat(novoDocumento).isNotNull();
        assertThat(novoDocumento.getCodigoIrmao()).isEqualTo(codigoIrmao);
        assertThat(novoDocumento.getNomeArquivo()).isEqualTo(nomeArquivo);
        assertThat(novoDocumento.getTipo()).isEqualTo(tipo);
        assertThat(novoDocumento.getDataUpload()).isNotNull();
        assertThat(novoDocumento.getDataUpload()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(novoDocumento.getStatus()).isEqualTo("ATIVO");
        assertThat(novoDocumento.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        documento.setId(id);

        // Assert
        assertThat(documento.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter código do irmão")
    void testSetGetCodigoIrmao() {
        // Arrange
        Long codigoIrmao = 456L;

        // Act
        documento.setCodigoIrmao(codigoIrmao);

        // Assert
        assertThat(documento.getCodigoIrmao()).isEqualTo(codigoIrmao);
    }

    @Test
    @DisplayName("Deve definir e obter nome do arquivo")
    void testSetGetNomeArquivo() {
        // Arrange
        String nomeArquivo = "diploma_maconico.jpg";

        // Act
        documento.setNomeArquivo(nomeArquivo);

        // Assert
        assertThat(documento.getNomeArquivo()).isEqualTo(nomeArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter caminho do arquivo")
    void testSetGetCaminhoArquivo() {
        // Arrange
        String caminhoArquivo = "/documentos/irmaos/123/diploma.jpg";

        // Act
        documento.setCaminhoArquivo(caminhoArquivo);

        // Assert
        assertThat(documento.getCaminhoArquivo()).isEqualTo(caminhoArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter tipo")
    void testSetGetTipo() {
        // Arrange
        String tipo = "CERTIFICADO";

        // Act
        documento.setTipo(tipo);

        // Assert
        assertThat(documento.getTipo()).isEqualTo(tipo);
    }

    @Test
    @DisplayName("Deve definir e obter descrição")
    void testSetGetDescricao() {
        // Arrange
        String descricao = "Diploma de Mestre Maçônico";

        // Act
        documento.setDescricao(descricao);

        // Assert
        assertThat(documento.getDescricao()).isEqualTo(descricao);
    }

    @Test
    @DisplayName("Deve definir e obter data de upload")
    void testSetGetDataUpload() {
        // Arrange
        LocalDateTime dataUpload = LocalDateTime.of(2024, 1, 15, 10, 30);

        // Act
        documento.setDataUpload(dataUpload);

        // Assert
        assertThat(documento.getDataUpload()).isEqualTo(dataUpload);
    }

    @Test
    @DisplayName("Deve definir e obter data de expiração")
    void testSetGetDataExpiracao() {
        // Arrange
        LocalDateTime dataExpiracao = LocalDateTime.of(2025, 1, 15, 10, 30);

        // Act
        documento.setDataExpiracao(dataExpiracao);

        // Assert
        assertThat(documento.getDataExpiracao()).isEqualTo(dataExpiracao);
    }

    @Test
    @DisplayName("Deve definir e obter status")
    void testSetGetStatus() {
        // Arrange
        String status = "EXPIRADO";

        // Act
        documento.setStatus(status);

        // Assert
        assertThat(documento.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Deve definir e obter assinatura digital")
    void testSetGetAssinaturaDigital() {
        // Arrange
        String assinaturaDigital = "ABC123XYZ789";

        // Act
        documento.setAssinaturaDigital(assinaturaDigital);

        // Assert
        assertThat(documento.getAssinaturaDigital()).isEqualTo(assinaturaDigital);
    }

    @Test
    @DisplayName("Deve definir e obter hash do arquivo")
    void testSetGetHashArquivo() {
        // Arrange
        String hashArquivo = "a1b2c3d4e5f6789";

        // Act
        documento.setHashArquivo(hashArquivo);

        // Assert
        assertThat(documento.getHashArquivo()).isEqualTo(hashArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter tamanho do arquivo")
    void testSetGetTamanhoArquivo() {
        // Arrange
        double tamanhoArquivo = 1024.5; // KB

        // Act
        documento.setTamanhoArquivo(tamanhoArquivo);

        // Assert
        assertThat(documento.getTamanhoArquivo()).isEqualTo(tamanhoArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter formato do arquivo")
    void testSetGetFormatoArquivo() {
        // Arrange
        String formatoArquivo = "PDF";

        // Act
        documento.setFormatoArquivo(formatoArquivo);

        // Assert
        assertThat(documento.getFormatoArquivo()).isEqualTo(formatoArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter usuário de upload")
    void testSetGetUsuarioUpload() {
        // Arrange
        String usuarioUpload = "admin";

        // Act
        documento.setUsuarioUpload(usuarioUpload);

        // Assert
        assertThat(documento.getUsuarioUpload()).isEqualTo(usuarioUpload);
    }

    @Test
    @DisplayName("Deve definir e obter status ativo")
    void testSetGetAtivo() {
        // Arrange
        boolean ativo = false;

        // Act
        documento.setAtivo(ativo);

        // Assert
        assertThat(documento.isAtivo()).isEqualTo(ativo);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-15 10:30:00";

        // Act
        documento.setCreatedAt(createdAt);

        // Assert
        assertThat(documento.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-15 11:00:00";

        // Act
        documento.setUpdatedAt(updatedAt);

        // Assert
        assertThat(documento.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        documento.setId(123L);
        documento.setNomeArquivo("identidade.pdf");
        documento.setTipo("IDENTIDADE");
        documento.setStatus("ATIVO");

        // Act
        String resultado = documento.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Documento");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Documento documento1 = new Documento();
        documento1.setId(123L);

        Documento documento2 = new Documento();
        documento2.setId(123L);

        Documento documento3 = new Documento();
        documento3.setId(456L);

        // Act & Assert
        assertThat(documento1).isEqualTo(documento2);
        assertThat(documento1).isNotEqualTo(documento3);
        assertThat(documento1).isNotEqualTo(null);
        assertThat(documento1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Documento documento1 = new Documento();
        documento1.setId(123L);

        Documento documento2 = new Documento();
        documento2.setId(123L);

        Documento documento3 = new Documento();
        documento3.setId(456L);

        // Act & Assert
        assertThat(documento1.hashCode()).isEqualTo(documento2.hashCode());
        assertThat(documento1.hashCode()).isNotEqualTo(documento3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de documento")
    void testDiferentesTipos() {
        // Arrange & Act
        documento.setTipo("IDENTIDADE");
        assertThat(documento.getTipo()).isEqualTo("IDENTIDADE");

        documento.setTipo("DIPLOMA");
        assertThat(documento.getTipo()).isEqualTo("DIPLOMA");

        documento.setTipo("CERTIFICADO");
        assertThat(documento.getTipo()).isEqualTo("CERTIFICADO");

        documento.setTipo("OUTRO");
        assertThat(documento.getTipo()).isEqualTo("OUTRO");
    }

    @Test
    @DisplayName("Deve lidar com diferentes status")
    void testDiferentesStatus() {
        // Arrange & Act
        documento.setStatus("ATIVO");
        assertThat(documento.getStatus()).isEqualTo("ATIVO");

        documento.setStatus("EXPIRADO");
        assertThat(documento.getStatus()).isEqualTo("EXPIRADO");

        documento.setStatus("CANCELADO");
        assertThat(documento.getStatus()).isEqualTo("CANCELADO");
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo zero")
    void testTamanhoArquivoZero() {
        // Act
        documento.setTamanhoArquivo(0.0);

        // Assert
        assertThat(documento.getTamanhoArquivo()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo negativo")
    void testTamanhoArquivoNegativo() {
        // Act
        documento.setTamanhoArquivo(-1.0);

        // Assert
        assertThat(documento.getTamanhoArquivo()).isEqualTo(-1.0);
        assertThat(documento.getTamanhoArquivo()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo grande")
    void testTamanhoArquivoGrande() {
        // Arrange
        double tamanhoGrande = 1048576.0; // 1GB em KB

        // Act
        documento.setTamanhoArquivo(tamanhoGrande);

        // Assert
        assertThat(documento.getTamanhoArquivo()).isEqualTo(tamanhoGrande);
        assertThat(documento.getTamanhoArquivo()).isGreaterThan(1000000.0);
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        documento.setNomeArquivo(null);
        documento.setCaminhoArquivo(null);
        documento.setTipo(null);
        documento.setDescricao(null);
        documento.setDataUpload(null);
        documento.setDataExpiracao(null);
        documento.setStatus(null);
        documento.setAssinaturaDigital(null);
        documento.setHashArquivo(null);
        documento.setFormatoArquivo(null);
        documento.setUsuarioUpload(null);
        documento.setCreatedAt(null);
        documento.setUpdatedAt(null);

        // Assert
        assertThat(documento.getNomeArquivo()).isNull();
        assertThat(documento.getCaminhoArquivo()).isNull();
        assertThat(documento.getTipo()).isNull();
        assertThat(documento.getDescricao()).isNull();
        assertThat(documento.getDataUpload()).isNull();
        assertThat(documento.getDataExpiracao()).isNull();
        assertThat(documento.getStatus()).isNull();
        assertThat(documento.getAssinaturaDigital()).isNull();
        assertThat(documento.getHashArquivo()).isNull();
        assertThat(documento.getFormatoArquivo()).isNull();
        assertThat(documento.getUsuarioUpload()).isNull();
        assertThat(documento.getCreatedAt()).isNull();
        assertThat(documento.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        documento.setNomeArquivo("");
        documento.setCaminhoArquivo("");
        documento.setTipo("");
        documento.setDescricao("");
        documento.setStatus("");
        documento.setAssinaturaDigital("");
        documento.setHashArquivo("");
        documento.setFormatoArquivo("");
        documento.setUsuarioUpload("");
        documento.setCreatedAt("");
        documento.setUpdatedAt("");

        // Assert
        assertThat(documento.getNomeArquivo()).isEmpty();
        assertThat(documento.getCaminhoArquivo()).isEmpty();
        assertThat(documento.getTipo()).isEmpty();
        assertThat(documento.getDescricao()).isEmpty();
        assertThat(documento.getStatus()).isEmpty();
        assertThat(documento.getAssinaturaDigital()).isEmpty();
        assertThat(documento.getHashArquivo()).isEmpty();
        assertThat(documento.getFormatoArquivo()).isEmpty();
        assertThat(documento.getUsuarioUpload()).isEmpty();
        assertThat(documento.getCreatedAt()).isEmpty();
        assertThat(documento.getUpdatedAt()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de arquivo")
    void testDiferentesFormatosArquivo() {
        // Arrange & Act
        documento.setFormatoArquivo("PDF");
        assertThat(documento.getFormatoArquivo()).isEqualTo("PDF");

        documento.setFormatoArquivo("JPG");
        assertThat(documento.getFormatoArquivo()).isEqualTo("JPG");

        documento.setFormatoArquivo("PNG");
        assertThat(documento.getFormatoArquivo()).isEqualTo("PNG");

        documento.setFormatoArquivo("DOC");
        assertThat(documento.getFormatoArquivo()).isEqualTo("DOC");

        documento.setFormatoArquivo("XLS");
        assertThat(documento.getFormatoArquivo()).isEqualTo("XLS");
    }

    @Test
    @DisplayName("Deve lidar com datas de upload futuras")
    void testDataUploadFutura() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFutura = agora.plusDays(1);

        // Act
        documento.setDataUpload(dataFutura);

        // Assert
        assertThat(documento.getDataUpload()).isEqualTo(dataFutura);
        assertThat(documento.getDataUpload()).isAfter(agora);
    }

    @Test
    @DisplayName("Deve lidar com datas de upload passadas")
    void testDataUploadPassada() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataPassada = agora.minusDays(1);

        // Act
        documento.setDataUpload(dataPassada);

        // Assert
        assertThat(documento.getDataUpload()).isEqualTo(dataPassada);
        assertThat(documento.getDataUpload()).isBefore(agora);
    }

    @Test
    @DisplayName("Deve lidar com data de expiração antes do upload")
    void testDataExpiracaoAntesUpload() {
        // Arrange
        LocalDateTime dataUpload = LocalDateTime.of(2024, 2, 1, 10, 0);
        LocalDateTime dataExpiracao = LocalDateTime.of(2024, 1, 15, 10, 0);

        // Act
        documento.setDataUpload(dataUpload);
        documento.setDataExpiracao(dataExpiracao);

        // Assert
        assertThat(documento.getDataExpiracao()).isBefore(documento.getDataUpload());
    }

    @Test
    @DisplayName("Deve lidar com data de expiração após o upload")
    void testDataExpiracaoAposUpload() {
        // Arrange
        LocalDateTime dataUpload = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime dataExpiracao = LocalDateTime.of(2025, 1, 15, 10, 0);

        // Act
        documento.setDataUpload(dataUpload);
        documento.setDataExpiracao(dataExpiracao);

        // Assert
        assertThat(documento.getDataExpiracao()).isAfter(documento.getDataUpload());
    }

    @Test
    @DisplayName("Deve lidar com diferentes usuários de upload")
    void testDiferentesUsuariosUpload() {
        // Arrange & Act
        documento.setUsuarioUpload("admin");
        assertThat(documento.getUsuarioUpload()).isEqualTo("admin");

        documento.setUsuarioUpload("joao.silva");
        assertThat(documento.getUsuarioUpload()).isEqualTo("joao.silva");

        documento.setUsuarioUpload("secretario");
        assertThat(documento.getUsuarioUpload()).isEqualTo("secretario");

        documento.setUsuarioUpload("veneravel");
        assertThat(documento.getUsuarioUpload()).isEqualTo("veneravel");
    }

    @Test
    @DisplayName("Deve lidar com nomes de arquivo com extensões")
    void testNomesArquivoComExtensoes() {
        // Arrange & Act
        documento.setNomeArquivo("documento.pdf");
        assertThat(documento.getNomeArquivo()).isEqualTo("documento.pdf");

        documento.setNomeArquivo("foto.jpg");
        assertThat(documento.getNomeArquivo()).isEqualTo("foto.jpg");

        documento.setNomeArquivo("planilha.xlsx");
        assertThat(documento.getNomeArquivo()).isEqualTo("planilha.xlsx");

        documento.setNomeArquivo("apresentacao.ppt");
        assertThat(documento.getNomeArquivo()).isEqualTo("apresentacao.ppt");
    }

    @Test
    @DisplayName("Deve lidar com caminhos de arquivo complexos")
    void testCaminhosArquivoComplexos() {
        // Arrange & Act
        documento.setCaminhoArquivo("/home/user/documents/pdfs/identidade.pdf");
        assertThat(documento.getCaminhoArquivo()).isEqualTo("/home/user/documents/pdfs/identidade.pdf");

        documento.setCaminhoArquivo("C:\\Users\\User\\Documents\\diploma.jpg");
        assertThat(documento.getCaminhoArquivo()).isEqualTo("C:\\Users\\User\\Documents\\diploma.jpg");

        documento.setCaminhoArquivo("./uploads/temp/file.png");
        assertThat(documento.getCaminhoArquivo()).isEqualTo("./uploads/temp/file.png");
    }

    @Test
    @DisplayName("Deve lidar com hash de arquivo em diferentes formatos")
    void testHashArquivoFormatos() {
        // Arrange & Act
        documento.setHashArquivo("a1b2c3d4e5f6");
        assertThat(documento.getHashArquivo()).isEqualTo("a1b2c3d4e5f6");

        documento.setHashArquivo("A1B2C3D4E5F6");
        assertThat(documento.getHashArquivo()).isEqualTo("A1B2C3D4E5F6");

        documento.setHashArquivo("a1b2-c3d4-e5f6");
        assertThat(documento.getHashArquivo()).isEqualTo("a1b2-c3d4-e5f6");

        documento.setHashArquivo("sha256:abc123");
        assertThat(documento.getHashArquivo()).isEqualTo("sha256:abc123");
    }

    @Test
    @DisplayName("Deve lidar com código do irmão zero")
    void testCodigoIrmaoZero() {
        // Act
        documento.setCodigoIrmao(0L);

        // Assert
        assertThat(documento.getCodigoIrmao()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve lidar com código do irmão negativo")
    void testCodigoIrmaoNegativo() {
        // Act
        documento.setCodigoIrmao(-1L);

        // Assert
        assertThat(documento.getCodigoIrmao()).isEqualTo(-1L);
        assertThat(documento.getCodigoIrmao()).isNegative();
    }

    @Test
    @DisplayName("Deve lidar com assinatura digital longa")
    void testAssinaturaDigitalLonga() {
        // Arrange
        String assinaturaLonga = "MIIDdzCCAl+gAwIBAgIEbGpqZTANBgkqhkiG9w0BAQsFADCBmDELMAkGA1UEBhMCQlIxEzARBgNVBAoTCkludGVybmV0IEJyYXNpbCBJZGVudGlkYWRlIENBLTEwHhcNMTIwOTEyMTIwMzAwWhcNMjUwOTEyMTIwMzAwWjCBmDELMAkGA1UEBhMCQlIxEzARBgNVBAoTCkludGVybmV0IEJyYXNpbCBJZGVudGlkYWRlIENBLTEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC5WZ";

        // Act
        documento.setAssinaturaDigital(assinaturaLonga);

        // Assert
        assertThat(documento.getAssinaturaDigital()).isEqualTo(assinaturaLonga);
        assertThat(documento.getAssinaturaDigital()).hasSizeGreaterThan(100);
    }

    @Test
    @DisplayName("Deve lidar com descrições longas")
    void testDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Este documento contém informações importantes sobre a identificação do irmão, incluindo dados pessoais, documentos oficiais, certificados de conclusão de cursos maçons, diplomas de graus conferidos, e outros documentos relevantes para a sua trajetória na maçonaria.";

        // Act
        documento.setDescricao(descricaoLonga);

        // Assert
        assertThat(documento.getDescricao()).isEqualTo(descricaoLonga);
        assertThat(documento.getDescricao()).hasSizeGreaterThan(50);
    }
}
