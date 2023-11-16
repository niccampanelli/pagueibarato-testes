package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagueibaratoapi.controllers.CategoriaController;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.responses.ResponseCategoria;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;
import com.pagueibaratoapi.repository.CategoriaRepository;


@RunWith(SpringRunner.class)
public class CategoriaControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    private MockMvc mockMvc;

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

        this.mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();

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

        // ObjectMapper mapper = new ObjectMapper();
        // String json = mapper.writeValueAsString(categoria);
        
        // MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/categoria")
        //         .contentType("application/json")
        //         .content(json))
        //         .andExpect(MockMvcResultMatchers.status().isOk())
        //         .andReturn();

        // String responseJson = result.getResponse().getContentAsString();
        // ResponseCategoria response = mapper.readValue(responseJson, ResponseCategoria.class);

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

    // @Test
    // public void listarCategorias() throws Exception {

    //     List<ResponseCategoria> responseCategoria = categoriaController.listar();

    //     System.out.println("TESTE:::::::::::: " + responseCategoria.get(0).getNome());
    //     System.out.println("TESTE:::::::::::: " + responseCategoria.size());

    //     // this.mockMvc.perform(MockMvcRequestBuilders.get("/categoria"))
    //     //             .andDo(MockMvcResultHandlers.print())
    //     //             .andExpect(MockMvcResultMatchers.status().isOk());

    // }

}