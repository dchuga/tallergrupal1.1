package ec.edu.espe.gimnasio_api.dto;

import java.time.LocalDateTime;

public class AsistenciaDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaHoraIngreso;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public LocalDateTime getFechaHoraIngreso() { return fechaHoraIngreso; }
    public void setFechaHoraIngreso(LocalDateTime fechaHoraIngreso) { this.fechaHoraIngreso = fechaHoraIngreso; }
}