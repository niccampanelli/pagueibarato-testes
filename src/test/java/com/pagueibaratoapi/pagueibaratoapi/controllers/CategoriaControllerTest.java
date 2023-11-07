package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagueibaratoapi.controllers.CategoriaController;
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
        System.out.println(responseCategoria.getNome());
        assertNull(responseCategoria.getNome());

    }

    // @Test
    // public void criarCategoriaComNomeExistente() throws Exception {
    //     Categoria categoria = new Categoria("Eletrônicos");

    //     when(categoriaRepository.existsByNomeIgnoreCase(any())).thenReturn(true);

    //     mockMvc.perform(post("/categorias")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(categoria)))
    //             .andExpect(status().isConflict())
    //             .andExpect(jsonPath("$.message").value("nome_existente"));
    // }

    // @Test
    // public void criarCategoriaComDadosInvalidos() throws Exception {
    //     Categoria categoria = new Categoria("");

    //     mockMvc.perform(post("/categorias")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(categoria)))
    //             .andExpect(status().isBadRequest());
    // }

    // @Test
    // public void criarCategoriaComErroInesperado() throws Exception {
    //     Categoria categoria = new Categoria("Eletrônicos");

    //     when(categoriaRepository.existsByNomeIgnoreCase(any())).thenThrow(new RuntimeException());

    //     mockMvc.perform(post("/categorias")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(categoria)))
    //             .andExpect(status().isInternalServerError())
    //             .andExpect(jsonPath("$.message").value("erro_inesperado"));
    // }
}