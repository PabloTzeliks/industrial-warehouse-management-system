package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.view.helper.MessageHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FornecedorDAO {

    public void salvar(Fornecedor fornecedor) {

        String sqlInsereFornecedor = """
                INSERT INTO Fornecedor (nome, cnpj) VALUES (?, ?); 
                """;

        String sqlValidaCnpj = """
                SELECT COUNT(*) cnpj FROM Fornecedor WHERE cnpj = ?;
                """;

        try (Connection conn = Conexao.getConexao()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlValidaCnpj)) {

                stmt.setString(1, fornecedor.getCnpj());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);

                    if (count > 0) {

                        throw new SQLException("Fornecedor ja esta em uso, CNPJ já cadastrado.");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsereFornecedor)) {

                stmt.setString(1, fornecedor.getNome());
                stmt.setString(2, fornecedor.getCnpj());

                stmt.executeUpdate();

                MessageHelper.sucesso("Fornecedor cadastrado com sucesso!");
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao inserir Fornecedor, observe: " + e.getMessage());
        }
    }

    public List<Fornecedor> listarFornecedores() {

        List<Fornecedor> fornecedores = new ArrayList<>();

        String sql = """
                SELECT id, nome, cnpj FROM Fornecedor;
                """;

        try (Connection conn = Conexao.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Fornecedor novoFornecedor = new Fornecedor(rs.getInt("id"), rs.getString("nome"), rs.getString("cnpj"));

                fornecedores.add(novoFornecedor);
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao listar Fornecedores, observe: " + e.getMessage());
        }

        return fornecedores;
    }

    public Optional<Fornecedor> buscarFornecedorPorId(int id) {

        String sql = """
                SELECT id, nome, cnpj FROM Fornecedor WHERE id = ?;
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if  (rs.next()) {

                return Optional.of(new Fornecedor(rs.getInt("id"), rs.getString("nome"), rs.getString("cnpj")));
            }

        } catch (SQLException e) {

            MessageHelper.erro("Erro ao buscar Fornecedor por ID, observe: " + e.getMessage());
        }

        return Optional.empty();
    }


}
