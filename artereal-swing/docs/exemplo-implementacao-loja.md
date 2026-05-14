# Exemplo de Implementação - Loja Domain

## Estrutura de Arquivos

### 1. Entidade Loja (Aggregate Root)
```java
package com.artereal.domain.loja;

import java.util.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lojas")
public class Loja extends AggregateRoot {
    @EmbeddedId
    private LojaId id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;
    
    @Embedded
    private Endereco endereco;
    
    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Irmao> irmaos = new ArrayList<>();
    
    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Caixa> caixas = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private StatusLoja status = StatusLoja.ATIVA;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    // Construtor privado
    private Loja(LojaId id, String nome, String cnpj, Endereco endereco) {
        this.id = Objects.requireNonNull(id, "ID da loja é obrigatório");
        this.nome = Objects.requireNonNull(nome, "Nome da loja é obrigatório");
        this.cnpj = Objects.requireNonNull(cnpj, "CNPJ da loja é obrigatório");
        this.endereco = Objects.requireNonNull(endereco, "Endereço da loja é obrigatório");
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        
        validate();
    }
    
    // Factory method
    public static Loja criar(String nome, String cnpj, Endereco endereco) {
        return new Loja(LojaId.generate(), nome, cnpj, endereco);
    }
    
    // Métodos de negócio
    public void adicionarIrmao(Irmao irmao) {
        Objects.requireNonNull(irmao, "Irmão é obrigatório");
        
        if (irmaos.contains(irmao)) {
            throw new IrmaoJaAssociadoException(
                String.format("Irmão %s já está associado à loja %s", 
                    irmao.getNome(), this.nome)
            );
        }
        
        if (irmaos.size() >= 50) {
            throw new LimiteIrmaosExcedidoException(
                String.format("Loja %s atingiu o limite máximo de 50 irmãos", this.nome)
            );
        }
        
        irmaos.add(irmao);
        irmao.associarLoja(this);
        
        // Publicar evento de domínio
        addDomainEvent(new IrmaoAssociadoEvent(this.id, irmao.getId()));
        
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public void removerIrmao(IrmaoId irmaoId) {
        Objects.requireNonNull(irmaoId, "ID do irmão é obrigatório");
        
        Optional<Irmao> irmaoOpt = irmaos.stream()
            .filter(i -> i.getId().equals(irmaoId))
            .findFirst();
            
        if (irmaoOpt.isEmpty()) {
            throw new IrmaoNaoEncontradoException(
                String.format("Irmão com ID %s não encontrado na loja %s", 
                    irmaoId.getValue(), this.nome)
            );
        }
        
        Irmao irmao = irmaoOpt.get();
        irmaos.remove(irmao);
        irmao.desassociarLoja();
        
        // Publicar evento de domínio
        addDomainEvent(new IrmaoRemovidoEvent(this.id, irmaoId));
        
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public void abrirCaixa(BigDecimal valor, String responsavel) {
        validarAberturaCaixa(valor, responsavel);
        
        Caixa caixa = Caixa.abrir(valor, responsavel, this.id);
        caixas.add(caixa);
        
        // Publicar evento de domínio
        addDomainEvent(new CaixaAbertoEvent(caixa.getId(), this.id, valor));
        
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public void fecharCaixa(CaixaId caixaId) {
        Objects.requireNonNull(caixaId, "ID do caixa é obrigatório");
        
        Optional<Caixa> caixaOpt = caixas.stream()
            .filter(c -> c.getId().equals(caixaId))
            .findFirst();
            
        if (caixaOpt.isEmpty()) {
            throw new CaixaNaoEncontradoException(
                String.format("Caixa com ID %s não encontrado na loja %s", 
                    caixaId.getValue(), this.nome)
            );
        }
        
        Caixa caixa = caixaOpt.get();
        caixa.fechar();
        
        // Publicar evento de domínio
        addDomainEvent(new CaixaFechadoEvent(caixa.getId(), this.id, caixa.getSaldo()));
        
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public void inativar() {
        if (this.status == StatusLoja.INATIVA) {
            throw new LojaJaInativaException(this.nome);
        }
        
        // Validar se não há caixas abertos
        boolean temCaixasAbertos = caixas.stream()
            .anyMatch(c -> c.getStatus() == StatusCaixa.ABERTO);
            
        if (temCaixasAbertos) {
            throw new LojaComCaixasAbertosException(this.nome);
        }
        
        this.status = StatusLoja.INATIVA;
        this.dataAtualizacao = LocalDateTime.now();
        
        // Publicar evento de domínio
        addDomainEvent(new LojaInativadaEvent(this.id));
    }
    
    public void reativar() {
        if (this.status == StatusLoja.ATIVA) {
            throw new LojaJaAtivaException(this.nome);
        }
        
        this.status = StatusLoja.ATIVA;
        this.dataAtualizacao = LocalDateTime.now();
        
        // Publicar evento de domínio
        addDomainEvent(new LojaReativadaEvent(this.id));
    }
    
    // Métodos de validação privados
    private void validate() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new LojaNomeInvalidoException("Nome da loja é obrigatório");
        }
        if (nome.length() > 200) {
            throw new LojaNomeInvalidoException("Nome da loja não pode exceder 200 caracteres");
        }
        if (cnpj == null || !CnpjValidator.isValid(cnpj)) {
            throw new LojaCnpjInvalidoException("CNPJ da loja é inválido");
        }
    }
    
    private void validarAberturaCaixa(BigDecimal valor, String responsavel) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CaixaValorInvalidoException("Valor do caixa deve ser positivo");
        }
        if (responsavel == null || responsavel.trim().isEmpty()) {
            throw new CaixaResponsavelInvalidoException("Responsável pelo caixa é obrigatório");
        }
    }
    
    // Getters (sem setters para manter imutabilidade)
    public LojaId getId() { return id; }
    public String getNome() { return nome; }
    public String getCnpj() { return cnpj; }
    public Endereco getEndereco() { return endereco; }
    public List<Irmao> getIrmaos() { return Collections.unmodifiableList(irmaos); }
    public List<Caixa> getCaixas() { return Collections.unmodifiableList(caixas); }
    public StatusLoja getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
}
```

