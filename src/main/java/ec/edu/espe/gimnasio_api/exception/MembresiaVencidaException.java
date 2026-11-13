package ec.edu.espe.gimnasio_api.exception;

public class MembresiaVencidaException extends RuntimeException {
    public MembresiaVencidaException(String mensaje) {
        super(mensaje);
    }
}