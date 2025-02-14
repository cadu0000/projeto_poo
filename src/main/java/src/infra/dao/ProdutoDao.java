package src.infra.dao;

import src.exceptions.produto.CreateProdutoError;
import src.exceptions.produto.GetAllProdutosException;
import src.exceptions.produto.GetProdutoByIdError;
import src.infra.db.ConnectionHelper;
import src.service.domain.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao {

    public void createProduct(Produto produto) {
        String sql = "INSERT INTO PRODUTO (NOME, VALOR_UNIT, QUANTIDADE) VALUES (?,?,?);";

        try{
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, produto.getNome());
            pst.setDouble(2, produto.getValor_unit());
            pst.setInt(3, produto.getQuantidade());

            pst.executeUpdate();

            pst.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new CreateProdutoError("Erro ao criar produto: " + e.getMessage());
        }
        System.out.println("O campo foi preenchido incorretamente");
    }

    public List<Produto> findAll() {
        String sql = "SELECT * FROM PRODUTO;";

        List<Produto> products = new ArrayList<>();
        try{
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("ID");
                String nome = rs.getString("NOME");
                double valorUnit = rs.getDouble("VALOR_UNIT");
                int quantidade = rs.getInt("QUANTIDADE");

                Produto produto = new Produto(id, nome, valorUnit, quantidade);
                products.add(produto);
            }

            pst.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GetAllProdutosException("Erro ao buscar todos os produtos: " + e.getMessage());
        }
        return products;
    }

    public Produto findById(int id) {
        String sql = "SELECT * FROM PRODUTO WHERE ID = ?;";

        try{
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("NOME");
                double valorUnit = rs.getDouble("VALOR_UNIT");
                int quantidade = rs.getInt("QUANTIDADE");

                return new Produto(id, nome, valorUnit, quantidade);
            } else {
                throw new GetProdutoByIdError("Produto com ID " + id + " não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new GetProdutoByIdError("Erro ao buscar produto por ID: " + e.getMessage());
        }
    }

    public void venderProduto(int idProdut0, int quantidade) {
        String sql = "UPDATE PRODUTO SET quantidade = quantidade - ? WHERE ID = ?;";

        try{
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setInt(1, quantidade);
            pst.setInt(2, idProdut0);

            int linhasAfetadas = pst.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new RuntimeException("Erro: Estoque insuficiente ou produto não encontrado.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao vender produto: " + e.getMessage());
        }
    }
}