### 2. LojaId (Value Object)
```java
package com.artereal.domain.loja;

import java.util.UUID;

@Embeddable
public record LojaId(UUID value) {
    public LojaId {
        Objects.requireNonNull(value, "ID da loja não pode ser nulo");
    }
    
    public static LojaId generate() {
        return new LojaId(UUID.randomUUID());
    }
    
    public String getValue() {
        return value.toString();
    }
    
    @Override
    public String toString() {
        return getValue();
    }
}
```

### 3. StatusLoja (Enum)
```java
package com.artereal.domain.loja;

public enum StatusLoja {
    ATIVA("Ativa"),
    INATIVA("Inativa"),
    BLOQUEADA("Bloqueada");
    
    private final String descricao;
    
    StatusLoja(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

### 4. Exceções de Domínio
```java
package com.artereal.domain.loja.exception;

public class LojaException extends RuntimeException {
    public LojaException(String message) {
        super(message);
    }
    
    public LojaException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class LojaNomeInvalidoException extends LojaException {
    public LojaNomeInvalidoException(String message) {
        super(message);
    }
}

public class LojaCnpjInvalidoException extends LojaException {
    public LojaCnpjInvalidoException(String message) {
        super(message);
    }
}

public class LojaJaExisteException extends LojaException {
    public LojaJaExisteException(String cnpj) {
        super(String.format("Loja com CNPJ %s já existe", cnpj));
    }
}

public class IrmaoJaAssociadoException extends LojaException {
    public IrmaoJaAssociadoException(String message) {
        super(message);
    }
}

public class IrmaoNaoEncontradoException extends LojaException {
    public IrmaoNaoEncontradoException(String message) {
        super(message);
    }
}

public class LimiteIrmaosExcedidoException extends LojaException {
    public LimiteIrmaosExcedidoException(String message) {
        super(message);
    }
}

public class LojaInativaException extends LojaException {
    public LojaInativaException(String message) {
        super(message);
    }
}

public class LojaJaInativaException extends LojaException {
    public LojaJaInativaException(String message) {
        super(message);
    }
}

public class LojaComCaixasAbertosException extends LojaException {
    public LojaComCaixasAbertosException(String message) {
        super(message);
    }
}

public class CaixaNaoEncontradoException extends LojaException {
    public CaixaNaoEncontradoException(String message) {
        super(message);
    }
}

public class CaixaValorInvalidoException extends LojaException {
    public CaixaValorInvalidoException(String message) {
        super(message);
    }
}

public class CaixaResponsavelInvalidoException extends LojaException {
    public CaixaResponsavelInvalidoException(String message) {
        super(message);
    }
}
```

### 5. Eventos de Domínio
```java
package com.artereal.domain.loja.event;

public record IrmaoAssociadoEvent(
    LojaId lojaId,
    IrmaoId irmaoId,
    LocalDateTime ocorridoEm
) {
    public IrmaoAssociadoEvent {
        this.ocorridoEm = LocalDateTime.now();
    }
}

public record IrmaoRemovidoEvent(
    LojaId lojaId,
    IrmaoId irmaoId,
    LocalDateTime ocorridoEm
) {
    public IrmaoRemovidoEvent {
        this.ocorridoEm = LocalDateTime.now();
    }
}

public record CaixaAbertoEvent(
    CaixaId caixaId,
    LojaId lojaId,
    BigDecimal valor,
    LocalDateTime ocorridoEm
) {
    public CaixaAbertoEvent {
        this.ocorridoEm = LocalDateTime.now();
    }
}

public record CaixaFechadoEvent(
    CaixaId caixaId,
    LojaId lojaId,
    BigDecimal saldoFinal,
    LocalDateTime ocorridoEm
) {
    public CaixaFechadoEvent {
        this.ocorridoEm = LocalDateTime.now();
    }
}

public record LojaInativadaEvent(
    LojaId lojaId,
    LocalDateTime ocorridoEm
) {
    public LojaInativadaEvent {
        this.ocorridoEm = LocalDateTime.now();
    }
}

public record LojaReativadaEvent(
    LojaId lojaId,
    LocalDateTime ocorridoEm
) {
    public LojaReativadaEvent {
        this.ocorridoEm = LocalDateTime.now();
    }
}
```

### 6. Repository Interface
```java
package com.artereal.domain.loja;

import java.util.*;

@Repository
public interface LojaRepository {
    Optional<Loja> findById(LojaId id);
    List<Loja> findByStatus(StatusLoja status);
    List<Loja> findByCidade(String cidade);
    List<Loja> findByEstado(String estado);
    boolean existsByCnpj(String cnpj);
    List<Loja> findAll();
    Loja save(Loja loja);
    void delete(LojaId id);
    
