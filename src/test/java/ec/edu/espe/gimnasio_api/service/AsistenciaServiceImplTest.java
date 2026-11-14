package ec.edu.espe.gimnasio_api.service;

import ec.edu.espe.gimnasio_api.exception.RecursoNoEncontradoException;
import ec.edu.espe.gimnasio_api.repository.AsistenciaRepository;
import ec.edu.espe.gimnasio_api.repository.ClienteRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AsistenciaServiceImplTest {

    private final AsistenciaRepository asistenciaRepository =
            mock(AsistenciaRepository.class);

    private final ClienteRepository clienteRepository =
            mock(ClienteRepository.class);

    private final AsistenciaServiceImpl servicio =
            new AsistenciaServiceImpl(
                    asistenciaRepository,
                    clienteRepository
            );

    @Test
    void listarAsistenciasDeClienteExistente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        when(asistenciaRepository
                .findByClienteIdOrderByFechaHoraIngresoDesc(1L))
                .thenReturn(List.of());

        var resultado = servicio.listarPorCliente(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(asistenciaRepository)
                .findByClienteIdOrderByFechaHoraIngresoDesc(1L);
    }

    @Test
    void clienteInexistenteGeneraExcepcion() {
        when(clienteRepository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> servicio.listarPorCliente(99L)
        );
    }
}
