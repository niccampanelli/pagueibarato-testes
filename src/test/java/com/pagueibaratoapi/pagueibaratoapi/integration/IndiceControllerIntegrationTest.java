package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.pagueibaratoapi.controllers.IndiceController;



@RunWith(SpringRunner.class)
public class IndiceControllerIntegrationTest {

    @InjectMocks
    private IndiceController indiceController;



    @Before
    public void setUp() {

        // Inicializa os objetos mockados
        MockitoAnnotations.openMocks(this);

    }


    
    /* --------------------------  LISTAGEM DO ÍNDICE  -------------------------- */

    @Test
    public void listarRecursosComSucesso() {
        
        List<Object> responseLinks = indiceController.listar();

        assertTrue(responseLinks.size() > 0);
        assertTrue(responseLinks.size() == 7);
        assertTrue(responseLinks.get(0).toString().contains("categoria"));


    }

    /* -------------------------------------------------------------------------- */

}