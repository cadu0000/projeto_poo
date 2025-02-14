package src.service.domain;

public class Produto {
    private int id;
    private String nome;
    private double valor_unit;
    private int quantidade;

    public Produto(int id, String nome, double valor_unit, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.valor_unit = valor_unit;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(double valor_unit) {
        this.valor_unit = valor_unit;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Produto{id=" + id + ", nome='" + nome + "', valor=" + valor_unit + ", quantidade=" + quantidade + "}";
    }
}

