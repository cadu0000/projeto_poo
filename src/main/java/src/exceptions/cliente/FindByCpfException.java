package src.exceptions.cliente;

public class FindByCpfException extends RuntimeException{
    public FindByCpfException() {
        super("Não foi possível localizar o cpf desse cliente no sistema");
    }

    public FindByCpfException(String message) {
        super(message);
    }
}
