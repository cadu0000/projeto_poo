package src.infra.dao;

import src.exceptions.cliente.CreateClienteException;
import src.exceptions.cliente.FindByCpfException;
import src.infra.db.ConnectionHelper;
import src.service.domain.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    public void create(Cliente cliente) {
        String sql = "INSERT INTO CLIENTE VALUES (?,?,?,?);";

        try {
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, cliente.getCpf());
            pst.setString(2, cliente.getNome());
            pst.setString(3, cliente.getEndereco());
            pst.setString(4, cliente.getTelefone());

            pst.executeUpdate();

            pst.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            throw new CreateClienteException("Erro ao cadastrar cliente: " + e.getMessage());
        }
        System.out.println("O campo foi preenchido incorretamente");
    }

    public List<Cliente> findAll() {
        String sql = "SELECT * FROM CLIENTE;";

        List<Cliente> clientes = new ArrayList<>();
        try {
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String cpf = rs.getString("CPF");
                String nome = rs.getString("NOME");
                String endereco = rs.getString("ENDERECO");
                String telefone = rs.getString("TELEFONE");

                Cliente cliente = new Cliente(cpf, nome, endereco, telefone);
                clientes.add(cliente);
            }

            pst.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao buscar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public Cliente findByCpf(String cpf) {
        String sql = "SELECT * FROM CLIENTE WHERE CPF = ?;";

        try{
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, cpf);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("NOME");
                String endereco = rs.getString("ENDERECO");
                String telefone = rs.getString("TELEFONE");

                return new Cliente(cpf, nome, endereco, telefone);
            }

            pst.close();
            connection.close();

            System.out.println("Cliente com CPF " + cpf + " não encontrado.");
            return null;

        } catch (SQLException | ClassNotFoundException e) {
            throw new FindByCpfException("Erro ao buscar cliente com CPF " + cpf + ": " + e.getMessage());
        }
    }
}
