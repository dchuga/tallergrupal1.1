package ec.edu.espe.gimnasio_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ClienteDTO {

    private Long id;

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "\\d{10}", message = "La cédula debe tener exactamente 10 dígitos numéricos")
    private String cedula;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 120, message = "El email no puede superar los 120 caracteres")
    private String email;

    // Opcional: si no se envía, el servicio la calcula con la duración del plan
    private LocalDate fechaVencimientoMembresia;

    @NotNull(message = "El plan es obligatorio")
    @Positive(message = "El id del plan debe ser un número positivo")
    private Long planId;

    // Opcional: un cliente puede no tener entrenador asignado
    @Positive(message = "El id del entrenador debe ser un número positivo")
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
