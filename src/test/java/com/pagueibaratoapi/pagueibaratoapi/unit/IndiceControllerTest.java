package com.pagueibaratoapi.pagueibaratoapi.unit;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pagueibaratoapi.controllers.CategoriaController;
import com.pagueibaratoapi.controllers.IndiceController;



@WebMvcTest(CategoriaController.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class IndiceControllerTest {

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