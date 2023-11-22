package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.CategoriaController;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.responses.ResponseCategoria;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;
import com.pagueibaratoapi.repository.CategoriaRepository;


@RunWith(SpringRunner.class)
public class CategoriaControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    @Autowired
    private CategoriaController categoriaController;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria categoria;
    private Categoria categoriaExistente;
    private Categoria categoriaInvalida;



    @Before
    public void setUp() {
        categoriaRepository.deleteAll();

        this.inicializarCategoria();
    }

    private void inicializarCategoria() {
        categoria = new Categoria();

        categoria.setNome("Perfumaria");
        categoria.setDescricao("Perfumes, sabonetes, shampoos, etc.");




        categoriaExistente = new Categoria();

        categoriaExistente.setNome("Livraria");
        categoriaExistente.setDescricao("Livros, revistas, jornais, etc.");



        categoriaInvalida = new Categoria();

        categoriaInvalida.setNome("");
        categoriaInvalida.setDescricao("Esta é uma categoria inválida.");
    }



    /* -------------------------  CRIAÇÃO DE CATEGORIA  ------------------------- */

    @Test
    public void criarCategoriaComSucesso() throws Exception {

        ResponseCategoria responseCategoria = categoriaController.criar(categoria);

        categoriaRepository.delete(categoriaRepository.findById(responseCategoria.getId()).get());

        assertTrue(categoria.getNome().equals(responseCategoria.getNome()));

    }

    @Test
    public void criarCategoriaComNomeExistente() throws Exception {

        try {

            categoriaController.criar(categoria);

        } 
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("nome_existente", e.getReason());
        }

    }

    @Test
    public void criarCategoriaComDadosInvalidos() throws Exception {

        try {

            categoriaController.criar(categoriaInvalida);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }

    }

    @Test
    public void criarCategoriaComCorpoNulo() throws Exception {

        try {

            categoriaController.criar(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("corpo_nulo", e.getReason());
        }

    }

    @Test
    public void criarCategoriaComIdFornecido() throws Exception {

        categoria.setId(1);

        try {

            categoriaController.criar(categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("id_fornecido", e.getReason());
        }

    }
    
    @Test
    public void criarCategoriaComNomeInvalido() throws Exception {

        categoria.setNome(null);

        try {

            categoriaController.criar(categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }

    }

    @Test
    public void criarCategoriaComDescricaoInvalida() throws Exception {

        categoria.setDescricao(null);

        try {

            categoriaController.criar(categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("descricao_invalido", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ----------------------  LEITURA DE CATEGORIA POR ID  --------------------- */

    @Test
    public void lerCategoriaPorIdComSucesso() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        ResponseCategoria responseCategoria = categoriaController.ler(categoriaCriada.getId());

        categoriaRepository.delete(categoriaCriada);

        assertTrue(categoria.getNome().equals(responseCategoria.getNome()));

    }

    @Test
    public void lerCategoriaPorComExcecaoNoSuchElement() throws Exception {

        try {

            categoriaController.ler(1);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    @Test
    public void lerCategoriaPorIdComExcecao() throws Exception {

        try {

            categoriaController.ler(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ------------------------  LISTAGEM DE CATEGORIAS  ------------------------ */

    @Test
    public void listarCategoriasComSucesso() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        List<ResponseCategoria> responseCategoria = categoriaController.listar();

        categoriaRepository.delete(categoriaCriada);

        assertTrue(categoria.getNome().equals(responseCategoria.get(0).getNome()));
        assertTrue(responseCategoria.size() == 1);

        // this.mockMvc.perform(MockMvcRequestBuilders.get("/categoria"))
        //             .andDo(MockMvcResultHandlers.print())
        //             .andExpect(MockMvcResultMatchers.status().isOk());

    }

    /* -------------------------------------------------------------------------- */





    /* -------------------------  EDIÇÃO DE CATEGORIAS  ------------------------- */

    @Test
    public void editarCategoriaComSucesso() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);


        Categoria categoriaEditada = new Categoria();

        categoriaEditada.setNome("Perfumaria e Higiene");


        ResponseCategoria responseCategoria = categoriaController.editar(categoriaCriada.getId(), categoriaEditada);

        categoriaRepository.delete(categoriaCriada);


        assertTrue(categoriaEditada.getNome().equals(responseCategoria.getNome()));
        assertTrue(categoria.getDescricao().equals(responseCategoria.getDescricao()));

    }

    @Test
    public void editarCategoriaComNomeExistente() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);
        
        Categoria categoriaExistenteCriada = categoriaRepository.save(categoriaExistente);

        Categoria categoriaEditada = new Categoria();

        categoriaEditada.setNome(categoriaExistenteCriada.getNome());

        try {

            categoriaController.editar(categoriaCriada.getId(), categoriaEditada);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("nome_existente", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
            categoriaRepository.delete(categoriaExistenteCriada);
        }

    }

    @Test
    public void editarCategoriaComCorpoNulo() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        try {

            categoriaController.editar(categoriaCriada.getId(), null);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("corpo_nulo", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
        }

    }

    @Test
    public void editarCategoriaComIdFornecido() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        try {

            categoriaController.editar(categoriaCriada.getId(), categoriaCriada);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("id_fornecido", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
        }

    }

    @Test
    public void editarCategoriaComNomeInvalido() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        Categoria categoriaEditada = new Categoria();

        categoriaEditada.setNome("");

        try {

            categoriaController.editar(categoriaCriada.getId(), categoriaEditada);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
        }

    }

    @Test
    public void editarCategoriaComDescricaoInvalida() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        Categoria categoriaEditada = new Categoria();

        categoriaEditada.setDescricao("");

        try {

            categoriaController.editar(categoriaCriada.getId(), categoriaEditada);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("descricao_invalido", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
        }

    }

    @Test
    public void editarCategoriaComExcecaoNoSuchElement() throws Exception {

        try {

            categoriaController.editar(2023, categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -----------------------  ATUALIZAÇÃO DE CATEGORIAS  ---------------------- */

    @Test
    public void atualizarCategoriaComSucesso() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        Categoria categoriaAtualizada = new Categoria();

        categoriaAtualizada.setNome("Perfumaria e Higiene");
        categoriaAtualizada.setDescricao("Perfumes, sabonetes, shampoos, beleza, etc.");

        ResponseCategoria responseCategoria = categoriaController.atualizar(categoriaCriada.getId(), 
                                                                            categoriaAtualizada);

        categoriaRepository.delete(categoriaCriada);

        assertTrue(categoriaCriada.getId().equals(responseCategoria.getId()));
        assertTrue(categoriaAtualizada.getNome().equals(responseCategoria.getNome()));
        assertTrue(categoriaAtualizada.getDescricao().equals(responseCategoria.getDescricao()));

    }

    @Test
    public void atualizarCategoriaComNomeExistente() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);
        
        Categoria categoriaExistenteCriada = categoriaRepository.save(categoriaExistente);

        Categoria categoriaAtualizada = new Categoria();

        categoriaAtualizada.setNome(categoriaExistenteCriada.getNome());
        categoriaAtualizada.setDescricao("Perfumes, sabonetes, shampoos, beleza, etc.");

        try {

            categoriaController.atualizar(categoriaCriada.getId(), categoriaAtualizada);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("nome_existente", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
            categoriaRepository.delete(categoriaExistenteCriada);
        }

    }

    @Test
    public void atualizarCategoriaComIdFornecido() throws Exception {

        Categoria categoriaAtualizada = new Categoria();

        categoriaAtualizada.setId(1);
        categoriaAtualizada.setNome("Perfumaria, Higiene e Beleza");
        categoriaAtualizada.setDescricao("Perfumes, sabonetes, shampoos, beleza, etc.");

        try {

            categoriaController.atualizar(1, categoriaAtualizada);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("id_fornecido", e.getReason());
        }

    }

    @Test
    public void atualizarCategoriaComNomeInvalido() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        Categoria categoriaAtualizada = new Categoria();

        categoriaAtualizada.setNome("");
        categoriaAtualizada.setDescricao("Perfumes, sabonetes, shampoos, beleza, etc.");

        try {

            categoriaController.atualizar(categoriaCriada.getId(), categoriaAtualizada);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
        }

    }

    @Test
    public void atualizarCategoriaComDescricaoInvalida() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        Categoria categoriaAtualizada = new Categoria();

        categoriaAtualizada.setNome("Perfumaria, Higiene e Beleza");
        categoriaAtualizada.setDescricao("");

        try {

            categoriaController.atualizar(categoriaCriada.getId(), categoriaAtualizada);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("descricao_invalido", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
        }

    }

    @Test
    public void atualizarCategoriaComExcecao() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        try {

            categoriaController.atualizar(categoriaCriada.getId(), null);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }
        finally {
            categoriaRepository.delete(categoriaCriada);
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -------------------------  DELEÇÃO DE CATEGORIAS  ------------------------- */

    @Test
    public void deletarCategoriaComSucesso() throws Exception {

        Categoria categoriaCriada = categoriaRepository.save(categoria);

        categoriaController.remover(categoriaCriada.getId());

        assertTrue(categoriaRepository.findById(categoriaCriada.getId()).isEmpty());
    }

    @Test
    public void deletarCategoriaComExcecaoNoSuchElement() throws Exception {

        try {

            categoriaController.remover(2023);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */

}