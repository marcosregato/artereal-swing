# 🏛️ Sistema ArteReal - Gestão Maçônica

Sistema moderno de gestão para lojas maçônicas, desenvolvido em Java com interface Swing intuitiva e banco de dados SQLite integrado.

## 🎯 Sobre o Projeto

O Sistema ArteReal é uma solução completa para administração de lojas maçônicas, resultante de engenharia reversa do sistema FoxPro original com modernização tecnológica significativa.

### ✨ Funcionalidades Principais

#### 👥 Gestão de Membros
- **Cadastro completo de Irmãos** com dados pessoais e profissionais
- **Administração de Lojas** com múltiplas filiais
- **Controle de Candidatos** e processo de iniciação
- **Gestão de Visitantes** com sistema de autorizações

#### 💰 Gestão Financeira
- **Controle de Caixa** completo
- **Gestão de Cheques** integrada
- **Relatórios financeiros** detalhados
- **Fluxo de caixa** automatizado

#### 📚 Biblioteca e Documentos
- **Biblioteca maçônica** com controle de empréstimos
- **Gestão documental** digital
- **Correspondência oficial** automatizada
- **Arquivos digitais** organizados

#### 📅 Eventos e Calendário
- **Calendário maçônico** com eventos importantes
- **Controle de sessões** (Magnas, Brancas, Eleições)
- **Gestão de afastamentos** e licenças
- **Controle de frequência** dos membros

#### 🖼️ Recursos Adicionais
- **Galeria de fotos** para eventos
- **Sistema de etiquetas** personalizado
- **Configurações globais** centralizadas
- **Relatórios personalizados**

## 🏗️ Arquitetura Técnica

### Stack Tecnológico
- **Java 17** - Linguagem principal
- **Swing** - Interface gráfica desktop
- **SQLite** - Banco de dados embarcado
- **Maven** - Gerenciamento de dependências
- **SLF4J** - Logging estruturado

### Padrões de Projeto
- **DAO Pattern** - Camada de acesso a dados
- **Singleton** - Gerenciamento de banco de dados
- **MVC** - Separação de responsabilidades
- **Observer** - Eventos e notificações

## 🎨 Interface do Usuário

### Design Moderno
- **Logo maçônico personalizado** com esquadro e compassos
- **Cores pastéis consistentes** em toda interface
- **Headers estilizados** com identidade visual
- **Layout responsivo** com SplitPane
- **Botões intuitivos** com feedback visual

### Experiência do Usuário
- **Formulários organizados** com seções visuais
- **Tabelas modernas** com seleção destacada
- **Pesquisa integrada** em todos os módulos
- **Navegação intuitiva** entre funcionalidades

## 📊 Estrutura do Projeto

```
ARTEREAL/
├── artereal-swing/
│   ├── src/main/java/com/artereal/swing/
│   │   ├── dao/           # Camada de acesso a dados
│   │   ├── model/         # Modelos de dados
│   │   ├── ui/
│   │   │   ├── panels/    # Painéis Swing
│   │   │   ├── components/ # Componentes reutilizáveis
│   │   │   └── dialogs/   # Diálogos modais
│   │   ├── database/      # Gerenciamento do banco
│   │   └── utils/         # Utilitários
│   ├── pom.xml           # Configuração Maven
│   └── executar.sh       # Script de execução
└── README.md            # Este arquivo
```

## 🚀 Instalação e Execução

### Pré-requisitos
- **Java 17+** instalado
- **Maven 3.6+** (opcional, para desenvolvimento)

### Execução Rápida
```bash
# Clonar o repositório
git clone <repository-url>
cd ARTEREAL/artereal-swing

# Executar o sistema
./executar.sh
```

### Compilação Manual
```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn exec:java -Dexec.mainClass="com.artereal.swing.ArteRealSwingApplication"
```

## 🔐 Acesso Padrão

Após a inicialização, use as credenciais padrão:

- **Usuário:** `admin`
- **Senha:** `admin123`

## 📋 Módulos Principais

### 1. Gestão de Irmãos
Cadastro completo com dados pessoais, profissionais e maçônicos.

### 2. Controle de Visitantes
Sistema de autorização para visitantes e irmãos de outras lojas.

### 3. Gestão Financeira
Controle completo de caixa, cheques e relatórios financeiros.

### 4. Biblioteca Maçônica
Catálogo de livros com controle de empréstimos e devoluções.

### 5. Calendário Maçônico
Eventos importantes e sessões programadas.

### 6. Documentos Digitais
Gestão de documentos oficiais e correspondências.

## 🎯 Características Técnicas

### Banco de Dados
- **19 tabelas** otimizadas
- **Relacionamentos** consistentes
- **Integridade** de dados garantida
- **Backup** automático

### Performance
- **Inicialização rápida** (< 3 segundos)
- **Interface responsiva** em tempo real
- **Cache inteligente** de consultas
- **Memória otimizada**

### Segurança
- **Controle de acesso** por usuário
- **Sessões seguras** com timeout
- **Validação** de dados de entrada
- **Logging** completo de auditoria

## 🔄 Histórico

### Versão Atual: 1.0.0
- ✅ Engenharia reversa completa do sistema FoxPro
- ✅ Modernização para Java 17 + Swing
- ✅ Interface moderna com logo maçônico
- ✅ Todas as funcionalidades originais preservadas
- ✅ Expansões significativas de recursos

## 📝 Desenvolvimento

### Como Contribuir
1. **Fork** o repositório
2. **Criar branch** para sua funcionalidade
3. **Commit** suas mudanças
4. **Push** para o branch
5. **Abrir Pull Request**

### Estrutura de Código
- **Convenções Java** seguidas rigorosamente
- **Comments** em português brasileiro
- **Logging** estruturado com SLF4J
- **Testes** unitários para componentes críticos

## 📞 Suporte

Para suporte técnico ou dúvidas:
- **Documentação:** Verificar os painéis de ajuda no sistema
- **Logs:** Analisar logs de execução para diagnóstico
- **Configurações:** Ajustar parâmetros no painel de configurações

## 📜 Licença

Este projeto é propriedade da ArteReal e desenvolvido para uso exclusivo em lojas maçônicas.

---

**🏛️ ArteReal - Gestão Maçônica Moderna**

*Desenvolvido com ❤️ para a comunidade maçônica brasileira*
