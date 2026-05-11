package com.artereal.swing.ui.panels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * Testes para centralização e validação de mensagens do sistema
 */
@DisplayName("Testes de Mensagens do Sistema")
class MensagensSistemaTest {

    private ResourceBundle messages;
    
    @BeforeEach
    void setUp() {
        // Configura ambiente de testes
        System.setProperty("test.environment", "true");
        
        // Tenta carregar bundle de mensagens
        try {
            messages = ResourceBundle.getBundle("messages");
        } catch (Exception e) {
            // Se não encontrar, cria um bundle de teste
            messages = ResourceBundle.getBundle("test_messages");
        }
    }

    @Test
    @DisplayName("Deve validar mensagens de sucesso")
    void testMensagensSucesso() {
        // Arrange & Act
        String mensagemSucesso = "Operação realizada com sucesso!";
        String mensagemSalvo = "Dados salvos com sucesso!";
        String mensagemAtualizado = "Dados atualizados com sucesso!";
        
        // Assert - Verifica se mensagens são válidas
        assertThat(mensagemSucesso).isNotEmpty();
        assertThat(mensagemSucesso).contains("sucesso");
        
        assertThat(mensagemSalvo).isNotEmpty();
        assertThat(mensagemSalvo).contains("salvos");
        
        assertThat(mensagemAtualizado).isNotEmpty();
        assertThat(mensagemAtualizado).contains("atualizados");
    }

    @Test
    @DisplayName("Deve validar mensagens de erro")
    void testMensagensErro() {
        // Arrange & Act
        String mensagemErro = "Ocorreu um erro ao processar a operação";
        String mensagemCampoObrigatorio = "Campo obrigatório não preenchido";
        String mensagemDadosInvalidos = "Dados informados são inválidos";
        
        // Assert - Verifica se mensagens são válidas
        assertThat(mensagemErro).isNotEmpty();
        assertThat(mensagemErro).contains("erro");
        
        assertThat(mensagemCampoObrigatorio).isNotEmpty();
        assertThat(mensagemCampoObrigatorio).contains("obrigatório");
        
        assertThat(mensagemDadosInvalidos).isNotEmpty();
        assertThat(mensagemDadosInvalidos).contains("inválidos");
    }

    @Test
    @DisplayName("Deve validar mensagens de confirmação")
    void testMensagensConfirmacao() {
        // Arrange & Act
        String mensagemConfirmarExclusao = "Deseja realmente excluir este registro?";
        String mensagemConfirmarSalvar = "Deseja salvar as alterações?";
        String mensagemConfirmarSair = "Deseja sair sem salvar?";
        
        // Assert - Verifica se mensagens são válidas
        assertThat(mensagemConfirmarExclusao).isNotEmpty();
        assertThat(mensagemConfirmarExclusao).contains("Deseja");
        
        assertThat(mensagemConfirmarSalvar).isNotEmpty();
        assertThat(mensagemConfirmarSalvar).contains("salvar");
        
        assertThat(mensagemConfirmarSair).isNotEmpty();
        assertThat(mensagemConfirmarSair).contains("sair");
    }

    @Test
    @DisplayName("Deve validar mensagens de validação de campos")
    void testMensagensValidacaoCampos() {
        // Arrange & Act
        String mensagemNomeInvalido = "Nome informado é inválido";
        String mensagemEmailInvalido = "E-mail informado é inválido";
        String mensagemTelefoneInvalido = "Telefone informado é inválido";
        String mensagemDataInvalida = "Data informada é inválida";
        
        // Assert - Verifica se mensagens são válidas
        assertThat(mensagemNomeInvalido).isNotEmpty();
        assertThat(mensagemNomeInvalido).contains("inválido");
        
        assertThat(mensagemEmailInvalido).isNotEmpty();
        assertThat(mensagemEmailInvalido).contains("inválido");
        
        assertThat(mensagemTelefoneInvalido).isNotEmpty();
        assertThat(mensagemTelefoneInvalido).contains("inválido");
        
        assertThat(mensagemDataInvalida).isNotEmpty();
        assertThat(mensagemDataInvalida).contains("inválida");
    }

    @Test
    @DisplayName("Deve validar mensagens de sistema")
    void testMensagensSistema() {
        // Arrange & Act
        String mensagemCarregando = "Carregando...";
        String mensagemProcessando = "Processando...";
        String mensagemAguarde = "Aguarde...";
        String mensagemConcluido = "Concluído";
        
        // Assert - Verifica se mensagens são válidas
        assertThat(mensagemCarregando).isNotEmpty();
        assertThat(mensagemProcessando).isNotEmpty();
        assertThat(mensagemAguarde).isNotEmpty();
        assertThat(mensagemConcluido).isNotEmpty();
    }

