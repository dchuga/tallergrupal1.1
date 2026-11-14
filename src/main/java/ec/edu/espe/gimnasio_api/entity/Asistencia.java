package ec.edu.espe.gimnasio_api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_ingreso", nullable = false)
    private LocalDateTime fechaHoraIngreso = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFechaHoraIngreso() { return fechaHoraIngreso; }
    public void setFechaHoraIngreso(LocalDateTime fechaHoraIngreso) { this.fechaHoraIngreso = fechaHoraIngreso; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
