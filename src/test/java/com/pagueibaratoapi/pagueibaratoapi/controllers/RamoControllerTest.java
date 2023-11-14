package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.checkerframework.checker.nullness.Opt;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.RamoController;
import com.pagueibaratoapi.models.requests.Ramo;
import com.pagueibaratoapi.models.responses.ResponseRamo;
import com.pagueibaratoapi.repository.RamoRepository;



public class RamoControllerTest {

    @InjectMocks
    private RamoController ramoController;

    @Mock
    private RamoRepository ramoRepository;

    @Mock
    private Optional<Ramo> optionalRamo;

    private Ramo ramo;



    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.inicializarRamo();
    }

    private void inicializarRamo() {
        this.ramo = new Ramo();

        this.ramo.setNome("Ramo Teste");
        this.ramo.setDescricao("Descrição do ramo teste");
    }



    /* ----------------------------  CRIAÇÃO DE RAMO  --------------------------- */

    @Test
    public void criarRamoComSucesso() throws Exception {

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.save(any())).thenReturn(ramo);

        ResponseRamo responseRamo = ramoController.criar(ramo);

        assertNotNull(responseRamo);
        assertEquals(ramo.getNome(), responseRamo.getNome());
    }

    @Test
    public void criarRamoComNomeExistente() throws Exception {

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        try {

            ramoController.criar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "ramo_existente");
        }
    }

    @Test
    public void criarRamoComExcecaoDadosInvalidos() throws Exception {
        this.ramo.setNome("");

        try {

            ramoController.criar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "nome_invalido");
        }

    }

    @Test
    public void criarRamoComExcecaoDataViolation() throws Exception {

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(ramoRepository.save(any())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {

            ramoController.criar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_insercao");
        }

    }

    @Test
    public void criarRamoComExcecaoIllegalArgument() throws Exception {

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(ramoRepository.save(any(Ramo.class))).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            ramoController.criar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void criarRamoComExcecaoInesperada() throws Exception {

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(ramoRepository.save(any(Ramo.class))).thenThrow(new RuntimeException("erro_inesperado"));

        try {

            ramoController.criar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ------------------------  LEITURA DE RAMO POR ID  ------------------------ */

    @Test
    public void lerRamoComSucesso() throws Exception {

        ramo.setId(1);

        when(ramoRepository.findById(anyInt())).thenReturn(optionalRamo);
        when(optionalRamo.get()).thenReturn(ramo);

        ResponseRamo responseRamo = ramoController.ler(1);

        assertNotNull(responseRamo);
        assertEquals(ramo.getId(), responseRamo.getId());
        assertEquals(ramo.getNome(), responseRamo.getNome());
        assertEquals(ramo.getDescricao(), responseRamo.getDescricao());

    }

    @Test
    public void lerRamoInexistente() throws Exception {

        ramo.setId(1);

        when(ramoRepository.findById(anyInt())).thenThrow(new NoSuchElementException("nao_encontrado"));

        try {

            ramoController.ler(1986);

        } catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "nao_encontrado");
        }

    }

    @Test
    public void lerRamoComExcecao() throws Exception {

        when(ramoRepository.findById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {

            ramoController.ler(1);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */

}