package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.MercadoController;
import com.pagueibaratoapi.models.requests.Mercado;
import com.pagueibaratoapi.models.requests.Ramo;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseMercado;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.RamoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;



@RunWith(SpringRunner.class)
public class MercadoControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    @Autowired
    private MercadoController mercadoController;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    @Autowired
    private RamoRepository ramoRepository;

    private Usuario usuario;

    private Ramo ramo;

    private Mercado mercado;



    @Before
    public void setUp() {
        usuarioRepository.deleteAll();
        mercadoRepository.deleteAll();
        ramoRepository.deleteAll();

        this.inicializarUsuario();
        this.inicializarRamo();
        this.inicializarMercado();
    }

    private void inicializarUsuario() {

        this.usuario = new Usuario();

        this.usuario.setLogradouro("Rua Teste");
        this.usuario.setNumero(12);
        this.usuario.setBairro("Bairro Teste");
        this.usuario.setCidade("Cidade Teste");
        this.usuario.setUf("SP");
        this.usuario.setCep("12345-678");
        this.usuario.setComplemento("Complemento Teste");
        this.usuario.setEmail("teste@email.com");
        this.usuario.setSenha("123456UsuarioT3ST3!");
        this.usuario.setNome("Usuario Teste");

    }

    private void inicializarRamo() {

        this.ramo = new Ramo();

        ramo.setNome("Ramo Teste");
        ramo.setDescricao("Descrição Teste");
    }

    private void inicializarMercado() {
        
        this.mercado = new Mercado();

        this.mercado.setNome("Mercado Teste");
        this.mercado.setLogradouro("Rua Teste");
        this.mercado.setNumero(12);
        this.mercado.setBairro("Bairro Teste");
        this.mercado.setCidade("Cidade Teste");
        this.mercado.setUf("SP");
        this.mercado.setCep("12345-678");
        this.mercado.setComplemento("Complemento Teste");

    }



    /* --------------------------  CRIAÇÃO DE MERCADO  -------------------------- */

    @Test
    public void criarMercadoComSucesso() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        ResponseMercado responseMercado = mercadoController.criar(this.mercado);

        mercadoRepository.deleteById(responseMercado.getId());

        assertEquals(responseMercado.getNome(), this.mercado.getNome());
        assertEquals(responseMercado.getLogradouro(), this.mercado.getLogradouro());
        assertEquals(responseMercado.getNumero(), this.mercado.getNumero());
        assertEquals(responseMercado.getBairro(), this.mercado.getBairro());
        assertEquals(responseMercado.getCidade(), this.mercado.getCidade());
        assertEquals(responseMercado.getUf(), this.mercado.getUf());
        assertEquals(responseMercado.getCep(), this.mercado.getCep());
        assertEquals(responseMercado.getRamoId(), this.mercado.getRamoId());

    }

    @Test
    public void criarMercadoComCorpoNulo() throws Exception {

        try {

            mercadoController.criar(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "corpo_nulo");
        }

    }

    @Test
    public void criarMercadoComIdFornecido() throws Exception {

        this.mercado.setId(1);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "id_fornecido");
        }

    }

    @Test
    public void criarMercadoComComplementoInvalido() throws Exception {

        this.mercado.setComplemento("Complemento Teste Complemento Teste Complemento Teste");

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "complemento_invalido");
        }

    }

    @Test
    public void criarMercadoComNomeInvalido() throws Exception {

        this.mercado.setNome("Mercado teste Mercado teste Mercado teste Mercado teste Mercado teste");

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "nome_invalido");
        }

    }

    @Test
    public void criarMercadoComLogradouroInvalido() throws Exception {

        this.mercado.setLogradouro("Rua");

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "logradouro_invalido");
        }

    }

    @Test
    public void criarMercadoComNumeroInvalido() throws Exception {

        this.mercado.setNumero(0);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "numero_invalido");
        }

    }

    @Test
    public void criarMercadoComBairroInvalido() throws Exception {

        this.mercado.setBairro("Test");

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "bairro_invalido");
        }

    }

    @Test
    public void criarMercadoComCidadeInvalida() throws Exception {

        this.mercado.setCidade("Sã");

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cidade_invalido");
        }

    }

    @Test
    public void criarMercadoComUfInvalida() throws Exception {

        this.mercado.setUf("S");

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "uf_invalido");
        }

    }

    @Test
    public void criarMercadoComCepInvalido() throws Exception {

        this.mercado.setCep("123456789");

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cep_invalido");
        }

    }

    @Test
    public void criarMercadoComUsuarioInvalido() throws Exception {

        this.mercado.setCriadoPor(0);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "usuario_invalido");
        }

    }

    @Test
    public void criarMercadoComRamoInvalido() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(0);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "ramo_invalido");
        }
        finally {
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void criarMercadoComUsuarioInexistente() throws Exception {

        this.mercado.setCriadoPor(1986);
        this.mercado.setRamoId(1);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getReason(), "usuario_nao_encontrado");
        }

    }

    @Test
    public void criarMercadoComUsuarioDeletado() throws Exception {

        Usuario usuarioDeletado = new Usuario();

        usuarioDeletado.setNome("");
        usuarioDeletado.setEmail("");
        usuarioDeletado.setSenha("");
        usuarioDeletado.setLogradouro("");
        usuarioDeletado.setNumero(-1);
        usuarioDeletado.setComplemento(null);
        usuarioDeletado.setBairro("");
        usuarioDeletado.setCidade("");
        usuarioDeletado.setUf("--");
        usuarioDeletado.setCep("00000-000");

        Usuario novoUsuario = usuarioRepository.save(usuarioDeletado);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(1);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getReason(), "usuario_nao_encontrado");
        }
        finally {
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void criarMercadoComRamoInexistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(1986);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getReason(), "ramo_nao_encontrado");
        }

    }

    @Test
    public void criarMercadoComNomeExistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado mercadoIgual = new Mercado();

        mercadoIgual.setNome("Mercado Teste");
        mercadoIgual.setLogradouro("Rua Teste");
        mercadoIgual.setNumero(12);
        mercadoIgual.setBairro("Bairro Teste");
        mercadoIgual.setCidade("Cidade Teste");
        mercadoIgual.setUf("SP");
        mercadoIgual.setCep("12345-678");
        mercadoIgual.setComplemento("Complemento Teste");
        mercadoIgual.setCriadoPor(novoUsuario.getId());
        mercadoIgual.setRamoId(novoRamo.getId());

        Mercado mercadoExistente = mercadoRepository.save(mercadoIgual);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getReason(), "mercado_existente");
        }
        finally {
            mercadoRepository.deleteById(mercadoExistente.getId());
        }

    }

    @Test
    public void criarMercadoComEnderecoExistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado mercadoIgual = new Mercado();

        mercadoIgual.setNome("Mercado igual");
        mercadoIgual.setLogradouro("Rua Teste");
        mercadoIgual.setNumero(12);
        mercadoIgual.setBairro("Bairro Teste");
        mercadoIgual.setCidade("Cidade Teste");
        mercadoIgual.setUf("SP");
        mercadoIgual.setCep("12345-678");
        mercadoIgual.setComplemento("Complemento Teste");
        mercadoIgual.setCriadoPor(novoUsuario.getId());
        mercadoIgual.setRamoId(novoRamo.getId());

        Mercado mercadoExistente = mercadoRepository.save(mercadoIgual);

        try {

            mercadoController.criar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getReason(), "mercado_existente");
        }
        finally {
            mercadoRepository.deleteById(mercadoExistente.getId());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ----------------------  LEITURA DE MERCADO POR ID  ----------------------- */

    @Test
    public void lerMercadoPorIdComSucesso() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado novoMercado = mercadoRepository.save(this.mercado);

        ResponseMercado responseMercado = mercadoController.ler(novoMercado.getId());

        mercadoRepository.deleteById(novoMercado.getId());

        assertEquals(responseMercado.getNome(), this.mercado.getNome());
        assertEquals(responseMercado.getLogradouro(), this.mercado.getLogradouro());
        assertEquals(responseMercado.getNumero(), this.mercado.getNumero());
        assertEquals(responseMercado.getBairro(), this.mercado.getBairro());
        assertEquals(responseMercado.getCidade(), this.mercado.getCidade());
        assertEquals(responseMercado.getUf(), this.mercado.getUf());
        assertEquals(responseMercado.getCep(), this.mercado.getCep());
        assertEquals(responseMercado.getRamoId(), this.mercado.getRamoId());

    }

    @Test
    public void lerMercadoComExcecaoNoSuchElement() throws Exception {

        try {

            mercadoController.ler(1986);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getReason(), "nao_encontrado");
        }

    }
    
    @Test
    public void lerMercadoComExcecao() throws Exception {

        try {

            mercadoController.ler(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -------------------------  LISTAGEM DE MERCADOS  ------------------------- */

    @Test
    public void listarMercadosComSucesso() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado novoMercado = mercadoRepository.save(this.mercado);

        Mercado mercado2 = new Mercado();

        mercado2.setNome("Mercado Teste 2");
        mercado2.setLogradouro("Rua Teste 2");
        mercado2.setNumero(12);
        mercado2.setBairro("Bairro Teste");
        mercado2.setCidade("Cidade Teste");
        mercado2.setUf("SP");
        mercado2.setCep("12345-678");
        mercado2.setComplemento("Complemento Teste");
        mercado2.setCriadoPor(novoUsuario.getId());
        mercado2.setRamoId(novoRamo.getId());

        Mercado novoMercado2 = mercadoRepository.save(mercado2);

        List<ResponseMercado> responseMercados = mercadoController.listar(new Mercado());

        mercadoRepository.deleteById(novoMercado.getId());
        mercadoRepository.deleteById(novoMercado2.getId());

        assertEquals(responseMercados.size(), 2);
        assertEquals(responseMercados.get(0).getLogradouro(), this.mercado.getLogradouro());

    }

    @Test
    public void listarMercadosComCorpoNulo() throws Exception {

        try {

            mercadoController.listar(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "corpo_nulo");
        }

    }

    @Test
    public void listarMercadosComIdFornecido() throws Exception {

        this.mercado.setId(1);

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "id_fornecido");
        }

    }

    @Test
    public void listarMercadosComComplementoInvalido() throws Exception {

        this.mercado.setComplemento("Complemento Teste Complemento Teste Complemento Teste");

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "complemento_invalido");
        }

    }

    @Test
    public void listarMercadosComNomeInvalido() throws Exception {

        this.mercado.setNome("Mercado teste Mercado teste Mercado teste Mercado teste Mercado teste");

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "nome_invalido");
        }

    }

    @Test
    public void listarMercadosComLogradouroInvalido() throws Exception {

        this.mercado.setLogradouro("Rua");

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "logradouro_invalido");
        }

    }

    @Test
    public void listarMercadosComNumeroInvalido() throws Exception {

        this.mercado.setNumero(-1);

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "numero_invalido");
        }

    }

    @Test
    public void listarMercadosComBairroInvalido() throws Exception {

        this.mercado.setBairro("Test");

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "bairro_invalido");
        }

    }

    @Test
    public void listarMercadosComCidadeInvalida() throws Exception {

        this.mercado.setCidade("Sã");

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cidade_invalido");
        }

    }

    @Test
    public void listarMercadosComUfInvalida() throws Exception {

        this.mercado.setUf("S");

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "uf_invalido");
        }

    }

    @Test
    public void listarMercadosComCepInvalido() throws Exception {

        this.mercado.setCep("123456789");

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cep_invalido");
        }

    }

    @Test
    public void listarMercadosComUsuarioInvalido() throws Exception {

        this.mercado.setCriadoPor(0);

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "usuario_invalido");
        }

    }

    @Test
    public void listarMercadosComRamoInvalido() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(0);

        try {

            mercadoController.listar(this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "ramo_invalido");
        }
        finally {
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* --------------------------  EDIÇÃO DE MERCADOS  -------------------------- */

    @Test
    public void editarMercadoComSucesso() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado novoMercado = mercadoRepository.save(this.mercado);

        Mercado mercadoEditado = new Mercado();

        mercadoEditado.setNome("Mercado Editado");
        mercadoEditado.setLogradouro("Rua Editada");
        mercadoEditado.setNumero(13);
        mercadoEditado.setBairro("Bairro Editado");
        mercadoEditado.setCidade("Cidade Editada");
        mercadoEditado.setUf("RJ");
        mercadoEditado.setCep("98765-432");
        mercadoEditado.setComplemento("Complemento Editado");
        mercadoEditado.setCriadoPor(novoUsuario.getId());
        mercadoEditado.setRamoId(novoRamo.getId());

        ResponseMercado responseMercado = mercadoController.editar(novoMercado.getId(), mercadoEditado);

        mercadoRepository.deleteById(novoMercado.getId());

        assertEquals(responseMercado.getId(), novoMercado.getId());
        assertEquals(responseMercado.getNome(), mercadoEditado.getNome());
        assertEquals(responseMercado.getLogradouro(), mercadoEditado.getLogradouro());
        assertEquals(responseMercado.getNumero(), mercadoEditado.getNumero());
        assertEquals(responseMercado.getBairro(), mercadoEditado.getBairro());
        assertEquals(responseMercado.getCidade(), mercadoEditado.getCidade());
        assertEquals(responseMercado.getUf(), mercadoEditado.getUf());
        assertEquals(responseMercado.getCep(), mercadoEditado.getCep());
        assertEquals(responseMercado.getRamoId(), mercadoEditado.getRamoId());

    }

    @Test
    public void editarMercadoComCorpoNulo() throws Exception {

        try {

            mercadoController.editar(1, null);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "corpo_nulo");
        }

    }

    @Test
    public void editarMercadoComIdFornecido() throws Exception {

        this.mercado.setId(1);

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "id_fornecido");
        }

    }

    @Test
    public void editarMercadoComComplementoInvalido() throws Exception {

        this.mercado.setComplemento("Complemento Teste Complemento Teste Complemento Teste");

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "complemento_invalido");
        }

    }

    @Test
    public void editarMercadoComNomeInvalido() throws Exception {

        this.mercado.setNome("Mercado teste Mercado teste Mercado teste Mercado teste Mercado teste");

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "nome_invalido");
        }

    }

    @Test
    public void editarMercadoComLogradouroInvalido() throws Exception {

        this.mercado.setLogradouro("Rua");

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "logradouro_invalido");
        }

    }

    @Test
    public void editarMercadoComNumeroInvalido() throws Exception {

        this.mercado.setNumero(-1);

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "numero_invalido");
        }

    }

    @Test
    public void editarMercadoComBairroInvalido() throws Exception {

        this.mercado.setBairro("Test");

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "bairro_invalido");
        }

    }

    @Test
    public void editarMercadoComCidadeInvalida() throws Exception {

        this.mercado.setCidade("Sã");

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cidade_invalido");
        }

    }

    @Test
    public void editarMercadoComUfInvalida() throws Exception {

        this.mercado.setUf("S");

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "uf_invalido");
        }

    }

    @Test
    public void editarMercadoComCepInvalido() throws Exception {

        this.mercado.setCep("123456789");

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cep_invalido");
        }

    }

    @Test
    public void editarMercadoComUsuarioInvalido() throws Exception {

        this.mercado.setCriadoPor(0);

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "usuario_invalido");
        }

    }

    @Test
    public void editarMercadoComRamoInvalido() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(0);

        try {

            mercadoController.editar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "ramo_invalido");
        }
        finally {
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void editarMercadoComEnderecoExistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado novoMercado = mercadoRepository.save(this.mercado);

        Mercado mercadoEditado = new Mercado();

        mercadoEditado.setNome("Mercado Editado");
        mercadoEditado.setLogradouro("Rua Teste");
        mercadoEditado.setNumero(12);
        mercadoEditado.setBairro("Bairro Teste");
        mercadoEditado.setCidade("Cidade Teste");
        mercadoEditado.setUf("SP");
        mercadoEditado.setCep("12345-678");
        mercadoEditado.setComplemento("Complemento Teste");
        mercadoEditado.setCriadoPor(novoUsuario.getId());
        mercadoEditado.setRamoId(novoRamo.getId());

        try {

            mercadoController.editar(novoMercado.getId(), mercadoEditado);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getReason(), "mercado_existente");
        }
        finally {
            mercadoRepository.deleteById(novoMercado.getId());
        }

    }

    @Test
    public void editarMercadoComRamoInexistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado novoMercado = mercadoRepository.save(this.mercado);

        Mercado mercadoEditado = new Mercado();

        mercadoEditado.setNome("Mercado Editado");
        mercadoEditado.setRamoId(2023);

        try {

            mercadoController.editar(novoMercado.getId(), mercadoEditado);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getReason(), "ramo_nao_encontrado");
        }
        finally {
            mercadoRepository.deleteById(novoMercado.getId());
            ramoRepository.deleteById(novoRamo.getId());
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void editarMercadoComNomeExistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado novoMercado = mercadoRepository.save(this.mercado);

        Mercado mercadoEditado = new Mercado();

        mercadoEditado.setNome("Mercado Editado");
        mercadoEditado.setLogradouro("Rua Editada 2");
        mercadoEditado.setNumero(13);
        mercadoEditado.setBairro("Bairro Editado");
        mercadoEditado.setCidade("Cidade Editada");
        mercadoEditado.setUf("RJ");
        mercadoEditado.setCep("98765-432");
        mercadoEditado.setComplemento("Complemento Editado");
        mercadoEditado.setCriadoPor(novoUsuario.getId());
        mercadoEditado.setRamoId(novoRamo.getId());

        try {

            mercadoController.editar(novoMercado.getId(), mercadoEditado);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getReason(), "mercado_existente");
        }
        finally {
            mercadoRepository.deleteById(novoMercado.getId());
            ramoRepository.deleteById(novoRamo.getId());
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void editarMercadoComExcecaoNoSuchElement() throws Exception {

        try {

            mercadoController.editar(1986, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getReason(), "nao_encontrado");
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -----------------------  ATUALIZAÇÃO DE MERCADOS  ------------------------ */

    @Test
    public void atualizarMercadoComSucesso() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado mercadoCriado = mercadoRepository.save(this.mercado);

        Mercado mercadoAtualizado = new Mercado();

        mercadoAtualizado.setNome("Mercado Atualizado");
        mercadoAtualizado.setLogradouro("Rua Atualizada");
        mercadoAtualizado.setNumero(13);
        mercadoAtualizado.setBairro("Bairro Atualizado");
        mercadoAtualizado.setCidade("Cidade Atualizada");
        mercadoAtualizado.setUf("RJ");
        mercadoAtualizado.setCep("98765-432");
        mercadoAtualizado.setComplemento("Comp. Atualizado");
        mercadoAtualizado.setCriadoPor(novoUsuario.getId());
        mercadoAtualizado.setRamoId(novoRamo.getId());

        ResponseMercado responseMercado = mercadoController.atualizar(mercadoCriado.getId(), mercadoAtualizado);

        mercadoRepository.deleteById(mercadoCriado.getId());
        usuarioRepository.deleteById(novoUsuario.getId());
        ramoRepository.deleteById(novoRamo.getId());

        assertEquals(responseMercado.getId(), mercadoCriado.getId());
        assertEquals(responseMercado.getNome(), mercadoAtualizado.getNome());
        assertEquals(responseMercado.getLogradouro(), mercadoAtualizado.getLogradouro());
        assertEquals(responseMercado.getNumero(), mercadoAtualizado.getNumero());
        assertEquals(responseMercado.getBairro(), mercadoAtualizado.getBairro());
        assertEquals(responseMercado.getCidade(), mercadoAtualizado.getCidade());
        assertEquals(responseMercado.getUf(), mercadoAtualizado.getUf());
        assertEquals(responseMercado.getCep(), mercadoAtualizado.getCep());
        assertEquals(responseMercado.getRamoId(), mercadoAtualizado.getRamoId());

    }

    @Test
    public void atualizarMercadoComCorpoNulo() throws Exception {

        try {

            mercadoController.atualizar(1, null);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "corpo_nulo");
        }

    }

    @Test
    public void atualizarMercadoComIdFornecido() throws Exception {

        this.mercado.setId(1);

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "id_fornecido");
        }

    }

    @Test
    public void atualizarMercadoComComplementoInvalido() throws Exception {

        this.mercado.setComplemento("Complemento Teste Complemento Teste Complemento Teste");

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "complemento_invalido");
        }

    }

    @Test
    public void atualizarMercadoComNomeInvalido() throws Exception {

        this.mercado.setNome("Mercado teste Mercado teste Mercado teste Mercado teste Mercado teste");

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "nome_invalido");
        }

    }

    @Test
    public void atualizarMercadoComLogradouroInvalido() throws Exception {

        this.mercado.setLogradouro("Rua");

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "logradouro_invalido");
        }

    }

    @Test
    public void atualizarMercadoComNumeroInvalido() throws Exception {

        this.mercado.setNumero(-1);

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "numero_invalido");
        }

    }

    @Test
    public void atualizarMercadoComBairroInvalido() throws Exception {

        this.mercado.setBairro("Test");

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "bairro_invalido");
        }

    }

    @Test
    public void atualizarMercadoComCidadeInvalida() throws Exception {

        this.mercado.setCidade("Sã");

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cidade_invalido");
        }

    }

    @Test
    public void atualizarMercadoComUfInvalida() throws Exception {

        this.mercado.setUf("S");

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "uf_invalido");
        }

    }

    @Test
    public void atualizarMercadoComCepInvalido() throws Exception {

        this.mercado.setCep("123456789");

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "cep_invalido");
        }

    }

    @Test
    public void atualizarMercadoComUsuarioInvalido() throws Exception {

        this.mercado.setCriadoPor(0);

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "usuario_invalido");
        }

    }

    @Test
    public void atualizarMercadoComRamoInvalido() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(0);

        try {

            mercadoController.atualizar(1, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(e.getReason(), "ramo_invalido");
        }
        finally {
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void atualizarMercadoComExcecaoNoSuchElement() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        try {

            mercadoController.atualizar(1986, this.mercado);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getReason(), "nao_encontrado");
        }
        finally {
            ramoRepository.deleteById(novoRamo.getId());
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void atualizarMercadoComUsuarioInexistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado mercadoCriado = mercadoRepository.save(this.mercado);

        Mercado mercadoAtualizado = new Mercado();

        mercadoAtualizado.setNome("Mercado Atualizado");
        mercadoAtualizado.setLogradouro("Rua Atualizada");
        mercadoAtualizado.setNumero(12);
        mercadoAtualizado.setBairro("Bairro Atualizado");
        mercadoAtualizado.setCidade("Cidade Atualizada");
        mercadoAtualizado.setUf("RJ");
        mercadoAtualizado.setCep("98765-432");
        mercadoAtualizado.setComplemento("Comp. Atualizado");
        mercadoAtualizado.setCriadoPor(1986);
        mercadoAtualizado.setRamoId(novoRamo.getId());

        try {

            mercadoController.atualizar(mercadoCriado.getId(), mercadoAtualizado);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getReason(), "usuario_nao_encontrado");
        }
        finally {
            mercadoRepository.deleteById(mercadoCriado.getId());
            ramoRepository.deleteById(novoRamo.getId());
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void atualizarMercadoComRamoInexistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado mercadoCriado = mercadoRepository.save(this.mercado);

        Mercado mercadoAtualizado = new Mercado();

        mercadoAtualizado.setNome("Mercado Atualizado");
        mercadoAtualizado.setLogradouro("Rua Atualizada");
        mercadoAtualizado.setNumero(12);
        mercadoAtualizado.setBairro("Bairro Atualizado");
        mercadoAtualizado.setCidade("Cidade Atualizada");
        mercadoAtualizado.setUf("RJ");
        mercadoAtualizado.setCep("98765-432");
        mercadoAtualizado.setComplemento("Comp. Atualizado");
        mercadoAtualizado.setCriadoPor(novoUsuario.getId());
        mercadoAtualizado.setRamoId(1986);

        try {

            mercadoController.atualizar(mercadoCriado.getId(), mercadoAtualizado);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getReason(), "ramo_nao_encontrado");
        }
        finally {
            mercadoRepository.deleteById(mercadoCriado.getId());
            usuarioRepository.deleteById(novoUsuario.getId());
            ramoRepository.deleteById(novoRamo.getId());
        }

    }

    @Test
    public void atualizarMercadoComNomeExistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado mercadoCriado = mercadoRepository.save(this.mercado);

        Mercado mercadoAtualizado = new Mercado();

        mercadoAtualizado.setNome("Mercado Teste");
        mercadoAtualizado.setLogradouro("Rua Atualizada");
        mercadoAtualizado.setNumero(12);
        mercadoAtualizado.setBairro("Bairro Atualizado");
        mercadoAtualizado.setCidade("Cidade Atualizada");
        mercadoAtualizado.setUf("RJ");
        mercadoAtualizado.setCep("98765-432");
        mercadoAtualizado.setComplemento("Comp. Atualizado");
        mercadoAtualizado.setCriadoPor(novoUsuario.getId());
        mercadoAtualizado.setRamoId(novoRamo.getId());

        try {

            mercadoController.atualizar(mercadoCriado.getId(), mercadoAtualizado);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getReason(), "mercado_existente");
        }
        finally {
            mercadoRepository.deleteById(mercadoCriado.getId());
            ramoRepository.deleteById(novoRamo.getId());
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    @Test
    public void atualizarMercadoComEnderecoExistente() throws Exception {

        Usuario novoUsuario = usuarioRepository.save(this.usuario);

        Ramo novoRamo = ramoRepository.save(this.ramo);

        this.mercado.setCriadoPor(novoUsuario.getId());
        this.mercado.setRamoId(novoRamo.getId());

        Mercado mercadoCriado = mercadoRepository.save(this.mercado);

        Mercado mercadoAtualizado = new Mercado();

        mercadoAtualizado.setNome("Mercado Atualizado");
        mercadoAtualizado.setLogradouro("Rua Teste");
        mercadoAtualizado.setNumero(12);
        mercadoAtualizado.setBairro("Bairro Teste");
        mercadoAtualizado.setCidade("Cidade Teste");
        mercadoAtualizado.setUf("SP");
        mercadoAtualizado.setCep("12345-678");
        mercadoAtualizado.setComplemento("Comp. Atualizado");
        mercadoAtualizado.setCriadoPor(novoUsuario.getId());
        mercadoAtualizado.setRamoId(novoRamo.getId());

        try {

            mercadoController.atualizar(mercadoCriado.getId(), mercadoAtualizado);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getReason(), "mercado_existente");
        }
        finally {
            mercadoRepository.deleteById(mercadoCriado.getId());
            ramoRepository.deleteById(novoRamo.getId());
            usuarioRepository.deleteById(novoUsuario.getId());
        }

    }

    /* -------------------------------------------------------------------------- */

}