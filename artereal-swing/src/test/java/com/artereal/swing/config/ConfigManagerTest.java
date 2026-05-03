package com.artereal.swing.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe ConfigManager
 */
@DisplayName("Testes Unitários - ConfigManager")
class ConfigManagerTest {

    @TempDir
    Path tempDir;

    private ConfigManager configManager;
    private String originalUserHome;

    @BeforeEach
    void setUp() {
        // Salva o diretório home original
        originalUserHome = System.getProperty("user.home");
        
        // Define um diretório temporário como home para testes
        System.setProperty("user.home", tempDir.toString());
        
        // Reseta a instância singleton para cada teste
        resetSingletonInstance();
        
        // Obtém nova instância
        configManager = ConfigManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Restaura o diretório home original
        System.setProperty("user.home", originalUserHome);
        
        // Reseta a instância singleton
        resetSingletonInstance();
    }

    /**
     * Reseta a instância singleton usando reflexão
     */
    private void resetSingletonInstance() {
        try {
            var field = ConfigManager.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            // Ignora erros de reflexão em ambiente de teste
        }
    }

    @Test
    @DisplayName("Deve implementar padrão Singleton corretamente")
    void testSingletonPattern() {
        // Act
        ConfigManager instance1 = ConfigManager.getInstance();
        ConfigManager instance2 = ConfigManager.getInstance();

        // Assert
        assertThat(instance1).isNotNull();
        assertThat(instance2).isNotNull();
        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("Deve criar arquivo de configurações no primeiro uso")
    void testCreateConfigFileOnFirstUse() throws IOException {
        // Arrange
        Path configFile = tempDir.resolve(".artereal/artereal.properties");

        // Act
        ConfigManager.getInstance(); // Trigger creation

        // Assert
        assertThat(Files.exists(configFile)).isTrue();
        assertThat(Files.size(configFile)).isGreaterThan(0);
    }

    @Test
    @DisplayName("Deve carregar configurações padrão")
    void testLoadDefaultConfigurations() {
        // Act & Assert
        assertThat(configManager.getString("database.url")).isEqualTo("jdbc:postgresql://localhost:5432/artereal_db");
        assertThat(configManager.getString("app.name")).isEqualTo("ArteReal - Sistema de Gestão Maçônica");
        assertThat(configManager.getString("app.version")).isEqualTo("1.0.0");
        assertThat(configManager.getString("ui.theme")).isEqualTo("default");
        assertThat(configManager.getString("ui.language")).isEqualTo("pt-BR");
        assertThat(configManager.getString("debug.enabled")).isEqualTo("false");
        assertThat(configManager.getString("cache.ttl")).isEqualTo("30");
        assertThat(configManager.getString("session.timeout")).isEqualTo("30");
    }

    @Test
    @DisplayName("Deve obter e definir propriedades")
    void testGetAndSetProperty() {
        // Arrange
        String key = "test.property";
        String value = "test.value";

        // Act
        configManager.set(key, value);
        String retrievedValue = configManager.getString(key);

        // Assert
        assertThat(retrievedValue).isEqualTo(value);
    }

    @Test
    @DisplayName("Deve retornar valor padrão quando propriedade não existe")
    void testGetPropertyWithDefault() {
        // Arrange
        String nonExistentKey = "non.existent.key";
        String defaultValue = "default.value";

        // Act
        String result = configManager.getString(nonExistentKey, defaultValue);

        // Assert
        assertThat(result).isEqualTo(defaultValue);
    }

    @Test
    @DisplayName("Deve retornar null quando propriedade não existe sem valor padrão")
    void testGetPropertyNonExistentWithoutDefault() {
        // Arrange
        String nonExistentKey = "non.existent.key";

        // Act
        String result = configManager.getString(nonExistentKey);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve converter propriedade para inteiro")
    void testGetPropertyAsInt() {
        // Arrange
        String key = "test.int.property";
        String value = "123";
        configManager.set(key, value);

        // Act
        int result = configManager.getInt(key, 0);

        // Assert
        assertThat(result).isEqualTo(123);
    }

    @Test
    @DisplayName("Deve retornar valor padrão quando conversão para inteiro falha")
    void testGetPropertyAsIntWithInvalidValue() {
        // Arrange
        String key = "test.invalid.int.property";
        String value = "not_a_number";
        configManager.set(key, value);

        // Act
        int result = configManager.getInt(key, 999);

        // Assert
        assertThat(result).isEqualTo(999);
    }

    @Test
    @DisplayName("Deve converter propriedade para boolean")
    void testGetPropertyAsBoolean() {
        // Arrange & Act
        configManager.set("test.true.property", "true");
        configManager.set("test.false.property", "false");
        configManager.set("test.yes.property", "YES");
        configManager.set("test.no.property", "NO");

        // Assert
        assertThat(configManager.getBoolean("test.true.property", false)).isTrue();
        assertThat(configManager.getBoolean("test.false.property", true)).isFalse();
        assertThat(configManager.getBoolean("test.yes.property", false)).isTrue();
        assertThat(configManager.getBoolean("test.no.property", true)).isFalse();
    }

    @Test
    @DisplayName("Deve retornar valor padrão quando conversão para boolean falha")
    void testGetPropertyAsBooleanWithInvalidValue() {
        // Arrange
        String key = "test.invalid.boolean.property";
        String value = "maybe";
        configManager.set(key, value);

        // Act
        boolean result = configManager.getBoolean(key, true);

        // Assert - "maybe" não é um valor boolean válido, deve retornar o padrão
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Deve salvar configurações no arquivo")
    void testSaveConfigurations() throws IOException {
        // Arrange
        String key = "test.save.property";
        String value = "saved.value";
        configManager.set(key, value);

        // Act
        configManager.saveConfigurations();

        // Reseta para forçar recarregamento
        resetSingletonInstance();
        ConfigManager newConfigManager = ConfigManager.getInstance();

        // Assert
        assertThat(newConfigManager.getString(key)).isEqualTo(value);
    }

    @Test
    @DisplayName("Deve lidar com propriedades nulas")
    void testHandleNullProperties() {
        // Act & Assert
        configManager.set("test.null.property", null);
        assertThat(configManager.getString("test.null.property")).isNull();
    }

    @Test
    @DisplayName("Deve lidar com chaves nulas")
    void testHandleNullKeys() {
        // Act & Assert
        configManager.set(null, "value");
        assertThat(configManager.getString(null)).isNull();
    }

    @Test
    @DisplayName("Deve remover propriedade")
    void testRemoveProperty() {
        // Arrange
        String key = "test.remove.property";
        String value = "to.be.removed";
        configManager.set(key, value);

        // Act
        configManager.remove(key);

        // Assert
        assertThat(configManager.getString(key)).isNull();
    }

    @Test
    @DisplayName("Deve verificar se propriedade existe")
    void testContainsProperty() {
        // Arrange
        String key = "test.contains.property";
        configManager.set(key, "value");

        // Act & Assert
        assertThat(configManager.contains(key)).isTrue();
        assertThat(configManager.contains("non.existent.property")).isFalse();
    }

    @Test
    @DisplayName("Deve obter todas as propriedades")
    void testGetAllProperties() {
        // Arrange
        configManager.set("test.prop1", "value1");
        configManager.set("test.prop2", "value2");

        // Act
        var properties = configManager.getAll();

        // Assert
        assertThat(properties).isNotNull();
        assertThat(properties).containsKey("test.prop1");
        assertThat(properties).containsKey("test.prop2");
        assertThat(properties.get("test.prop1")).isEqualTo("value1");
        assertThat(properties.get("test.prop2")).isEqualTo("value2");
    }

    @Test
    @DisplayName("Deve limpar todas as propriedades")
    void testClearProperties() {
        // Arrange
        configManager.set("test.prop1", "value1");
        configManager.set("test.prop2", "value2");

        // Act
        configManager.clear();

        // Assert
        assertThat(configManager.getAll()).isEmpty();
    }

    @Test
    @DisplayName("Deve lidar com configurações de UI")
    void testUIConfigurations() {
        // Act & Assert - Configurações padrão de UI
        assertThat(configManager.getInt("ui.window.width", 0)).isEqualTo(1200);
        assertThat(configManager.getInt("ui.window.height", 0)).isEqualTo(800);
        assertThat(configManager.getBoolean("ui.window.maximized", false)).isFalse();
    }

    @Test
    @DisplayName("Deve lidar com configurações de backup")
    void testBackupConfigurations() {
        // Act & Assert - Configurações padrão de backup
        assertThat(configManager.getBoolean("backup.auto.enabled", false)).isTrue();
        assertThat(configManager.getInt("backup.interval.hours", 0)).isEqualTo(24);
    }

    @Test
    @DisplayName("Deve lidar com configurações de logs")
    void testLogConfigurations() {
        // Act & Assert - Configurações padrão de logs
        assertThat(configManager.getString("logs.level")).isEqualTo("INFO");
        assertThat(configManager.getInt("logs.max.files", 0)).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve lidar com configurações de relatórios")
    void testReportConfigurations() {
        // Act & Assert - Configurações padrão de relatórios
        assertThat(configManager.getString("reports.default.format")).isEqualTo("PDF");
    }

    @Test
    @DisplayName("Deve lidar com valores vazios")
    void testHandleEmptyValues() {
        // Arrange
        String key = "test.empty.property";

        // Act
        configManager.set(key, "");
        String result = configManager.getString(key);

        // Assert
        assertThat(result).isEqualTo("");
        assertThat(configManager.contains(key)).isTrue();
    }

    @Test
    @DisplayName("Deve lidar com caracteres especiais em valores")
    void testHandleSpecialCharacters() {
        // Arrange
        String key = "test.special.property";
        String value = "valor com caracteres especiais: áéíóú ñç @#$%&*()";

        // Act
        configManager.set(key, value);
        String result = configManager.getString(key);

        // Assert
        assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("Deve lidar com chaves longas")
    void testHandleLongKeys() {
        // Arrange
        String longKey = "this.is.a.very.long.property.name.with.many.dots.and.words.to.test.system.robustness";
        String value = "long.key.value";

        // Act
        configManager.set(longKey, value);
        String result = configManager.getString(longKey);

        // Assert
        assertThat(result).isEqualTo(value);
    }

    @Test
    @DisplayName("Deve lidar com valores muito longos")
    void testHandleLongValues() {
        // Arrange
        String key = "test.long.value.property";
        StringBuilder longValue = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longValue.append("valor muito longo ");
        }

        // Act
        configManager.set(key, longValue.toString());
        String result = configManager.getString(key);

        // Assert
        assertThat(result).hasSizeGreaterThan(10000);
        assertThat(result).startsWith("valor muito longo");
    }

    @Test
    @DisplayName("Deve ser thread-safe")
    void testThreadSafety() throws InterruptedException {
        // Arrange
        int numThreads = 10;
        int operationsPerThread = 100;
        Thread[] threads = new Thread[numThreads];

        // Act
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    String key = "thread." + threadId + ".property." + j;
                    String value = "value." + threadId + "." + j;
                    configManager.set(key, value);
                    
                    // Verifica se o valor foi salvo corretamente
                    assertThat(configManager.getString(key)).isEqualTo(value);
                }
            });
        }

        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - Verifica se todas as propriedades foram salvas
        int expectedProperties = numThreads * operationsPerThread;
        assertThat(configManager.getAll().size()).isGreaterThanOrEqualTo(expectedProperties);
    }