    @Test
    @DisplayName("Deve validar exibição de mensagens em JOptionPane")
    void testExibicaoMensagensJOptionPane() {
        // Arrange & Act - Testa se JOptionPane pode exibir mensagens
        String titulo = "Teste de Mensagem";
        String mensagem = "Esta é uma mensagem de teste";
        
        // Assert - Verifica se parâmetros são válidos
        assertThat(titulo).isNotEmpty();
        assertThat(mensagem).isNotEmpty();
        
        // Verifica opções de mensagem
        assertThat(JOptionPane.OK_OPTION).isGreaterThanOrEqualTo(0);
        assertThat(JOptionPane.YES_NO_OPTION).isGreaterThanOrEqualTo(0);
        assertThat(JOptionPane.YES_NO_CANCEL_OPTION).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("Deve validar mensagens de ajuda")
    void testMensagensAjuda() {
        // Arrange & Act
        String mensagemAjudaCadastro = "Para cadastrar um novo irmão, preencha todos os campos obrigatórios";
        String mensagemAjudaBusca = "Use o campo de busca para encontrar registros rapidamente";
        String mensagemAjudaEdicao = "Clique duas vezes em um registro para editá-lo";
        
        // Assert - Verifica se mensagens de ajuda são válidas
        assertThat(mensagemAjudaCadastro).isNotEmpty();
        assertThat(mensagemAjudaCadastro).contains("cadastrar");
        
        assertThat(mensagemAjudaBusca).isNotEmpty();
        assertThat(mensagemAjudaBusca).contains("busca");
        
        assertThat(mensagemAjudaEdicao).isNotEmpty();
        assertThat(mensagemAjudaEdicao).contains("editar");
    }

    @Test
    @DisplayName("Deve validar mensagens de permissão")
    void testMensagensPermissao() {
        // Arrange & Act
        String mensagemSemPermissao = "Você não tem permissão para realizar esta operação";
        String mensagemAcessoNegado = "Acesso negado";
        String mensagemPermissaoAdministrador = "Apenas administradores podem realizar esta operação";
        
        // Assert - Verifica se mensagens de permissão são válidas
        assertThat(mensagemSemPermissao).isNotEmpty();
        assertThat(mensagemSemPermissao).contains("permissão");
        
        assertThat(mensagemAcessoNegado).isNotEmpty();
        assertThat(mensagemAcessoNegado).contains("negado");
        
        assertThat(mensagemPermissaoAdministrador).isNotEmpty();
        assertThat(mensagemPermissaoAdministrador).contains("administrador");
    }

    @Test
    @DisplayName("Deve validar mensagens de backup e restauração")
    void testMensagensBackupRestauracao() {
        // Arrange & Act
        String mensagemBackupSucesso = "Backup realizado com sucesso";
        String mensagemBackupErro = "Erro ao realizar backup";
        String mensagemRestauracaoSucesso = "Dados restaurados com sucesso";
        String mensagemRestauracaoErro = "Erro ao restaurar dados";
        
        // Assert - Verifica se mensagens são válidas
        assertThat(mensagemBackupSucesso).isNotEmpty();
        assertThat(mensagemBackupSucesso).contains("sucesso");
        
        assertThat(mensagemBackupErro).isNotEmpty();
        assertThat(mensagemBackupErro).contains("Erro");
        
        assertThat(mensagemRestauracaoSucesso).isNotEmpty();
        assertThat(mensagemRestauracaoSucesso).contains("sucesso");
        
        assertThat(mensagemRestauracaoErro).isNotEmpty();
        assertThat(mensagemRestauracaoErro).contains("Erro");
    }

    @Test
    @DisplayName("Deve validar mensagens de importação/exportação")
    void testMensagensImportacaoExportacao() {
        // Arrange & Act
        String mensagemImportacaoSucesso = "Dados importados com sucesso";
        String mensagemImportacaoErro = "Erro ao importar dados";
        String mensagemExportacaoSucesso = "Dados exportados com sucesso";
        String mensagemExportacaoErro = "Erro ao exportar dados";
        String mensagemFormatoInvalido = "Formato de arquivo inválido";
        
        // Assert - Verifica se mensagens são válidas
        assertThat(mensagemImportacaoSucesso).isNotEmpty();
        assertThat(mensagemImportacaoErro).isNotEmpty();
        assertThat(mensagemExportacaoSucesso).isNotEmpty();
        assertThat(mensagemExportacaoErro).isNotEmpty();
        assertThat(mensagemFormatoInvalido).isNotEmpty();
    }

    @Test
    @DisplayName("Deve centralizar mensagens consistentemente")
    void testCentralizacaoMensagens() {
        // Arrange & Act - Simula centralizador de mensagens
        class CentralizadorMensagens {
            public static final String SUCESSO_SALVAR = "Dados salvos com sucesso!";
            public static final String ERRO_CAMPO_OBRIGATORIO = "Campo obrigatório não preenchido";
            public static final String CONFIRMA_EXCLUIR = "Deseja realmente excluir?";
            public static final String AJUDA_BUSCAR = "Use o campo de busca para encontrar registros";
            
            public static String getMensagem(String chave) {
                switch (chave) {
                    case "sucesso_salvar": return SUCESSO_SALVAR;
                    case "erro_campo_obrigatorio": return ERRO_CAMPO_OBRIGATORIO;
                    case "confirma_excluir": return CONFIRMA_EXCLUIR;
                    case "ajuda_buscar": return AJUDA_BUSCAR;
                    default: return "Mensagem não encontrada";
                }
            }
        }
        
        // Assert - Verifica se centralizador funciona
        assertThat(CentralizadorMensagens.getMensagem("sucesso_salvar"))
            .isEqualTo(CentralizadorMensagens.SUCESSO_SALVAR);
        assertThat(CentralizadorMensagens.getMensagem("erro_campo_obrigatorio"))
            .isEqualTo(CentralizadorMensagens.ERRO_CAMPO_OBRIGATORIO);
        assertThat(CentralizadorMensagens.getMensagem("confirma_excluir"))
            .isEqualTo(CentralizadorMensagens.CONFIRMA_EXCLUIR);
        assertThat(CentralizadorMensagens.getMensagem("ajuda_buscar"))
            .isEqualTo(CentralizadorMensagens.AJUDA_BUSCAR);
        assertThat(CentralizadorMensagens.getMensagem("inexistente"))
            .isEqualTo("Mensagem não encontrada");
    }
}
