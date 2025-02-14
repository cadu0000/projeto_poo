package src.exceptions.cliente;

public class CreateClienteException extends RuntimeException{
    public CreateClienteException(String message) {
        super(message);
    }

    public CreateClienteException() {
        super("Erro ao criar novo cliente");
    }
}