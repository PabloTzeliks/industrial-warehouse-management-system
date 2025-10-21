package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.model.Material;
import pablo.tzeliks.view.helper.MessageHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Material> listarMateriais() {

        List<Material> materiais = new ArrayList<>();

        String sql = """
                SELECT id, nome, unidade, estoque FROM Material;
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Material novoMaterial = new Material(rs.getInt("id"), rs.getString("nome"), rs.getString("unidade"),  rs.getDouble("estoque"));

                materiais.add(novoMaterial);
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao listar Materiáis, observe: " + e.getMessage());
        }

        return materiais;
    }

    public Optional<Material> buscarMaterialPorId(int id) {

        String sql = """
                SELECT id, nome, unidade, estoque FROM Material WHERE id = ?;
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if  (rs.next()) {

                return Optional.of(new Material(rs.getInt("id"), rs.getString("nome"), rs.getString("unidade"), rs.getDouble("estoque")));
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao buscar Material por ID, observe: " + e.getMessage());
        }

        return Optional.empty();
    }
}
