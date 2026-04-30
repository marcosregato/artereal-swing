#!/bin/bash

# Script de execução do Sistema ArteReal
# Sistema de Gestão Maçônica Completo

echo "=========================================="
echo "    SISTEMA ARTEREAL - GESTÃO MAÇÔNICA    "
echo "=========================================="
echo ""

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ ERRO: Java não está instalado ou não está no PATH"
    echo "Por favor, instale Java 17 ou superior para executar o sistema"
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ ERRO: Versão do Java incompatível ($JAVA_VERSION)"
    echo "O sistema requer Java 17 ou superior"
    exit 1
fi

echo "✅ Java $JAVA_VERSION detectado"

# Compilar o projeto
echo "🔨 Compilando o projeto..."
cd "$(dirname "$0")"

if ! mvn clean compile -q; then
    echo "❌ ERRO: Falha na compilação do projeto"
    echo "Verifique os erros acima e tente novamente"
    exit 1
fi

echo "✅ Projeto compilado com sucesso"

# Executar o sistema
echo ""
echo "🚀 Iniciando o Sistema ArteReal..."
echo "📋 Funcionalidades disponíveis:"
echo "   • Gestão de Irmãos e Lojas"
echo "   • Controle de Visitantes e Acesso"
echo "   • Gestão Financeira e Cheques"
echo "   • Documentos Digitais e Assinaturas"
echo "   • Calendário Maçônico"
echo "   • Gestão de Afastamentos"
echo "   • Configurações Globais"
echo "   • Frequência e Candidatos"
echo ""
echo "🔐 Login padrão:"
echo "   Usuário: Administrador"
echo "   Senha: admin123"
echo ""
echo "⚠️  Pressione Ctrl+C para encerrar o sistema"
echo "=========================================="

# Executar a aplicação
mvn exec:java -Dexec.mainClass="com.artereal.swing.ArteRealSwingApplication" -q

echo ""
echo "👋 Sistema ArteReal encerrado"
echo "=========================================="
