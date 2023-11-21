package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.nullness.Opt;
import org.hibernate.criterion.Example;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.RamoController;
import com.pagueibaratoapi.models.requests.Ramo;
import com.pagueibaratoapi.models.responses.ResponseRamo;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;
import com.pagueibaratoapi.repository.RamoRepository;



@RunWith(SpringRunner.class)
public class RamoControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    @Autowired
    private RamoController ramoController;

    @Autowired
    private RamoRepository ramoRepository;

    private Ramo ramo;

    private List<Ramo> ramos;



    @Before
    public void setUp() {
        this.ramoRepository.deleteAll();

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

        ResponseRamo responseRamo = ramoController.criar(this.ramo);

        ramoRepository.deleteById(responseRamo.getId());

        assertNotNull(responseRamo);
        assertEquals(this.ramo.getNome(), responseRamo.getNome());

    }

    @Test
    public void criarRamoComCorpoNulo() throws Exception {

        try {

            ramoController.criar(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("corpo_nulo", e.getReason());
        }

    }

    @Test
    public void criarRamoComIdFornecido() throws Exception {

        this.ramo.setId(1);

        try {

            ramoController.criar(this.ramo);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("id_fornecido", e.getReason());
        }

    }

    @Test
    public void criarRamoComNomeInvalido() throws Exception {

        this.ramo.setNome("ma");

        try {

            ramoController.criar(this.ramo);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }

    }

    @Test
    public void criarRamoComDescricaoInvalida() throws Exception {

        this.ramo.setDescricao("teste");

        try {

            ramoController.criar(this.ramo);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("descricao_invalido", e.getReason());
        }

    }

    @Test
    public void criarRamoComNomeExistente() throws Exception {

        Ramo ramoCriado = ramoRepository.save(this.ramo);

        Ramo ramoComNomeIgual = new Ramo();

        ramoComNomeIgual.setNome(ramoCriado.getNome());
        ramoComNomeIgual.setDescricao("Descrição do ramo teste 2");

        try {

            ramoController.criar(ramoComNomeIgual);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("ramo_existente", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ------------------------  LEITURA DE RAMO POR ID  ------------------------ */

    @Test
    public void lerRamoPorIdComSucesso() throws Exception {

        Ramo ramoCriado = ramoRepository.save(this.ramo);

        ResponseRamo responseRamo = ramoController.ler(ramoCriado.getId());

        ramoRepository.deleteById(responseRamo.getId());

        assertNotNull(responseRamo);
        assertEquals(ramoCriado.getId(), responseRamo.getId());
        assertEquals(ramoCriado.getNome(), responseRamo.getNome());

    }

    @Test
    public void lerRamoPorIdComExcecaoNoSuchElement() throws Exception {

        try {

            ramoController.ler(2023);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    @Test
    public void lerRamoPorIdComExcecao() throws Exception {

        try {

            ramoController.ler(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ---------------------------  LISTAGEM DE RAMOS  -------------------------- */

    @Test
    public void listarRamosComSucesso() throws Exception {

        ramoRepository.saveAll(this.ramos);

        List<ResponseRamo> responseRamos = ramoController.listar(new Ramo());

        ramoRepository.deleteAll();

        assertNotNull(responseRamos);
        assertEquals(2, responseRamos.size());

    }

    @Test
    public void listarRamosComCorpoNulo() throws Exception {

        try {

            ramoController.listar(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("corpo_nulo", e.getReason());
        }

    }

    @Test
    public void listarRamosComIdFornecido() throws Exception {

        this.ramo.setId(1);

        try {

            ramoController.listar(this.ramo);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("id_fornecido", e.getReason());
        }

    }

    @Test
    public void listarRamosComNomeInvalido() throws Exception {

        this.ramo.setNome("ma");

        try {

            ramoController.listar(this.ramo);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }

    }

    @Test
    public void listarRamosComDescricaoInvalida() throws Exception {

        this.ramo.setDescricao("teste");

        try {

            ramoController.listar(this.ramo);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("descricao_invalido", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */

}