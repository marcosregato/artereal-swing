package com.artereal.swing.domain.loja;

import com.artereal.swing.domain.irmao.Irmao;
import com.artereal.swing.domain.irmao.StatusIrmao;
import com.artereal.swing.domain.caixa.Caixa;
import com.artereal.swing.domain.caixa.StatusCaixa;
import com.artereal.swing.domain.loja.valueobjects.Cnpj;
import com.artereal.swing.domain.loja.valueobjects.Endereco;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;

/**
 * Entidade Loja - Aggregate Root do bounded context de Lojas
 * 
 * Esta entidade representa uma loja da ArteReal com todas as regras de negócio
 * relacionadas à gestão de lojas, irmãos e caixas.
 */
public class Loja {
    
    private Long id;
    private String nome;
    private Cnpj cnpj;
    private Endereco endereco;
    private StatusLoja status;
    private List<Irmao> irmaos;
    private List<Caixa> caixas;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Construtor privado para forçar uso de factory methods
    private Loja(Long id, String nome, Cnpj cnpj, Endereco endereco) {
        this.id = Objects.requireNonNull(id, "ID da loja é obrigatório");
        this.nome = Objects.requireNonNull(nome, "Nome da loja é obrigatório");
        this.cnpj = Objects.requireNonNull(cnpj, "CNPJ da loja é obrigatório");
        this.endereco = Objects.requireNonNull(endereco, "Endereço da loja é obrigatório");
        this.status = StatusLoja.ATIVA;
        this.irmaos = new ArrayList<>();
        this.caixas = new ArrayList<>();
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        
        validate();
    }
    
    /**
     * Factory method para criar uma nova loja
     */
    public static Loja criar(String nome, String cnpj, String endereco, 
                           String cidade, String estado) {
        // Gerar ID sequencial simples para demonstração
        Long novoId = System.currentTimeMillis();
        Cnpj cnpjVo = new Cnpj(cnpj);
        Endereco enderecoVo = new Endereco(endereco, null, null, null, cidade, estado, null);
        return new Loja(novoId, nome, cnpjVo, enderecoVo);
    }
    
