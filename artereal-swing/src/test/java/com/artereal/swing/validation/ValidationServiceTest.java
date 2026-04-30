package com.artereal.swing.validation;

import com.artereal.swing.model.Usuario;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para o ValidationService do Sistema ArteReal
 */
class ValidationServiceTest {

    @TempDir
    Path tempDir;
    
    private String originalUserHome;

    @BeforeEach
    void setUp() {
        originalUserHome = System.getProperty("user.home");
        System.setProperty("user.home", tempDir.toString());
    }

    @AfterEach
    void tearDown() {
        if (originalUserHome != null) {
            System.setProperty("user.home", originalUserHome);
        }
    }

    @Test
    @DisplayName("Deve validar usuário para criação com sucesso")
    void testValidateUsuarioForCreationSuccess() {
        Usuario usuario = new Usuario();
        usuario.setNome("Test User");
        usuario.setSenha("password123");
        usuario.setAdministrador(false);

        ValidationResult result = ValidationService.validateUsuarioForCreation(usuario);

        assertTrue(result.isValid(), "Usuário válido deve passar na validação");
        assertNull(result.getErrorMessage(), "Não deve haver mensagem de erro");
    }

    @Test
    @DisplayName("Deve falhar validação de usuário com nome nulo")
    void testValidateUsuarioForCreationNomeNulo() {
        Usuario usuario = new Usuario();
        usuario.setNome(null);
        usuario.setSenha("password123");

        ValidationResult result = ValidationService.validateUsuarioForCreation(usuario);

        assertFalse(result.isValid(), "Usuário com nome nulo deve falhar na validação");
        assertTrue(result.getErrorMessage().contains("Nome é obrigatório"), 
            "Mensagem deve mencionar nome obrigatório");
    }

    @Test
    @DisplayName("Deve falhar validação de usuário com nome muito curto")
    void testValidateUsuarioForCreationNomeCurto() {
        Usuario usuario = new Usuario();
        usuario.setNome("AB");
        usuario.setSenha("password123");

        ValidationResult result = ValidationService.validateUsuarioForCreation(usuario);

        assertFalse(result.isValid(), "Usuário com nome curto deve falhar na validação");
        assertTrue(result.getErrorMessage().contains("pelo menos 3 caracteres"), 
            "Mensagem deve mencionar tamanho mínimo");
    }

    @Test
    @DisplayName("Deve falhar validação de usuário com senha nula")
    void testValidateUsuarioForCreationSenhaNula() {
        Usuario usuario = new Usuario();
        usuario.setNome("Test User");
        usuario.setSenha(null);

        ValidationResult result = ValidationService.validateUsuarioForCreation(usuario);

        assertFalse(result.isValid(), "Usuário com senha nula deve falhar na validação");
        assertTrue(result.getErrorMessage().contains("Senha é obrigatória"), 
            "Mensagem deve mencionar senha obrigatória");
    }

    @Test
    @DisplayName("Deve falhar validação de usuário com senha muito curta")
    void testValidateUsuarioForCreationSenhaCurta() {
        Usuario usuario = new Usuario();
        usuario.setNome("Test User");
        usuario.setSenha("123");

        ValidationResult result = ValidationService.validateUsuarioForCreation(usuario);

        assertFalse(result.isValid(), "Usuário com senha curta deve falhar na validação");
        assertTrue(result.getErrorMessage().contains("pelo menos 6 caracteres"), 
            "Mensagem deve mencionar tamanho mínimo da senha");
    }

    @Test
    @DisplayName("Deve validar usuário para atualização com ID")
    void testValidateUsuarioForUpdateSuccess() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test User");
        usuario.setSenha("password123");

        ValidationResult result = ValidationService.validateUsuarioForUpdate(usuario);

