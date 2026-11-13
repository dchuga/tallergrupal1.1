package ec.edu.espe.gimnasio_api.dto;

import java.time.LocalDate;

public class ClienteDTO {
    private Long id;
    private String cedula;
    private String nombre;
    private String email;
    private LocalDate fechaVencimientoMembresia;
    private Long planId;
    private Long entrenadorId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getFechaVencimientoMembresia() { return fechaVencimientoMembresia; }
    public void setFechaVencimientoMembresia(LocalDate fechaVencimientoMembresia) { this.fechaVencimientoMembresia = fechaVencimientoMembresia; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getEntrenadorId() { return entrenadorId; }
    public void setEntrenadorId(Long entrenadorId) { this.entrenadorId = entrenadorId; }
}