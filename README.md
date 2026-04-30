# ArteReal - Sistema de Gestão Maçônica

Sistema desktop para gestão administrativa de lojas maçônicas, desenvolvido com Java Swing e banco de dados SQLite.

## 🎯 Visão Geral

Este é o sistema ArteReal moderno, criado a partir da engenharia reversa do sistema FoxPro original. Oferece interface nativa e banco de dados local para instalação em computadores individuais.

## 🏗️ Arquitetura

- **Java 17**: Plataforma de desenvolvimento
- **Swing**: Interface gráfica nativa
- **SQLite**: Banco de dados embutido
- **Maven**: Gerenciamento de dependências
- **DAO Pattern**: Acesso a dados

## 📁 Estrutura do Projeto

```
ARTEREAL/
├── artereal-swing/                    # Versão desktop (principal)
│   ├── src/main/java/com/artereal/swing/
│   │   ├── ArteRealSwingApplication.java  # Classe principal
│   │   ├── database/
│   │   │   └── DatabaseManager.java      # Gerenciador do SQLite
│   │   ├── model/
│   │   │   └── Irmao.java                # Modelo de dados
│   │   ├── dao/
│   │   │   └── IrmaoDAO.java             # Acesso a dados
│   │   └── ui/
│   │       ├── MainFrame.java            # Janela principal
│   │       └── panels/                  # Painéis de interface
│   ├── pom.xml                           # Configuração Maven
│   ├── run.sh                           # Script de execução
│   └── README.md                        # Documentação
└── LISTA.TXT                           # Lista de arquivos FoxPro originais
```

## 🚀 Execução

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Build e Execução
```bash
# Entrar no diretório do projeto
cd ARTEREAL/artereal-swing

# Executar script automatizado
./run.sh
```

### Manualmente
```bash
cd ARTEREAL/artereal-swing
mvn clean package -DskipTests
java -jar target/artereal-swing-1.0.0-jar-with-dependencies.jar
```

## 🗄️ Banco de Dados

O sistema utiliza SQLite com as seguintes características:

- **Localização**: `~/.artereal/artereal.db`
- **Inicialização**: Automática na primeira execução
- **Backup**: Copiar o arquivo `.artereal/artereal.db`

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

## 🔄 Migração FoxPro → Java

| FoxPro Original | Versão Java |
|-----------------|-------------|
| IRMAO.DBF | tabela `irmao` |
| LOJAS.DBF | tabela `loja` |
| SESSAO.DBF | tabela `sessao` |
| CAIXA.DBF | tabela `caixa` |
| BIBLIOT.DBF | tabela `biblioteca` |
| EMPREST.DBF | tabela `emprestimo` |
| PAGAR.DBF | tabela `pagar` |

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

## 🔧 Configuração

### Propriedades do Sistema
O banco de dados é criado automaticamente no diretório home do usuário:
```bash
# Linux/Mac
~/.artereal/artereal.db

# Windows
%USERPROFILE%\.artereal\artereal.db
```

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

## 📝 Histórico do Projeto

### Engenharia Reversa Concluída
- ✅ Análise completa dos arquivos FoxPro
- ✅ Mapeamento de todas as tabelas e relacionamentos
- ✅ Identificação de funcionalidades principais
- ✅ Design da arquitetura Java moderna

### Implementação
- ✅ Versão desktop com Swing + SQLite
- ✅ Interface nativa e responsiva
- ✅ Banco de dados local e automático
- ✅ Funcionalidades essenciais implementadas

## 📞 Suporte

Para suporte técnico:
1. Verificar os logs de erro
2. Consultar a seção de troubleshooting
3. Revisar a documentação no diretório do projeto

## 📄 Licença

Este projeto é desenvolvido para uso em lojas maçônicas e segue os princípios da maçonaria.

---

**ArteReal Masonic Lodge Management System**

*Versão desktop moderna para gestão maçônica tradicional*
