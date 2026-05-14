package com.artereal.swing.integration;

import com.artereal.swing.application.loja.CriarLojaUseCase;
import com.artereal.swing.application.loja.CriarLojaRequest;
import com.artereal.swing.application.loja.LojaResponse;
import com.artereal.swing.domain.loja.Loja;
import com.artereal.swing.domain.loja.LojaRepository;
import com.artereal.swing.domain.loja.StatusLoja;
import com.artereal.swing.infrastructure.persistence.LojaJpaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.assertj.core.api.Assertions.*;
import com.artereal.swing.infrastructure.config.ConfiguracaoBanco.*;

import java.util.List;
import javax.sql.*;
import com.artereal.swing.domain.loja.LojaException;

/**
 * Testes de integração para Lojas
 * 
 * Esta classe contém testes de integração para validar o funcionamento
 * completo da arquitetura hexagonal com DDD.
 */
class LojaIntegrationTest {
    
    private CriarLojaUseCase criarLojaUseCase;
    private LojaRepository lojaRepository;
    
    @BeforeEach
    void setUp() {
        // Limpar repositório antes de cada teste
        lojaRepository = new LojaJpaRepository(createTestDataSource());
        criarLojaUseCase = new CriarLojaUseCase(lojaRepository);
    }
    
    @AfterEach
    void tearDown() {
        // Limpar repositório após cada teste
        if (lojaRepository instanceof LojaJpaRepository) {
            // Limpar dados de teste
            lojaRepository.findAll().forEach(loja -> {
                try {
                    lojaRepository.delete(loja.getId());
                } catch (Exception e) {
                    // Ignorar erros de limpeza
                }
            });
        }
    }
    
    @Test
    @DisplayName("Deve criar e recuperar loja com sucesso")
    void deveCriarERecuperarLojaComSucesso() {
        // Arrange
        CriarLojaRequest request = new CriarLojaRequest(
            "Loja Integração Teste",
            "98.765.432/0001-91",
            "Rua Integração, 456",
            "São Paulo",
            "SP"
        );
        
        // Act
        LojaResponse response = criarLojaUseCase.execute(request);
        
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.nome()).isEqualTo("Loja Integração Teste");
        assertThat(response.cnpj()).isEqualTo("98.765.432/0001-91");
        assertThat(response.endereco()).isEqualTo("Rua Integração, 456");
        assertThat(response.cidade()).isEqualTo("São Paulo");
        assertThat(response.estado()).isEqualTo("SP");
        assertThat(response.status()).isEqualTo("ATIVA");
        assertThat(response.isAtiva()).isTrue();
        
