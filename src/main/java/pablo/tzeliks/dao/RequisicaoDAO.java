package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.model.Material;
import pablo.tzeliks.model.Requisicao;
import pablo.tzeliks.model.enums.StatusRequisicao;
import pablo.tzeliks.view.helper.MessageHelper;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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
                stmt.setString(3, requisicao.getStatus().name());

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

    public List<Requisicao> listarRequisicao(StatusRequisicao status) {

        List<Requisicao> requisicoes = new ArrayList<>();

        String sql = """
                SELECT id, setor, dataSolicitacao, status FROM Requisicao WHERE status = ?;
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                LocalDate dataSolicitacao = LocalDate.parse(rs.getString("dataSolicitacao"));
                StatusRequisicao statusRequisicao = StatusRequisicao.valueOf(rs.getString("status"));

                Requisicao novaRequisicao = new Requisicao(rs.getInt("id"), rs.getString("setor"), dataSolicitacao,  statusRequisicao);

                requisicoes.add(novaRequisicao);
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao listar Requisições, observe: " + e.getMessage());
        }

        return requisicoes;
    }

    public Optional<Requisicao> buscarRequisicaoPorId(int id) {

        String sql = """
                SELECT id, setor, dataSolicitacao, status FROM Requisicao WHERE id = ?;
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if  (rs.next()) {

                LocalDate dataSolicitacao = LocalDate.parse(rs.getString("dataSolicitacao"));
                StatusRequisicao statusRequisicao = StatusRequisicao.valueOf(rs.getString("status"));

                return Optional.of(new Requisicao(rs.getInt("id"), rs.getString("setor"), dataSolicitacao, statusRequisicao));
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao buscar Requisição por ID, observe: " + e.getMessage());
        }

        return Optional.empty();
    }

    public void atenderRequisicao(Requisicao requisicao) {

        Map<Integer, Double> quantidadeMaterial = new HashMap<Integer, Double>();

        String sqlBuscaItensRequisicao = """
                SELECT idRequisicao, idMaterial, quantidade FROM RequisicaoItem WHERE idRequisicao = ?;
                """;

        String sqlUpdateItensRequisicao = """
                UPDATE Material SET estoque = estoque - ? WHERE id = ? AND estoque >= ?;
                """;

        String sqlUpdateStatusRequisicao = """
                UPDATE Requisicao SET status = ? WHERE id = ?;
                """;

        try (Connection conn = Conexao.getConexao()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlBuscaItensRequisicao)) {

                stmt.setInt(1, requisicao.getId());

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {

                    quantidadeMaterial.put(rs.getInt("idMaterial"), rs.getDouble("quantidade"));
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateItensRequisicao)) {

                for (Integer chave : quantidadeMaterial.keySet()) {

                    stmt.setDouble(1, quantidadeMaterial.get(chave));
                    stmt.setInt(2, chave);
                    stmt.setDouble(3, quantidadeMaterial.get(chave));

                    int linhasAfetadas = stmt.executeUpdate();

                    if (linhasAfetadas == 0) {

                        throw new SQLException("Erro ao mudar as quantidades dos Materiais.");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateStatusRequisicao)) {

                stmt.setString(1, StatusRequisicao.ATENDIDA.name());
                stmt.setInt(2, requisicao.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas == 0) {

                    throw new SQLException("Erro ao alterar o Status da Requisição.");
                }
            }

            MessageHelper.sucesso("Requisição Atendida com sucesso!");

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao Executar uma Requisição, observe: " + e.getMessage());

            e.printStackTrace();
        }
    }
}