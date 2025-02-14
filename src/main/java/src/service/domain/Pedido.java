package src.service.domain;
import java.util.Map;

public class Pedido {
    private int id;
    private String cpfCliente;
    private String cpfFuncionario;
    private double valorTotal;
    private Map<Produto, Integer> itens;

    public Pedido(int id, String cpfCliente, String cpfFuncionario, double valorTotal, Map<Produto, Integer> itens) {
        this.id = id;
        this.cpfCliente = cpfCliente;
        this.cpfFuncionario = cpfFuncionario;
        this.valorTotal = valorTotal;
        this.itens = itens;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getCpfFuncionario() {
        return cpfFuncionario;
    }

    public void setCpfFuncionario(String cpfFuncionario) {
        this.cpfFuncionario = cpfFuncionario;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Map<Produto, Integer> getItens() {
        return itens;
    }

    public void setItens(Map<Produto, Integer> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cpfCliente='" + cpfCliente + '\'' +
                ", cpfFuncionario='" + cpfFuncionario + '\'' +
                ", valorTotal=" + valorTotal +
                ", itens=" + itens +
                '}';
    }
}