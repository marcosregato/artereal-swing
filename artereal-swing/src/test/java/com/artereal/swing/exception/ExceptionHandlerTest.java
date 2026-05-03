package com.artereal.swing.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para a classe ExceptionHandler
 */
@DisplayName("Testes Unitários - ExceptionHandler")
class ExceptionHandlerTest {

    private Component parentComponent;
    private MockedStatic<JOptionPane> mockedJOptionPane;

    @BeforeEach
    void setUp() {
        parentComponent = new JPanel();
        mockedJOptionPane = Mockito.mockStatic(JOptionPane.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedJOptionPane != null) {
            mockedJOptionPane.close();
        }
    }

    @Test
    @DisplayName("Deve tratar exceção genérica com componente pai")
    void testHandleGenericExceptionWithParent() {
        // Arrange
        Exception testException = new RuntimeException("Test exception message");

        // Act
        ExceptionHandler.handle(testException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção genérica com contexto personalizado")
    void testHandleGenericExceptionWithContext() {
        // Arrange
        Exception testException = new RuntimeException("Test exception message");
        String context = "Contexto específico do erro";

        // Act
        ExceptionHandler.handle(testException, parentComponent, context);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção de banco de dados")
    void testHandleDatabaseException() {
        // Arrange
        SQLException sqlException = new SQLException("Database connection failed");

        // Act
        ExceptionHandler.handleDatabase(sqlException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro de Banco de Dados"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção de banco de dados com mensagem específica")
    void testHandleDatabaseExceptionWithSpecificMessage() {
        // Arrange
        SQLException sqlException = new SQLException("Connection timeout");

        // Act
        ExceptionHandler.handleDatabase(sqlException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro de Banco de Dados"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção com componente pai nulo")
    void testHandleExceptionWithNullParent() {
        // Arrange
        Exception testException = new RuntimeException("Test exception");

        // Act
        ExceptionHandler.handle(testException, null);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            isNull(),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção com contexto nulo")
    void testHandleExceptionWithNullContext() {
        // Arrange
        Exception testException = new RuntimeException("Test exception");

        // Act
        ExceptionHandler.handle(testException, parentComponent, null);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção com mensagem vazia")
    void testHandleExceptionWithEmptyMessage() {
        // Arrange
        Exception testException = new RuntimeException("");

        // Act
        ExceptionHandler.handle(testException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar SQLException com mensagem de usuário amigável")
    void testHandleSQLExceptionWithUserFriendlyMessage() {
        // Arrange
        SQLException sqlException = new SQLException("Unique constraint violation");

        // Act
        ExceptionHandler.handleDatabase(sqlException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro de Banco de Dados"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar TimeoutException")
    void testHandleTimeoutException() {
        // Arrange
        TimeoutException timeoutException = new TimeoutException("Operation timed out");

        // Act
        ExceptionHandler.handle(timeoutException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar NumberFormatException")
    void testHandleNumberFormatException() {
        // Arrange
        NumberFormatException numberFormatException = new NumberFormatException("For input string: \"abc\"");

        // Act
        ExceptionHandler.handle(numberFormatException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar IllegalArgumentException")
    void testHandleIllegalArgumentException() {
        // Arrange
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Invalid argument");

        // Act
        ExceptionHandler.handle(illegalArgumentException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar NullPointerException")
    void testHandleNullPointerException() {
        // Arrange
        NullPointerException nullPointerException = new NullPointerException("Null pointer access");

        // Act
        ExceptionHandler.handle(nullPointerException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção com causa")
    void testHandleExceptionWithCause() {
        // Arrange
        Exception cause = new SQLException("Database error");
        Exception testException = new RuntimeException("Wrapper exception", cause);

        // Act
        ExceptionHandler.handle(testException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção com mensagem longa")
    void testHandleExceptionWithLongMessage() {
        // Arrange
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longMessage.append("Esta é uma mensagem de erro muito longa ");
        }
        Exception testException = new RuntimeException(longMessage.toString());

        // Act
        ExceptionHandler.handle(testException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção com caracteres especiais")
    void testHandleExceptionWithSpecialCharacters() {
        // Arrange
        Exception testException = new RuntimeException("Erro com caracteres especiais: áéíóú ñç @#$%&*()");

        // Act
        ExceptionHandler.handle(testException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar múltiplas exceções em sequência")
    void testHandleMultipleExceptionsInSequence() {
        // Arrange
        Exception exception1 = new RuntimeException("First error");
        Exception exception2 = new SQLException("Second error");

        // Act
        ExceptionHandler.handle(exception1, parentComponent);
        ExceptionHandler.handleDatabase(exception2, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            any(String.class),
            eq(JOptionPane.ERROR_MESSAGE)
        ), times(2));
    }

    @Test
    @DisplayName("Deve tratar exceção sem mensagem")
    void testHandleExceptionWithoutMessage() {
        // Arrange
        Exception testException = new RuntimeException((String) null);

        // Act
        ExceptionHandler.handle(testException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção customizada")
    void testHandleCustomException() {
        // Arrange
        Exception customException = new CustomTestException("Custom error message");

        // Act
        ExceptionHandler.handle(customException, parentComponent);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção de banco de dados com componente pai nulo")
    void testHandleDatabaseExceptionWithNullParent() {
        // Arrange
        SQLException sqlException = new SQLException("Database error");

        // Act
        ExceptionHandler.handleDatabase(sqlException, null);

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            isNull(),
            any(String.class),
            eq("Erro de Banco de Dados"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    @Test
    @DisplayName("Deve tratar exceção com contexto vazio")
    void testHandleExceptionWithEmptyContext() {
        // Arrange
        Exception testException = new RuntimeException("Test error");

        // Act
        ExceptionHandler.handle(testException, parentComponent, "");

        // Assert
        mockedJOptionPane.verify(() -> JOptionPane.showMessageDialog(
            eq(parentComponent),
            any(String.class),
            eq("Erro"),
            eq(JOptionPane.ERROR_MESSAGE)
        ));
    }

    /**
     * Classe de exceção customizada para testes
     */
    private static class CustomTestException extends Exception {
        public CustomTestException(String message) {
            super(message);
        }
    }
}
