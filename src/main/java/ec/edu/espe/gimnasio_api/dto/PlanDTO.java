package ec.edu.espe.gimnasio_api.dto;

import java.math.BigDecimal;

public class PlanDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer duracionDias;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getDuracionDias() { return duracionDias; }
    public void setDuracionDias(Integer duracionDias) { this.duracionDias = duracionDias; }
}