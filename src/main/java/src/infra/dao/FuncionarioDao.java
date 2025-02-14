package src.infra.dao;

import src.exceptions.funcionario.FindByCpfException;
import src.infra.db.ConnectionHelper;
import src.service.domain.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionarioDao {

    public void createFuncionario(Funcionario funcionario) {
        try {
            String sql = "INSERT INTO FUNCIONARIO VALUES (?, ?, ?, ?);";

            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, funcionario.getCpf());
            pst.setString(2, funcionario.getNome());
            pst.setString(3, funcionario.getEndereco());
            pst.setString(4, funcionario.getTelefone());

            pst.execute();

            pst.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Funcionario findByCpf(String cpf) {
        String sql = "SELECT * FROM FUNCIONARIO WHERE CPF = ?;";

        try{
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, cpf);

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                String nome = rs.getString("NOME");
                String endereco = rs.getString("ENDERECO");
                String telefone = rs.getString("TELEFONE");

                return new Funcionario(cpf, nome, endereco, telefone);
            }
            pst.close();
            connection.close();

            System.out.println("Funcionario com CPF " + cpf + " não encontrado.");
            return null;

        } catch (ClassNotFoundException | SQLException e) {
            throw new FindByCpfException("Erro ao buscar funcionário com CPF " + cpf + ": " + e.getMessage());
        }
    }
}
