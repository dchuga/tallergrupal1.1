package ec.edu.espe.gimnasio_api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ec.edu.espe.gimnasio_api.entity.Asistencia;
import ec.edu.espe.gimnasio_api.entity.Cliente;
import ec.edu.espe.gimnasio_api.entity.Entrenador;
import ec.edu.espe.gimnasio_api.entity.Plan;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class PersistenciaRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    private Plan plan;
    private Entrenador entrenador;

    @BeforeEach
    void preparar() {
        plan = new Plan();
        plan.setNombre("Mensual");
        plan.setPrecio(new BigDecimal("30.00"));
        plan.setDuracionDias(30);
        plan = planRepository.save(plan);

        entrenador = new Entrenador();
        entrenador.setNombre("Carlos");
        entrenador.setApellido("Mora");
        entrenador.setEspecialidad("Musculacion");
        entrenador.setEmail("carlos.mora@gimnasio.com");
        entrenador = entrenadorRepository.save(entrenador);
    }

    private Cliente crearCliente(String cedula, String email, LocalDate vencimiento, Entrenador ent) {
        Cliente cliente = new Cliente();
        cliente.setCedula(cedula);
        cliente.setNombre("Cliente " + cedula);
        cliente.setEmail(email);
        cliente.setFechaVencimientoMembresia(vencimiento);
        cliente.setPlan(plan);
        cliente.setEntrenador(ent);
        return clienteRepository.save(cliente);
    }

    @Test
    void guardaYConsultaClientePersistido() {
        Cliente guardado = crearCliente("0912345678", "a@mail.com", LocalDate.now().plusDays(10), entrenador);

        assertTrue(clienteRepository.findById(guardado.getId()).isPresent());
        assertTrue(clienteRepository.existsByCedula("0912345678"));
    }

    @Test
    void consultaClientesConMembresiaVencida() {
        crearCliente("0911111111", "vencido@mail.com", LocalDate.now().minusDays(5), entrenador);
        crearCliente("0922222222", "vigente@mail.com", LocalDate.now().plusDays(20), entrenador);

        List<Cliente> vencidos = clienteRepository.findByFechaVencimientoMembresiaBefore(LocalDate.now());

        assertEquals(1, vencidos.size());
        assertEquals("0911111111", vencidos.get(0).getCedula());
    }

    @Test
    void consultaClientesPorEntrenador() {
        crearCliente("0933333333", "b@mail.com", LocalDate.now().plusDays(15), entrenador);
        crearCliente("0944444444", "c@mail.com", LocalDate.now().plusDays(15), null);

        List<Cliente> clientes = clienteRepository.buscarPorEntrenador(entrenador.getId());

        assertEquals(1, clientes.size());
        assertEquals("0933333333", clientes.get(0).getCedula());
    }

    @Test
    void consultaAsistenciasEnRango() {
        Cliente cliente = crearCliente("0955555555", "d@mail.com", LocalDate.now().plusDays(15), entrenador);

        Asistencia dentro = new Asistencia();
        dentro.setCliente(cliente);
        dentro.setFechaHoraIngreso(LocalDateTime.now().minusHours(2));
        asistenciaRepository.save(dentro);

        Asistencia fuera = new Asistencia();
        fuera.setCliente(cliente);
        fuera.setFechaHoraIngreso(LocalDateTime.now().minusDays(10));
        asistenciaRepository.save(fuera);

        List<Asistencia> resultado = asistenciaRepository.buscarEnRango(
                cliente.getId(), LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertEquals(1, resultado.size());
    }
}
