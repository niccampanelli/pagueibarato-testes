package com.pagueibaratoapi.pagueibaratoapi.unit;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.EstoqueController;
import com.pagueibaratoapi.models.requests.Estoque;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseEstoque;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;



public class EstoqueControllerTest {

    @InjectMocks
    private EstoqueController estoqueController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MercadoRepository mercadoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private Optional<Usuario> optionalUsuario;

    @Mock
    private Optional<Estoque> optionalEstoque;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void criarEstoqueComSucesso() throws Exception {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(1);
        requestEstoque.setMercadoId(1);
        requestEstoque.setProdutoId(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("john@email.com");

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(true);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(requestEstoque.getMercadoId())).thenReturn(true);

        when(produtoRepository.existsById(requestEstoque.getProdutoId())).thenReturn(true);

        when(estoqueRepository.findAll(
                Example.of(
                        requestEstoque,
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING))))
                .thenReturn(new ArrayList<Estoque>());

        Estoque estoqueSalvo = new Estoque();
        estoqueSalvo.setId(1);
        estoqueSalvo.setCriadoPor(1);
        estoqueSalvo.setMercadoId(1);
        estoqueSalvo.setProdutoId(1);

        when(estoqueRepository.save(requestEstoque)).thenReturn(estoqueSalvo);

        ResponseEstoque responseEstoque = estoqueController.criar(requestEstoque);

        System.out.println("Id: " + responseEstoque.getId());
        System.out.println("Id do produto: " + responseEstoque.getProdutoId());
        System.out.println("Id do mercado: " + responseEstoque.getMercadoId());
        System.out.println("Id do usuário: " + responseEstoque.getCriadoPor());