        // Verificar persistência
        Loja lojaRecuperada = lojaRepository.findById(response.id()).orElse(null);
        assertThat(lojaRecuperada).isNotNull();
        assertThat(lojaRecuperada.getNome()).isEqualTo("Loja Integração Teste");
        assertThat(lojaRecuperada.getStatus()).isEqualTo(StatusLoja.ATIVA);
    }
    
    @Test
    @DisplayName("Deve listar todas as lojas")
    void deveListarTodasAsLojas() {
        // Arrange
        Loja loja1 = criarLojaTeste("Loja 1");
        Loja loja2 = criarLojaTeste("Loja 2");
        Loja loja3 = criarLojaTeste("Loja 3");
        
        lojaRepository.save(loja1);
        lojaRepository.save(loja2);
        lojaRepository.save(loja3);
        
        // Act
        List<Loja> lojas = lojaRepository.findAll();
        
        // Assert
        assertThat(lojas).hasSize(3);
        assertThat(lojas).extracting("nome").containsExactly("Loja 1", "Loja 2", "Loja 3");
    }
    
    @Test
    @DisplayName("Deve buscar lojas por status")
    void deveBuscarLojasPorStatus() {
        // Arrange
        Loja lojaAtiva = criarLojaTeste("Loja Ativa");
        Loja lojaInativa = criarLojaTeste("Loja Inativa");
        
        lojaAtiva.inativar();
        lojaRepository.save(lojaAtiva); // Será salva como inativa
        lojaRepository.save(lojaInativa);
        
        // Act
        List<Loja> lojasAtivas = lojaRepository.findByStatus(StatusLoja.ATIVA);
        List<Loja> lojasInativas = lojaRepository.findByStatus(StatusLoja.INATIVA);
        
        // Assert
        assertThat(lojasAtivas).hasSize(1);
        assertThat(lojasInativas).hasSize(2);
        assertThat(lojasAtivas.get(0).getNome()).isEqualTo("Loja Inativa");
    }
    
    @Test
    @DisplayName("Deve buscar lojas por cidade")
    void deveBuscarLojasPorCidade() {
        // Arrange
        Loja loja1 = Loja.criar("Loja São Paulo", "12.345.678/0001-95", "Rua Teste, 123", "São Paulo", "SP");
        Loja loja2 = Loja.criar("Loja Rio", "12.345.678/0002-96", "Rua Teste, 456", "Rio de Janeiro", "RJ");
        
        lojaRepository.save(loja1);
        lojaRepository.save(loja2);
        
        // Act
        List<Loja> lojasSP = lojaRepository.findByCidade("São Paulo");
        List<Loja> lojasRio = lojaRepository.findByCidade("Rio");
        
        // Assert
        assertThat(lojasSP).hasSize(1);
        assertThat(lojasRio).hasSize(1);
        assertThat(lojasSP.get(0).getNome()).isEqualTo("Loja São Paulo");
    }
    
    @Test
    @DisplayName("Deve verificar duplicidade de CNPJ")
    void deveVerificarDuplicidadeDeCnpj() {
        // Arrange
        Loja loja = criarLojaTeste("Loja CNPJ");
        lojaRepository.save(loja);
        
        // Act & Assert
        assertThat(lojaRepository.existsByCnpj("12.345.678/0001-95")).isTrue();
        
        // Tentar criar outra loja com mesmo CNPJ
        CriarLojaRequest requestDuplicado = new CriarLojaRequest(
            "Loja Duplicada",
            "12.345.678/0001-95",
            "Rua Duplicada, 123",
            "São Paulo",
            "SP"
        );
        
        assertThatThrownBy(() -> criarLojaUseCase.execute(requestDuplicado))
            .isInstanceOf(LojaException.class)
            .hasMessage("Já existe uma loja com o CNPJ informado");
    }
    
    @Test
    @DisplayName("Deve contar lojas corretamente")
    void deveContarLojasCorretamente() {
        // Arrange
        Loja loja1 = criarLojaTeste("Loja 1");
        Loja loja2 = criarLojaTeste("Loja 2");
        Loja loja3 = criarLojaTeste("Loja 3");
        
        lojaRepository.save(loja1);
        lojaRepository.save(loja2);
        lojaRepository.save(loja3);
        
        loja1.inativar();
        
        // Act
        long totalLojas = lojaRepository.count();
        long lojasAtivas = lojaRepository.countByStatus(StatusLoja.ATIVA);
        long lojasInativas = lojaRepository.countByStatus(StatusLoja.INATIVA);
        
        // Assert
        assertThat(totalLojas).isEqualTo(3);
        assertThat(lojasAtivas).isEqualTo(2);
        assertThat(lojasInativas).isEqualTo(1);
    }
    
    @Test
    @DisplayName("Deve testar paginação de lojas")
    void deveTestarPaginacaoDeLojas() {
        // Arrange
        for (int i = 0; i < 25; i++) {
            Loja loja = criarLojaTeste("Loja " + i);
            lojaRepository.save(loja);
        }
        
        // Act
        List<Loja> pagina1 = lojaRepository.findAllWithPagination(0, 10);
        List<Loja> pagina2 = lojaRepository.findAllWithPagination(1, 10);
        List<Loja> pagina3 = lojaRepository.findAllWithPagination(2, 10);
        
        // Assert
        assertThat(pagina1).hasSize(10);
        assertThat(pagina2).hasSize(10);
        assertThat(pagina3).hasSize(5);
        
        // Verificar se não há duplicatas entre páginas
        java.util.Set<Long> ids = new java.util.HashSet<>();
        pagina1.forEach(l -> ids.add(l.getId()));
        pagina2.forEach(l -> ids.add(l.getId()));
        pagina3.forEach(l -> ids.add(l.getId()));
        
        assertThat(ids).hasSize(25);
    }
    
    @Test
    @DisplayName("Deve buscar lojas por nome")
    void deveBuscarLojasPorNome() {
        // Arrange
        Loja loja1 = criarLojaTeste("Loja Especial Alpha");
        Loja loja2 = criarLojaTeste("Loja Especial Beta");
        Loja loja3 = criarLojaTeste("Loja Comum");
        
        lojaRepository.save(loja1);
        lojaRepository.save(loja2);
        lojaRepository.save(loja3);
        
        // Act
        List<Loja> lojasEspeciais = lojaRepository.findByNomeContaining("Especial");
        List<Loja> todasLojas = lojaRepository.findAll();
        
        // Assert
        assertThat(lojasEspeciais).hasSize(2);
        assertThat(todasLojas).hasSize(3);
    }
    
    /**
     * Cria uma loja para teste usando factory method
     */
    private Loja criarLojaTeste(String nome) {
        return Loja.criar(
            nome,
            "12.345.678/0001-9" + System.currentTimeMillis() % 100,
            "Rua Teste, " + System.currentTimeMillis() % 1000,
            "Cidade Teste",
            "SP"
        );
    }
    
    /**
     * Cria um DataSource para teste
     */
    private DataSource createTestDataSource() {
        return new SimpleDataSource(
            "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
            "sa",
            "",
            "org.h2.Driver"
        );
    }
}
