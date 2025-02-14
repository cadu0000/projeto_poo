package src.exceptions.pedido;

public class GetPedidosError extends RuntimeException {
    public GetPedidosError(String message) {
        super(message);
    }

    public GetPedidosError() {
        super("Erro ao criar o pedido");
    }
}
