package entities;

public class Embarque {

    private Integer id;
    private Integer idNavio;
    private Integer idCarga;
    private String descricao;

    public Embarque(Integer id, Integer idNavio, Integer idCarga, String descricao) {
        this.id = id;
        this.idNavio = idNavio;
        this.idCarga = idCarga;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdNavio() {
        return idNavio;
    }

    public void setIdNavio(Integer idNavio) {
        this.idNavio = idNavio;
    }

    public Integer getIdCarga() {
        return idCarga;
    }

    public void setIdCarga(Integer idCarga) {
        this.idCarga = idCarga;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // toString (util para relatorios)
    @Override
    public String toString() {
        return "Embarque{" +
                "id=" + id +
                ", idNavio=" + idNavio +
                ", idCarga=" + idCarga +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
