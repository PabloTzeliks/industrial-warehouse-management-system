package pablo.tzeliks.model;

public class Material {

    private int id;
    private String nome;
    private String unidade;
    private double estoque;

    public Material(int id, String nome, String unidade, double estoque) {
        this.id = id;
        this.nome = nome;
        this.unidade = unidade;
        this.estoque = estoque;
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

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getEstoque() {
        return estoque;
    }

    public void setEstoque(double estoque) {
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d" + "\nNome: %s" + "\nUnidade %s" + "\nEstoque: %.2f", id, nome, unidade, estoque
        );
    }
}