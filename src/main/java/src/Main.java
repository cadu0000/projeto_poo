package src;

import src.infra.dao.ClienteDao;
import src.infra.dao.FuncionarioDao;
import src.infra.dao.PedidoDao;
import src.infra.dao.ProdutoDao;
import src.service.domain.Cliente;
import src.service.domain.Funcionario;
import src.service.domain.Pedido;
import src.service.domain.Produto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        exibirMenu();
    }
        public static void exibirMenu() {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nEscolha uma opção:");
                System.out.println("1. Cadastrar um produto");
                System.out.println("2. Cadastrar um cliente");
                System.out.println("3. Buscar produto");
                System.out.println("4. Listar produtos");
                System.out.println("5. Efetuar venda");
                System.out.println("6. Listar todas as vendas");
                System.out.println("0. Sair");

                int op = scanner.nextInt();
                scanner.nextLine();

                switch (op) {
                    case 1 -> cadastrarProduto();
                    case 2 -> cadastrarCliente();
                    case 3 -> buscarProduto();
                    case 4 -> listarTodosOsProdutosDisponiveis();
                    case 5 -> efetuarVenda();
                    case 6 -> listarTodasAsVendas();
                    case 0 -> {
                        return;
                    }
                }
            }
        }

        public static void cadastrarProduto() {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("Digite o nome do produto: ");
                String nome = scanner.nextLine();
                System.out.println("Digite o valor do produto: ");
                double valor = scanner.nextDouble();
                System.out.println("Digite a quantidade do produto: ");
                int quantidade = scanner.nextInt();

                ProdutoDao produtoDao = new ProdutoDao();

                Produto produto = new Produto(0, nome, valor, quantidade);
                produtoDao.createProduct(produto);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        public static void cadastrarCliente(){
            Scanner scanner = new Scanner(System.in);
            try{
                System.out.println("Digite o nome do cliente: ");
                String nome = scanner.nextLine();
                System.out.println("Digite o cpf do cliente: ");
                String cpf = scanner.nextLine();
                System.out.println("Digite o endereço do cliente: ");
                String endereco = scanner.nextLine();
                System.out.println("Digite o telefone do cliente: ");
                String telefone = scanner.nextLine();

                Cliente cliente = new Cliente(cpf, nome, endereco, telefone);
                ClienteDao clienteDao = new ClienteDao();
                clienteDao.create(cliente);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        public static void buscarProduto(){
            Scanner scanner = new Scanner(System.in);
            try{
                System.out.println("Digite o id do produto");
                int id = scanner.nextInt();

                ProdutoDao produtoDao = new ProdutoDao();
                Produto produto = produtoDao.findById(id);
                System.out.println(produto.toString());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        public static void listarTodosOsProdutosDisponiveis(){
            try{
                ProdutoDao produtoDao = new ProdutoDao();
                List<Produto> produtos = produtoDao.findAll();

                for (Produto produto : produtos) {
                    System.out.println(produto.toString());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    public static void efetuarVenda() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Digite o CPF do cliente: ");
            String cpfCliente = scanner.nextLine().trim();
            ClienteDao clienteDao = new ClienteDao();
            Cliente cliente = clienteDao.findByCpf(cpfCliente);

            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            System.out.print("Digite o CPF do funcionário: ");
            String cpfFuncionario = scanner.nextLine().trim();
            FuncionarioDao funcionarioDao = new FuncionarioDao();
            Funcionario funcionario = funcionarioDao.findByCpf(cpfFuncionario);

            if (funcionario == null) {
                System.out.println("Funcionário não encontrado.");
                return;
            }

            Map<Produto, Integer> itens = new HashMap<>();
            double valorTotal = 0;

            while (true) {
                System.out.print("Digite o ID do produto: ");
                int idProduto = Integer.parseInt(scanner.nextLine().trim());

                ProdutoDao produtoDao = new ProdutoDao();
                Produto produto = produtoDao.findById(idProduto);

                if (produto == null) {
                    System.out.println("Produto não encontrado.");
                    continue;
                }

                System.out.print("Digite a quantidade desejada: ");
                int quantidade = Integer.parseInt(scanner.nextLine().trim());

                if (quantidade > produto.getQuantidade()) {
                    System.out.println("Quantidade indisponível em estoque.");
                    continue;
                }

                itens.put(produto, quantidade);
                valorTotal += produto.getValor_unit() * quantidade;

                System.out.print("Deseja adicionar outro produto? (1 - Sim / 2 - Não): ");
                int opcao = Integer.parseInt(scanner.nextLine().trim());

                if (opcao == 2) {
                    break;
                }
            }

            if (itens.isEmpty()) {
                System.out.println("Nenhum produto foi adicionado ao pedido.");
                return;
            }

            Pedido pedido = new Pedido(0, cliente.getCpf(), funcionario.getCpf(), valorTotal, itens);
            PedidoDao pedidoDao = new PedidoDao();
            pedidoDao.createPedido(pedido);

            ProdutoDao produtoDao = new ProdutoDao();
            for (Map.Entry<Produto, Integer> entry : itens.entrySet()) {
                produtoDao.venderProduto(entry.getKey().getId(), entry.getValue());
            }
            System.out.println("Venda realizada com sucesso! Pedido registrado.");

        } catch (NumberFormatException e) {
            System.out.println("Erro: Entrada inválida. Certifique-se de digitar números corretamente.");
        } catch (Exception e) {
            System.out.println("Erro ao efetuar venda: " + e.getMessage());
        }
    }
        public static void listarTodasAsVendas(){
        try{
            PedidoDao pedidoDao = new PedidoDao();
            List<Pedido> pedidos = pedidoDao.listarVendas();

            for(Pedido pedido : pedidos){
                System.out.println(pedido.toString());
            }
        } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
}