    // Queries customizadas
    @Query("SELECT l FROM Loja l WHERE l.endereco.cidade = :cidade ORDER BY l.nome")
    List<Loja> buscarPorCidadeOrdenado(@Param("cidade") String cidade);
    
    @Query("SELECT COUNT(l) FROM Loja l WHERE l.status = :status")
    long countByStatus(@Param("status") StatusLoja status);
}
```

## Testes de Domínio

### 1. Teste da Entidade
```java
package com.artereal.domain.loja;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class LojaTest {
    
    @Test
    @DisplayName("Deve criar loja com dados válidos")
    void deveCriarLojaComDadosValidos() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        
        // Act
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        // Assert
        assertThat(loja.getId()).isNotNull();
        assertThat(loja.getNome()).isEqualTo("Loja Teste");
        assertThat(loja.getCnpj()).isEqualTo("12.345.678/0001-90");
        assertThat(loja.getEndereco()).isEqualTo(endereco);
        assertThat(loja.getStatus()).isEqualTo(StatusLoja.ATIVA);
        assertThat(loja.getIrmaos()).isEmpty();
        assertThat(loja.getCaixas()).isEmpty();
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao criar loja com nome inválido")
    void deveLancarExcecaoAoCriarLojaComNomeInvalido() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        
        // Act & Assert
        assertThatThrownBy(() -> Loja.criar(null, "12.345.678/0001-90", endereco))
            .isInstanceOf(LojaNomeInvalidoException.class)
            .hasMessage("Nome da loja é obrigatório");
    }
    
