package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagueibaratoapi.controllers.CategoriaController;
import com.pagueibaratoapi.models.exceptions.DadosConflitantesException;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.responses.ResponseCategoria;
import com.pagueibaratoapi.repository.CategoriaRepository;

@WebMvcTest(CategoriaController.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private CategoriaController categoriaController;

    @Mock
    private CategoriaRepository categoriaRepository;

    private Categoria categoria;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoria = new Categoria();
        categoria.setNome("Eletrodomésticos");
        categoria.setDescricao("Eletrodomésticos em geral");
    }

    @Test
    public void criarCategoriaComSucesso() throws Exception {


        when(categoriaRepository.existsByNomeIgnoreCase(any())).thenReturn(false);
        when(categoriaRepository.save(any())).thenReturn(categoria);


        // mockMvc.perform(post("/categorias")
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .content(objectMapper.writeValueAsString(categoria)))
        //         .andExpect(status().isOk())
        //         .andExpect(jsonPath("$.nome").value("Eletrônicos"));

        ResponseCategoria responseCategoria = categoriaController.criar(categoria);

        assertTrue(categoria.getNome().equals(responseCategoria.getNome()));

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
}