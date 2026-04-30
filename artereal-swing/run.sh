#!/bin/bash

# Script para executar o ArteReal Swing

echo "🔨 Building ArteReal Swing Application..."

# Verifica se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Java não está instalado. Por favor, instale Java 17 ou superior."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 ou superior é necessário. Versão atual: $JAVA_VERSION"
    exit 1
fi

# Verifica se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não está instalado. Por favor, instale o Maven."
    exit 1
fi

# Limpa e compila
echo "🧹 Limpando e compilando..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Falha no build. Verifique os erros acima."
    exit 1
fi

echo "✅ Build concluído com sucesso!"
echo ""
echo "🚀 Iniciando ArteReal Swing Application..."
echo "📱 A aplicação abrirá em uma janela desktop"
echo ""
echo "📁 Banco de dados: ~/.artereal/artereal.db"
echo ""

# Executa a aplicação
java -jar target/artereal-swing-1.0.0-jar-with-dependencies.jar