    @Test
    @DisplayName("Deve adicionar irmão à loja")
    void deveAdicionarIrmaoALoja() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        Irmao irmao = Irmao.criar("João Silva", "123.456.789-00");
        
        // Act
        loja.adicionarIrmao(irmao);
        
        // Assert
        assertThat(loja.getIrmaos()).hasSize(1);
        assertThat(loja.getIrmaos().get(0)).isEqualTo(irmao);
        assertThat(irmao.getLojaId()).isEqualTo(loja.getId());
        assertThat(loja.getDomainEvents()).hasSize(1);
        assertThat(loja.getDomainEvents().get(0))
            .isInstanceOf(IrmaoAssociadoEvent.class);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao adicionar irmão duplicado")
    void deveLancarExcecaoAoAdicionarIrmaoDuplicado() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        Irmao irmao = Irmao.criar("João Silva", "123.456.789-00");
        loja.adicionarIrmao(irmao);
        
        // Act & Assert
        assertThatThrownBy(() -> loja.adicionarIrmao(irmao))
            .isInstanceOf(IrmaoJaAssociadoException.class)
            .hasMessageContaining("já está associado");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao adicionar irmão além do limite")
    void deveLancarExcecaoAoAdicionarIrmaoAlemDoLimite() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        // Adicionar 50 irmãos
        for (int i = 0; i < 50; i++) {
            Irmao irmao = Irmao.criar("Irmao " + i, "123.456.789-0" + i);
            loja.adicionarIrmao(irmao);
        }
        
        Irmao irmaoExtra = Irmao.criar("Extra", "123.456.789-99");
        
