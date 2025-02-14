package src.exceptions.produto;


public class GetProdutoByIdError extends RuntimeException {
        public GetProdutoByIdError(String message) {
            super(message);
        }

        public GetProdutoByIdError() {
            super("Erro ao buscar o produto pelo ID");
        }
    }


