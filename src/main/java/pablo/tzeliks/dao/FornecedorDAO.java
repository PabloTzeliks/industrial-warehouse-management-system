package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.model.Fornecedor;
import pablo.tzeliks.view.helper.MessageHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


}
