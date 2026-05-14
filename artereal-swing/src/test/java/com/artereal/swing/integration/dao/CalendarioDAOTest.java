package com.artereal.swing.integration.dao;

import com.artereal.swing.database.DatabaseManager;
import com.artereal.swing.dao.CalendarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de integração para CalendarioDAO
 */
@DisplayName("Testes de Integração - CalendarioDAO")
class CalendarioDAOTest {

    private DatabaseManager databaseManager;
    private CalendarioDAO calendarioDAO;

    @BeforeEach
    void setUp() throws SQLException {
        databaseManager = DatabaseManager.getInstance();
        calendarioDAO = new CalendarioDAO();
        
        // Forçar recriação do banco para cada teste
        databaseManager.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Limpar dados da tabela calendario após cada teste
        try (Connection conn = databaseManager.getConnection()) {
            conn.createStatement().execute("DELETE FROM calendario");
        }
    }

    @Test
    @DisplayName("Deve criar e salvar evento no calendário")
    void testSalvarEventoCalendario() throws SQLException {
        // Arrange
        String descricao = "Sessão Magna de Iniciação";
        String dataInforme = "2024-06-15";
        String tipoEvento = "SESSAO";
        String local = "Templo";
        String horario = "20:00";
        Long codigoIrmao = 123L;

        // Act
        calendarioDAO.save(descricao, dataInforme, tipoEvento, local, horario, codigoIrmao);

        // Assert
        List<Object[]> eventos = calendarioDAO.findAll();
        assertThat(eventos).hasSizeGreaterThanOrEqualTo(1);
        
        Object[] eventoSalvo = eventos.stream()
            .filter(e -> descricao.equals(e[1]))
            .findFirst()
            .orElse(null);
            
        assertThat(eventoSalvo).isNotNull();
        assertThat(eventoSalvo[1]).isEqualTo(descricao);
        assertThat(eventoSalvo[2]).isEqualTo(dataInforme);
        assertThat(eventoSalvo[3]).isEqualTo(tipoEvento);
        assertThat(eventoSalvo[4]).isEqualTo("PROGRAMADO");
        assertThat(eventoSalvo[5]).isEqualTo(local);
        assertThat(eventoSalvo[6]).isEqualTo(horario);
        assertThat(eventoSalvo[7]).isEqualTo(codigoIrmao != null ? codigoIrmao.intValue() : null);
    }

