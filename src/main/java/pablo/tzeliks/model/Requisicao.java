package pablo.tzeliks.model;

import pablo.tzeliks.model.enums.StatusRequisicao;

import java.time.LocalDate;

public class Requisicao {

    private int id;
    private String setor;
    private LocalDate dataSolicitacao;
    private StatusRequisicao status;

    public Requisicao(int id, String setor, StatusRequisicao status) {
        this.id = id;
        this.setor = setor;
        this.dataSolicitacao = LocalDate.now();
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDate dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public StatusRequisicao getStatus() {
        return status;
    }

    public void setStatus(StatusRequisicao status) {
        this.status = status;
    }
}