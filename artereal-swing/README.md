# ArteReal - Versão Swing + PostgreSQL

Sistema desktop para gestão administrativa de lojas maçônicas, desenvolvido com Java Swing e banco de dados PostgreSQL.

## 🎯 Visão Geral

Esta é a versão desktop do sistema ArteReal, criada a partir da engenharia reversa do sistema FoxPro original. Oferece interface nativa e banco de dados local para instalação em computadores individuais.

**Versão Atual**: 2.3.0

## 🏗️ Arquitetura

O sistema foi migrado para **Arquitetura Hexagonal (Ports and Adapters)** combinada com **Domain-Driven Design (DDD)** para maior escalabilidade, testabilidade e manutenibilidade.

### Tecnologias
- **Java 17**: Plataforma de desenvolvimento
- **Swing**: Interface gráfica nativa
- **PostgreSQL**: Banco de dados relacional
- **Maven**: Gerenciamento de dependências
- **SLF4J + Logback**: Logging
- **JUnit 5 + AssertJ**: Testes

### Camadas da Arquitetura

#### Domain Layer (Núcleo do Negócio)
- Entidades e Agregados: Loja, Irmao, Sessao, Caixa
- Value Objects: Endereco, Cnpj, Dinheiro
- Domain Services: LojaDomainService, SessaoDomainService
- Repositories (Interfaces): LojaRepository, IrmaoRepository
- Domain Events: IrmaoAssociadoEvent, CaixaAbertoEvent

#### Application Layer (Casos de Uso)
- Use Cases: CriarLojaUseCase, TransferirIrmaoUseCase, AbrirCaixaUseCase
- DTOs: CriarLojaRequest, LojaResponse, EnderecoRequest
- Mappers: LojaMapper, IrmaoMapper
- Validators: CriarLojaValidator
- Service Facades: LojaServiceFacade (coordena use cases)

#### Infrastructure Layer (Adaptadores)
- Database Adapters: LojaJpaRepository, IrmaoJpaRepository, CaixaJpaRepository
- Configuration: ConfiguracaoBanco, DomainConfig
- DataSource: SimpleDataSource (implementação customizada)

#### Presentation Layer (UI)
- Painéis Swing migrados para nova arquitetura
- LojasPanel, IrmaosPanel, CaixaPanel

## 📁 Estrutura do Projeto

```
artereal-swing/
├── src/main/java/com/artereal/swing/
│   ├── ArteRealSwingApplication.java  # Classe principal
│   ├── domain/                       # Domain Layer (DDD)
│   │   ├── loja/                     # Bounded Context Loja
│   │   │   ├── Loja.java             # Entidade Aggregate Root
│   │   │   ├── LojaRepository.java   # Interface Repository
│   │   │   ├── valueobjects/         # Value Objects
│   │   │   │   ├── Cnpj.java
│   │   │   │   └── Endereco.java
│   │   │   └── events/               # Domain Events
│   │   ├── irmao/                    # Bounded Context Irmao
│   │   └── caixa/                    # Bounded Context Caixa
│   ├── application/                  # Application Layer
│   │   ├── loja/
│   │   │   ├── CriarLojaUseCase.java
│   │   │   ├── AtualizarLojaUseCase.java
│   │   │   ├── DeletarLojaUseCase.java
│   │   │   ├── ListarLojasUseCase.java
│   │   │   ├── LojaServiceFacade.java
│   │   │   ├── CriarLojaRequest.java
│   │   │   └── LojaResponse.java
│   │   ├── irmao/
│   │   └── caixa/
│   ├── infrastructure/               # Infrastructure Layer
│   │   ├── persistence/
│   │   │   ├── LojaJpaRepository.java
│   │   │   ├── IrmaoJpaRepository.java
│   │   │   └── CaixaJpaRepository.java
│   │   └── config/
│   │       └── ConfiguracaoBanco.java
│   ├── presentation/                 # Presentation Layer
│   │   └── LojaController.java
│   ├── dao/                          # DAO antigo (legado)
│   ├── model/                        # Modelos antigos (legado)
│   └── ui/                           # Interface Swing
│       ├── MainFrame.java
│       └── panels/
│           ├── DashboardPanel.java
│           ├── LojasPanel.java
│           ├── IrmaosPanel.java
│           └── ...
├── src/test/java/                    # Testes
│   ├── domain/
│   ├── application/
│   ├── integration/
│   └── ui/
├── docs/                             # Documentação
│   └── arquitetura-hexagonal-ddd.md
├── pom.xml                           # Configuração Maven
└── README.md                         # Documentação
```

## 🚀 Execução

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 12+ (ou H2 para testes)

### Build e Execução
```bash
# Clone ou baixe o projeto
cd artereal-swing

# Compilação
mvn clean compile

# Executar testes
mvn test

# Empacotamento
mvn package -DskipTests

# Execução
java -jar target/artereal-swing-2.3.0-jar-with-dependencies.jar
```

### Script Automático
```bash
# Criar script de execução
echo '#!/bin/bash
mvn clean package -DskipTests
java -jar target/artereal-swing-2.3.0-jar-with-dependencies.jar
' > run.sh
chmod +x run.sh
./run.sh
```

## 🗄️ Banco de Dados

O sistema utiliza PostgreSQL com as seguintes características:

