package ec.edu.espe.gimnasio_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EntrenadorDTO {

    private Long id;

    @NotBlank(message = "El nombre del entrenador es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido del entrenador es obligatorio")
    @Size(min = 2, max = 80, message = "El apellido debe tener entre 2 y 80 caracteres")
    private String apellido;

    // Opcional en la entidad
    @Size(max = 80, message = "La especialidad no puede superar los 80 caracteres")
    private String especialidad;

    @NotBlank(message = "El email del entrenador es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 120, message = "El email no puede superar los 120 caracteres")
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
