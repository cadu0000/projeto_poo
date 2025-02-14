package src.exceptions.produto;

public class CreateProdutoError extends RuntimeException{
    public CreateProdutoError(String message) {
        super(message);
    }

    public CreateProdutoError() {
        super("Erro ao criar novo produto");
    }
}