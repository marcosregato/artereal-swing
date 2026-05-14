# 📋 Padrões de UX para Formulários - Sistema ArteReal

## 🎯 **Visão Geral**
Este documento define os padrões de UX (User Experience) para todos os formulários do sistema ArteReal, garantindo consistência, usabilidade e manutenibilidade.

## 🏗️ **Arquitetura Base**

### **Classe Base: BaseEnhancedFormPanel**
- **Localização**: `/ui/panels/forms/BaseEnhancedFormPanel.java`
- **Propósito**: Fornece estrutura consistente para todos os formulários
- **Características**:
  - GridBagLayout para alinhamento preciso
  - Pesos dinâmicos para responsividade
  - Hierarquia visual padronizada
  - Classes de estrutura reutilizáveis

### **Classes de Estrutura**
```java
FormSection      // Grupo principal de seções
FormSubsection   // Subseção dentro de uma seção
FormField        // Campo individual com configuração
```

## 🎨 **Padrões Visuais**

### **Cores e Fontes**
```java
SECTION_TITLE_COLOR = new Color(70, 130, 180)  // Azul principal
FIELD_LABEL_COLOR = new Color(70, 130, 180)     // Azul consistente
SECTION_TITLE_FONT = Font("Segoe UI", Font.BOLD, 14)    // Títulos
FIELD_LABEL_FONT = Font("Segoe UI", Font.PLAIN, 12)    // Labels
```

### **Ícones Contextuais**
Use ícones consistentes para reconhecimento rápido:

#### **Identificação**
- 🔢 Código
- 📛 Nome
- 🆔 ID
- 🏷️ Etiqueta

#### **Dados Pessoais**
- 👤 Pessoa
- 📅 Data
- 📋 Documento
- 📄 RG/CPF

#### **Localização**
- 📍 Local
- 🏠 Endereço
- 🏘️ Bairro
- 🏙️ Cidade
- 🗺️ Estado
- 📮 CEP

#### **Contato**
- 📞 Telefone
- 📱 Celular
- 📧 E-mail
- 📱 WhatsApp

#### **Dados Maçônicos**
- 🔷 Ritual
- ⭐ Grau/Potência
- 🏛️ Loja
- 👔 Cargo
- 📜 Informações

#### **Conteúdo**
- 📚 Livros
- 📖 Leitura
- 📝 Anotações
- 📄 Documentos

#### **Status e Controle**
- 📊 Status
- 🔄 Situação
- ⏰ Hora
- 💰 Financeiro

## 📐 **Estrutura Padrão de Formulários**

### **Grupo 1: DADOS PRINCIPAIS**
Contém identificação e informações básicas essenciais.

```java
new FormSection(
    "📋 DADOS PRINCIPAIS",
    new FormSubsection(
        "🆔 Identificação",
        // Campos: Código, Nome, Data, Status
    )
)
```

### **Grupo 2: DADOS ESPECÍFICOS**
Contém informações particulares do domínio.

```java
new FormSection(
    "🎯 DADOS ESPECÍFICOS",
    new FormSubsection(
        "📂 Categoria Específica",
        // Campos específicos do domínio
    )
)
```

### **Grupo 3: LOCALIZAÇÃO/CONTATO**
Contém informações de localização e contato.

```java
new FormSection(
    "📍 LOCALIZAÇÃO" ou "📞 CONTATO",
    new FormSubsection(
        "🏠 Endereço" ou "📱 Informações",
        // Campos de endereço ou contato
    )
)
```

### **Grupo 4: STATUS E CONTROLE**
Contém situação atual e informações de controle.

```java
new FormSection(
    "📊 STATUS E CONTROLE",
    new FormSubsection(
        "🔄 Situação Atual",
        // Campos de status e datas
    )
)
```

### **Grupo 5: INFORMAÇÕES ADICIONAIS**
Contém observações e notas complementares.

```java
new FormSection(
    "📝 INFORMAÇÕES ADICIONAIS",
    new FormSubsection(
        "📋 Observações",
        new FormField("", createObservationsPanel(textArea), 0, 0, 2, 1.0)
    )
)
```

## ⚖️ **Pesos e Layout**

### **Pesos Dinâmicos (weight)**
- **Campos principais**: 0.7
- **Campos secundários**: 0.5
- **Campos complementares**: 0.3
- **Linhas completas**: colSpan=2, weight=1.0

