package pablo.tzeliks.dao;

import pablo.tzeliks.dao.conexao.Conexao;
import pablo.tzeliks.dao.exception.DatabaseException;
import pablo.tzeliks.model.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FornecedorDAO {

    public void salvar(Fornecedor fornecedor) {

        String sql = """
                INSERT INTO Fornecedor (id, nome, cnpj) VALUES (?, ?, ?); 
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fornecedor.getId());
            stmt.setString(2, fornecedor.getNome());
            stmt.setString(3, fornecedor.getCnpj());

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new DatabaseException("Erro ao inserir Fornecedor, observe: " + e.getMessage());
        }
    }


}
