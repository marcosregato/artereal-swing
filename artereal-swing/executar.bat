@echo off
REM Script de execução do Sistema ArteReal para Windows
REM Sistema de Gestão Maçônica Completo

echo ==========================================
echo    SISTEMA ARTEREAL - GESTÃO MAÇÔNICA    
echo ==========================================
echo.

REM Verificar se Java está instalado
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ ERRO: Java não está instalado ou não está no PATH
    echo Por favor, instale Java 17 ou superior para executar o sistema
    pause
    exit /b 1
)

REM Verificar versão do Java
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr "version"') do set "JAVA_VERSION=%%i"
set "JAVA_VERSION=%JAVA_VERSION:"=%"
set "JAVA_VERSION=%JAVA_VERSION:.=%"
set "JAVA_VERSION=%JAVA_VERSION:"=%"

if %JAVA_VERSION% LSS 17 (
    echo ❌ ERRO: Versão do Java incompatível (%JAVA_VERSION%)
    echo O sistema requer Java 17 ou superior
    pause
    exit /b 1
)

echo ✅ Java %JAVA_VERSION% detectado

REM Compilar o projeto
echo 🔨 Compilando o projeto...
cd /d "%~dp0"

call mvn clean compile -q
if errorlevel 1 (
    echo ❌ ERRO: Falha na compilação do projeto
    echo Verifique os erros acima e tente novamente
    pause
    exit /b 1
)

echo ✅ Projeto compilado com sucesso

REM Executar o sistema
echo.
echo 🚀 Iniciando o Sistema ArteReal...
echo 📋 Funcionalidades disponíveis:
echo    • Gestão de Irmãos e Lojas
echo    • Controle de Visitantes e Acesso
echo    • Gestão Financeira e Despesas
echo    • Documentos Digitais e Assinaturas
echo    • Calendário Maçônico
echo    • Gestão de Afastamentos
echo    • Configurações Globais
echo    • Frequência e Candidatos
echo.
echo 🔐 Login padrão:
echo    Usuário: Administrador
echo    Senha: admin123
echo.
echo ⚠️  Pressione Ctrl+C para encerrar o sistema
echo ==========================================

REM Executar a aplicação
call mvn exec:java -Dexec.mainClass="com.artereal.swing.ArteRealSwingApplication" -q

echo.
echo 👋 Sistema ArteReal encerrado
echo ==========================================
pause
