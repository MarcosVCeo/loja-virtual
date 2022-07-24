package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Categoria;
import modelo.Produto;

public class ProdutoDAO {

	private Connection connection;

	public ProdutoDAO(Connection connection) {
		this.connection = connection;
	}

	public void salvar(Produto produto){
		var sql = "INSERT INTO produto (nome, descricao) " +
				  "VALUES (?, ?) ";

		try (var pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstm.setString(1, produto.getNome());
			pstm.setString(2, produto.getDescricao());

			pstm.execute();

			try (var rst = pstm.getGeneratedKeys()) {
				while (rst.next()) {
					produto.setId(rst.getInt(1));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void salvarComCategoria(Produto produto) throws SQLException {
		var sql = "INSERT INTO produto (nome, descricao, categoria_id " +
				  "VALUES (?, ?, ?) ";

		try (var pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			pstm.setString(1, produto.getNome());
			pstm.setString(2, produto.getDescricao());
			pstm.setInt(3, produto.getCategoriaId());

			pstm.execute();

			try (var rst = pstm.getGeneratedKeys()) {
				while (rst.next()) {
					produto.setId(rst.getInt(1));
				}
			}
		}
	}

	public List<Produto> listar() {
		var produtos = new ArrayList<Produto>();
		var sql = "SELECT " +
				  "    id, " +
				  "    nome, " +
				  "    descricao " +
				  "FROM produto ";

		try (var pstm = connection.prepareStatement(sql)) {
			pstm.execute();

			trasformarResultSetEmProduto(produtos, pstm);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return produtos;
	}

	public List<Produto> buscar(Categoria ct) throws SQLException {
		var produtos = new ArrayList<Produto>();
		var sql = "SELECT " +
				  "    id, " +
				  "    nome, " +
				  "    descricao " +
				  "FROM produto " +
				  "WHERE categoria_id = ?";

		try (var pstm = connection.prepareStatement(sql)) {
			pstm.setInt(1, ct.getId());
			pstm.execute();

			trasformarResultSetEmProduto(produtos, pstm);
		}
		return produtos;
	}

	public void deletar(Integer id) {
		var sql = "DELETE " +
				  "FROM produto " +
				  "WHERE id = ? ";

		try (var stm = connection.prepareStatement(sql)) {
			stm.setInt(1, id);
			stm.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void alterar(String nome, String descricao, Integer id) throws SQLException {
		var sql = "UPDATE produto " +
				  "SET " +
				  "nome = ?," +
				  "descricao = ? " +
				  "WHERE id = ? ";

		try (var stm = connection.prepareStatement(sql)) {
			stm.setString(1, nome);
			stm.setString(2, descricao);
			stm.setInt(3, id);
			stm.execute();

			System.out.println(stm.getUpdateCount());
		}
	}

	private void trasformarResultSetEmProduto(List<Produto> produtos, PreparedStatement pstm) throws SQLException {
		try (var rst = pstm.getResultSet()) {
			while (rst.next()) {
				var produto = new Produto(
						rst.getInt(1),
						rst.getString(2),
						rst.getString(3));

				produtos.add(produto);
			}
		}
	}
}
