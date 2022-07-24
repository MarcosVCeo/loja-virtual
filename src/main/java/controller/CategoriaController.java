package controller;

import dao.CategoriaDAO;
import factory.ConnectionFactory;
import modelo.Categoria;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaController {

    private CategoriaDAO categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO(new ConnectionFactory().recuperarConexao());
    }

    public List<Categoria> listar() {
        return categoriaDAO.listar();
    }
}