        assertTrue(responseEstoque.getId() != null);
    }

    @Test
    public void criarEstoqueComUsuarioInvalido() {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(null);
        requestEstoque.setMercadoId(1);
        requestEstoque.setProdutoId(1);

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(false);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(null);

        when(mercadoRepository.existsById(requestEstoque.getMercadoId())).thenReturn(true);

        when(produtoRepository.existsById(requestEstoque.getProdutoId())).thenReturn(true);

        when(estoqueRepository.findAll(
                Example.of(
                        requestEstoque,
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING))))
                .thenReturn(new ArrayList<Estoque>());

        try {
            estoqueController.criar(requestEstoque);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void criarEstoqueComProdutoInvalido() {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(1);
        requestEstoque.setMercadoId(1);
        requestEstoque.setProdutoId(null);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("john@email.com");

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(true);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(requestEstoque.getMercadoId())).thenReturn(true);

        when(produtoRepository.existsById(requestEstoque.getProdutoId())).thenReturn(false);

        when(estoqueRepository.findAll(
                Example.of(
                        requestEstoque,
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING))))
                .thenReturn(new ArrayList<Estoque>());

        try {
            estoqueController.criar(requestEstoque);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("produto_invalido"));
        }
    }

    @Test
    public void criarEstoqueComMercadoInvalido() {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(1);
        requestEstoque.setMercadoId(null);
        requestEstoque.setProdutoId(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("john@email.com");

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(true);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(requestEstoque.getMercadoId())).thenReturn(false);

        when(produtoRepository.existsById(requestEstoque.getProdutoId())).thenReturn(true);

        when(estoqueRepository.findAll(
                Example.of(
                        requestEstoque,
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING))))
                .thenReturn(new ArrayList<Estoque>());

        try {
            estoqueController.criar(requestEstoque);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("mercado_invalido"));
        }
    }

    @Test
    public void criarEstoqueJaExistente() {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(1);
        requestEstoque.setMercadoId(1);
        requestEstoque.setProdutoId(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("john@email.com");

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(true);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(requestEstoque.getMercadoId())).thenReturn(true);

        when(produtoRepository.existsById(requestEstoque.getProdutoId())).thenReturn(true);

        Estoque estoqueEncontrado = new Estoque();
        estoqueEncontrado.setId(1);
        estoqueEncontrado.setCriadoPor(1);
        estoqueEncontrado.setMercadoId(1);
        estoqueEncontrado.setProdutoId(1);

        List<Estoque> estoquesSemelhantes = List.of(estoqueEncontrado);

        Estoque estoqueComparar = new Estoque();
        estoqueComparar.setProdutoId(requestEstoque.getProdutoId());
        estoqueComparar.setMercadoId(requestEstoque.getMercadoId());

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoquesSemelhantes);

        try {
            estoqueController.criar(requestEstoque);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("estoque_existente"));
        }
    }

    @Test
    public void criarEstoqueComExcecaoDadosInvalidos() {
        try {
            estoqueController.criar(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void criarEstoqueComExcecaoDataIntegrityViolation() {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(1);
        requestEstoque.setMercadoId(1);
        requestEstoque.setProdutoId(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("john@email.com");

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(true);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(requestEstoque.getMercadoId())).thenReturn(true);

        when(produtoRepository.existsById(requestEstoque.getProdutoId())).thenReturn(true);

        when(estoqueRepository.findAll(
                Example.of(
                        requestEstoque,
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING))))
                .thenReturn(new ArrayList<Estoque>());

        Estoque estoqueSalvo = new Estoque();
        estoqueSalvo.setId(1);
        estoqueSalvo.setCriadoPor(1);
        estoqueSalvo.setMercadoId(1);
        estoqueSalvo.setProdutoId(1);

        when(estoqueRepository.save(requestEstoque)).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            estoqueController.criar(requestEstoque);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("erro_insercao"));
        }
    }

    @Test
    public void criarEstoqueComExcecaoIllegalArgument() {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(1);
        requestEstoque.setMercadoId(1);
        requestEstoque.setProdutoId(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("john@email.com");

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(true);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(mercadoRepository.existsById(requestEstoque.getMercadoId())).thenReturn(true);

        when(produtoRepository.existsById(requestEstoque.getProdutoId())).thenReturn(true);

        when(estoqueRepository.findAll(
                Example.of(
                        requestEstoque,
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING))))
                .thenReturn(new ArrayList<Estoque>());

        Estoque estoqueSalvo = new Estoque();
        estoqueSalvo.setId(1);
        estoqueSalvo.setCriadoPor(1);
        estoqueSalvo.setMercadoId(1);
        estoqueSalvo.setProdutoId(1);

        when(estoqueRepository.save(requestEstoque)).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            estoqueController.criar(requestEstoque);
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void criarEstoqueComExcecao() {

        Estoque requestEstoque = new Estoque();
        requestEstoque.setCriadoPor(1);
        requestEstoque.setMercadoId(1);
        requestEstoque.setProdutoId(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("john@email.com");

        when(usuarioRepository.existsById(requestEstoque.getCriadoPor())).thenReturn(true);
        when(usuarioRepository.findById(requestEstoque.getCriadoPor())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(null);

        try {
            estoqueController.criar(requestEstoque);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }
    }

    @Test
    public void lerEstoquePorIdComSucesso() {

        Estoque estoque = new Estoque();
        estoque.setId(1);
        estoque.setCriadoPor(1);
        estoque.setMercadoId(1);
        estoque.setProdutoId(1);

        when(estoqueRepository.findById(anyInt())).thenReturn(optionalEstoque);
        when(optionalEstoque.get()).thenReturn(estoque);

        ResponseEstoque responseEstoque = estoqueController.ler(anyInt());

        System.out.println("Id: " + responseEstoque.getId());
        System.out.println("Id do produto: " + responseEstoque.getProdutoId());
        System.out.println("Id do mercado: " + responseEstoque.getMercadoId());
        System.out.println("Id do usuário: " + responseEstoque.getCriadoPor());

        assertTrue(responseEstoque.getId() != null);
    }

    @Test
    public void lerEstoquePorIdComExcecaoNoSuchElement() {

        when(estoqueRepository.findById(anyInt())).thenReturn(optionalEstoque);
        when(optionalEstoque.get()).thenThrow(new NoSuchElementException("nao_encontrado"));

        try {
            estoqueController.ler(anyInt());
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void lerEstoquePorIdComExcecao() {

        when(estoqueRepository.findById(null)).thenReturn(optionalEstoque);
        when(optionalEstoque.get()).thenReturn(null);

        try {
            estoqueController.ler(null);
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }
    }

    @Test
    public void listarTodosEstoquesComSucesso() {

        Estoque estoque1 = new Estoque();
        estoque1.setId(1);
        estoque1.setCriadoPor(1);
        estoque1.setMercadoId(1);
        estoque1.setProdutoId(1);

        Estoque estoque2 = new Estoque();
        estoque2.setId(2);
        estoque2.setCriadoPor(1);
        estoque2.setMercadoId(1);
        estoque2.setProdutoId(1);

        List<Estoque> estoques = List.of(estoque1, estoque2);

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoques);

        List<ResponseEstoque> responseEstoques = estoqueController.listar(new Estoque());

        System.out.println("Quantidade de estoques: " + responseEstoques.size());

        for (ResponseEstoque responseEstoque : responseEstoques) {
            System.out.println("Id: " + responseEstoque.getId());
            System.out.println("Id do produto: " + responseEstoque.getProdutoId());
            System.out.println("Id do mercado: " + responseEstoque.getMercadoId());
            System.out.println("Id do usuário: " + responseEstoque.getCriadoPor());
        }

        assertTrue(responseEstoques.size() == 2);
    }

    @Test
    public void listarEstoquesSemelhantesComSucesso() {

        Estoque estoqueExemplo = new Estoque();
        estoqueExemplo.setCriadoPor(1);
        estoqueExemplo.setMercadoId(1);
        estoqueExemplo.setProdutoId(1);

        Estoque estoque1 = new Estoque();
        estoque1.setId(1);
        estoque1.setCriadoPor(1);
        estoque1.setMercadoId(1);
        estoque1.setProdutoId(1);

        List<Estoque> estoques = List.of(estoque1);

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoques);

        List<ResponseEstoque> responseEstoques = estoqueController.listar(estoqueExemplo);

        System.out.println("Quantidade de estoques: " + responseEstoques.size());

        for (ResponseEstoque responseEstoque : responseEstoques) {
            System.out.println("Id: " + responseEstoque.getId());
            System.out.println("Id do produto: " + responseEstoque.getProdutoId());
            System.out.println("Id do mercado: " + responseEstoque.getMercadoId());
            System.out.println("Id do usuário: " + responseEstoque.getCriadoPor());
        }

        assertTrue(responseEstoques.size() == 1);
    }

    @Test
    public void listarTodosEstoquesComExcecaoDadosInvalidos() {

        try {
            estoqueController.listar(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void listarTodosEstoquesComExcecaoNullPointer() {

        List<Estoque> estoques = List.of();

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoques);

        try {
            List<ResponseEstoque> responseEstoques = estoqueController.listar(new Estoque());

            System.out.println("Quantidade de estoques: " + responseEstoques.size());
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void listarTodosEstoquesComExcecaoUnsupportedOperation() {
        
        when(estoqueRepository.findAll(isA(Example.class))).thenThrow(new UnsupportedOperationException("erro_inesperado"));
        
        try {
            estoqueController.listar(new Estoque());
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }
    }

    @Test
    public void listarTodosEstoquesComExcecao() {

        Estoque estoque = new Estoque();
        estoque.setId(1);
        estoque.setCriadoPor(null);
        estoque.setMercadoId(null);
        estoque.setProdutoId(null);

        List<Estoque> estoques = List.of(estoque);

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoques);

        try {
            estoqueController.listar(new Estoque());
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }
    }

    @Test
    public void listarEstoquesSemelhantesComExcecaoDadosInvalidos() {

        Estoque estoqueExemplo = new Estoque();
        estoqueExemplo.setCriadoPor(null);
        estoqueExemplo.setMercadoId(null);
        estoqueExemplo.setProdutoId(null);

        try {
            estoqueController.listar(estoqueExemplo);
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void listarEstoquesSemelhantesComExcecaoNullPointer() {

        Estoque estoqueExemplo = new Estoque();
        estoqueExemplo.setCriadoPor(1);
        estoqueExemplo.setMercadoId(1);
        estoqueExemplo.setProdutoId(1);

        List<Estoque> estoques = List.of();

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoques);

        try {
            estoqueController.listar(estoqueExemplo);
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void listarEstoquesSemelhantesComExcecaoUnsupportedOperation() {

        Estoque estoqueExemplo = new Estoque();
        estoqueExemplo.setCriadoPor(1);
        estoqueExemplo.setMercadoId(1);
        estoqueExemplo.setProdutoId(1);

        when(estoqueRepository.findAll(isA(Example.class))).thenThrow(new UnsupportedOperationException("erro_inesperado"));

        try {
            estoqueController.listar(estoqueExemplo);
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }
    }

    @Test
    public void listarEstoquesSemelhantesComExcecao() {

        Estoque estoqueExemplo = new Estoque();
        estoqueExemplo.setCriadoPor(1);
        estoqueExemplo.setMercadoId(1);
        estoqueExemplo.setProdutoId(1);

        Estoque estoque = new Estoque();
        estoque.setId(1);
        estoque.setCriadoPor(null);
        estoque.setMercadoId(null);
        estoque.setProdutoId(null);

        List<Estoque> estoques = List.of(estoque);

        when(estoqueRepository.findAll(isA(Example.class))).thenReturn(estoques);

        try {
            estoqueController.listar(estoqueExemplo);
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }
    }

    @Test
    public void removerEstoqueComSucesso() {

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        estoqueController.remover(anyInt());

        verify(estoqueRepository).deleteById(anyInt());
    }
    
    @Test
    public void removerEstoqueComExcecaoNoSuchElement() {

        when(estoqueRepository.existsById(anyInt())).thenReturn(false);

        try {
            estoqueController.remover(anyInt());
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void removerEstoqueComExcecaoDataIntegrityViolation() {

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        doThrow(new DataIntegrityViolationException("erro_remocao")).when(estoqueRepository).deleteById(anyInt());

        try {
            estoqueController.remover(anyInt());
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void removerEstoqueComExcecaoIllegalArgument() {

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        doThrow(new IllegalArgumentException("erro_inesperado")).when(estoqueRepository).deleteById(anyInt());

        try {
            estoqueController.remover(anyInt());
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void removerEstoqueComExcecao() {

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        doThrow(new RuntimeException("erro_inesperado")).when(estoqueRepository).deleteById(anyInt());

        try {
            estoqueController.remover(anyInt());
        }
        catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }
    }
}
