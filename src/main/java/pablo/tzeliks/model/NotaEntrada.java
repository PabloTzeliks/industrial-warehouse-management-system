package pablo.tzeliks.model;

import java.time.LocalDate;

public class NotaEntrada {

    private int id;
    private Fornecedor fornecedor;
    private LocalDate dataEntrada;

    public NotaEntrada(int id, Fornecedor fornecedor) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.dataEntrada = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                "Fornecedor: " + fornecedor +
                "Data Entrada:" + dataEntrada;
    }
}
