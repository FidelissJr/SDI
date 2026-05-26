package entities;
public class Carga {

    private Integer id;
    private String descricao;
    private int volume;

    public Carga(Integer id, String descricao, int volume) {
        this.id = id;
        this.descricao = descricao;
        this.volume = volume;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    // toString (útil para relatórios)
    @Override
    public String toString() {
        return "Carga{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", volume=" + volume +
                '}';
    }
}