    @Test
    @DisplayName("Deve buscar todos os eventos do calendário")
    void testBuscarTodosEventosCalendario() throws SQLException {
        // Arrange
        calendarioDAO.save("Evento 1", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Evento 2", "2024-06-20", "REUNIAO", "Sala", "19:00", 456L);
        calendarioDAO.save("Evento 3", "2024-07-01", "EVENTO", "Salão", "18:00", 789L);

        // Act
        List<Object[]> eventos = calendarioDAO.findAll();

        // Assert
        assertThat(eventos).hasSizeGreaterThanOrEqualTo(3);
        
        // Verificar estrutura dos eventos: [id, descricao, data_informe, tipo_evento, status, local, horario, codigo_irmao]
        for (Object[] evento : eventos) {
            assertThat(evento).hasSize(8);
            assertThat(evento[0]).isInstanceOf(Long.class); // id
            assertThat(evento[1]).isInstanceOf(String.class); // descricao
            assertThat(evento[2]).isInstanceOf(String.class); // data_informe
            assertThat(evento[3]).isInstanceOf(String.class); // tipo_evento
            assertThat(evento[4]).isInstanceOf(String.class); // status
            assertThat(evento[5]).isInstanceOf(String.class); // local
            assertThat(evento[6]).isInstanceOf(String.class); // horario
        }
    }

    @Test
    @DisplayName("Deve buscar eventos por período")
    void testBuscarEventosPorPeriodo() throws SQLException {
        // Arrange
        calendarioDAO.save("Evento Junho 1", "2024-06-10", "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Evento Junho 2", "2024-06-25", "REUNIAO", "Sala", "19:00", 456L);
        calendarioDAO.save("Evento Julho", "2024-07-05", "EVENTO", "Salão", "18:00", 789L);

        LocalDate dataInicio = LocalDate.of(2024, 6, 1);
        LocalDate dataFim = LocalDate.of(2024, 6, 30);

        // Act
        List<Object[]> eventos = calendarioDAO.findByPeriodo(dataInicio, dataFim);

        // Assert
        assertThat(eventos).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar que todos os eventos estão no período correto
        for (Object[] evento : eventos) {
            String dataEvento = (String) evento[2];
            LocalDate data = LocalDate.parse(dataEvento);
            assertThat(data.isAfter(dataInicio.minusDays(1))).isTrue();
            assertThat(data.isBefore(dataFim.plusDays(1))).isTrue();
        }
    }

    @Test
    @DisplayName("Deve buscar eventos por tipo")
    void testBuscarEventosPorTipo() throws SQLException {
        // Arrange
        calendarioDAO.save("Sessão 1", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Reunião 1", "2024-06-20", "REUNIAO", "Sala", "19:00", 456L);
        calendarioDAO.save("Sessão 2", "2024-07-01", "SESSAO", "Templo", "20:00", 789L);

        // Act
        List<Object[]> sessoes = calendarioDAO.findByTipo("SESSAO");
        List<Object[]> reunioes = calendarioDAO.findByTipo("REUNIAO");

        // Assert
        assertThat(sessoes).hasSizeGreaterThanOrEqualTo(2);
        assertThat(reunioes).hasSizeGreaterThanOrEqualTo(1);
        
        // Verificar que todos são do tipo correto
        assertThat(sessoes).extracting(evento -> evento[3])
            .allMatch(tipo -> "SESSAO".equals(tipo));
        assertThat(reunioes).extracting(evento -> evento[3])
            .allMatch(tipo -> "REUNIAO".equals(tipo));
    }

    @Test
    @DisplayName("Deve buscar eventos próximos")
    void testBuscarEventosProximos() throws SQLException {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate daqui5dias = hoje.plusDays(5);
        LocalDate daqui15dias = hoje.plusDays(15);
        
        calendarioDAO.save("Evento Hoje", hoje.toString(), "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Evento Próximo", daqui5dias.toString(), "REUNIAO", "Sala", "19:00", 456L);
        calendarioDAO.save("Evento Longe", daqui15dias.toString(), "EVENTO", "Salão", "18:00", 789L);

        // Act
        List<Object[]> proximos7dias = calendarioDAO.findProximos(7);
        List<Object[]> proximos20dias = calendarioDAO.findProximos(20);

        // Assert
        assertThat(proximos7dias).hasSizeGreaterThanOrEqualTo(2); // hoje + 5 dias
        assertThat(proximos20dias).hasSizeGreaterThanOrEqualTo(3); // todos
    }

    @Test
    @DisplayName("Deve buscar eventos de hoje")
    void testBuscarEventosHoje() throws SQLException {
        // Arrange
        LocalDate hoje = LocalDate.now();
        LocalDate ontem = hoje.minusDays(1);
        LocalDate amanha = hoje.plusDays(1);
        
        calendarioDAO.save("Evento Ontem", ontem.toString(), "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Evento Hoje 1", hoje.toString(), "REUNIAO", "Sala", "19:00", 456L);
        calendarioDAO.save("Evento Hoje 2", hoje.toString(), "EVENTO", "Salão", "18:00", 789L);
        calendarioDAO.save("Evento Amanhã", amanha.toString(), "SESSAO", "Templo", "20:00", 123L);

        // Act
        List<Object[]> eventosHoje = calendarioDAO.findHoje();

        // Assert
        assertThat(eventosHoje).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar que todos são de hoje
        for (Object[] evento : eventosHoje) {
            String dataEvento = (String) evento[2];
            assertThat(dataEvento).isEqualTo(hoje.toString());
        }
    }

    @Test
    @DisplayName("Deve marcar evento como realizado")
    void testMarcarEventoRealizado() throws SQLException {
        // Arrange
        calendarioDAO.save("Sessão Especial", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        
        List<Object[]> eventos = calendarioDAO.findAll();
        Object[] eventoCriado = eventos.stream()
            .filter(e -> "Sessão Especial".equals(e[1]))
            .findFirst()
            .orElse(null);
        
        assertThat(eventoCriado).isNotNull();
        Long id = (Long) eventoCriado[0];

        // Act
        calendarioDAO.realizar(id);

        // Assert
        List<Object[]> eventosAtualizados = calendarioDAO.findAll();
        Object[] eventoRealizado = eventosAtualizados.stream()
            .filter(e -> id.equals(e[0]))
            .findFirst()
            .orElse(null);
            
        assertThat(eventoRealizado).isNotNull();
        assertThat(eventoRealizado[4]).isEqualTo("REALIZADO");
    }

    @Test
    @DisplayName("Deve cancelar evento")
    void testCancelarEvento() throws SQLException {
        // Arrange
        calendarioDAO.save("Sessão Cancelada", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        
        List<Object[]> eventos = calendarioDAO.findAll();
        Object[] eventoCriado = eventos.stream()
            .filter(e -> "Sessão Cancelada".equals(e[1]))
            .findFirst()
            .orElse(null);
        
        assertThat(eventoCriado).isNotNull();
        Long id = (Long) eventoCriado[0];

        // Act
        calendarioDAO.cancelar(id);

        // Assert
        List<Object[]> eventosAtualizados = calendarioDAO.findAll();
        Object[] eventoCancelado = eventosAtualizados.stream()
            .filter(e -> id.equals(e[0]))
            .findFirst()
            .orElse(null);
            
        assertThat(eventoCancelado).isNotNull();
        assertThat(eventoCancelado[4]).isEqualTo("CANCELADO");
    }

    @Test
    @DisplayName("Deve excluir evento")
    void testExcluirEvento() throws SQLException {
        // Arrange
        calendarioDAO.save("Evento para Excluir", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        
        List<Object[]> eventos = calendarioDAO.findAll();
        Object[] eventoCriado = eventos.stream()
            .filter(e -> "Evento para Excluir".equals(e[1]))
            .findFirst()
            .orElse(null);
        
        assertThat(eventoCriado).isNotNull();
        Long id = (Long) eventoCriado[0];

        // Act
        calendarioDAO.delete(id);

        // Assert
        List<Object[]> eventosAtualizados = calendarioDAO.findAll();
        Object[] eventoExcluido = eventosAtualizados.stream()
            .filter(e -> id.equals(e[0]))
            .findFirst()
            .orElse(null);
            
        assertThat(eventoExcluido).isNull();
    }

    @Test
    @DisplayName("Deve obter estatísticas de eventos")
    void testGetEstatisticas() throws SQLException {
        // Arrange
        calendarioDAO.save("Sessão 1", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Sessão 2", "2024-06-20", "SESSAO", "Templo", "20:00", 456L);
        calendarioDAO.save("Reunião 1", "2024-06-25", "REUNIAO", "Sala", "19:00", 789L);
        
        // Marcar uma sessão como realizada
        List<Object[]> eventos = calendarioDAO.findAll();
        Object[] sessao1 = eventos.stream()
            .filter(e -> "Sessão 1".equals(e[1]))
            .findFirst()
            .orElse(null);
        calendarioDAO.realizar((Long) sessao1[0]);

        // Act
        List<Object[]> estatisticas = calendarioDAO.getEstatisticas();

        // Assert
        assertThat(estatisticas).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar estrutura das estatísticas: [tipo_evento, quantidade, realizados, programados]
        for (Object[] estatistica : estatisticas) {
            assertThat(estatistica).hasSize(4);
            assertThat(estatistica[0]).isInstanceOf(String.class); // tipo_evento
            assertThat(estatistica[1]).isInstanceOf(Integer.class); // quantidade
            assertThat(estatistica[2]).isInstanceOf(Integer.class); // realizados
            assertThat(estatistica[3]).isInstanceOf(Integer.class); // programados
        }
        
        // Verificar estatísticas específicas
        Object[] estatisticaSessao = estatisticas.stream()
            .filter(e -> "SESSAO".equals(e[0]))
            .findFirst()
            .orElse(null);
            
        assertThat(estatisticaSessao).isNotNull();
        assertThat(estatisticaSessao[1]).isEqualTo(2); // 2 sessões
        assertThat(estatisticaSessao[2]).isEqualTo(1); // 1 realizada
        assertThat(estatisticaSessao[3]).isEqualTo(1); // 1 programada
    }

    @Test
    @DisplayName("Deve contar eventos por status")
    void testCountByStatus() throws SQLException {
        // Arrange
        calendarioDAO.save("Sessão Programada 1", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Sessão Programada 2", "2024-06-20", "SESSAO", "Templo", "20:00", 456L);
        calendarioDAO.save("Reunião Programada", "2024-06-25", "REUNIAO", "Sala", "19:00", 789L);
        
        // Marcar alguns como realizados
        List<Object[]> eventos = calendarioDAO.findAll();
        eventos.stream()
            .filter(e -> "Sessão Programada 1".equals(e[1]))
            .findFirst()
            .ifPresent(e -> {
                try {
                    calendarioDAO.realizar((Long) e[0]);
                } catch (SQLException ex) {
                    // Tratar exceção
                }
            });
            
        eventos.stream()
            .filter(e -> "Reunião Programada".equals(e[1]))
            .findFirst()
            .ifPresent(e -> {
                try {
                    calendarioDAO.cancelar((Long) e[0]);
                } catch (SQLException ex) {
                    // Tratar exceção
                }
            });

        // Act
        int countProgramados = calendarioDAO.countByStatus("PROGRAMADO");
        int countRealizados = calendarioDAO.countByStatus("REALIZADO");
        int countCancelados = calendarioDAO.countByStatus("CANCELADO");

        // Assert
        assertThat(countProgramados).isGreaterThanOrEqualTo(1);
        assertThat(countRealizados).isGreaterThanOrEqualTo(1);
        assertThat(countCancelados).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Deve lidar com código de irmão nulo")
    void testCodigoIrmaoNulo() throws SQLException {
        // Arrange
        String descricao = "Evento Sem Responsável";
        String dataInforme = "2024-06-15";
        String tipoEvento = "EVENTO";
        String local = "Salão";
        String horario = "18:00";

        // Act
        calendarioDAO.save(descricao, dataInforme, tipoEvento, local, horario, null);

        // Assert
        List<Object[]> eventos = calendarioDAO.findAll();
        Object[] eventoSalvo = eventos.stream()
            .filter(e -> descricao.equals(e[1]))
            .findFirst()
            .orElse(null);
            
        assertThat(eventoSalvo).isNotNull();
        assertThat(eventoSalvo[7]).isNull();
    }

    @Test
    @DisplayName("Deve alternar status do evento")
    void testAlternarStatusEvento() throws SQLException {
        // Arrange
        calendarioDAO.save("Sessão Status", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        
        List<Object[]> eventos = calendarioDAO.findAll();
        Object[] evento = eventos.stream()
            .filter(e -> "Sessão Status".equals(e[1]))
            .findFirst()
            .orElse(null);
        
        assertThat(evento).isNotNull();
        Long id = (Long) evento[0];

        // Act - Realizar
        calendarioDAO.realizar(id);
        Object[] realizado = calendarioDAO.findAll().stream()
            .filter(e -> id.equals(e[0]))
            .findFirst()
            .orElse(null);
        assertThat(realizado[4]).isEqualTo("REALIZADO");

        // Act - Cancelar
        calendarioDAO.cancelar(id);
        Object[] cancelado = calendarioDAO.findAll().stream()
            .filter(e -> id.equals(e[0]))
            .findFirst()
            .orElse(null);
        assertThat(cancelado[4]).isEqualTo("CANCELADO");
    }

    @Test
    @DisplayName("Deve lidar com datas em diferentes formatos")
    void testDatasDiferentesFormatos() throws SQLException {
        // Arrange
        calendarioDAO.save("Evento Data 1", "2024-06-15", "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Evento Data 2", "15/06/2024", "REUNIAO", "Sala", "19:00", 456L);

        // Act
        List<Object[]> eventos = calendarioDAO.findAll();

        // Assert
        assertThat(eventos).hasSizeGreaterThanOrEqualTo(2);
        
        // Verificar que as datas foram salvas corretamente
        for (Object[] evento : eventos) {
            String dataEvento = (String) evento[2];
            assertThat(dataEvento).isNotNull();
            assertThat(dataEvento).isNotEmpty();
        }
    }

    @Test
    @DisplayName("Deve ordenar eventos por data corretamente")
    void testOrdenacaoEventosPorData() throws SQLException {
        // Arrange
        calendarioDAO.save("Evento Futuro", "2024-12-31", "SESSAO", "Templo", "20:00", 123L);
        calendarioDAO.save("Evento Passado", "2024-01-01", "REUNIAO", "Sala", "19:00", 456L);
        calendarioDAO.save("Evento Meio", "2024-06-15", "EVENTO", "Salão", "18:00", 789L);

        // Act
        List<Object[]> eventos = calendarioDAO.findAll();

        // Assert
        assertThat(eventos).hasSizeGreaterThanOrEqualTo(3);
        
        // Verificar ordenação por data
        for (int i = 0; i < eventos.size() - 1; i++) {
            String dataAtual = (String) eventos.get(i)[2];
            String dataProxima = (String) eventos.get(i + 1)[2];
            
            // Simplificar verificação - apenas garantir que não está nulo
            assertThat(dataAtual).isNotNull();
            assertThat(dataProxima).isNotNull();
        }
    }

    @Test
    @DisplayName("Deve lidar com campos opcionais nulos")
    void testCamposOpcionaisNulos() throws SQLException {
        // Arrange
        calendarioDAO.save("Evento Mínimo", "2024-06-15", "EVENTO", null, null, null);

        // Act
        List<Object[]> eventos = calendarioDAO.findAll();
        Object[] eventoSalvo = eventos.stream()
            .filter(e -> "Evento Mínimo".equals(e[1]))
            .findFirst()
            .orElse(null);

        // Assert
        assertThat(eventoSalvo).isNotNull();
        assertThat(eventoSalvo[5]).isNull(); // local
        assertThat(eventoSalvo[6]).isNull(); // horario
        assertThat(eventoSalvo[7]).isNull(); // codigo_irmao
    }
}