        assertTrue(result.isValid(), "Usuário com ID deve passar na validação de atualização");
    }

    @Test
    @DisplayName("Deve falhar validação de usuário para atualização sem ID")
    void testValidateUsuarioForUpdateSemId() {
        Usuario usuario = new Usuario();
        usuario.setNome("Test User");
        usuario.setSenha("password123");

        ValidationResult result = ValidationService.validateUsuarioForUpdate(usuario);

        assertFalse(result.isValid(), "Usuário sem ID deve falhar na validação de atualização");
        assertTrue(result.getErrorMessage().contains("ID é obrigatório"), 
            "Mensagem deve mencionar ID obrigatório");
    }

    @Test
    @DisplayName("Deve validar credenciais para autenticação")
    void testValidateUsuarioForAuthenticationSuccess() {
        Usuario usuario = new Usuario();
        usuario.setNome("testuser");
        usuario.setSenha("password123");

        ValidationResult result = ValidationService.validateUsuarioForAuthentication(usuario);

        assertTrue(result.isValid(), "Credenciais válidas devem passar na validação");
    }

    @Test
    @DisplayName("Deve falhar validação de autenticação com nome vazio")
    void testValidateUsuarioForAuthenticationNomeVazio() {
        Usuario usuario = new Usuario();
        usuario.setNome("");
        usuario.setSenha("password123");

        ValidationResult result = ValidationService.validateUsuarioForAuthentication(usuario);

        assertFalse(result.isValid(), "Nome vazio deve falhar na validação");
        assertTrue(result.getErrorMessage().contains("Nome de usuário é obrigatório"), 
            "Mensagem deve mencionar nome obrigatório");
    }

    @Test
    @DisplayName("Deve validar username com formato correto")
    void testValidateUsernameSuccess() {
        ValidationResult result = ValidationService.validateUsername("testuser123");

        assertTrue(result.isValid(), "Username válido deve passar");
    }

    @Test
    @DisplayName("Deve falhar validação de username com caracteres especiais")
    void testValidateUsernameCaracteresEspeciais() {
        ValidationResult result = ValidationService.validateUsername("test@user");

        assertFalse(result.isValid(), "Username com caracteres especiais deve falhar");
        assertTrue(result.getErrorMessage().contains("apenas letras, números e underscores"), 
            "Mensagem deve mencionar formato inválido");
    }

    @Test
    @DisplayName("Deve validar email com formato correto")
    void testValidateEmailSuccess() {
        ValidationResult result = ValidationService.validateEmail("test@example.com");

        assertTrue(result.isValid(), "Email válido deve passar");
    }

    @Test
    @DisplayName("Deve falhar validação de email com formato incorreto")
    void testValidateEmailFormatoIncorreto() {
        ValidationResult result = ValidationService.validateEmail("email-invalido");

        assertFalse(result.isValid(), "Email inválido deve falhar");
    }

    @Test
    @DisplayName("Deve validar telefone com formato correto")
    void testValidateTelefoneSuccess() {
        ValidationResult result = ValidationService.validateTelefone("(11) 1234-5678");

        assertTrue(result.isValid(), "Telefone válido deve passar");
    }

    @Test
    @DisplayName("Deve falhar validação de telefone com formato incorreto")
    void testValidateTelefoneFormatoIncorreto() {
        ValidationResult result = ValidationService.validateTelefone("123456");

        assertFalse(result.isValid(), "Telefone inválido deve falhar");
    }

    @Test
    @DisplayName("Deve validar CEP com formato correto")
    void testValidateCEPSuccess() {
        ValidationResult result = ValidationService.validateCEP("12345-678");

        assertTrue(result.isValid(), "CEP válido deve passar");
    }

    @Test
    @DisplayName("Deve falhar validação de CEP com formato incorreto")
    void testValidateCEPFormatoIncorreto() {
        ValidationResult result = ValidationService.validateCEP("123456");

        assertFalse(result.isValid(), "CEP inválido deve falhar");
    }

    @Test
    @DisplayName("Deve validar nome completo")
    void testValidateNomeCompletoSuccess() {
        ValidationResult result = ValidationService.validateNomeCompleto("João da Silva");

        assertTrue(result.isValid(), "Nome completo válido deve passar");
    }

    @Test
    @DisplayName("Deve falhar validação de nome completo com números")
    void testValidateNomeCompletoComNumeros() {
        ValidationResult result = ValidationService.validateNomeCompleto("João123");

        assertFalse(result.isValid(), "Nome com números deve falhar");
    }

    @Test
    @DisplayName("Deve formatar mensagem de erro corretamente")
    void testFormatErrorMessage() {
        ValidationResult result = ValidationResult.invalid(
            new ValidationError("campo", "Mensagem de erro", "ERROR_CODE")
        );

        String mensagem = ValidationService.formatErrorMessage(result);

        assertEquals("Mensagem de erro", mensagem, "Mensagem formatada deve ser a mensagem de erro");
    }

    @Test
    @DisplayName("Deve retornar null para mensagem de erro quando validação é válida")
    void testFormatErrorMessageValid() {
        ValidationResult result = ValidationResult.valid();

        String mensagem = ValidationService.formatErrorMessage(result);

        assertNull(mensagem, "Mensagem deve ser null quando validação é válida");
    }

    @Test
    @DisplayName("Deve identificar strings não vazias corretamente")
    void testIsNotBlank() {
        assertTrue(ValidationService.isNotBlank("texto"), "Texto não deve ser considerado vazio");
        assertTrue(ValidationService.isNotBlank(" texto "), "Texto com espaços não deve ser considerado vazio");
        assertFalse(ValidationService.isNotBlank(""), "String vazia deve ser considerada vazia");
        assertFalse(ValidationService.isBlank("texto"), "Texto não deve ser considerado vazio");
        assertTrue(ValidationService.isBlank(""), "String vazia deve ser considerada vazia");
        assertTrue(ValidationService.isBlank(null), "Null deve ser considerado vazio");
    }

    @Test
    @DisplayName("Deve identificar números positivos corretamente")
    void testIsPositive() {
        assertTrue(ValidationService.isPositive(1), "1 deve ser positivo");
        assertTrue(ValidationService.isPositive(1.5), "1.5 deve ser positivo");
        assertFalse(ValidationService.isPositive(0), "0 não deve ser positivo");
        assertFalse(ValidationService.isPositive(-1), "-1 não deve ser positivo");
        assertFalse(ValidationService.isPositive(null), "Null não deve ser positivo");
    }

    @Test
    @DisplayName("Deve validar formato de email rapidamente")
    void testIsValidEmail() {
        assertTrue(ValidationService.isValidEmail("test@example.com"), "Email válido deve ser aceito");
        assertFalse(ValidationService.isValidEmail("email-invalido"), "Email inválido deve ser rejeitado");
        assertFalse(ValidationService.isValidEmail(null), "Null deve ser rejeitado");
    }

    @Test
    @DisplayName("Deve validar formato de telefone rapidamente")
    void testIsValidTelefone() {
        assertTrue(ValidationService.isValidTelefone("(11) 1234-5678"), "Telefone válido deve ser aceito");
        assertTrue(ValidationService.isValidTelefone("1234-5678"), "Telefone válido deve ser aceito");
        assertFalse(ValidationService.isValidTelefone("123456"), "Telefone inválido deve ser rejeitado");
    }

    @Test
    @DisplayName("Deve validar formato de CEP rapidamente")
    void testIsValidCEP() {
        assertTrue(ValidationService.isValidCEP("12345-678"), "CEP válido deve ser aceito");
        assertTrue(ValidationService.isValidCEP("12345678"), "CEP válido deve ser aceito");
        assertFalse(ValidationService.isValidCEP("123456"), "CEP inválido deve ser rejeitado");
    }

    @Test
    @DisplayName("Deve validar formato de CPF rapidamente")
    void testIsValidCPF() {
        assertTrue(ValidationService.isValidCPF("123.456.789-01"), "CPF válido deve ser aceito");
        assertTrue(ValidationService.isValidCPF("12345678901"), "CPF válido deve ser aceito");
        assertFalse(ValidationService.isValidCPF("123456"), "CPF inválido deve ser rejeitado");
    }

    @Test
    @DisplayName("Deve realizar validação rápida com sucesso")
    void testQuickValidateSuccess() {
        Validator<String> validator = CommonValidators.nomeCompleto("nome");
        
        boolean result = ValidationService.quickValidate(validator, "João Silva");
        
        assertTrue(result, "Validação rápida deve retornar true para dados válidos");
    }

    @Test
    @DisplayName("Deve realizar validação rápida com falha")
    void testQuickValidateFailure() {
        Validator<String> validator = CommonValidators.nomeCompleto("nome");
        
        boolean result = ValidationService.quickValidate(validator, "123");
        
        assertFalse(result, "Validação rápida deve retornar false para dados inválidos");
    }

    @Test
    @DisplayName("Deve lançar exceção quando validação falha")
    void testValidateOrThrowFailure() {
        Validator<String> validator = CommonValidators.nomeCompleto("nome");
        
        assertThrows(ValidationService.ValidationException.class, () -> {
            ValidationService.validateOrThrow(validator, "123");
        }, "Deve lançar exceção para dados inválidos");
    }

    @Test
    @DisplayName("Não deve lançar exceção quando validação tem sucesso")
    void testValidateOrThrowSuccess() {
        Validator<String> validator = CommonValidators.nomeCompleto("nome");
        
        assertDoesNotThrow(() -> {
            ValidationService.validateOrThrow(validator, "João Silva");
        }, "Não deve lançar exceção para dados válidos");
    }

    @Test
    @DisplayName("Deve lançar exceção com mensagem customizada")
    void testValidateOrThrowCustomMessage() {
        Validator<String> validator = CommonValidators.nomeCompleto("nome");
        
        ValidationService.ValidationException exception = assertThrows(
            ValidationService.ValidationException.class, () -> {
                ValidationService.validateOrThrow(validator, "123", "Erro de validação");
            }
        );
        
        assertTrue(exception.getMessage().contains("Erro de validação"), 
            "Mensagem customizada deve estar na exceção");
    }

    @Test
    @DisplayName("Deve formatar todas as mensagens de erro")
    void testFormatAllErrorMessages() {
        ValidationError error1 = new ValidationError("campo1", "Erro 1", "CODE1");
        ValidationError error2 = new ValidationError("campo2", "Erro 2", "CODE2");
        ValidationResult result = ValidationResult.invalid(java.util.Arrays.asList(error1, error2));

        String mensagem = ValidationService.formatAllErrorMessages(result);

        assertTrue(mensagem.contains("Erro 1"), "Mensagem deve conter primeiro erro");
        assertTrue(mensagem.contains("Erro 2"), "Mensagem deve conter segundo erro");
        assertTrue(mensagem.contains("Múltiplos erros"), "Mensagem deve indicar múltiplos erros");
    }

    @Test
    @DisplayName("Deve validar senha forte")
    void testValidateSenhaForte() {
        // Senha forte válida
        ValidationResult result = ValidationService.validateSenhaForte("Senha123@");
        assertTrue(result.isValid(), "Senha forte deve ser válida");

        // Senha fraca - sem letra maiúscula
        result = ValidationService.validateSenhaForte("senha123@");
        assertFalse(result.isValid(), "Senha sem maiúscula deve ser inválida");

        // Senha fraca - sem letra minúscula
        result = ValidationService.validateSenhaForte("SENHA123@");
        assertFalse(result.isValid(), "Senha sem minúscula deve ser inválida");

        // Senha fraca - sem número
        result = ValidationService.validateSenhaForte("SenhaABC@");
        assertFalse(result.isValid(), "Senha sem número deve ser inválida");

        // Senha fraca - sem caractere especial
        result = ValidationService.validateSenhaForte("Senha123");
        assertFalse(result.isValid(), "Senha sem caractere especial deve ser inválida");
    }

    @Test
    @DisplayName("Deve validar senha básica")
    void testValidatePassword() {
        // Senha válida
        ValidationResult result = ValidationService.validatePassword("password123");
        assertTrue(result.isValid(), "Senha válida deve passar");

        // Senha muito curta
        result = ValidationService.validatePassword("123");
        assertFalse(result.isValid(), "Senha curta deve ser inválida");

        // Senha nula
        result = ValidationService.validatePassword(null);
        assertFalse(result.isValid(), "Senha nula deve ser inválida");
    }
}
