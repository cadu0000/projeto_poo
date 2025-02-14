package src.exceptions.funcionario;

public class FindByCpfException extends RuntimeException{
    public FindByCpfException() {
        super("Não foi possível localizar o cpf desse funcionário no sistema");
    }

    public FindByCpfException(String message) {
        super(message);
    }
}
