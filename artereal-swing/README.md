# ArteReal - Versão Swing + PostgreSQL

Sistema desktop para gestão administrativa de lojas maçônicas, desenvolvido com Java Swing e banco de dados PostgreSQL.

## 🎯 Visão Geral

Esta é a versão desktop do sistema ArteReal, criada a partir da engenharia reversa do sistema FoxPro original. Oferece interface nativa e banco de dados local para instalação em computadores individuais.

## 🏗️ Arquitetura

- **Java 17**: Plataforma de desenvolvimento
- **Swing**: Interface gráfica nativa
- **PostgreSQL**: Banco de dados relacional
- **Maven**: Gerenciamento de dependências
- **DAO Pattern**: Acesso a dados

## 📁 Estrutura do Projeto

```
artereal-swing/
├── src/main/java/com/artereal/swing/
│   ├── ArteRealSwingApplication.java  # Classe principal
│   ├── database/
│   │   └── DatabaseManager.java      # Gerenciador do PostgreSQL
│   ├── model/
│   │   └── Irmao.java                # Modelo de dados
│   ├── dao/
│   │   └── IrmaoDAO.java             # Acesso a dados
│   └── ui/
│       ├── MainFrame.java            # Janela principal
│       └── panels/                  # Painéis de interface
│           ├── DashboardPanel.java
│           ├── IrmaosPanel.java
│           ├── LojasPanel.java
│           ├── SessoesPanel.java
│           ├── CaixaPanel.java
│           ├── BibliotecaPanel.java
│           └── RelatoriosPanel.java
├── pom.xml                           # Configuração Maven
└── README.md                         # Documentação
```

## 🚀 Execução

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Build e Execução
```bash
# Clone ou baixe o projeto
cd artereal-swing

# Compilação
mvn clean compile

# Empacotamento
mvn package -DskipTests

# Execução
java -jar target/artereal-swing-1.0.0-jar-with-dependencies.jar
```

### Script Automático
```bash
# Criar script de execução
echo '#!/bin/bash
mvn clean package -DskipTests
java -jar target/artereal-swing-1.0.0-jar-with-dependencies.jar
' > run.sh
chmod +x run.sh
./run.sh
```

## 🗄️ Banco de Dados

O sistema utiliza PostgreSQL com as seguintes características:

- **Servidor**: PostgreSQL (localhost:5432)
- **Database**: artereal_db
- **Inicialização**: Automática na primeira execução
- **Migração**: Scripts SQL para criação de tabelas
- **Backup**: Exportar dump do banco artereal_db
- **Migração**: Scripts SQL integrados

### Tabelas Principais
- `irmao` - Cadastro de irmãos
- `loja` - Lojas maçônicas
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

### Propriedades do Sistema
O banco de dados é criado automaticamente no diretório home do usuário:
```bash
# Linux/Mac
~/.artereal/artereal.db

# Windows
%USERPROFILE%\.artereal\artereal.db
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

### Versão 1.1
- [ ] Formulários de cadastro completos
- [ ] Validação de campos
- [ ] Filtros de busca

### Versão 1.2
- [ ] Relatórios em PDF
- [ ] Backup automático
- [ ] Importação de dados

### Versão 2.0
- [ ] Multiusuário
- [ ] Sincronização na nuvem
- [ ] Interface melhorada

## 📝 Notas de Desenvolvimento

### Padrões Utilizados
- **DAO Pattern**: Para acesso a dados
- **MVC**: Separação de responsabilidades
- **Singleton**: DatabaseManager
- **Observer**: Eventos da interface

### Boas Práticas
- Logging com SLF4J
- Tratamento de exceções
- Validação de entrada
- Recursos liberados corretamente

## 📞 Suporte

Para suporte técnico:
1. Verificar os logs de erro
2. Consultar a seção de troubleshooting
3. Abrir issue no repositório

## 📄 Licença

Este projeto é desenvolvido para uso em lojas maçônicas e segue os princípios da maçonaria.

---

**ArteReal Masonic Lodge Management System - Versão Swing**

*Versão desktop moderna para gestão maçônica tradicional*
