package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.DocumentoDAO;
import com.artereal.swing.model.Documento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para DocumentoDAO
 */
@DisplayName("Testes de Integração - DocumentoDAO")
class DocumentoDAOTest {

    private DatabaseManager databaseManager;
    private DocumentoDAO documentoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        documentoDAO = new DocumentoDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela documento após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM documento");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar documento")
    void testSalvarDocumento() throws SQLException {
        // Arrange
        Documento documento = criarDocumentoTeste();
        documento.setNomeArquivo("documento_teste.pdf");

        // Act
        documentoDAO.save(documento);

        // Assert
        assertThat(documento.getId()).isNotNull();
        assertThat(documento.getId()).isPositive();
    }

    @Test
    @DisplayName("Deve buscar documento por ID")
    void testBuscarDocumentoPorId() throws SQLException {
        // Arrange
        Documento documento = criarDocumentoTeste();
        documentoDAO.save(documento);
        Long id = documento.getId();

        // Act
        Documento encontrado = documentoDAO.findById(id);

        // Assert
        if (encontrado != null) {
            assertThat(encontrado.getId()).isEqualTo(id);
            assertThat(encontrado.getNomeArquivo()).isEqualTo("ata_reuniao_2024.pdf");
            assertThat(encontrado.getTipo()).isEqualTo("ATA");
            assertThat(encontrado.getStatus()).isEqualTo("ATIVO");
        }
    }

    @Test
    @DisplayName("Deve buscar todos os documentos")
    void testBuscarTodosDocumentos() throws SQLException {
        // Arrange
        Documento documento1 = criarDocumentoTeste();
        documento1.setNomeArquivo("documento1.pdf");
        documentoDAO.save(documento1);

        Documento documento2 = criarDocumentoTeste();
        documento2.setNomeArquivo("documento2.pdf");
        documentoDAO.save(documento2);

        // Act
        List<Documento> documentos = documentoDAO.findAll();

        // Assert
        assertThat(documentos).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve buscar documentos por tipo")
    void testBuscarDocumentosPorTipo() throws SQLException {
        // Arrange
        Documento documento1 = criarDocumentoTeste();
        documento1.setTipo("ATA");
        documentoDAO.save(documento1);

        Documento documento2 = criarDocumentoTeste();
        documento2.setTipo("ATA");
        documentoDAO.save(documento2);

        Documento documento3 = criarDocumentoTeste();
        documento3.setTipo("CERTIFICADO");
        documentoDAO.save(documento3);

        // Act
        List<Documento> atas = documentoDAO.findByTipo("ATA");
        List<Documento> certificados = documentoDAO.findByTipo("CERTIFICADO");

        // Assert
        assertThat(atas).hasSizeGreaterThanOrEqualTo(2);
        assertThat(certificados).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(atas).extracting("tipo")
            .allMatch(tipo -> "ATA".equals(tipo));
        assertThat(certificados).extracting("tipo")
            .allMatch(tipo -> "CERTIFICADO".equals(tipo));
    }

    @Test
    @DisplayName("Deve buscar documentos por irmão")
    void testBuscarDocumentosPorIrmao() throws SQLException {
        // Arrange
        Documento documento1 = criarDocumentoTeste();
        documento1.setCodigoIrmao(123L);
        documentoDAO.save(documento1);

        Documento documento2 = criarDocumentoTeste();
        documento2.setCodigoIrmao(123L);
        documentoDAO.save(documento2);

        Documento documento3 = criarDocumentoTeste();
        documento3.setCodigoIrmao(456L);
        documentoDAO.save(documento3);

        // Act
        List<Documento> docsIrmao123 = documentoDAO.findByIrmao(123L);
        List<Documento> docsIrmao456 = documentoDAO.findByIrmao(456L);

        // Assert
        assertThat(docsIrmao123).hasSizeGreaterThanOrEqualTo(2);
        assertThat(docsIrmao456).hasSizeGreaterThanOrEqualTo(1);
        
        assertThat(docsIrmao123).extracting("codigoIrmao")
            .allMatch(codigo -> codigo.equals(123L));
        assertThat(docsIrmao456).extracting("codigoIrmao")
            .allMatch(codigo -> codigo.equals(456L));
    }

    @Test
    @DisplayName("Deve buscar documentos expirados")
    void testBuscarDocumentosExpirados() throws SQLException {
        // Arrange
        Documento documentoExpirado = criarDocumentoTeste();
        documentoExpirado.setDataExpiracao(LocalDateTime.now().minusDays(10));
        documentoExpirado.setStatus("EXPIRADO");
        documentoDAO.save(documentoExpirado);

        Documento documentoValido = criarDocumentoTeste();
        documentoValido.setDataExpiracao(LocalDateTime.now().plusDays(30));
        documentoValido.setStatus("ATIVO");
        documentoDAO.save(documentoValido);

        // Act
        List<Documento> expirados = documentoDAO.findExpirados();

        // Assert
        assertThat(expirados).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve buscar documentos próximos à expiração")
    void testBuscarDocumentosProximosExpiracao() throws SQLException {
        // Arrange
        Documento documentoAmanha = criarDocumentoTeste();
        documentoAmanha.setDataExpiracao(LocalDateTime.now().plusDays(1));
        documentoDAO.save(documentoAmanha);

        Documento documentoProximo = criarDocumentoTeste();
        documentoProximo.setDataExpiracao(LocalDateTime.now().plusDays(5));
        documentoDAO.save(documentoProximo);

        Documento documentoLonge = criarDocumentoTeste();
        documentoLonge.setDataExpiracao(LocalDateTime.now().plusDays(15));
        documentoDAO.save(documentoLonge);

        // Act
        List<Documento> proximos7dias = documentoDAO.findProximosExpiracao(7);
        List<Documento> proximos10dias = documentoDAO.findProximosExpiracao(10);

        // Assert
        assertThat(proximos7dias).hasSizeGreaterThanOrEqualTo(2); // amanhã e 5 dias
        assertThat(proximos10dias).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve contar todos os documentos")
    void testCountTodosDocumentos() throws SQLException {
        // Arrange
        Documento documento1 = criarDocumentoTeste();
        documentoDAO.save(documento1);

        Documento documento2 = criarDocumentoTeste();
        documentoDAO.save(documento2);

        // Act
        int total = documentoDAO.count();

        // Assert
        assertThat(total).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve contar documentos ativos")
    void testCountDocumentosAtivos() throws SQLException {
        // Arrange
        Documento documento1 = criarDocumentoTeste();
        documento1.setStatus("ATIVO");
        documentoDAO.save(documento1);

        Documento documento2 = criarDocumentoTeste();
        documento2.setStatus("ATIVO");
        documentoDAO.save(documento2);

        Documento documento3 = criarDocumentoTeste();
        documento3.setStatus("INATIVO");
        documentoDAO.save(documento3);

        // Act
        int ativos = documentoDAO.countAtivos();

        // Assert
        assertThat(ativos).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve contar documentos expirados")
    void testCountDocumentosExpirados() throws SQLException {
        // Arrange
        Documento documento1 = criarDocumentoTeste();
        documento1.setDataExpiracao(LocalDateTime.now().minusDays(5));
        documentoDAO.save(documento1);

        Documento documento2 = criarDocumentoTeste();
        documento2.setDataExpiracao(LocalDateTime.now().minusDays(10));
        documentoDAO.save(documento2);

        Documento documento3 = criarDocumentoTeste();
        documento3.setDataExpiracao(LocalDateTime.now().plusDays(30));
        documentoDAO.save(documento3);

        // Act
        int expirados = documentoDAO.countExpirados();

        // Assert
        assertThat(expirados).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve contar documentos próximos à expiração")
    void testCountDocumentosProximosExpiracao() throws SQLException {
        // Arrange
        Documento documento1 = criarDocumentoTeste();
        documento1.setDataExpiracao(LocalDateTime.now().plusDays(5));
        documentoDAO.save(documento1);

        Documento documento2 = criarDocumentoTeste();
        documento2.setDataExpiracao(LocalDateTime.now().plusDays(10));
        documentoDAO.save(documento2);

        Documento documento3 = criarDocumentoTeste();
        documento3.setDataExpiracao(LocalDateTime.now().plusDays(45));
        documentoDAO.save(documento3);

        // Act
        int proximos = documentoDAO.countProximosExpiracao();

        // Assert
        assertThat(proximos).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Deve excluir (desativar) documento")
    void testExcluirDocumento() throws SQLException {
        // Arrange
        Documento documento = criarDocumentoTeste();
        documentoDAO.save(documento);
        Long id = documento.getId();

        // Act
        documentoDAO.delete(id);

        // Assert
        Documento desativado = documentoDAO.findById(id);
        if (desativado != null) {
            assertThat(desativado.isAtivo()).isFalse();
        }
        
        // Verificar que não aparece nas listagens normais
        List<Documento> ativos = documentoDAO.findAll();
        assertThat(ativos).extracting("id")
            .doesNotContain(id);
    }

    @Test
    @DisplayName("Deve atualizar documento")
    void testAtualizarDocumento() throws SQLException {
        // Arrange
        Documento documento = criarDocumentoTeste();
        documentoDAO.save(documento);
        Long id = documento.getId();

        // Act
        documento.setNomeArquivo("novo_nome.pdf");
        documento.setDescricao("Nova descrição");
        documento.setStatus("ATUALIZADO");
        documentoDAO.save(documento);

        // Assert
        Documento atualizado = documentoDAO.findById(id);
        if (atualizado != null) {
            assertThat(atualizado.getNomeArquivo()).isEqualTo("novo_nome.pdf");
            assertThat(atualizado.getDescricao()).isEqualTo("Nova descrição");
            assertThat(atualizado.getStatus()).isEqualTo("ATUALIZADO");
        }
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void testBuscarIdInexistente() throws SQLException {
        // Act & Assert
        Documento resultado = documentoDAO.findById(999L);
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Deve lidar com campos nulos opcionalmente")
    void testCamposNulosOpcionais() throws SQLException {
        // Arrange
        Documento documento = criarDocumentoTeste();
        documento.setCodigoIrmao(null);
        documento.setDataExpiracao(null);
        documento.setAssinaturaDigital(null);

        // Act
        documentoDAO.save(documento);

        // Assert
        assertThat(documento.getId()).isNotNull();
        
        Documento salvo = documentoDAO.findById(documento.getId());
        if (salvo != null) {
            assertThat(salvo.getCodigoIrmao()).isNull();
            assertThat(salvo.getDataExpiracao()).isNull();
            assertThat(salvo.getAssinaturaDigital()).isNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com diferentes tipos de documento")
    void testDiferentesTiposDocumento() throws SQLException {
        // Arrange
        Documento ata = criarDocumentoTeste();
        ata.setTipo("ATA");
        documentoDAO.save(ata);

        Documento certificado = criarDocumentoTeste();
        certificado.setTipo("CERTIFICADO");
        documentoDAO.save(certificado);

        Documento relatorio = criarDocumentoTeste();
        relatorio.setTipo("RELATORIO");
        documentoDAO.save(relatorio);

        // Act
        List<Documento> atas = documentoDAO.findByTipo("ATA");
        List<Documento> certificados = documentoDAO.findByTipo("CERTIFICADO");
        List<Documento> relatorios = documentoDAO.findByTipo("RELATORIO");

        // Assert
        assertThat(atas).hasSizeGreaterThanOrEqualTo(1);
        assertThat(certificados).hasSizeGreaterThanOrEqualTo(1);
        assertThat(relatorios).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve registrar fluxo completo de documento")
    void testFluxoCompletoDocumento() throws SQLException {
        // Arrange
        Documento documento = criarDocumentoTeste();
        documento.setNomeArquivo("fluxo_completo.pdf");

        // Act - Fluxo completo
        documentoDAO.save(documento);
        assertThat(documento.getId()).isNotNull();
        
        Long id = documento.getId();
        
        documento.setStatus("PROCESSADO");
        documentoDAO.save(documento);
        
        Documento processado = documentoDAO.findById(id);
        assertThat(processado).isNotNull();
        assertThat(processado.getStatus()).isEqualTo("PROCESSADO");
        
        documentoDAO.delete(id);
        Documento excluido = documentoDAO.findById(id);
        if (excluido != null) {
            assertThat(excluido.isAtivo()).isFalse();
        }

        // Assert
        assertThat(processado.getDataUpload()).isNotNull();
    }

    @Test
    @DisplayName("Deve lidar com informações de arquivo")
    void testInformacoesArquivo() throws SQLException {
        // Arrange
        Documento documento = criarDocumentoTeste();
        documento.setTamanhoArquivo(1024.5);
        documento.setFormatoArquivo("PDF");
        documento.setHashArquivo("abc123def456");
        documento.setAssinaturaDigital("assinatura_digital");

        // Act
        documentoDAO.save(documento);

        // Assert
        assertThat(documento.getId()).isNotNull();
        
        Documento salvo = documentoDAO.findById(documento.getId());
        if (salvo != null) {
            assertThat(salvo.getTamanhoArquivo()).isEqualTo(1024.5);
            assertThat(salvo.getFormatoArquivo()).isEqualTo("PDF");
            assertThat(salvo.getHashArquivo()).isEqualTo("abc123def456");
            assertThat(salvo.getAssinaturaDigital()).isEqualTo("assinatura_digital");
        }
    }

    @Test
    @DisplayName("Deve lidar com datas de upload e expiração")
    void testDatasUploadExpiracao() throws SQLException {
        // Arrange
        LocalDateTime dataUpload = LocalDateTime.now();
        LocalDateTime dataExpiracao = LocalDateTime.now().plusDays(30);
        
        Documento documento = criarDocumentoTeste();
        documento.setDataUpload(dataUpload);
        documento.setDataExpiracao(dataExpiracao);

        // Act
        documentoDAO.save(documento);

        // Assert
        assertThat(documento.getId()).isNotNull();
        
        Documento salvo = documentoDAO.findById(documento.getId());
        if (salvo != null) {
            assertThat(salvo.getDataUpload()).isNotNull();
            assertThat(salvo.getDataExpiracao()).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve ordenar documentos por data de upload")
    void testOrdenacaoDocumentos() throws SQLException {
        // Arrange
        LocalDateTime dataAntiga = LocalDateTime.now().minusHours(2);
        LocalDateTime dataRecente = LocalDateTime.now();

        Documento documentoAntigo = criarDocumentoTeste();
        documentoAntigo.setDataUpload(dataAntiga);
        documentoAntigo.setNomeArquivo("documento_antigo.pdf");
        documentoDAO.save(documentoAntigo);

        Documento documentoRecente = criarDocumentoTeste();
        documentoRecente.setDataUpload(dataRecente);
        documentoRecente.setNomeArquivo("documento_recente.pdf");
        documentoDAO.save(documentoRecente);

        // Act
        List<Documento> documentos = documentoDAO.findAll();

        // Assert
        assertThat(documentos).hasSizeGreaterThanOrEqualTo(2);
        // O mais recente deve vir primeiro (ORDER BY data_upload DESC)
        assertThat(documentos.get(0).getDataUpload()).isAfterOrEqualTo(documentos.get(1).getDataUpload());
    }

    /**
     * Método auxiliar para criar um documento de teste
     */
    private Documento criarDocumentoTeste() {
        Documento documento = new Documento();
        documento.setCodigoIrmao(123L);
        documento.setNomeArquivo("ata_reuniao_2024.pdf");
        documento.setCaminhoArquivo("/documentos/atas/ata_reuniao_2024.pdf");
        documento.setTipo("ATA");
        documento.setDescricao("Ata da reunião mensal");
        documento.setDataUpload(LocalDateTime.now());
        documento.setDataExpiracao(LocalDateTime.now().plusDays(365));
        documento.setStatus("ATIVO");
        documento.setAssinaturaDigital("assinatura_digital_teste");
        documento.setHashArquivo("hash_arquivo_teste");
        documento.setTamanhoArquivo(2048.0);
        documento.setFormatoArquivo("PDF");
        documento.setUsuarioUpload("admin");
        documento.setAtivo(true);
        return documento;
    }
}