- **Servidor**: PostgreSQL (localhost:5432)
- **Database**: artereal_db
- **Usuário**: system
- **Senha**: system
- **Inicialização**: Automática na primeira execução
- **Testes**: H2 em memória (jdbc:h2:mem:testdb)

### Configuração
A configuração do banco de dados é gerenciada através de `ConfiguracaoBanco`:
```java
// Produção
ConfiguracaoBanco.criarDataSource()

// Testes
ConfiguracaoBanco.criarDataSourceTeste()
```

### Tabelas Principais
- `loja` - Lojas maçônicas
- `irmao` - Cadastro de irmãos
- `sessao` - Sessões e reuniões
- `caixa` - Transações financeiras
- `biblioteca` - Acervo de livros
- `emprestimo` - Empréstimos da biblioteca
- `pagar` - Contas a pagar

## 🖥️ Interface

### Menu Principal
- **Cadastros**: Irmãos, Lojas
- **Administrativo**: Sessões, Caixa, Biblioteca
- **Relatórios**: Geração de relatórios
- **Ajuda**: Sobre o sistema

### Dashboard
- Estatísticas em tempo real
- Cards informativos
- Navegação rápida

### Funcionalidades
- **Gestão de Irmãos**: CRUD completo
- **Controle Financeiro**: Entradas e saídas
- **Biblioteca**: Empréstimos e devoluções
- **Sessões**: Agendamento e controle
- **Relatórios**: Diversos formatos

## 🔧 Configuração

### Configuração do Banco de Dados
A configuração do banco de dados é centralizada em `ConfiguracaoBanco`:
```java
// Produção (PostgreSQL)
DataSource dataSource = ConfiguracaoBanco.criarDataSource();

// Testes (H2 em memória)
DataSource testDataSource = ConfiguracaoBanco.criarDataSourceTeste();
```

### Personalização
- **Look and Feel**: Detectado automaticamente
- **Tema**: Nativo do sistema operacional
- **Fonte**: Arial (padrão)

## 📊 Funcionalidades Implementadas

### ✅ Módulos Ativos
- [x] Dashboard com estatísticas
- [x] Gestão de Irmãos
- [x] Gestão de Lojas
- [x] Gestão de Sessões
- [x] Controle Financeiro
- [x] Biblioteca
- [x] Relatórios básicos

### 🔄 Em Desenvolvimento
- [ ] Formulários detalhados
- [ ] Validação de dados
- [ ] Relatórios avançados
- [ ] Backup/Restore
- [ ] Importação/Exportação

## 🐛 Troubleshooting

### Problemas Comuns

#### Java não encontrado
```bash
# Verificar versão
java -version

# Instalar Java 17
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# Windows
# Baixar do site oficial da Oracle
```

#### Erro de permissão
```bash
# Linux/Mac
chmod +x target/artereal-swing-1.0.0-jar-with-dependencies.jar
```

#### Banco de dados corrompido
```bash
# Remover banco de dados
rm ~/.artereal/artereal.db
# Executar novamente para recriar
```

## 📱 Screenshots

*(Adicionar screenshots da interface quando disponível)*

## 🔮 Roadmap

### Versão 2.3 (Em Progresso)
- [ ] Completar migração de UI Panels para nova arquitetura
- [ ] Estender domain.Loja com campos necessários para UI
- [ ] Criar DTOs de UI para conversão entre domain e UI
- [ ] Migrar IrmaosPanel para nova arquitetura
- [ ] Migrar CaixaPanel para nova arquitetura
- [ ] Remover classes DAO/Model legadas após migração completa

### Versão 2.4
- [ ] Formulários de cadastro completos
- [ ] Validação de campos
- [ ] Filtros de busca avançados

### Versão 2.5
- [ ] Relatórios em PDF
- [ ] Backup automático
- [ ] Importação/Exportação de dados

### Versão 3.0
- [ ] Multiusuário
- [ ] Sincronização na nuvem
- [ ] Interface melhorada
- [ ] API REST para integração

## 📝 Notas de Desenvolvimento

### Padrões Utilizados
- **Hexagonal Architecture (Ports and Adapters)**: Separação entre domínio e infraestrutura
- **Domain-Driven Design (DDD)**: Bounded Contexts, Aggregates, Value Objects
- **Repository Pattern**: Abstração de persistência
- **Use Case Pattern**: Orquestração de lógica de negócio
- **Service Facade**: Coordenação de use cases para UI
- **Factory Method**: Criação de entidades de domínio
- **Observer**: Eventos de domínio
- **Builder Pattern**: Construção de DTOs complexos

### Boas Práticas
- Logging com SLF4J + Logback
- Tratamento de exceções com exceções de domínio
- Validação de entrada em DTOs e entidades
- Recursos liberados corretamente (try-with-resources)
- Testes unitários e de integração
- Injeção de dependências via construtor
- Imutabilidade de Value Objects
- Reflection para invocar construtores privados de entidades

## 📞 Suporte

Para suporte técnico:
1. Verificar os logs de erro
2. Consultar a seção de troubleshooting
3. Abrir issue no repositório

## 📄 Licença

Este projeto é desenvolvido para uso em lojas maçônicas e segue os princípios da maçonaria.

---

**ArteReal Masonic Lodge Management System - Versão Swing**

*Versão 2.3.0 - Arquitetura Hexagonal com DDD*

*Sistema desktop moderno para gestão maçônica tradicional com arquitetura escalável e testável*
