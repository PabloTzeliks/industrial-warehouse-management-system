package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.model.NotaEntrada;
import pablo.tzeliks.view.helper.MessageHelper;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class NotaEntradaDAO {

    public void salvar(NotaEntrada notaEntrada, HashMap<Integer, Double> estoquePecas) {

        String sqlInsereNotaEntrada = """
                INSERT INTO NotaEntrada (idFornecedor, dataEntrada) VALUES (?, ?); 
                """;

        String sqlInsereNotaEntradaItem = """
                INSERT INTO NotaEntradaItem (idNotaEntrada, idMaterial, quantidade) VALUES (?, ?, ?);
                """;

        String sqlUpdateMaterial = """
                UPDATE Material SET estoque = estoque - ? WHERE id = ? AND estoque >= ?;
                """;

        int idNotaEntrada = 0;

        try (Connection conn = Conexao.getConexao()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsereNotaEntrada, Statement.RETURN_GENERATED_KEYS)) {

                Date dataEntrada = Date.valueOf(notaEntrada.getDataEntrada());

                stmt.setInt(1, notaEntrada.getFornecedor().getId());
                stmt.setDate(2, dataEntrada);

                int linhas = stmt.executeUpdate();

                if (linhas == 0) {

                    throw new SQLException("Nenhum Máterial insérido para adicionar a Nota de Entrada");
                }

                try (ResultSet rs = stmt.getGeneratedKeys()) {

                    if (rs.next()) {

                        idNotaEntrada = rs.getInt(1);
                    } else {
                        throw new SQLException("ID da Nota de Entrada não foi retornado.");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsereNotaEntradaItem)) {

                for (Map.Entry<Integer, Double> estoque : estoquePecas.entrySet()) {

                    stmt.setInt(1, idNotaEntrada);
                    stmt.setInt(2, estoque.getKey());
                    stmt.setDouble(3, estoque.getValue());

                    int linhas = stmt.executeUpdate();

                    if (linhas == 0) {

                        throw new SQLException("Ocorreu um erro para inserir os Materiáis da Nota de Entrada");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateMaterial)) {

                for (Map.Entry<Integer, Double> estoque : estoquePecas.entrySet()) {

                    stmt.setDouble(1, estoque.getValue());
                    stmt.setInt(2, estoque.getKey());
                    stmt.setDouble(3, estoque.getValue());

                    int linhas = stmt.executeUpdate();

                    if (linhas == 0) {

                        throw new SQLException("Ocorreu um erro para atualizar o estoque dos Materiáis da Nota de Entrada");
                    }
                }
            }

            MessageHelper.sucesso("Nota Entrada cadastrada com sucesso!");

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao inserir Nota Entrada, observe: " + e.getMessage());

            e.printStackTrace();
        }
    }
}