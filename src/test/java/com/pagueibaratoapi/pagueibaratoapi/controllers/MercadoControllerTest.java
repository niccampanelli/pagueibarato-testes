package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertEquals;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.MercadoController;
import com.pagueibaratoapi.models.exceptions.DadosInvalidosException;
import com.pagueibaratoapi.models.requests.Estoque;
import com.pagueibaratoapi.models.requests.Mercado;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Sugestao;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseEstoque;
import com.pagueibaratoapi.models.responses.ResponseEstoqueProduto;
import com.pagueibaratoapi.models.responses.ResponseMercado;
import com.pagueibaratoapi.models.responses.ResponseProduto;
import com.pagueibaratoapi.models.responses.ResponseSugestao;
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

    private Mercado mercado;

    private Usuario usuario;

    private Usuario usuarioInexistente;

    private Estoque estoque;

    private Estoque estoqueInexistente;

    private Produto produto;

    private Sugestao sugestao;

    private List<Estoque> estoques;

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
        
        estoqueInexistente = new Estoque();

        estoque.setProdutoId(1);
        estoque.setCriadoPor(null);
        estoque.setCriadoPor(1);

        estoques = List.of(estoque);
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





    /* --------------------  CRIAÇÃO DE ESTOQUE DO MERCADO  --------------------- */

    @Test
    public void criarEstoqueComSucesso() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findAll(
            Example.of(
                estoque,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            )
        )).thenReturn(new ArrayList<Estoque>());

        when(estoqueRepository.save(any())).thenReturn(estoque);

        ResponseEstoque responseEstoque = mercadoController.criarEstoque(1, 1, estoque);

        assertEquals(responseEstoque.getCriadoPor(), estoque.getCriadoPor());
        assertEquals(responseEstoque.getProdutoId(), estoque.getProdutoId());
        assertEquals(responseEstoque.getMercadoId(), estoque.getMercadoId());
    }

    @Test
    public void criarEstoqueComUsuarioNulo() {

        try {

            mercadoController.criarEstoque(1, 1, estoqueInexistente);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }
    }

    @Test
    public void criarEstoqueComUsuarioInvalido() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {
            
            mercadoController.criarEstoque(1, 1, estoque);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("usuario_invalido", e.getReason());
        }
        
    }

    @Test
    public void criarEstoqueComUsuarioInexistente() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuarioInexistente);

        try {
            
            mercadoController.criarEstoque(1, 1, estoque);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }
        
    }

    @Test
    public void criarEstoqueComProdutoInvalido() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(false);

        try {
                
            mercadoController.criarEstoque(1, 1, estoque);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("produto_invalido", e.getReason());
        }

    }

    @Test
    public void criarEstoqueComMercadoInvalido() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);
        when(mercadoRepository.existsById(anyInt())).thenReturn(false);

        try {
                    
            mercadoController.criarEstoque(1, 1, estoque);

        }
        catch (ResponseStatusException e) {

            assertEquals(400, e.getRawStatusCode());
            assertEquals("mercado_invalido", e.getReason());
            
        }
    }

    @Test
    public void criarEstoqueComEstoqueExistente() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoques);

        try {

            mercadoController.criarEstoque(1, 1, estoque);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("estoque_existente", e.getReason());
        }

    }

    @Test
    public void criarEstoqueComExcecao() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findAll(
            Example.of(
                estoque,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            )
        )).thenReturn(new ArrayList<Estoque>());

        when(estoqueRepository.save(any())).thenThrow(new NullPointerException("erro_inesperado"));

        try {

            mercadoController.criarEstoque(1, 1, estoque);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */
    




    /* ---------------------  CRIAÇÃO DE PRODUTO NO MERCADO --------------------- */

    @Test
    public void criarProdutoComSucesso() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        Produto responseProduto = new Produto();

        responseProduto.setId(1);
        responseProduto.setNome("Teste produto");
        responseProduto.setMarca("Teste marca");
        responseProduto.setTamanho("Teste tamanho");
        responseProduto.setCor("Teste cor");
        responseProduto.setCriadoPor(1);
        responseProduto.setCategoriaId(1);

        estoque = new Estoque();

        estoque.setId(1);
        estoque.setProdutoId(1);
        estoque.setCriadoPor(1);
        estoque.setMercadoId(1);


        when(produtoRepository.save(any())).thenReturn(responseProduto);

        when(estoqueRepository.save(any())).thenReturn(estoque);

        ResponseEstoqueProduto responseEstoque = mercadoController.criarProduto(1, produto);

        assertEquals("Teste produto", responseEstoque.getNome());
        assertEquals(produto.getMarca(), responseEstoque.getMarca());
        assertEquals(produto.getTamanho(), responseEstoque.getTamanho());
        assertEquals(produto.getCor(), responseEstoque.getCor());
    }

    @Test
    public void criarProdutoComUsuarioNaoEncontrado() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criarProduto(1, produto);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }
    }

    @Test
    public void criarProdutoComCategoriaNaoEncontrada() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criarProduto(1, produto);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("categoria_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void criarProdutoComCaracteristicasExistentes() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(anyString(), anyString(), anyString(), anyString())).thenReturn(produto);

        try {

            mercadoController.criarProduto(1, produto);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("produto_existente", e.getReason());
        }

    }

    @Test
    public void criarProdutoComUsuarioInvalido() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuarioInexistente);

        try {

            mercadoController.criarProduto(1, produto);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void criarProdutoComMercadoInvalido() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criarProduto(1, produto);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("mercado_invalido", e.getReason());
        }

    }

    @Test
    public void criarProdutoComExcecao() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(null);

        try {

            mercadoController.criarProduto(1, produto);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* ---------------  CRIAÇÃO DE SUGESTÃO DE PRODUTO DO MERCADO --------------- */

    @Test
    public void criarSugestaoComSucesso() throws Exception {

        this.estoque.setId(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findByProdutoIdAndMercadoId(anyInt(), anyInt())).thenReturn(estoque);

        when(sugestaoRepository.save(sugestao)).thenReturn(sugestao);

        ResponseSugestao responseSugestao = mercadoController.criarSugestao(1, 1, sugestao);

        float precoSugestao = sugestao.getPreco() / 100;

        assertTrue(precoSugestao == responseSugestao.getPreco());
        assertEquals(estoque.getId(), responseSugestao.getEstoqueId());

    }

    @Test
    public void criarSugestaoComUsuarioInvalido() throws Exception {

        sugestao.setCriadoPor(null);

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("usuario_invalido", e.getReason());
        }

    }

    @Test
    public void criarSugestaoComPrecoInvalido() throws Exception {

        sugestao.setPreco(null);

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("preco_invalido", e.getReason());
        }

    }

    @Test
    public void criarSugestaoComUsuarioNaoEncontrado() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void criarSugestaoComProdutoInvalido() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("produto_invalido", e.getReason());
        }

    }

    @Test
    public void criarSugestaoComMercadoInexistente() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("mercado_invalido", e.getReason());
        }

    }

    @Test
    public void criarSugestaoComUsuarioInexistente() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuarioInexistente);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void criarSugestaoComEstoqueInexistente() throws Exception {

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findByProdutoIdAndMercadoId(anyInt(), anyInt())).thenReturn(null);

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("estoque_nao_encontrado", e.getReason());
        }
    }

    @Test
    public void criarSugestaoComExcecao() throws Exception {

        this.estoque.setId(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findByProdutoIdAndMercadoId(anyInt(), anyInt())).thenReturn(estoque);

        when(sugestaoRepository.save(sugestao)).thenThrow(new NullPointerException("erro_inesperado"));

        try {

            mercadoController.criarSugestao(1, 1, sugestao);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
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





    /* --------------------  LISTAGEM DE PRODUTOS DO MERCADO -------------------- */

    @Test
    public void listarProdutosComSucesso() {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findByMercadoId(anyInt())).thenReturn(estoques);

        when(produtoRepository.findById(anyInt())).thenReturn(Optional.of(produto));

        List<ResponseProduto> responseProdutos = mercadoController.listarProdutos(1);

        assertEquals(1, responseProdutos.size());
        assertEquals(produto.getNome(), responseProdutos.get(0).getNome());
    }

    @Test
    public void listarProdutosComMercadoNaoEncontrado() {

        when(mercadoRepository.existsById(anyInt())).thenReturn(false);

        try {

            mercadoController.listarProdutos(1);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("mercado_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void listarProdutosComEstoqueNaoEncontrado() {

        when(mercadoRepository.existsById(anyInt())).thenReturn(true);

        when(estoqueRepository.findByMercadoId(anyInt())).thenReturn(new ArrayList<>());

        try {

            mercadoController.listarProdutos(1);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("estoque_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void listarProdutosComExcecao() {

        when(mercadoRepository.existsById(anyInt())).thenThrow(new RuntimeException());

        try {

            mercadoController.listarProdutos(1);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }
        
    }

    /* -------------------------------------------------------------------------- */

}