package entities;

public class Navio {

    private Integer id;
    private String descricao;
    private int capacidade;
    private int capacidadeRestante;

    public Navio(Integer id, String descricao, int capacidade) {
        this.id = id;
        this.descricao = descricao;
        this.capacidade = capacidade;
        this.capacidadeRestante = capacidade;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public int getCapacidadeRestante() {
        return capacidadeRestante;
    }

    public void setCapacidadeRestante(int capacidadeRestante) {
        this.capacidadeRestante = capacidadeRestante;
    }

    // toString (util para relatorios)
    @Override
    public String toString() {
        return "Navio{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", capacidade=" + capacidade +
                ", capacidadeRestante=" + capacidadeRestante +
                '}';
    }
}
