package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.CategoriaController;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.responses.ResponseCategoria;
import com.pagueibaratoapi.repository.CategoriaRepository;

@WebMvcTest(CategoriaController.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoriaControllerTest {

    @InjectMocks
    private CategoriaController categoriaController;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private Optional<Categoria> optionalCategoriaMock;

    @Mock
    private List<ResponseCategoria> responseCategoria;

    private Categoria categoria;

    private List<Categoria> categorias;



    @Before
    public void configurar() {
        // Inicializa os objetos mockados
        MockitoAnnotations.openMocks(this);

        // Inicializa o objeto categoria, atribuindo valores aos seus atributos
        this.inicializarCategoria();

        // Inicializa a lista de categorias, atribuindo valores aos seus atributos
        this.inicializarCategorias();
    }

    private void inicializarCategoria() {
        categoria = new Categoria();
        categoria.setNome("Eletrodomésticos");
        categoria.setDescricao("Eletrodomésticos em geral");
    }

    private void inicializarCategorias() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(1);
        categoria1.setNome("Eletrodomésticos");
        categoria1.setDescricao("Eletrodomésticos em geral");

        Categoria categoria2 = new Categoria();
        categoria2.setId(2);
        categoria2.setNome("Eletroeletrônicos");
        categoria2.setDescricao("Eletroeletrônicos em geral");

        Categoria categoria3 = new Categoria();
        categoria3.setId(3);
        categoria3.setNome("Eletroportáteis");
        categoria3.setDescricao("Eletroportáteis em geral");

        categorias = List.of(categoria1, categoria2, categoria3);

    }


    /* ------------------------  CRIAÇÃO DE CATEGORIA  ------------------------ */

    @Test
    public void criarCategoriaComSucesso() throws Exception {


        when(categoriaRepository.existsByNomeIgnoreCase(any())).thenReturn(false);
        when(categoriaRepository.save(any())).thenReturn(categoria);

        ResponseCategoria response = categoriaController.criar(categoria);

        assertTrue(categoria.getNome().equals(response.getNome()));

    }

    @Test
    public void criarCategoriaComNomeExistente() throws Exception {

        when(categoriaRepository.existsByNomeIgnoreCase(any())).thenReturn(true);

        try {
            categoriaController.criar(categoria);
        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
        }

    }

    @Test
    public void criarCategoriaComDadosInvalidos() throws Exception {

        try {
            categoriaController.criar(null);
        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
        }

    }

    @Test
    public void criarCategoriaComExcecaoDataViolation() throws Exception {

        when(categoriaRepository.save(categoria)).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {

            categoriaController.criar(categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_insercao", e.getReason());
        }

    }

    @Test
    public void criarCategoriaComExcecaoIllegalArgument() throws Exception {

        when(categoriaRepository.save(categoria)).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            categoriaController.criar(categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    // @Test
    // public void criarCategoriaComExcecao() throws Exception {

    //     when(categoriaRepository.save(any())).thenThrow(new RuntimeException("erro_inesperado"));

    //     try {

    //         categoriaController.criar(categoria);

    //     }
    //     catch (ResponseStatusException e) {
    //         System.out.println(e);
    //         assertEquals(500, e.getRawStatusCode());
    //         assertEquals("erro_inesperado", e.getReason());
    //         assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
    //     }

    // }

    /* -------------------------------------------------------------------------- */





    /* ---------------------  LEITURA DE CATEGORIA POR ID  ---------------------- */

    @Test
    public void lerCategoriaPorIdComSucesso() throws Exception {

        when(categoriaRepository.findById(any())).thenReturn(Optional.ofNullable(categoria));

        ResponseCategoria response = categoriaController.ler(1);

        assertTrue(categoria.getNome().equals(response.getNome()));
    }

    @Test
    public void lerCategoriaPorIdComExcecaoNoSuchElement() throws Exception {

        when(categoriaRepository.findById(any())).thenThrow(new NoSuchElementException("nao_encontrado"));

        try {

            categoriaController.ler(1986);
            
        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }
    }

    @Test
    public void lerCategoriaPorIdComExcecao() throws Exception {

        when(categoriaRepository.findById(any())).thenReturn(optionalCategoriaMock);
        when(optionalCategoriaMock.get()).thenThrow(new NullPointerException());

        try {

            categoriaController.ler(1986);
            
        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }
    }

    /* -------------------------------------------------------------------------- */





    /* ------------------------  LISTAGEM DE CATEGORIAS  ------------------------ */

    @Test
    public void listarCategoriasComSucesso() throws Exception {

        // Configura o mock do repository para retornar a lista de categorias
        when(categoriaRepository.findAll()).thenReturn(categorias);

        // Chama o método para listar categorias do controller
        List<ResponseCategoria> categoriasResponse = categoriaController.listar();

        // Verifica se a lista de categorias retornada pelo controller é igual a lista de categorias criada
        assertTrue(!categoriasResponse.isEmpty());
    }

    @Test
    public void listarCategoriasComSucessoSemRetorno() throws Exception {

        // Cria uma lista de categorias vazia que o mock do repository deve retornar
        List<Categoria> categoriasList = new ArrayList<Categoria>();

        // Configura o mock do repository para retornar a lista de categorias
        when(categoriaRepository.findAll()).thenReturn(categoriasList);

        // Chama o método para listar categorias do controller
        List<ResponseCategoria> categoriasResponse = categoriaController.listar();

        // Verifica se a lista de categorias retornada pelo controller é igual a lista de categorias criada
        assertTrue(categoriasResponse.isEmpty());
    }

    @Test
    public void listarCategoriasComExcecaoNullPointer() throws Exception {

        // Configura o mock do repository para que retorne uma exceção NullPointerException
        when(categoriaRepository.findAll()).thenThrow(new NullPointerException());

        try {

            // Chama o método para listar categorias do controller
            categoriaController.listar();

        }
        catch (ResponseStatusException e) {
            // Verifica se o código de resposta é 404
            assertEquals(404, e.getRawStatusCode());
            // Verifica se a mensagem de erro é "nao_encontrado"
            assertEquals("nao_encontrado", e.getReason());
        }
    }

    @Test
    public void listarCategoriasComExcecaoUnsupportedOperation() throws Exception {

        // Configura o mock do repository para que retorne uma exceção UnsupportedOperationException
        when(categoriaRepository.findAll()).thenThrow(new UnsupportedOperationException());

        try {

            // Chama o método para listar categorias do controller
            categoriaController.listar();

        }
        catch (ResponseStatusException e) {
            // Verifica se o código de resposta é 404
            assertEquals(500, e.getRawStatusCode());
            // Verifica se a mensagem de erro é "erro_inesperado"
            assertEquals("erro_inesperado", e.getReason());
            // Verifica se a causa da exceção é UnsupportedOperationException
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }
    }

    @Test
    public void listarCategoriasComExcecao() throws Exception {

        // Configura o mock do repository para que retorne uma exceção
        when(responseCategoria.add(any())).thenThrow(new NullPointerException());

        try {

            // Chama o método para listar categorias do controller
            categoriaController.listar();

        }
        catch (ResponseStatusException e) {
            // Verifica se o código de resposta é 404
            assertEquals(500, e.getRawStatusCode());
            // Verifica se a mensagem de erro é "erro_inesperado"
            assertEquals("erro_inesperado", e.getReason());
            // Verifica se a causa da exceção é UnsupportedOperationException
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }
    }

    /* -------------------------------------------------------------------------- */





    /* -------------------------  EDIÇÃO DE CATEGORIAS  ------------------------- */

    @Test
    public void editarCategoriaComSucesso() throws Exception {

        when(categoriaRepository.findById(any())).thenReturn(Optional.ofNullable(categoria));
        when(categoriaRepository.save(any())).thenReturn(categoria);

        ResponseCategoria response = categoriaController.editar(1, categoria);

        assertTrue(categoria.getNome().equals(response.getNome()));
    }

    @Test
    public void editarCategoriaComExcecaoDadosConflitantes() throws Exception {

        when(categoriaRepository.existsByNomeIgnoreCase(any())).thenReturn(true);

        try {

            categoriaController.editar(1, categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("nome_existente", e.getReason());
        }
    }

    @Test
    public void editarCategoriaComExcecaoDadosInvalidos() throws Exception {

        try {

            categoriaController.editar(1, null);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
        }

    }

    @Test
    public void editarCategoriaComExcecaoDataViolation() throws Exception {

        when(categoriaRepository.findById(anyInt())).thenReturn(optionalCategoriaMock);
        when(optionalCategoriaMock.get()).thenReturn(categoria);
        when(categoriaRepository.save(any())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {

            categoriaController.editar(1, categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_insercao", e.getReason());
        }

    }

    @Test
    public void editarCategoriaComExcecaoIllegalArgument() throws Exception {

        when(categoriaRepository.findById(anyInt())).thenReturn(optionalCategoriaMock);
        when(optionalCategoriaMock.get()).thenReturn(categoria);
        when(categoriaRepository.save(any())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            categoriaController.editar(1, categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void editarCategoriaComExcecaoNoSuchElement() throws Exception {

        when(categoriaRepository.findById(anyInt())).thenThrow(new NoSuchElementException());

        try {

            categoriaController.editar(2023, categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    @Test
    public void editarCategoriaComExcecao() throws Exception {

        when(categoriaRepository.findById(any())).thenReturn(optionalCategoriaMock);
        when(optionalCategoriaMock.get()).thenThrow(new NullPointerException());

        try {

            categoriaController.editar(1, categoria);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */
}