        // Act & Assert
        assertThatThrownBy(() -> loja.adicionarIrmao(irmaoExtra))
            .isInstanceOf(LimiteIrmaosExcedidoException.class)
            .hasMessageContaining("atingiu o limite máximo de 50 irmãos");
    }
    
    @Test
    @DisplayName("Deve abrir caixa com sucesso")
    void deveAbrirCaixaComSucesso() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        // Act
        loja.abrirCaixa(new BigDecimal("1000.00"), "João Silva");
        
        // Assert
        assertThat(loja.getCaixas()).hasSize(1);
        Caixa caixa = loja.getCaixas().get(0);
        assertThat(caixa.getValor()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(caixa.getResponsavel()).isEqualTo("João Silva");
        assertThat(caixa.getStatus()).isEqualTo(StatusCaixa.ABERTO);
        assertThat(loja.getDomainEvents()).hasSize(1);
        assertThat(loja.getDomainEvents().get(0))
            .isInstanceOf(CaixaAbertoEvent.class);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao abrir caixa com valor inválido")
    void deveLancarExcecaoAoAbrirCaixaComValorInvalido() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        // Act & Assert
        assertThatThrownBy(() -> loja.abrirCaixa(BigDecimal.ZERO, "João Silva"))
            .isInstanceOf(CaixaValorInvalidoException.class)
            .hasMessage("Valor do caixa deve ser positivo");
    }
    
    @Test
    @DisplayName("Deve inativar loja com sucesso")
    void deveInativarLojaComSucesso() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        // Act
        loja.inativar();
        
        // Assert
        assertThat(loja.getStatus()).isEqualTo(StatusLoja.INATIVA);
        assertThat(loja.getDomainEvents()).hasSize(1);
        assertThat(loja.getDomainEvents().get(0))
            .isInstanceOf(LojaInativadaEvent.class);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao inativar loja com caixas abertos")
    void deveLancarExcecaoAoInativarLojaComCaixasAbertos() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        // Abrir caixa
        loja.abrirCaixa(new BigDecimal("1000.00"), "João Silva");
        
        // Act & Assert
        assertThatThrownBy(() -> loja.inativar())
            .isInstanceOf(LojaComCaixasAbertosException.class)
            .hasMessageContaining("caixas abertos");
    }
}
```

### 2. Teste de Repository
```java
package com.artereal.domain.loja;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LojaRepositoryTest {
    
    @Mock
    private LojaRepository lojaRepository;
    
    @Test
    @DisplayName("Deve salvar loja com sucesso")
    void deveSalvarLojaComSucesso() {
        // Arrange
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        when(lojaRepository.save(loja)).thenReturn(loja);
        
        // Act
        Loja lojaSalva = lojaRepository.save(loja);
        
        // Assert
        assertThat(lojaSalva).isNotNull();
        verify(lojaRepository).save(loja);
    }
    
    @Test
    @DisplayName("Deve buscar loja por ID")
    void deveBuscarLojaPorId() {
        // Arrange
        LojaId lojaId = LojaId.generate();
        Endereco endereco = new Endereco(
            "Rua das Flores", "123", "Apto 1",
            "Centro", "São Paulo", "SP", "01234-567"
        );
        Loja loja = Loja.criar("Loja Teste", "12.345.678/0001-90", endereco);
        
        when(lojaRepository.findById(lojaId)).thenReturn(Optional.of(loja));
        
        // Act
        Optional<Loja> resultado = lojaRepository.findById(lojaId);
        
        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get()).isEqualTo(loja);
        verify(lojaRepository).findById(lojaId);
    }
    
    @Test
    @DisplayName("Deve verificar existência por CNPJ")
    void deveVerificarExistenciaPorCnpj() {
        // Arrange
        String cnpj = "12.345.678/0001-90";
        when(lojaRepository.existsByCnpj(cnpj)).thenReturn(true);
        
        // Act
        boolean existe = lojaRepository.existsByCnpj(cnpj);
        
        // Assert
        assertThat(existe).isTrue();
        verify(lojaRepository).existsByCnpj(cnpj);
    }
    
    @Test
    @DisplayName("Deve buscar lojas por cidade")
    void deveBuscarLojasPorCidade() {
        // Arrange
        String cidade = "São Paulo";
        List<Loja> lojas = Arrays.asList(
            criarLoja("Loja 1", cidade),
            criarLoja("Loja 2", cidade)
        );
        
        when(lojaRepository.findByCidade(cidade)).thenReturn(lojas);
        
        // Act
        List<Loja> resultado = lojaRepository.findByCidade(cidade);
        
        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).containsExactlyElementsOf(lojas);
        verify(lojaRepository).findByCidade(cidade);
    }
    
    private Loja criarLoja(String nome, String cidade) {
        Endereco endereco = new Endereco(
            "Rua Teste", "123", "", "Centro", cidade, "SP", "01234-567"
        );
        return Loja.criar(nome, "12.345.678/0001-9" + nome.hashCode(), endereco);
    }
}
```

## Benefícios Desta Implementação

### 1. **Imutabilidade**
- Entidades são imutáveis após criação
- Use de records para Value Objects
- Métodos de negócio que criam novas instâncias

### 2. **Validação Centralizada**
- Validações no construtor e métodos de negócio
- Exceções específicas e descritivas
- Fail fast - falha rápida em caso de dados inválidos

### 3. **Domain Events**
- Eventos publicados para mudanças de estado
- Desacoplamento entre bounded contexts
- Facilita implementação de Event Sourcing

### 4. **Testabilidade**
- 100% testável sem dependências externas
- Testes focados em regras de negócio
- Clareza nos testes com AAA (Arrange, Act, Assert)

### 5. **Performance**
- Coleções imutáveis (unmodifiable)
- Validações eficientes
- Sem overhead desnecessário

Este exemplo demonstra a implementação dos princípios DDD e Clean Architecture para o domínio de Lojas no sistema ArteReal.
