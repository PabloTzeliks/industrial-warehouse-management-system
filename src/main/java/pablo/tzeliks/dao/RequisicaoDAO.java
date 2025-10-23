package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.model.Material;
import pablo.tzeliks.model.Requisicao;
import pablo.tzeliks.model.enums.StatusRequisicao;
import pablo.tzeliks.view.helper.MessageHelper;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequisicaoDAO {

    public void salvar(Requisicao requisicao, HashMap<Integer, Double> dicionarioEstoque) {

        int requisicaoId = 0;

        String sqlInsereRequisicao = """
                INSERT INTO Requisicao (setor, dataSolicitacao, status) VALUES (?, ?, ?); 
                """;

        String sqlInsereAssociativaRequisicao = """
                INSERT INTO RequisicaoItem (idRequisicao, idMaterial, quantidade) VALUES (?, ?, ?);
                """;

        try (Connection conn = Conexao.getConexao()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsereRequisicao, Statement.RETURN_GENERATED_KEYS)) {

                Date dataSolicitacao = Date.valueOf(requisicao.getDataSolicitacao());

                stmt.setString(1, requisicao.getSetor());
                stmt.setDate(2, dataSolicitacao);
                stmt.setString(3, requisicao.getSetor());

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {

                    if (rs.next()) {

                        requisicaoId = rs.getInt(1);
                    } else {
                        throw new SQLException("ID da Requisição não retornado.");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsereAssociativaRequisicao)) {

                for (Integer i : dicionarioEstoque.keySet()) {

                    stmt.setInt(1, requisicaoId);
                    stmt.setInt(2, i);
                    stmt.setDouble(3, dicionarioEstoque.get(i));

                    stmt.executeUpdate();
                }

                MessageHelper.sucesso("Material cadastrado com sucesso!");
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao inserir a Requisição, observe: " + e.getMessage());
        }
    }

    public List<Requisicao> listarRequisicao() {

        List<Requisicao> requisicoes = new ArrayList<>();

        String sql = """
                SELECT id, setor, dataSolicitacao, status FROM Requisicao WHERE status = ?;
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StatusRequisicao.PENDENTE.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                LocalDate dataSolicitacao = LocalDate.parse(rs.getString("dataSolicitacao"));
                StatusRequisicao status = StatusRequisicao.valueOf(rs.getString("status"));

                Requisicao novaRequisicao = new Requisicao(rs.getInt("id"), rs.getString("setor"), dataSolicitacao,  status);

                requisicoes.add(novaRequisicao);
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao listar Requisições, observe: " + e.getMessage());
        }

        return requisicoes;
    }
}