    @Test
    @DisplayName("Deve persistir configurações entre instâncias")
    void testPersistBetweenInstances() throws IOException {
        // Arrange
        String key = "test.persistence.property";
        String value = "persistent.value";

        // Act - Configura na primeira instância
        configManager.set(key, value);
        configManager.saveConfigurations();

        // Cria nova instância (simula reinicialização)
        resetSingletonInstance();
        ConfigManager newConfigManager = ConfigManager.getInstance();

        // Assert
        assertThat(newConfigManager.getString(key)).isEqualTo(value);
    }

    @Test
    @DisplayName("Deve lidar com sobrecarga de valores existentes")
    void testOverwriteExistingValues() {
        // Arrange
        String key = "test.overwrite.property";
        configManager.set(key, "original.value");

        // Act
        configManager.set(key, "new.value");

        // Assert
        assertThat(configManager.getString(key)).isEqualTo("new.value");
    }

    @Test
    @DisplayName("Deve lidar com remoção de propriedade não existente")
    void testRemoveNonExistentProperty() {
        // Arrange
        String nonExistentKey = "non.existent.property";

        // Act & Assert - Não deve lançar exceção
        configManager.remove(nonExistentKey);
        assertThat(configManager.contains(nonExistentKey)).isFalse();
    }
}
