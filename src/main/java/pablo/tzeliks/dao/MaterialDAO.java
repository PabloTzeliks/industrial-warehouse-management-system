package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.model.Material;
import pablo.tzeliks.view.helper.MessageHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialDAO {

    public void salvar(Material material) {

        String sqlInsereFornecedor = """
                INSERT INTO Material (nome, unidade, estoque) VALUES (?, ?, ?); 
                """;

        String sqlValidaCnpj = """
                SELECT COUNT(*) nome FROM Material WHERE nome = ?;
                """;

        try (Connection conn = Conexao.getConexao()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlValidaCnpj)) {

                stmt.setString(1, material.getNome());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);

                    if (count > 0) {

                        throw new SQLException("Material ja esta em uso, nome já cadastrado.");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsereFornecedor)) {

                stmt.setString(1, material.getNome());
                stmt.setString(2, material.getUnidade());
                stmt.setDouble(3, material.getEstoque());

                stmt.executeUpdate();

                MessageHelper.sucesso("Material cadastrado com sucesso!");
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao inserir Material, observe: " + e.getMessage());
        }
    }
}
