package com.pagueibaratoapi.pagueibaratoapi.integration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pagueibaratoapi.controllers.CategoriaController;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;

public class CategoriaControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private CategoriaController categoriaController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
    }

    @Test
    public void listarCategorias() throws Exception {

        this.mockMvc.perform().andExpect(status().isOk());

    }
}