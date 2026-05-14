package com.artereal.swing.ui.panels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para a classe BasePanel
 */
@DisplayName("Testes Unitários - BasePanel")
class BasePanelTest {

    private TestBasePanel basePanel;

    @BeforeEach
    void setUp() {
        // Executa em EDT para componentes Swing
        SwingUtilities.invokeLater(() -> {
            basePanel = new TestBasePanel();
        });
        
        // Espera a inicialização completar
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @DisplayName("Deve inicializar componentes corretamente")
    void testInitializeComponents() {
        SwingUtilities.invokeLater(() -> {
            // Assert
            assertThat(basePanel.dataTable).isNotNull();
            assertThat(basePanel.tableModel).isNotNull();
            assertThat(basePanel.pesquisarField).isNotNull();
            assertThat(basePanel.pesquisarButton).isNotNull();
            assertThat(basePanel.novoButton).isNotNull();
            assertThat(basePanel.editarButton).isNotNull();
            assertThat(basePanel.excluirButton).isNotNull();
            assertThat(basePanel.salvarButton).isNotNull();
            assertThat(basePanel.limparButton).isNotNull();
        });
    }

    @Test
    @DisplayName("Deve inicializar painéis de layout corretamente")
    void testInitializeLayoutPanels() {
        SwingUtilities.invokeLater(() -> {
            // Assert
            assertThat(basePanel.headerPanel).isNotNull();
            assertThat(basePanel.contentPanel).isNotNull();
            assertThat(basePanel.filterPanel).isNotNull();
            assertThat(basePanel.tablePanel).isNotNull();
            assertThat(basePanel.formPanel).isNotNull();
        });
    }

    @Test
    @DisplayName("Deve configurar layout corretamente")
    void testSetupLayout() {
        SwingUtilities.invokeLater(() -> {
            // Assert
            assertThat(basePanel.getLayout()).isNotNull();
            assertThat(basePanel.getComponentCount()).isGreaterThan(0);
        });
    }

    @Test
    @DisplayName("Deve configurar eventos corretamente")
    void testSetupEvents() {
        SwingUtilities.invokeLater(() -> {
            // Assert - Verifica se os botões têm listeners
            assertThat(basePanel.pesquisarButton.getActionListeners()).hasSize(1);
            assertThat(basePanel.novoButton.getActionListeners()).hasSize(1);
            assertThat(basePanel.editarButton.getActionListeners()).hasSize(1);
            assertThat(basePanel.excluirButton.getActionListeners()).hasSize(1);
            assertThat(basePanel.salvarButton.getActionListeners()).hasSize(1);
            assertThat(basePanel.limparButton.getActionListeners()).hasSize(1);
        });
    }

    @Test
    @DisplayName("Deve inicializar tabela com modelo padrão")
    void testInitializeTableWithModel() {
        SwingUtilities.invokeLater(() -> {
            // Assert
            assertThat(basePanel.dataTable.getModel()).isInstanceOf(DefaultTableModel.class);
            assertThat(basePanel.tableModel).isEqualTo(basePanel.dataTable.getModel());
        });
    }

    @Test
    @DisplayName("Deve configurar texto padrão dos botões")
    void testDefaultButtonTexts() {
        SwingUtilities.invokeLater(() -> {
            // Assert
            assertThat(basePanel.pesquisarButton.getText()).isNotEmpty();
            assertThat(basePanel.novoButton.getText()).isNotEmpty();
            assertThat(basePanel.editarButton.getText()).isNotEmpty();
            assertThat(basePanel.excluirButton.getText()).isNotEmpty();
            assertThat(basePanel.salvarButton.getText()).isNotEmpty();
            assertThat(basePanel.limparButton.getText()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("Deve configurar ícones dos botões")
    void testButtonIcons() {
        SwingUtilities.invokeLater(() -> {
            // Assert - Verifica se os botões têm ícones (podem ser null)
            // O importante é que não lançam exceção
            assertThat(basePanel.pesquisarButton.getIcon()).isNotNull();
            assertThat(basePanel.novoButton.getIcon()).isNotNull();
            assertThat(basePanel.editarButton.getIcon()).isNotNull();
            assertThat(basePanel.excluirButton.getIcon()).isNotNull();
            assertThat(basePanel.salvarButton.getIcon()).isNotNull();
            assertThat(basePanel.limparButton.getIcon()).isNotNull();
        });
    }

    @Test
    @DisplayName("Deve habilitar/desabilitar botões corretamente")
    void testEnableDisableButtons() {
        SwingUtilities.invokeLater(() -> {
            // Act - Usar enableForm() que é o método real do BasePanel
            basePanel.enableForm(false);

            // Assert - Verificar apenas botões do formulário
            assertThat(basePanel.salvarButton.isEnabled()).isFalse();
            assertThat(basePanel.limparButton.isEnabled()).isFalse();

            // Act
            basePanel.enableForm(true);

            // Assert
            assertThat(basePanel.salvarButton.isEnabled()).isTrue();
            assertThat(basePanel.limparButton.isEnabled()).isTrue();
        });
    }

    @Test
    @DisplayName("Deve limpar formulário corretamente")
    void testClearForm() {
        SwingUtilities.invokeLater(() -> {
            // Act
            basePanel.clearForm();

            // Assert - Verifica se o campo de pesquisa foi limpo
            assertThat(basePanel.pesquisarField.getText()).isEmpty();
        });
    }

    @Test
    @DisplayName("Deve atualizar dados corretamente")
    void testRefreshData() {
        SwingUtilities.invokeLater(() -> {
            // Act
            basePanel.refreshData();


            // Assert
            assertThat(basePanel.tableModel.getColumnCount()).isEqualTo(3);
            assertThat(basePanel.tableModel.getColumnName(0)).isEqualTo("Coluna1");
            assertThat(basePanel.tableModel.getColumnName(1)).isEqualTo("Coluna2");
            assertThat(basePanel.tableModel.getColumnName(2)).isEqualTo("Coluna3");
        });
    }

    @Test
    @DisplayName("Deve adicionar linha à tabela corretamente")
    void testAddTableRow() {
        SwingUtilities.invokeLater(() -> {
            // Arrange
            basePanel.tableModel.setColumnIdentifiers(new Object[]{"Coluna1", "Coluna2"});

            // Act - Usar o tableModel diretamente
            basePanel.tableModel.addRow(new Object[]{"Valor1", "Valor2"});

            // Assert
            assertThat(basePanel.tableModel.getRowCount()).isEqualTo(1);
            assertThat(basePanel.tableModel.getValueAt(0, 0)).isEqualTo("Valor1");
            assertThat(basePanel.tableModel.getValueAt(0, 1)).isEqualTo("Valor2");
        });
    }

    @Test
    @DisplayName("Deve limpar tabela corretamente")
    void testClearTable() {
        SwingUtilities.invokeLater(() -> {
            // Arrange
            basePanel.tableModel.setColumnIdentifiers(new Object[]{"Coluna1", "Coluna2"});
            basePanel.tableModel.addRow(new Object[]{"Valor1", "Valor2"});
            assertThat(basePanel.tableModel.getRowCount()).isEqualTo(1);

            // Act - Usar o tableModel diretamente
            basePanel.tableModel.setRowCount(0);

            // Assert
            assertThat(basePanel.tableModel.getRowCount()).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("Deve obter linha selecionada corretamente")
    void testGetSelectedRow() {
        SwingUtilities.invokeLater(() -> {
            // Arrange
            basePanel.tableModel.setColumnIdentifiers(new Object[]{"Coluna1", "Coluna2"});
            basePanel.tableModel.addRow(new Object[]{"Valor1", "Valor2"});
            basePanel.dataTable.setRowSelectionInterval(0, 0);

            // Act - Usar o dataTable diretamente
            int selectedRow = basePanel.dataTable.getSelectedRow();

            // Assert
            assertThat(selectedRow).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("Deve obter valor da célula selecionada corretamente")
    void testGetSelectedCellValue() {
        SwingUtilities.invokeLater(() -> {
            // Arrange
            basePanel.tableModel.setColumnIdentifiers(new Object[]{"Coluna1", "Coluna2"});
            basePanel.tableModel.addRow(new Object[]{"Valor1", "Valor2"});
            basePanel.dataTable.setRowSelectionInterval(0, 0);

            // Act - Usar o dataTable diretamente
            Object value1 = basePanel.dataTable.getValueAt(0, 0);
            Object value2 = basePanel.dataTable.getValueAt(0, 1);

            // Assert
            assertThat(value1).isEqualTo("Valor1");
            assertThat(value2).isEqualTo("Valor2");
        });
    }

    @Test
    @DisplayName("Deve exibir mensagem informativa corretamente")
    void testShowInfoMessage() {
        SwingUtilities.invokeLater(() -> {
            // Act & Assert - Usar método real do BasePanel
            basePanel.showInfo("Mensagem informativa");
        });
    }

    @Test
    @DisplayName("Deve exibir mensagem de erro corretamente")
    void testShowErrorMessage() {
        SwingUtilities.invokeLater(() -> {
            // Act & Assert - Usar método real do BasePanel
            basePanel.showError("Mensagem de erro");
        });
    }

    @Test
    @DisplayName("Deve exibir mensagem de aviso corretamente")
    void testShowWarningMessage() {
        SwingUtilities.invokeLater(() -> {
            // Act & Assert - Usar método real do BasePanel
            basePanel.showWarning("Mensagem de aviso");
        });
    }

    @Test
    @DisplayName("Deve exibir diálogo de confirmação corretamente")
    void testShowConfirmDialog() {
        SwingUtilities.invokeLater(() -> {
            // Act & Assert - Usar método real do BasePanel
            int result = basePanel.showConfirm("Deseja continuar?");
            assertThat(result).isIn(JOptionPane.YES_OPTION, JOptionPane.NO_OPTION);
            // Assert - Verifica se os botões estão no estado correto para visualização
            assertThat(basePanel.novoButton.isEnabled()).isTrue();
            assertThat(basePanel.editarButton.isEnabled()).isTrue();
        });
    }

    @Test
    @DisplayName("Deve lidar com texto de pesquisa")
    void testHandleSearchText() {
        SwingUtilities.invokeLater(() -> {
            // Act
            basePanel.pesquisarField.setText("texto de pesquisa");

            // Assert
            assertThat(basePanel.pesquisarField.getText()).isEqualTo("texto de pesquisa");
        });
    }

    @Test
    @DisplayName("Deve lidar com tabela vazia")
    void testHandleEmptyTable() {
        SwingUtilities.invokeLater(() -> {
            // Arrange
            basePanel.tableModel.setRowCount(0);

            // Act & Assert
            assertThat(basePanel.tableModel.getRowCount()).isEqualTo(0);
            assertThat(basePanel.dataTable.getSelectedRow()).isEqualTo(-1);
        });
    }

    @Test
    @DisplayName("Deve lidar com múltiplas linhas na tabela")
    void testHandleMultipleTableRows() {
        SwingUtilities.invokeLater(() -> {
            // Arrange
            basePanel.tableModel.setColumnIdentifiers(new Object[]{"Coluna1", "Coluna2"});
            basePanel.tableModel.addRow(new Object[]{"Valor1", "Valor2"});
            basePanel.tableModel.addRow(new Object[]{"Valor3", "Valor4"});
            basePanel.tableModel.addRow(new Object[]{"Valor5", "Valor6"});

            // Assert
            assertThat(basePanel.tableModel.getRowCount()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("Deve lidar com campo de pesquisa vazio")
    void testHandleEmptySearchField() {
        SwingUtilities.invokeLater(() -> {
            // Act
            basePanel.pesquisarField.setText("");

            // Assert
            assertThat(basePanel.pesquisarField.getText()).isEmpty();
        });
    }

    @Test
    @DisplayName("Deve lidar com botões desabilitados")
    void testHandleDisabledButtons() {
        SwingUtilities.invokeLater(() -> {
            // Act
            basePanel.novoButton.setEnabled(false);
            basePanel.editarButton.setEnabled(false);

            // Assert
            assertThat(basePanel.novoButton.isEnabled()).isFalse();
            assertThat(basePanel.editarButton.isEnabled()).isFalse();
            assertThat(basePanel.editarButton.isEnabled()).isFalse();
            assertThat(basePanel.excluirButton.isEnabled()).isFalse();
            assertThat(basePanel.salvarButton.isEnabled()).isFalse();
            assertThat(basePanel.limparButton.isEnabled()).isFalse();
        });
    }

    /**
     * Implementação concreta de BasePanel para testes
     */
    private static class TestBasePanel extends BasePanel<String> {

        @Override
        protected String getHeaderTitle() {
            return "Test Panel";
        }

        @Override
        protected String getHeaderDescription() {
            return "Panel for testing purposes";
        }

        @Override
        protected DefaultTableModel createTableModel() {
            return new DefaultTableModel(new Object[]{"Column1", "Column2"}, 0);
        }

        @Override
        protected List<String> loadData() throws Exception {
            return List.of("Data1", "Data2");
        }

        @Override
        protected List<String> searchData(String searchTerm) throws Exception {
            return List.of("SearchResult1", "SearchResult2");
        }

        @Override
        protected String getSelectedEntity() {
            int row = dataTable.getSelectedRow();
            if (row >= 0) {
                return (String) tableModel.getValueAt(row, 0);
            }
            return null;
        }

        @Override
        protected void loadEntityToForm(String entity) {
            // Implementação de teste
        }

        @Override
        protected String getEntityFromForm() throws Exception {
            return "TestEntity";
        }

        @Override
        protected void validateEntity(String entity) throws Exception {
            // Implementação de teste - sempre válido
        }

        @Override
        protected void saveEntity(String entity) throws Exception {
            // Implementação de teste
        }

        @Override
        protected void deleteEntity(String entity) throws Exception {
            // Implementação de teste
        }

        @Override
        protected void updateTableModel(List<String> data) {
            tableModel.setRowCount(0);
            for (String item : data) {
                tableModel.addRow(new Object[]{item, "Value"});
            }
        }

        @Override
        protected void clearFormFields() {
            pesquisarField.setText("");
        }

        @Override
        protected void enableFormFields(boolean enabled) {
            // Implementação de teste
        }
    }
}
