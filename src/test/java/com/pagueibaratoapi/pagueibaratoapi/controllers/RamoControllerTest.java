package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.checkerframework.checker.nullness.Opt;
import org.hibernate.criterion.Example;
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

    private List<Ramo> ramos;



    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.inicializarRamo();
    }

    private void inicializarRamo() {
        this.ramo = new Ramo();

        this.ramo.setNome("Ramo Teste");
        this.ramo.setDescricao("Descrição do ramo teste");

        Ramo ramo2 = new Ramo();

        ramo2.setNome("Ramo Teste 2");
        ramo2.setDescricao("Descrição do ramo teste 2");

        this.ramos = List.of(this.ramo, ramo2);
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





    /* ---------------------------  LISTAGEM DE RAMOS  -------------------------- */

    @Test
    public void listarRamosComSucesso() throws Exception {

        when(ramoRepository.findAll(isA(org.springframework.data.domain.Example.class))).thenReturn(ramos);
        
        List<ResponseRamo> responseRamos = ramoController.listar(ramo);

        assertNotNull(responseRamos);
        assertEquals(2, responseRamos.size());
    }

    @Test
    public void listarRamosComExcecaoDadosInvalidos() throws Exception {

        try {

            ramoController.listar(null);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "corpo_nulo");
        }

    }

    @Test
    public void listarRamosComExcecaoNaoEncontrado() throws Exception {

        when(ramoRepository.findAll(any(org.springframework.data.domain.Example.class))).thenReturn(new ArrayList<Ramo>());

        try {

            ramoController.listar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "nao_encontrado");
        }

    }

    @Test
    public void listarRamosComExcecaoUnsupportedOperation() throws Exception {

        when(ramoRepository.findAll(any(org.springframework.data.domain.Example.class))).thenThrow(new UnsupportedOperationException("erro_inesperado"));

        try {

            ramoController.listar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }

    }

    @Test
    public void listarRamosComExcecao() throws Exception {

        when(ramoRepository.findAll(any(org.springframework.data.domain.Example.class))).thenThrow(new RuntimeException("erro_inesperado"));

        try {

            ramoController.listar(ramo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ---------------------------  EDIÇÃO DE RAMOS  ---------------------------- */

    @Test
    public void editarRamoComSucesso() throws Exception {
        
        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Editado");
        requestRamo.setDescricao("Descrição do ramo editado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.findById(anyInt())).thenReturn(optionalRamo);
        when(optionalRamo.get()).thenReturn(ramo);

        when(ramoRepository.save(any())).thenReturn(requestRamo);

        ResponseRamo responseRamo = ramoController.editar(1, requestRamo);

        assertNotNull(responseRamo);
        assertEquals(requestRamo.getNome(), responseRamo.getNome());
        assertEquals(requestRamo.getDescricao(), responseRamo.getDescricao());

    }

    @Test
    public void editarRamoComNomeExistente() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Editado");
        requestRamo.setDescricao("Descrição do ramo editado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        try {

            ramoController.editar(1, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "ramo_existente");
        }
    }

    @Test
    public void editarRamoComExcecaoDadosInvalidos() throws Exception {

        try {

            ramoController.editar(1, null);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "corpo_nulo");
        }

    }

    @Test
    public void editarRamoComExcecaoDataViolation() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Editado");
        requestRamo.setDescricao("Descrição do ramo teste editado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.findById(anyInt())).thenReturn(optionalRamo);
        when(optionalRamo.get()).thenReturn(ramo);

        when(ramoRepository.save(any())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {

            ramoController.editar(1, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_insercao");
        }

    }

    @Test
    public void editarRamoComExcecaoIllegalArgument() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Editado");
        requestRamo.setDescricao("Descrição do ramo teste editado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.findById(anyInt())).thenReturn(optionalRamo);
        when(optionalRamo.get()).thenReturn(ramo);

        when(ramoRepository.save(any())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            ramoController.editar(1, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
        
    }

    @Test
    public void editarRamoComExcecaoNoSuchElement() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Editado");
        requestRamo.setDescricao("Descrição do ramo teste editado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.findById(anyInt())).thenReturn(optionalRamo);
        when(optionalRamo.get()).thenThrow(new NoSuchElementException("nao_encontrado"));

        try {

            ramoController.editar(1986, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "nao_encontrado");
        }

    }

    @Test
    public void editarRamoComExcecao() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Editado");
        requestRamo.setDescricao("Descrição do ramo teste editado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.findById(anyInt())).thenReturn(optionalRamo);

        try {

            ramoController.editar(1, requestRamo);

        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("NullPointerException"));
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -------------------------  ATUALIZAÇÃO DE RAMOS  ------------------------- */

    @Test
    public void atualizarRamoComSucesso() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Atualizado");
        requestRamo.setDescricao("Descrição do ramo teste atualizado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.save(any())).thenReturn(requestRamo);

        ResponseRamo responseRamo = ramoController.atualizar(1, requestRamo);

        assertNotNull(responseRamo);
        assertTrue(1 == responseRamo.getId());
        assertEquals(requestRamo.getNome(), responseRamo.getNome());
        assertEquals(requestRamo.getDescricao(), responseRamo.getDescricao());
    }

    @Test
    public void atualizarRamoComNomeExistente() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Atualizado");
        requestRamo.setDescricao("Descrição do ramo teste atualizado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        try {

            ramoController.atualizar(1, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "ramo_existente");
        }

    }

    @Test
    public void atualizarRamoComExcecaoDadosInvalidos() throws Exception {

        try {

            ramoController.atualizar(1, null);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "corpo_nulo");
        }

    }

    @Test
    public void atualizarRamoComExcecaoNoSuchElement() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Atualizado");
        requestRamo.setDescricao("Descrição do ramo teste atualizado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        
        when(ramoRepository.save(any())).thenThrow(new NoSuchElementException("nao_encontrado"));

        try {

            ramoController.atualizar(1986, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "nao_encontrado");
        }

    }

    @Test
    public void atualizarRamoComExcecaoDataViolation() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Atualizado");
        requestRamo.setDescricao("Descrição do ramo teste atualizado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.save(any())).thenThrow(new DataIntegrityViolationException("erro_insercao"));
        
        try {

            ramoController.atualizar(1, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_insercao");
        }

    }

    @Test
    public void atualizarRamoComExcecaoIllegalArgument() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Atualizado");
        requestRamo.setDescricao("Descrição do ramo teste atualizado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.save(any())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            ramoController.atualizar(1, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void atualizarRamoComExcecaoInesperada() throws Exception {

        Ramo requestRamo = new Ramo();
        requestRamo.setNome("Ramo Teste Atualizado");
        requestRamo.setDescricao("Descrição do ramo teste atualizado");

        when(ramoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(ramoRepository.save(any())).thenThrow(new RuntimeException("erro_inesperado"));

        try {

            ramoController.atualizar(2023, requestRamo);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "erro_inesperado");
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */

}