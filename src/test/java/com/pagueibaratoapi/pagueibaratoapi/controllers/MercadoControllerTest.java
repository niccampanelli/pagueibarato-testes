package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.MercadoController;
import com.pagueibaratoapi.models.requests.Estoque;
import com.pagueibaratoapi.models.requests.Mercado;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Sugestao;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseMercado;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.RamoRepository;
import com.pagueibaratoapi.repository.SugestaoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;

public class MercadoControllerTest {

    @InjectMocks
    private MercadoController mercadoController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RamoRepository ramoRepository;

    @Mock
    private MercadoRepository mercadoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private SugestaoRepository sugestaoRepository;

    @Mock
    private Optional<Usuario> optionalUsuario;

    @Mock
    private Optional<Mercado> optionalMercado;
    
    @Mock
    private Optional<Produto> optionalProduto;

    private Mercado mercado;

    private Usuario usuario;

    private Usuario usuarioInexistente;

    private Estoque estoque;

    private Produto produto;

    private Sugestao sugestao;

    private List<Mercado> mercados;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.inicializarMercado();
        this.inicializarUsuario();
        this.inicializarEstoque();
        this.inicializarProduto();
        this.inicializarSugestao();
    }

    private void inicializarMercado() {
        mercado = new Mercado();
        
        mercado.setCriadoPor(1);
        mercado.setNome("Mercado Teste");
        mercado.setLogradouro("Rua Teste");
        mercado.setNumero(123);
        mercado.setBairro("Bairro Teste");
        mercado.setCidade("Cidade Teste");
        mercado.setUf("SP");
        mercado.setCep("12345-678");
        mercado.setRamoId(1);
        mercado.setComplemento("Complemento Teste");

        Mercado mercado2 = new Mercado();
        mercado2.setCriadoPor(1);
        mercado2.setNome("Mercado Teste 2");
        mercado2.setLogradouro("Rua Teste 2");
        mercado2.setNumero(123);
        mercado2.setBairro("Bairro Teste 2");
        mercado2.setCidade("Cidade Teste 2");
        mercado2.setUf("SP");
        mercado2.setCep("12345-678");
        mercado2.setRamoId(1);
        mercado2.setComplemento("Complemento Teste 2");

        Mercado mercado3 = new Mercado();
        mercado3.setCriadoPor(1);
        mercado3.setNome("Mercado Teste 3");
        mercado3.setLogradouro("Rua Teste 3");
        mercado3.setNumero(123);
        mercado3.setBairro("Bairro Teste 2");
        mercado3.setCidade("Cidade Teste 2");
        mercado3.setUf("SP");
        mercado3.setCep("12345-678");
        mercado3.setRamoId(1);
        mercado3.setComplemento("Complemento Teste 3");

        this.mercados = List.of(mercado, mercado2, mercado3);
    }

    private void inicializarUsuario() {
        usuario = new Usuario();

        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario@email.com");
        usuario.setBairro("Bairro Teste");
        usuario.setCidade("Cidade Teste");
        usuario.setUf("SP");
        usuario.setCep("12345-678");
        usuario.setSenha("123456");
        usuario.setComplemento("Complemento Teste");
        usuario.setLogradouro("Logradouro Teste");
        usuario.setNumero(12);

        usuarioInexistente = new Usuario();

        usuarioInexistente.setId(1);
        usuarioInexistente.setNome("Usuario Teste");
        usuarioInexistente.setEmail("");
        usuarioInexistente.setBairro("Bairro Teste");
        usuarioInexistente.setCidade("Cidade Teste");
        usuarioInexistente.setUf("SP");
        usuarioInexistente.setCep("12345-678");
        usuarioInexistente.setSenha("123456");
        usuarioInexistente.setComplemento("Complemento Teste");
        usuarioInexistente.setLogradouro("Logradouro Teste");
        usuarioInexistente.setNumero(12);
    }

    private void inicializarEstoque() {
        estoque = new Estoque();

        estoque.setProdutoId(1);
        estoque.setCriadoPor(1);
        estoque.setMercadoId(1);

        estoque.setProdutoId(1);
        estoque.setCriadoPor(null);
        estoque.setCriadoPor(1);
    }

    private void inicializarProduto() {
        produto = new Produto();

        produto.setNome("Teste produto");
        produto.setMarca("Teste marca");
        produto.setTamanho("Teste tamanho");
        produto.setCor("Teste cor");
        produto.setCriadoPor(1);
        produto.setCategoriaId(1);
    }

    private void inicializarSugestao() {
        sugestao = new Sugestao();

        sugestao.setPreco(10.5f);
        sugestao.setCriadoPor(1);
        sugestao.setEstoqueId(1);
    }



    /* --------------------------  CRIAÇÃO DE MERCADO  -------------------------- */

    @Test
    public void criarMercadoComSucesso() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.save(any())).thenReturn(mercado);

        ResponseMercado responseMercado = mercadoController.criar(mercado);

        assertEquals(responseMercado.getNome(), mercado.getNome());
        assertEquals(responseMercado.getLogradouro(), mercado.getLogradouro());
        assertEquals(responseMercado.getNumero(), mercado.getNumero());
        assertEquals(responseMercado.getBairro(), mercado.getBairro());
        assertEquals(responseMercado.getCidade(), mercado.getCidade());
        assertEquals(responseMercado.getUf(), mercado.getUf());
        assertEquals(responseMercado.getCep(), mercado.getCep());
        assertEquals(responseMercado.getRamoId(), mercado.getRamoId());
    }

    @Test
    public void criarMercadoComUsuarioInvalido() {

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criar(mercado);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }
    }

    @Test
    public void criarMercadoComRamoInvalido() {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(ramoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criar(mercado);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "ramo_nao_encontrado");
        }
    }

    @Test
    public void criarMercadoComNomeExistente() {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        try {

            mercadoController.criar(mercado);

        } catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "mercado_existente");
        }
    }

    @Test
    public void criarMercadoComEnderecoExistente() {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(),
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(mercado);

        try {

            mercadoController.criar(mercado);

        } catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "mercado_existente");
        }
    }

    @Test
    public void criarMercadoComExcecaoNoSuchElement() {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuarioInexistente);

        try {

            mercadoController.criar(mercado);

        } catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "usuario_nao_encontrado");
        }
    }

    @Test
    public void criarMercadoComExcecaoDataViolation() {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.save(any())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {

            mercadoController.criar(mercado);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_insercao", e.getReason());
        }
    }

    @Test
    public void criarMercadoComExcecaoIllegalArgument() {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.save(any())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            mercadoController.criar(mercado);

        } catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    /* -------------------------------------------------------------------------- */





    /* -----------------------  LEITURA DE MERCADO POR ID ----------------------- */

    @Test
    public void lerMercadoPorIdComSucesso() {

        mercado.setId(1);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        ResponseMercado responseMercado = mercadoController.ler(1);

        assertTrue(1 == responseMercado.getId());

    }

    @Test
    public void lerMercadoPorIdComExcecaoNoSuchElement() {

        when(mercadoRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {

            mercadoController.ler(1);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    @Test
    public void lerMercadoPorIdComExcecao() {

        when(mercadoRepository.findById(anyInt())).thenThrow(new RuntimeException());

        try {

            mercadoController.ler(1);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -------------------------  LISTAGEM DE MERCADOS  ------------------------- */

    @Test
    public void listarComSucesso() throws Exception {

        when(mercadoRepository.findAll(isA(Example.class))).thenReturn(mercados);

        List<ResponseMercado> response = mercadoController.listar(mercado);

        assertEquals(3, response.size());
        assertEquals(mercado.getId(), response.get(0).getId());
        assertEquals(mercado.getNome(), response.get(0).getNome());
        assertEquals(mercado.getLogradouro(), response.get(0).getLogradouro());
        assertEquals(mercado.getNumero(), response.get(0).getNumero());
        assertEquals(mercado.getBairro(), response.get(0).getBairro());
        assertEquals(mercado.getCidade(), response.get(0).getCidade());
        assertEquals(mercado.getUf(), response.get(0).getUf());
        assertEquals(mercado.getCep(), response.get(0).getCep());
        assertEquals(mercado.getComplemento(), response.get(0).getComplemento());
    }

    @Test
    public void listarComFiltroInvalido() throws Exception {

        try {

            mercadoController.listar(null);

        } 
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("corpo_nulo", e.getReason());
        }

    }

    @Test
    public void listarComExcecaoNullPointer() throws Exception {

        when(mercadoRepository.findAll(isA(Example.class))).thenThrow(new NullPointerException());

        try {

            mercadoController.listar(mercado);

        } 
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    @Test
    public void listarComExcecaoUnsupportedOperation() throws Exception {

        when(mercadoRepository.findAll(isA(Example.class))).thenReturn(new ArrayList<>());

        try {
                
            mercadoController.listar(mercado);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }

    }

    @Test
    public void listarComExcecao() throws Exception {

        when(mercadoRepository.findAll(isA(Example.class))).thenThrow(new RuntimeException());

        try {

            mercadoController.listar(mercado);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }
    }

    /* -------------------------------------------------------------------------- */





    /* --------------------------  EDIÇÃO DE MERCADOS  -------------------------- */

    @Test
    public void editarComSucesso() {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.save(any())).thenReturn(mercados.get(1));

        ResponseMercado responseMercado = mercadoController.editar(1, mercados.get(1));

        assertEquals("Mercado Teste 2", responseMercado.getNome());
    }

    @Test
    public void editarComEnderecoExistente() {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(mercado);

        try {

            mercadoController.editar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "mercado_existente");
        }
    }

    @Test
    public void editarComRamoInexistente() throws Exception {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(ramoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.editar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "ramo_nao_encontrado");
        }

    }

    @Test
    public void editarComMercadoJaExistente() throws Exception {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        try {

            mercadoController.editar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals(e.getCause().getMessage(), "mercado_existente");
        }

    }

    @Test
    public void editarMercadoComExcecaoDataIntegrity() throws Exception {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.save(any())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {

            mercadoController.editar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_insercao", e.getReason());
        }

    }

    @Test
    public void editarMercadoComExcecaoIllegalArgument() throws Exception {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.save(any())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            mercadoController.editar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void editarMercadoComExcecaoNoSuchElement() throws Exception {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.save(any())).thenThrow(new NoSuchElementException("nao_encontrado"));

        try {

            mercadoController.editar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.util.NoSuchElementException"));
        }

    }

    @Test
    public void editarMercadoComExcecao() throws Exception {

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);

        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.save(any())).thenThrow(new RuntimeException());

        try {

            mercadoController.editar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -----------------------  ATUALIZAÇÃO DE MERCADOS  ------------------------ */

    @Test
    public void atualizarComSucesso() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);
        
        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(mercadoRepository.save(any())).thenReturn(mercados.get(1));

        ResponseMercado responseMercado = mercadoController.atualizar(1, mercados.get(1));

        assertEquals("Mercado Teste 2", responseMercado.getNome());

    }

    @Test
    public void atualizarComMercadoInexistente() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.atualizar(2023, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }
        
    }

    @Test
    public void atualizarComUsuarioInexistente() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.atualizar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void atualizarComRamoInexistente() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(ramoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.atualizar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("ramo_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void atualizarComMercadoExistente() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        try {

            mercadoController.atualizar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("mercado_existente", e.getReason());
        }

    }

    @Test
    public void atualizarComEnderecoExistente() throws Exception {
        
        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(mercado);

        try {

            mercadoController.atualizar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("mercado_existente", e.getReason());
        }

    }

    @Test
    public void atualizarComExcecaoDataViolaton() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);
        
        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(mercadoRepository.save(any())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {

            mercadoController.atualizar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_insercao", e.getReason());
        }

    }

    @Test
    public void atualizarComExcecaoIllegalArgument() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);
        
        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(mercadoRepository.save(any())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {

            mercadoController.atualizar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void atualizarComExcecao() throws Exception {
        
        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(ramoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

        when(mercadoRepository.findByEndereco(anyString(), anyInt(), anyString(), 
                                              anyString(), anyString(), anyString(), 
                                              anyString())
        ).thenReturn(null);
        
        when(mercadoRepository.findById(anyInt())).thenReturn(optionalMercado);
        when(optionalMercado.get()).thenReturn(mercado);

        when(mercadoRepository.save(any())).thenThrow(new RuntimeException("erro_inesperado"));

        try {

            mercadoController.atualizar(1, mercados.get(1));

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */





    /* --------------------------  DELEÇÃO DE MERCADOS  ------------------------- */

    @Test
    public void removerComSucesso() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        mercadoController.remover(1);

        verify(mercadoRepository).deleteById(anyInt());
    }

    @Test
    public void removerComExcecaoNoSuchElement() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.remover(1);

        } 
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    @Test
    public void removerComExcecaoDataViolation() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);
        doThrow(new DataIntegrityViolationException("erro_remocao")).when(mercadoRepository).deleteById(anyInt());

        try {

            mercadoController.remover(1);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_remocao", e.getReason());
        }

    }

    @Test
    public void removerComExcecaoIllegalArgument() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);
        doThrow(new IllegalArgumentException("erro_inesperado")).when(mercadoRepository).deleteById(anyInt());

        try {

            mercadoController.remover(1);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void removerComExcecao() throws Exception {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);
        doThrow(new RuntimeException()).when(mercadoRepository).deleteById(anyInt());

        try {

            mercadoController.remover(1);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */

}