    /**
     * Adiciona um irmão à loja
     */
    public void adicionarIrmao(Irmao irmao) {
        Objects.requireNonNull(irmao, "Irmão é obrigatório");
        
        if (irmaos.contains(irmao)) {
            throw new LojaException("Irmão já está associado à loja");
        }
        
        if (irmaos.size() >= 50) {
            throw new LojaException("Loja atingiu o limite máximo de 50 irmãos");
        }
        
        irmaos.add(irmao);
        irmao.associarLoja(this);
        dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Remove um irmão da loja
     */
    public void removerIrmao(Long irmaoId) {
        Objects.requireNonNull(irmaoId, "ID do irmão é obrigatório");
        
        Optional<Irmao> irmaoOpt = irmaos.stream()
            .filter(i -> i.getId().equals(irmaoId))
            .findFirst();
            
        if (irmaoOpt.isEmpty()) {
            throw new LojaException("Irmão não encontrado na loja");
        }
        
        Irmao irmao = irmaoOpt.get();
        irmaos.remove(irmao);
        irmao.desassociarLoja();
        dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Abre um novo caixa na loja
     */
    public void abrirCaixa(BigDecimal valor, String responsavel) {
        Objects.requireNonNull(valor, "Valor do caixa é obrigatório");
        Objects.requireNonNull(responsavel, "Responsável pelo caixa é obrigatório");
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new LojaException("Valor do caixa deve ser positivo");
        }
        
        if (responsavel.trim().isEmpty()) {
            throw new LojaException("Responsável pelo caixa é obrigatório");
        }
        
        // Verificar se há caixas abertos
        boolean temCaixasAbertos = caixas.stream()
            .anyMatch(c -> c.getStatus() == StatusCaixa.ABERTO);
            
        if (temCaixasAbertos) {
            throw new LojaException("Já existe um caixa aberto na loja");
        }
        
        Caixa caixa = Caixa.abrir(valor, responsavel, this.id);
        caixas.add(caixa);
        dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Fecha um caixa existente
     */
    public void fecharCaixa(Long caixaId) {
        Objects.requireNonNull(caixaId, "ID do caixa é obrigatório");
        
        Optional<Caixa> caixaOpt = caixas.stream()
            .filter(c -> c.getId().equals(caixaId))
            .findFirst();
            
        if (caixaOpt.isEmpty()) {
            throw new LojaException("Caixa não encontrado na loja");
        }
        
        Caixa caixa = caixaOpt.get();
        caixa.fechar();
        dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Atualiza dados da loja
     */
    public void atualizarDados(String nome, String cnpj, String endereco, 
                               String cidade, String estado) {
        Objects.requireNonNull(nome, "Nome da loja é obrigatório");
        Objects.requireNonNull(cnpj, "CNPJ da loja é obrigatório");
        Objects.requireNonNull(endereco, "Endereço da loja é obrigatório");
        
        this.nome = nome;
        this.cnpj = new Cnpj(cnpj);
        this.endereco = new Endereco(endereco, null, null, null, cidade, estado, null);
        this.dataAtualizacao = LocalDateTime.now();
        
        validate();
    }
    
    /**
     * Inativa a loja
     */
    public void inativar() {
        if (this.status == StatusLoja.INATIVA) {
            throw new LojaException("Loja já está inativa");
        }
        
        // Verificar se há caixas abertos
        boolean temCaixasAbertos = caixas.stream()
            .anyMatch(c -> c.getStatus() == StatusCaixa.ABERTO);
            
        if (temCaixasAbertos) {
            throw new LojaException("Não é possível inativar loja com caixas abertos");
        }
        
        this.status = StatusLoja.INATIVA;
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Reativa a loja
     */
    public void reativar() {
        if (this.status == StatusLoja.ATIVA) {
            throw new LojaException("Loja já está ativa");
        }
        
        this.status = StatusLoja.ATIVA;
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Validações de negócio
     */
    private void validate() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new LojaException("Nome da loja é obrigatório");
        }
        if (nome.length() > 200) {
            throw new LojaException("Nome da loja não pode exceder 200 caracteres");
        }
        // Cnpj e Endereco já são validados nos seus Value Objects
    }
    
    /**
     * Calcula o número total de irmãos ativos
     */
    public int getQuantidadeIrmaosAtivos() {
        return (int) irmaos.stream()
            .filter(i -> i.getStatus() == StatusIrmao.ATIVO)
            .count();
    }
    
    /**
     * Calcula o valor total em caixas abertos
     */
    public BigDecimal getValorTotalCaixasAbertos() {
        return caixas.stream()
            .filter(c -> c.getStatus() == StatusCaixa.ABERTO)
            .map(Caixa::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Cnpj getCnpj() { return cnpj; }
    public String getCnpjFormatado() { return cnpj.formatado(); }
    public Endereco getEndereco() { return endereco; }
    public String getEnderecoCompleto() { return endereco.getEnderecoCompleto(); }
    public String getCidade() { return endereco.cidade(); }
    public String getEstado() { return endereco.estado(); }
    public StatusLoja getStatus() { return status; }
    public List<Irmao> getIrmaos() { return Collections.unmodifiableList(irmaos); }
    public List<Caixa> getCaixas() { return Collections.unmodifiableList(caixas); }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    
    // Métodos de negócio adicionais
    public boolean isAtiva() {
        return status == StatusLoja.ATIVA;
    }
    
    public boolean temCaixasAbertos() {
        return caixas.stream()
            .anyMatch(c -> c.getStatus() == StatusCaixa.ABERTO);
    }
    
    
    @Override
    public String toString() {
        return String.format("Loja{id=%d, nome='%s', cnpj='%s', status=%s}", 
            id, nome, cnpj.formatado(), status);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loja)) return false;
        Loja outra = (Loja) o;
        return Objects.equals(id, outra.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