### **Exemplos de Configuração**
```java
// Campo principal (nome)
new FormField("📛 Nome:", nomeField, 0, 1, 1, 0.7)

// Campo secundário (código)
new FormField("🔢 Código:", codigoField, 0, 0, 1, 0.3)

// Linha completa (endereço)
new FormField("🏠 Endereço:", enderecoField, 0, 0, 2, 1.0)
```

## 📝 **Guia de Implementação**

### **Passo 1: Herdar BaseEnhancedFormPanel**
```java
public class SeuFormPanel extends BaseEnhancedFormPanel {
    // Implementação
}
```

### **Passo 2: Declarar Campos**
```java
private JTextField codigoField;
private JTextField nomeField;
private JTextArea observacoesArea;
```

### **Passo 3: Inicializar Campos**
```java
private void initializeFields() {
    codigoField = createTextField(10);
    nomeField = createTextField(40);
    observacoesArea = createTextArea(3, 50);
    
    applyFieldStyling(codigoField, "codigo");
    applyFieldStyling(nomeField, "nome");
}
```

### **Passo 4: Configurar Estrutura**
```java
private void setupFormStructure() {
    FormSection[] sections = {
        // Seguir estrutura padrão de 5 grupos
    };
    addFormGroup(sections);
}
```

### **Passo 5: Implementar clearForm()**
```java
@Override
public void clearForm() {
    codigoField.setText("");
    nomeField.setText("");
    observacoesArea.setText("");
}
```

### **Passo 6: Fornecer Getters**
```java
public JTextField getCodigoField() { return codigoField; }
public JTextField getNomeField() { return nomeField; }
public JTextArea getObservacoesArea() { return observacoesArea; }
```

## 🎯 **Formulários Implementados**

### **1. LojasFormPanel** ✅
- **Grupos**: Dados Principais, Localização, Administração
- **Campos**: 17 campos organizados em 6 subseções
- **Focus**: Gestão de lojas maçônicas

### **2. IrmaosFormPanel** ✅
- **Grupos**: Dados Pessoais, Contato, Endereço, Dados Maçônicos
- **Campos**: 18 campos organizados em 5 subseções
- **Focus**: Gestão de irmãos maçônicos

### **3. SessoesFormPanel** ✅
- **Grupos**: Dados da Sessão, Participantes, Conteúdo, Estatísticas
- **Campos**: 15 campos organizados em 5 subseções
- **Focus**: Gestão de sessões maçônicas

### **4. BibliotecaFormPanel** ✅
- **Grupos**: Dados do Livro, Classificação, Status, Empréstimo
- **Campos**: 16 campos organizados em 5 subseções
- **Focus**: Gestão de biblioteca maçônica

## 🚀 **Template para Novos Formulários**

Use `FormTemplateExample.java` como base para novos formulários:

```bash
# Copiar template
cp FormTemplateExample.java SeuNovoFormPanel.java

# Adaptar conforme necessidade:
# 1. Renomear classe
# 2. Ajustar campos
# 3. Modificar grupos conforme domínio
# 4. Implementar lógica específica
```

## 📊 **Benefícios Alcançados**

### **UX Consistente**
- Hierarquia visual padronizada
- Ícones contextuais reconhecíveis
- Agrupamento lógico por funcionalidade
- Espaçamento consistente

### **Design Responsivo**
- GridBagLayout com pesos dinâmicos
- Alinhamento preciso e organizado
- Layout flexível para diferentes tamanhos
- Campos principais com destaque visual

### **Manutenibilidade**
- Classe base reutilizável
- Estrutura declarativa e clara
- Código DRY (Don't Repeat Yourself)
- Extensibilidade facilitada

## 🔧 **Boas Práticas**

### **Nomenclatura**
- Use nomes descritivos para campos
- Siga padrão: `tipoCampoField`
- Ex: `codigoField`, `nomeField`, `dataNascimentoField`

### **Organização**
- Agrupe campos por funcionalidade
- Use ícones contextuais consistentemente
- Mantenha ordem lógica de preenchimento

### **Estilização**
- Use `applyFieldStyling()` para campos padrão
- Use PadraoLayout para estilização específica
- Mantenha cores e fontes consistentes

### **Validação**
- Implemente validação no nível de formulário
- Use mensagens de erro consistentes
- Forneça feedback visual ao usuário

---

**Status: IMPLEMENTAÇÃO COMPLETA ✅**  
**Última Atualização: 10/05/2026**  
**Versão: 1.0**
