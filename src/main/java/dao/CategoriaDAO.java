package dao;

import modelo.Categoria;
import modelo.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

	private Connection connection;

	public CategoriaDAO(Connection connection) {
		this.connection = connection;
	}

	public List<Categoria> listar() {
		var categorias = new ArrayList<Categoria>();
		var sql = "SELECT ID, NOME " +
				  "FROM CATEGORIA ";

		try (var pstm = connection.prepareStatement(sql)) {
			pstm.execute();

			try (var rst = pstm.getResultSet()) {
				while (rst.next()) {
					var categoria = new Categoria(
							rst.getInt(1),
							rst.getString(2));

					categorias.add(categoria);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return categorias;
	}

	public List<Categoria> listarComProduto() throws SQLException {
		Categoria ultima = null;
		var categorias = new ArrayList<Categoria>();

		String sql = "SELECT " +
				     "    c.ID, " +
				     "    c.NOME, " +
				     "    p.ID, " +
				     "    p.NOME, " +
				     "    p.DESCRICAO " +
				     "FROM CATEGORIA c " +
				     "INNER JOIN PRODUTO p ON c.ID = p.CATEGORIA_ID ";

		try (var pstm = connection.prepareStatement(sql)) {
			pstm.execute();

			try (var rst = pstm.getResultSet()) {
				while (rst.next()) {
					if (ultima == null || !ultima.getNome().equals(rst.getString(2))) {

						var categoria = new Categoria(
								rst.getInt(1),
								rst.getString(2));

						categorias.add(categoria);
						ultima = categoria;
					}
					var produto = new Produto(
							rst.getInt(3),
							rst.getString(4),
							rst.getString(5));

					ultima.adicionar(produto);
				}
			}
			return categorias;
		}
	}
}
