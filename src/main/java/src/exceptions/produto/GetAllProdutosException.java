package src.exceptions.produto;

public class GetAllProdutosException extends RuntimeException{
    public GetAllProdutosException() {
        super("Erro ao obter a lista de produtos disponíveis");
    }

    public GetAllProdutosException(String message) {
        super(message);
    }
}
