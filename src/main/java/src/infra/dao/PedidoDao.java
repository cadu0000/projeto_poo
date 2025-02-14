package src.infra.dao;

import src.exceptions.pedido.CreatePedidoError;
import src.exceptions.pedido.GetPedidosError;
import src.infra.db.ConnectionHelper;
import src.service.domain.Pedido;
import src.service.domain.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoDao {

    public void createPedido(Pedido pedido) {
        String pedidoSQL = "INSERT INTO PEDIDO (CPF_CLIENTE_FK, CPF_FUNCIONARIO_FK, VALOR_TOTAL)" +
                " VALUES (?, ?, ?);";

        String itemPedidoSQL = "INSERT INTO ITEM_PEDIDO (ID_PEDIDO_FK, ID_PRODUTO_FK, QUANTIDADE, VALOR) VALUES (?, ?, ?, ?);";

        try (Connection connection = ConnectionHelper.getConnection()) {

            connection.setAutoCommit(false);

            PreparedStatement pst = connection.prepareStatement(pedidoSQL, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, pedido.getCpfCliente());
            pst.setString(2, pedido.getCpfFuncionario());
            pst.setDouble(3, pedido.getValorTotal());

            int affectedRows = pst.executeUpdate();

            if (affectedRows == 0) {
                throw new CreatePedidoError("Erro ao criar o pedido: Nenhuma linha afetada.");
            }

            ResultSet generatedKeys = pst.getGeneratedKeys();
            int pedidoId;

            if (generatedKeys.next()) {
                pedidoId = generatedKeys.getInt(1);
            } else {
                throw new CreatePedidoError("Erro ao obter o ID do pedido.");
            }

            PreparedStatement itemPst = connection.prepareStatement(itemPedidoSQL);

            for (Map.Entry<Produto, Integer> entry : pedido.getItens().entrySet()) {
                itemPst.setInt(1, pedidoId);
                itemPst.setInt(2, entry.getKey().getId());
                itemPst.setInt(3, entry.getValue());
                itemPst.setDouble(4, entry.getKey().getValor_unit() * entry.getValue());
                itemPst.addBatch();
            }

            itemPst.executeBatch();
            connection.commit();

            pst.close();
            itemPst.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new CreatePedidoError("Erro ao criar pedido: " + e.getMessage());
        }
    }

        public List<Pedido> listarVendas () {
            List<Pedido> pedidos = new ArrayList<>();
            String sql = "SELECT p.ID AS pedido_id, p.CPF_CLIENTE_FK, p.CPF_FUNCIONARIO_FK, p.VALOR_TOTAL, " +
                    "i.ID AS item_id, i.ID_PRODUTO_FK, i.QUANTIDADE, i.VALOR, pr.NOME AS produto_nome " +
                    "FROM PEDIDO p INNER JOIN ITEM_PEDIDO i ON p.ID = i.ID_PEDIDO_FK " +
                    "INNER JOIN PRODUTO pr ON i.ID_PRODUTO_FK = pr.ID;";

            try {
                Connection connection = ConnectionHelper.getConnection();
                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                Pedido pedidoAtual = null;

                while (rs.next()) {
                    int idPedido = rs.getInt("pedido_id");

                    if (pedidoAtual == null || pedidoAtual.getId() != idPedido) {
                        if (pedidoAtual != null) {
                            pedidos.add(pedidoAtual);
                        }

                        String cpfCliente = rs.getString("CPF_CLIENTE_FK");
                        String cpfFuncionario = rs.getString("CPF_FUNCIONARIO_FK");
                        double valorTotal = rs.getDouble("VALOR_TOTAL");
                        pedidoAtual = new Pedido(idPedido, cpfCliente, cpfFuncionario, valorTotal, new HashMap<>());
                    }

                    int idProduto = rs.getInt("ID_PRODUTO_FK");
                    String nomeProduto = rs.getString("produto_nome");
                    int quantidade = rs.getInt("QUANTIDADE");
                    double valor = rs.getDouble("VALOR");

                    Produto produto = new Produto(idProduto, nomeProduto, valor, quantidade);
                    pedidoAtual.getItens().put(produto, quantidade);
                }

                if (pedidoAtual != null) {
                    pedidos.add(pedidoAtual);
                }
                pst.close();
                connection.close();

            } catch (ClassNotFoundException | SQLException e) {
                throw new GetPedidosError("Erro ao listar pedidos: " + e.getMessage());
            }

            return pedidos;
        }
    }

