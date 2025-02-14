package src.exceptions.pedido;

public class CreatePedidoError extends RuntimeException{
    public CreatePedidoError(String message) {
        super(message);
    }

    public CreatePedidoError() {
        super("Erro ao criar o pedido");
    }
}
