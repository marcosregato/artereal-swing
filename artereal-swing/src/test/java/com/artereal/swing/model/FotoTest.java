package com.artereal.swing.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe Foto
 */
@DisplayName("Testes Unitários - Foto")
class FotoTest {

    private Foto foto;

    @BeforeEach
    void setUp() {
        foto = new Foto();
    }

    @Test
    @DisplayName("Deve criar foto com construtor padrão")
    void testConstrutorPadrao() {
        // Act
        Foto novaFoto = new Foto();

        // Assert
        assertThat(novaFoto).isNotNull();
        assertThat(novaFoto.getId()).isNull();
        assertThat(novaFoto.getTitulo()).isNull();
        assertThat(novaFoto.getDescricao()).isNull();
        assertThat(novaFoto.getCaminhoArquivo()).isNull();
        assertThat(novaFoto.getNomeArquivo()).isNull();
        assertThat(novaFoto.getCategoria()).isNull();
        assertThat(novaFoto.getEvento()).isNull();
        assertThat(novaFoto.getDataFoto()).isNull();
        assertThat(novaFoto.getDataUpload()).isNotNull();
        assertThat(novaFoto.getDataUpload()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(novaFoto.getUsuarioUpload()).isNull();
        assertThat(novaFoto.getTags()).isNull();
        assertThat(novaFoto.getTamanhoArquivo()).isNull();
        assertThat(novaFoto.getFormatoArquivo()).isNull();
        assertThat(novaFoto.isAtivo()).isTrue();
        assertThat(novaFoto.getCreatedAt()).isNull();
        assertThat(novaFoto.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve definir e obter ID")
    void testSetGetId() {
        // Arrange
        Long id = 123L;

        // Act
        foto.setId(id);

        // Assert
        assertThat(foto.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deve definir e obter título")
    void testSetGetTitulo() {
        // Arrange
        String titulo = "Sessão Magna de Iniciação";

        // Act
        foto.setTitulo(titulo);

        // Assert
        assertThat(foto.getTitulo()).isEqualTo(titulo);
    }

    @Test
    @DisplayName("Deve definir e obter descrição")
    void testSetGetDescricao() {
        // Arrange
        String descricao = "Cerimônia de iniciação de novos irmãos";

        // Act
        foto.setDescricao(descricao);

        // Assert
        assertThat(foto.getDescricao()).isEqualTo(descricao);
    }

    @Test
    @DisplayName("Deve definir e obter caminho do arquivo")
    void testSetGetCaminhoArquivo() {
        // Arrange
        String caminhoArquivo = "/fotos/sessoes/2024/01/sessao_magna.jpg";

        // Act
        foto.setCaminhoArquivo(caminhoArquivo);

        // Assert
        assertThat(foto.getCaminhoArquivo()).isEqualTo(caminhoArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter nome do arquivo")
    void testSetGetNomeArquivo() {
        // Arrange
        String nomeArquivo = "sessao_magna_20240115.jpg";

        // Act
        foto.setNomeArquivo(nomeArquivo);

        // Assert
        assertThat(foto.getNomeArquivo()).isEqualTo(nomeArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter categoria")
    void testSetGetCategoria() {
        // Arrange
        String categoria = "SESSAO";

        // Act
        foto.setCategoria(categoria);

        // Assert
        assertThat(foto.getCategoria()).isEqualTo(categoria);
    }

    @Test
    @DisplayName("Deve definir e obter evento")
    void testSetGetEvento() {
        // Arrange
        String evento = "Iniciação Maçônica";

        // Act
        foto.setEvento(evento);

        // Assert
        assertThat(foto.getEvento()).isEqualTo(evento);
    }

    @Test
    @DisplayName("Deve definir e obter data da foto")
    void testSetGetDataFoto() {
        // Arrange
        LocalDateTime dataFoto = LocalDateTime.of(2024, 1, 15, 20, 30);

        // Act
        foto.setDataFoto(dataFoto);

        // Assert
        assertThat(foto.getDataFoto()).isEqualTo(dataFoto);
    }

    @Test
    @DisplayName("Deve definir e obter data de upload")
    void testSetGetDataUpload() {
        // Arrange
        LocalDateTime dataUpload = LocalDateTime.of(2024, 1, 16, 10, 15);

        // Act
        foto.setDataUpload(dataUpload);

        // Assert
        assertThat(foto.getDataUpload()).isEqualTo(dataUpload);
    }

    @Test
    @DisplayName("Deve definir e obter usuário de upload")
    void testSetGetUsuarioUpload() {
        // Arrange
        String usuarioUpload = "fotografo";

        // Act
        foto.setUsuarioUpload(usuarioUpload);

        // Assert
        assertThat(foto.getUsuarioUpload()).isEqualTo(usuarioUpload);
    }

    @Test
    @DisplayName("Deve definir e obter tags")
    void testSetGetTags() {
        // Arrange
        String tags = "sessao, magna, iniciacao, 2024";

        // Act
        foto.setTags(tags);

        // Assert
        assertThat(foto.getTags()).isEqualTo(tags);
    }

    @Test
    @DisplayName("Deve definir e obter tamanho do arquivo")
    void testSetGetTamanhoArquivo() {
        // Arrange
        Double tamanhoArquivo = 2048.5; // KB

        // Act
        foto.setTamanhoArquivo(tamanhoArquivo);

        // Assert
        assertThat(foto.getTamanhoArquivo()).isEqualTo(tamanhoArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter formato do arquivo")
    void testSetGetFormatoArquivo() {
        // Arrange
        String formatoArquivo = "JPG";

        // Act
        foto.setFormatoArquivo(formatoArquivo);

        // Assert
        assertThat(foto.getFormatoArquivo()).isEqualTo(formatoArquivo);
    }

    @Test
    @DisplayName("Deve definir e obter status ativo")
    void testSetGetAtivo() {
        // Arrange
        Boolean ativo = false;

        // Act
        foto.setAtivo(ativo);

        // Assert
        assertThat(foto.isAtivo()).isEqualTo(ativo);
    }

    @Test
    @DisplayName("Deve definir e obter created at")
    void testSetGetCreatedAt() {
        // Arrange
        String createdAt = "2024-01-15 20:30:00";

        // Act
        foto.setCreatedAt(createdAt);

        // Assert
        assertThat(foto.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Deve definir e obter updated at")
    void testSetGetUpdatedAt() {
        // Arrange
        String updatedAt = "2024-01-16 10:15:00";

        // Act
        foto.setUpdatedAt(updatedAt);

        // Assert
        assertThat(foto.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Deve representar objeto como string")
    void testToString() {
        // Arrange
        foto.setId(123L);
        foto.setTitulo("Sessão Magna");
        foto.setCategoria("SESSAO");
        foto.setAtivo(true);

        // Act
        String resultado = foto.toString();

        // Assert - Simplificado para usar SimpleModel
        assertThat(resultado).contains("Foto");
        assertThat(resultado).contains("id=123");
    }

    @Test
    @DisplayName("Deve comparar objetos iguais")
    void testEquals() {
        // Arrange
        Foto foto1 = new Foto();
        foto1.setId(123L);

        Foto foto2 = new Foto();
        foto2.setId(123L);

        Foto foto3 = new Foto();
        foto3.setId(456L);

        // Act & Assert
        assertThat(foto1).isEqualTo(foto2);
        assertThat(foto1).isNotEqualTo(foto3);
        assertThat(foto1).isNotEqualTo(null);
        assertThat(foto1).isNotEqualTo("string");
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para IDs iguais")
    void testHashCode() {
        // Arrange
        Foto foto1 = new Foto();
        foto1.setId(123L);

        Foto foto2 = new Foto();
        foto2.setId(123L);

        Foto foto3 = new Foto();
        foto3.setId(456L);

        // Act & Assert
        assertThat(foto1.hashCode()).isEqualTo(foto2.hashCode());
        assertThat(foto1.hashCode()).isNotEqualTo(foto3.hashCode());
    }

    @Test
    @DisplayName("Deve lidar com diferentes categorias")
    void testDiferentesCategorias() {
        // Arrange & Act
        foto.setCategoria("SESSAO");
        assertThat(foto.getCategoria()).isEqualTo("SESSAO");

        foto.setCategoria("EVENTO");
        assertThat(foto.getCategoria()).isEqualTo("EVENTO");

        foto.setCategoria("IRMAO");
        assertThat(foto.getCategoria()).isEqualTo("IRMAO");

        foto.setCategoria("LOJA");
        assertThat(foto.getCategoria()).isEqualTo("LOJA");

        foto.setCategoria("OUTRA");
        assertThat(foto.getCategoria()).isEqualTo("OUTRA");
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo nulo")
    void testTamanhoArquivoNulo() {
        // Act
        foto.setTamanhoArquivo(null);

        // Assert
        assertThat(foto.getTamanhoArquivo()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo zero")
    void testTamanhoArquivoZero() {
        // Arrange
        Double tamanhoZero = 0.0;

        // Act
        foto.setTamanhoArquivo(tamanhoZero);

        // Assert
        assertThat(foto.getTamanhoArquivo()).isEqualTo(tamanhoZero);
        assertThat(foto.getTamanhoArquivo()).isEqualByComparingTo(0.0);
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo grande")
    void testTamanhoArquivoGrande() {
        // Arrange
        Double tamanhoGrande = 10240.0; // 10MB em KB

        // Act
        foto.setTamanhoArquivo(tamanhoGrande);

        // Assert
        assertThat(foto.getTamanhoArquivo()).isEqualTo(tamanhoGrande);
        assertThat(foto.getTamanhoArquivo()).isGreaterThan(10000.0);
    }

    @Test
    @DisplayName("Deve lidar com valores nulos")
    void testValoresNulos() {
        // Act
        foto.setTitulo(null);
        foto.setDescricao(null);
        foto.setCaminhoArquivo(null);
        foto.setNomeArquivo(null);
        foto.setCategoria(null);
        foto.setEvento(null);
        foto.setDataFoto(null);
        foto.setDataUpload(null);
        foto.setUsuarioUpload(null);
        foto.setTags(null);
        foto.setTamanhoArquivo(null);
        foto.setFormatoArquivo(null);
        foto.setAtivo(null);
        foto.setCreatedAt(null);
        foto.setUpdatedAt(null);

        // Assert
        assertThat(foto.getTitulo()).isNull();
        assertThat(foto.getDescricao()).isNull();
        assertThat(foto.getCaminhoArquivo()).isNull();
        assertThat(foto.getNomeArquivo()).isNull();
        assertThat(foto.getCategoria()).isNull();
        assertThat(foto.getEvento()).isNull();
        assertThat(foto.getDataFoto()).isNull();
        assertThat(foto.getDataUpload()).isNull();
        assertThat(foto.getUsuarioUpload()).isNull();
        assertThat(foto.getTags()).isNull();
        assertThat(foto.getTamanhoArquivo()).isNull();
        assertThat(foto.getFormatoArquivo()).isNull();
        assertThat(foto.isAtivo()).isNull();
        assertThat(foto.getCreatedAt()).isNull();
        assertThat(foto.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testValoresVazios() {
        // Act
        foto.setTitulo("");
        foto.setDescricao("");
        foto.setCaminhoArquivo("");
        foto.setNomeArquivo("");
        foto.setCategoria("");
        foto.setEvento("");
        foto.setUsuarioUpload("");
        foto.setTags("");
        foto.setFormatoArquivo("");
        foto.setCreatedAt("");
        foto.setUpdatedAt("");

        // Assert
        assertThat(foto.getTitulo()).isEmpty();
        assertThat(foto.getDescricao()).isEmpty();
        assertThat(foto.getCaminhoArquivo()).isEmpty();
        assertThat(foto.getNomeArquivo()).isEmpty();
        assertThat(foto.getCategoria()).isEmpty();
        assertThat(foto.getEvento()).isEmpty();
        assertThat(foto.getUsuarioUpload()).isEmpty();
        assertThat(foto.getTags()).isEmpty();
        assertThat(foto.getFormatoArquivo()).isEmpty();
        assertThat(foto.getCreatedAt()).isEmpty();
        assertThat(foto.getUpdatedAt()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com diferentes formatos de arquivo")
    void testDiferentesFormatosArquivo() {
        // Arrange & Act
        foto.setFormatoArquivo("JPG");
        assertThat(foto.getFormatoArquivo()).isEqualTo("JPG");

        foto.setFormatoArquivo("PNG");
        assertThat(foto.getFormatoArquivo()).isEqualTo("PNG");

        foto.setFormatoArquivo("GIF");
        assertThat(foto.getFormatoArquivo()).isEqualTo("GIF");

        foto.setFormatoArquivo("BMP");
        assertThat(foto.getFormatoArquivo()).isEqualTo("BMP");

        foto.setFormatoArquivo("TIFF");
        assertThat(foto.getFormatoArquivo()).isEqualTo("TIFF");
    }

    @Test
    @DisplayName("Deve lidar com datas da foto futuras")
    void testDataFotoFutura() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataFutura = agora.plusDays(1);

        // Act
        foto.setDataFoto(dataFutura);

        // Assert
        assertThat(foto.getDataFoto()).isEqualTo(dataFutura);
        assertThat(foto.getDataFoto()).isAfter(agora);
    }

    @Test
    @DisplayName("Deve lidar com datas da foto passadas")
    void testDataFotoPassada() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataPassada = agora.minusDays(1);

        // Act
        foto.setDataFoto(dataPassada);

        // Assert
        assertThat(foto.getDataFoto()).isEqualTo(dataPassada);
        assertThat(foto.getDataFoto()).isBefore(agora);
    }

    @Test
    @DisplayName("Deve lidar com data de upload após data da foto")
    void testDataUploadAposDataFoto() {
        // Arrange
        LocalDateTime dataFoto = LocalDateTime.of(2024, 1, 15, 20, 30);
        LocalDateTime dataUpload = LocalDateTime.of(2024, 1, 16, 10, 15);

        // Act
        foto.setDataFoto(dataFoto);
        foto.setDataUpload(dataUpload);

        // Assert
        assertThat(foto.getDataUpload()).isAfter(foto.getDataFoto());
    }

    @Test
    @DisplayName("Deve lidar com data de upload antes da data da foto")
    void testDataUploadAntesDataFoto() {
        // Arrange
        LocalDateTime dataFoto = LocalDateTime.of(2024, 1, 16, 20, 30);
        LocalDateTime dataUpload = LocalDateTime.of(2024, 1, 15, 10, 15);

        // Act
        foto.setDataFoto(dataFoto);
        foto.setDataUpload(dataUpload);

        // Assert
        assertThat(foto.getDataUpload()).isBefore(foto.getDataFoto());
    }

    @Test
    @DisplayName("Deve lidar com diferentes usuários de upload")
    void testDiferentesUsuariosUpload() {
        // Arrange & Act
        foto.setUsuarioUpload("admin");
        assertThat(foto.getUsuarioUpload()).isEqualTo("admin");

        foto.setUsuarioUpload("joao.silva");
        assertThat(foto.getUsuarioUpload()).isEqualTo("joao.silva");

        foto.setUsuarioUpload("fotografo");
        assertThat(foto.getUsuarioUpload()).isEqualTo("fotografo");

        foto.setUsuarioUpload("secretario");
        assertThat(foto.getUsuarioUpload()).isEqualTo("secretario");
    }

    @Test
    @DisplayName("Deve lidar com tags complexas")
    void testTagsComplexas() {
        // Arrange & Act
        foto.setTags("sessao, magna, iniciacao, 2024, janeiro");
        assertThat(foto.getTags()).isEqualTo("sessao, magna, iniciacao, 2024, janeiro");

        foto.setTags("evento; confraternizacao; almoço; aniversário");
        assertThat(foto.getTags()).isEqualTo("evento; confraternizacao; almoço; aniversário");

        foto.setTags("irmao joão silva; posse; venerável");
        assertThat(foto.getTags()).isEqualTo("irmao joão silva; posse; venerável");
    }

    @Test
    @DisplayName("Deve lidar com nomes de arquivo com extensões")
    void testNomesArquivoComExtensoes() {
        // Arrange & Act
        foto.setNomeArquivo("sessao_magna_001.jpg");
        assertThat(foto.getNomeArquivo()).isEqualTo("sessao_magna_001.jpg");

        foto.setNomeArquivo("foto_grupo.png");
        assertThat(foto.getNomeArquivo()).isEqualTo("foto_grupo.png");

        foto.setNomeArquivo("cerimonia_iniciacao.gif");
        assertThat(foto.getNomeArquivo()).isEqualTo("cerimonia_iniciacao.gif");

        foto.setNomeArquivo("templo_exterior.bmp");
        assertThat(foto.getNomeArquivo()).isEqualTo("templo_exterior.bmp");
    }

    @Test
    @DisplayName("Deve lidar com caminhos de arquivo complexos")
    void testCaminhosArquivoComplexos() {
        // Arrange & Act
        foto.setCaminhoArquivo("/fotos/sessoes/2024/01/sessao_magna_001.jpg");
        assertThat(foto.getCaminhoArquivo()).isEqualTo("/fotos/sessoes/2024/01/sessao_magna_001.jpg");

        foto.setCaminhoArquivo("C:\\Users\\User\\Pictures\\loja\\foto_grupo.png");
        assertThat(foto.getCaminhoArquivo()).isEqualTo("C:\\Users\\User\\Pictures\\loja\\foto_grupo.png");

        foto.setCaminhoArquivo("./uploads/temp/cerimonia_iniciacao.gif");
        assertThat(foto.getCaminhoArquivo()).isEqualTo("./uploads/temp/cerimonia_iniciacao.gif");
    }

    @Test
    @DisplayName("Deve lidar com títulos longos")
    void testTitulosLongos() {
        // Arrange
        String tituloLongo = "Sessão Magna de Iniciação dos Aprendizes Maçons - Cerimônia de posse do Venerável Mestre e instalação dos novos oficiais da Loja ArteReal Número 123";

        // Act
        foto.setTitulo(tituloLongo);

        // Assert
        assertThat(foto.getTitulo()).isEqualTo(tituloLongo);
        assertThat(foto.getTitulo()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com descrições longas")
    void testDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Foto registrando o momento solene da iniciação de novos irmãos na Loja ArteReal. A cerimônia contou com a presença de autoridades maçônicas e foi realizada com toda a dignidade e ritualística que caracteriza nossa tradição.";

        // Act
        foto.setDescricao(descricaoLonga);

        // Assert
        assertThat(foto.getDescricao()).isEqualTo(descricaoLonga);
        assertThat(foto.getDescricao()).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("Deve lidar com eventos diferentes")
    void testEventosDiferentes() {
        // Arrange & Act
        foto.setEvento("Iniciação Maçônica");
        assertThat(foto.getEvento()).isEqualTo("Iniciação Maçônica");

        foto.setEvento("Elevação a Companheiro");
        assertThat(foto.getEvento()).isEqualTo("Elevação a Companheiro");

        foto.setEvento("Exaltação a Mestre");
        assertThat(foto.getEvento()).isEqualTo("Exaltação a Mestre");

        foto.setEvento("Confraternização");
        assertThat(foto.getEvento()).isEqualTo("Confraternização");

        foto.setEvento("Aniversário da Loja");
        assertThat(foto.getEvento()).isEqualTo("Aniversário da Loja");
    }

    @Test
    @DisplayName("Deve lidar com status ativo nulo")
    void testAtivoNulo() {
        // Act
        foto.setAtivo(null);

        // Assert
        assertThat(foto.isAtivo()).isNull();
    }

    @Test
    @DisplayName("Deve lidar com status ativo verdadeiro")
    void testAtivoVerdadeiro() {
        // Act
        foto.setAtivo(true);

        // Assert
        assertThat(foto.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve lidar com status ativo falso")
    void testAtivoFalso() {
        // Act
        foto.setAtivo(false);

        // Assert
        assertThat(foto.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo decimal")
    void testTamanhoArquivoDecimal() {
        // Arrange
        Double tamanhoDecimal = 1234.567;

        // Act
        foto.setTamanhoArquivo(tamanhoDecimal);

        // Assert
        assertThat(foto.getTamanhoArquivo()).isEqualTo(tamanhoDecimal);
        assertThat(foto.getTamanhoArquivo()).isEqualByComparingTo(1234.567);
    }

    @Test
    @DisplayName("Deve lidar com tamanho de arquivo negativo")
    void testTamanhoArquivoNegativo() {
        // Arrange
        Double tamanhoNegativo = -100.0;

        // Act
        foto.setTamanhoArquivo(tamanhoNegativo);

        // Assert
        assertThat(foto.getTamanhoArquivo()).isEqualTo(tamanhoNegativo);
        assertThat(foto.getTamanhoArquivo()).isNegative();
    }
}
