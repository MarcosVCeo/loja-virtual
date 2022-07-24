package controller;

import dao.ProdutoDAO;
import factory.ConnectionFactory;
import modelo.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoController {

    private ProdutoDAO produtoDAO;

    public ProdutoController() {
        this.produtoDAO = new ProdutoDAO(new ConnectionFactory().recuperarConexao());
    }

	public void deletar(Integer id) {

		produtoDAO.deletar(id);

	}

	public void salvar(Produto produto) {
		produtoDAO.salvar(produto);
	}

    public List<Produto> listar() {
        return produtoDAO.listar();
    }

    public void alterar(String nome, String descricao, Integer id) {
        try (var connection = new ConnectionFactory().recuperarConexao()) {
            new ProdutoDAO(connection).alterar(nome, descricao, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
