package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.ProdutoController;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseProduto;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;

public class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private Optional<Usuario> optionalUsuario;

    @Mock
    private Optional<Produto> optionalProduto;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void criarProdutoComSucesso() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("teste@email.com");

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        Produto produtoCriado = new Produto();
        produtoCriado.setId(1);
        produtoCriado.setNome("Produto Teste");
        produtoCriado.setMarca("Marca Teste");
        produtoCriado.setCor("Cor Teste");
        produtoCriado.setTamanho("Tamanho Teste");
        produtoCriado.setCategoriaId(1);
        produtoCriado.setCriadoPor(1);

        when(produtoRepository.save(requestProduto)).thenReturn(produtoCriado);

        ResponseProduto responseProduto = produtoController.criar(requestProduto);

        assertTrue(responseProduto.getId() != null);
    }

    @Test
    public void criarProdutoComExcecaoDadosInvalidos() {

        Produto requestProduto = new Produto();
        requestProduto.setId(1);

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void criarProdutoComUsuarioInvalido() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void criarProdutoComCategoriaInvalida() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("teste@email.com");

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(false);

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void criarProdutoComExcecaoDadosConflitantes() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("teste@email.com");

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(requestProduto);

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.CONFLICT.toString()));
        }
    }

    @Test
    public void criarProdutoComExcecaoNoSuchElement() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("teste@email.com");

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(null);

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void criarProdutoComExcecaoDataIntegrityViolation() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void criarProdutoComExcecaoIllegalArgument() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void criarProdutoComExcecao() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            produtoController.criar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void lerProdutoPorIdComSucesso() {

        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto Teste");
        produto.setMarca("Marca Teste");
        produto.setCor("Cor Teste");
        produto.setTamanho("Tamanho Teste");
        produto.setCategoriaId(1);
        produto.setCriadoPor(1);

        when(produtoRepository.findById(anyInt())).thenReturn(optionalProduto);
        when(optionalProduto.get()).thenReturn(produto);

        ResponseProduto responseProduto = produtoController.ler(anyInt());

        System.out.println(responseProduto.getId());

        assertTrue(responseProduto.getId() != null);
    }

    @Test
    public void lerProdutoPorIdComExcecaoNoSuchElement() {

        when(produtoRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            produtoController.ler(anyInt());
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void lerProdutoComExcecao() {
        
        when(produtoRepository.findById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            produtoController.ler(anyInt());
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void listarTodosProdutosComSucesso() {

        Produto produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Produto Teste 1");
        produto1.setMarca("Marca Teste 1");
        produto1.setCor("Cor Teste 1");
        produto1.setTamanho("Tamanho Teste 1");
        produto1.setCategoriaId(1);
        produto1.setCriadoPor(1);

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setNome("Produto Teste 2");
        produto2.setMarca("Marca Teste 2");
        produto2.setCor("Cor Teste 2");
        produto2.setTamanho("Tamanho Teste 2");
        produto2.setCategoriaId(2);
        produto2.setCriadoPor(2);

        List<Produto> produtos = List.of(produto1, produto2);

        when(produtoRepository.findAll(isA(Example.class))).thenReturn(produtos);

        List<ResponseProduto> responseProdutos = produtoController.listar(new Produto());

        System.out.println(responseProdutos.size());

        assertTrue(responseProdutos.size() == 2);
    }

    @Test
    public void listarTodosProdutosComExcecaoDadosInvalidos() {

        Produto requestProduto = new Produto();
        requestProduto.setId(1);

        try {
            produtoController.listar(requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void listarTodosProdutosComExcecaoNullPointer() {

        List<Produto> produtos = List.of();

        when(produtoRepository.findAll(isA(Example.class))).thenReturn(produtos);

        try {
            produtoController.listar(new Produto());
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void listarTodosProdutosComExecaoUnsupportedOperation() {

        when(produtoRepository.findAll(isA(Example.class))).thenThrow(new UnsupportedOperationException("erro_inesperado"));

        try {
            produtoController.listar(new Produto());
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }
    }

    @Test
    public void listarTodosProdutosComExcecao() {

        when(produtoRepository.findAll(isA(Example.class))).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            produtoController.listar(new Produto());
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void listarProdutosSemelhantesComSucesso() {

        Produto produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Produto Teste 1");
        produto1.setMarca("Marca Teste 1");
        produto1.setCor("Cor Teste 1");
        produto1.setTamanho("Tamanho Teste 1");
        produto1.setCategoriaId(1);
        produto1.setCriadoPor(1);

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setNome("Produto Teste 2");
        produto2.setMarca("Marca Teste 2");
        produto2.setCor("Cor Teste 2");
        produto2.setTamanho("Tamanho Teste 2");
        produto2.setCategoriaId(2);
        produto2.setCriadoPor(2);

        List<Produto> produtos = List.of(produto1, produto2);

        when(produtoRepository.findAll(isA(Example.class))).thenReturn(produtos);

        Produto produtoExemplo = new Produto();
        produtoExemplo.setNome("Produto Teste 1");
        produtoExemplo.setMarca("Marca Teste 1");
        produtoExemplo.setCor("Cor Teste 1");
        produtoExemplo.setTamanho("Tamanho Teste 1");
        produtoExemplo.setCategoriaId(1);
        produtoExemplo.setCriadoPor(1);

        List<ResponseProduto> responseProdutos = produtoController.listar(produtoExemplo);

        System.out.println(responseProdutos.size());

        assertTrue(responseProdutos.size() == 2);
    }

    @Test
    public void listarProdutosSemelhantesComExcecaoDadosInvalidos() {

        Produto produtoExemplo = new Produto();
        produtoExemplo.setId(1);

        try {
            produtoController.listar(produtoExemplo);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void listarProdutosSemelhantesComExcecaoNullPointer() {

        List<Produto> produtos = List.of();

        when(produtoRepository.findAll(isA(Example.class))).thenReturn(produtos);

        Produto produtoExemplo = new Produto();
        produtoExemplo.setNome("Produto Teste 1");

        try {
            produtoController.listar(produtoExemplo);
        } catch (ResponseStatusException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void listarProdutosSemelhantesComExecaoUnsupportedOperation() {

        when(produtoRepository.findAll(isA(Example.class))).thenThrow(new UnsupportedOperationException("erro_inesperado"));

        Produto produtoExemplo = new Produto();
        produtoExemplo.setNome("Produto Teste 1");

        try {
            produtoController.listar(produtoExemplo);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }
    }

    @Test
    public void listarProdutosSemelhantesComExcecao() {

        when(produtoRepository.findAll(isA(Example.class))).thenThrow(new RuntimeException("erro_inesperado"));

        Produto produtoExemplo = new Produto();
        produtoExemplo.setNome("Produto Teste 1");

        try {
            produtoController.listar(produtoExemplo);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void editarProdutoComSucesso() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(null);

        Produto produtoAtual = new Produto();
        produtoAtual.setId(1);
        produtoAtual.setNome("Produto Teste");
        produtoAtual.setMarca("Marca Teste");
        produtoAtual.setCor("Cor Teste");
        produtoAtual.setTamanho("Tamanho Teste");
        produtoAtual.setCategoriaId(1);

        when(produtoRepository.findById(anyInt())).thenReturn(optionalProduto);
        when(optionalProduto.get()).thenReturn(produtoAtual);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        Produto produtoEditado = new Produto();
        produtoEditado.setId(1);
        produtoEditado.setNome("Produto Teste");
        produtoEditado.setMarca("Marca Teste");
        produtoEditado.setCor("Cor Teste");
        produtoEditado.setTamanho("Tamanho Teste");
        produtoEditado.setCategoriaId(1);
        produtoEditado.setCriadoPor(1);

        when(produtoRepository.save(any())).thenReturn(produtoEditado);

        ResponseProduto responseProduto = produtoController.editar(1, requestProduto);

        assertTrue(responseProduto.getId() != null);
    }

    @Test
    public void editarProdutoComExcecaoDadosConflitantes() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(new Produto());

        try {
            produtoController.editar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.CONFLICT.toString()));
        }
    }

    @Test
    public void editarProdutoComExcecaoDadosInvalidos() {

        Produto requestProduto = new Produto();
        requestProduto.setId(1);

        try {
            produtoController.editar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void editarProdutoComCategoriaInvalida() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(null);

        Produto produtoAtual = new Produto();
        produtoAtual.setId(1);
        produtoAtual.setNome("Produto Teste");
        produtoAtual.setMarca("Marca Teste");
        produtoAtual.setCor("Cor Teste");
        produtoAtual.setTamanho("Tamanho Teste");
        produtoAtual.setCategoriaId(1);

        when(produtoRepository.findById(anyInt())).thenReturn(optionalProduto);
        when(optionalProduto.get()).thenReturn(produtoAtual);

        when(categoriaRepository.existsById(anyInt())).thenReturn(false);

        try {
            produtoController.editar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void editarProdutoComExcecaoDataIntegrityViolation() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            produtoController.editar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void editarProdutoComExcecaoIllegalArgument() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            produtoController.editar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void editarProdutoComExcecaoNoSuchElement() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(null);

        when(produtoRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            produtoController.editar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void editarProdutoComExcecao() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste");
        requestProduto.setMarca("Marca Teste");
        requestProduto.setCor("Cor Teste");
        requestProduto.setTamanho("Tamanho Teste");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenThrow(new RuntimeException("erro_inesperado"));

        try {
            produtoController.editar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void atualizarProdutoComSucesso() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(null);

        when(produtoRepository.findById(anyInt())).thenReturn(optionalProduto);
        when(optionalProduto.get()).thenReturn(new Produto());

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1);
        produtoAtualizado.setNome("Produto Teste Atualizado");
        produtoAtualizado.setMarca("Marca Teste Atualizado");
        produtoAtualizado.setCor("Cor Teste Atualizado");
        produtoAtualizado.setTamanho("Tamanho Teste Atualizado");
        produtoAtualizado.setCategoriaId(1);
        produtoAtualizado.setCriadoPor(1);

        when(produtoRepository.save(any())).thenReturn(produtoAtualizado);

        ResponseProduto responseProduto = produtoController.atualizar(1, requestProduto);

        assertTrue(responseProduto.getId() != null);
    }

    @Test
    public void atualizarProdutoComExcecaoDadosInvalidos() {

        try {
            produtoController.atualizar(1, new Produto());
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void atualizarProdutoComUsuarioInvalido() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {
            produtoController.atualizar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void atualizarProdutoComCategoriaInvalida() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(false);

        try {
            produtoController.atualizar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void atualizarProdutoComExcecaoDadosConflitantes() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(new Produto());

        try {
            produtoController.atualizar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.CONFLICT.toString()));
        }
    }

    @Test
    public void atualizarProdutoComExcecaoNoSuchElement() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(categoriaRepository.existsById(anyInt())).thenReturn(true);

        when(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(),
                requestProduto.getMarca(),
                requestProduto.getTamanho(),
                requestProduto.getCor()))
                .thenReturn(null);

        when(produtoRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            produtoController.atualizar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void atualizarProdutoComExcecaoDataIntegrityViolation() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            produtoController.atualizar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void atualizarProdutoComExcecaoIllegalArgument() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            produtoController.atualizar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void atualizarProdutoComExcecao() {

        Produto requestProduto = new Produto();
        requestProduto.setNome("Produto Teste Atualizado");
        requestProduto.setMarca("Marca Teste Atualizado");
        requestProduto.setCor("Cor Teste Atualizado");
        requestProduto.setTamanho("Tamanho");
        requestProduto.setCategoriaId(1);
        requestProduto.setCriadoPor(1);

        when(usuarioRepository.existsById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            produtoController.atualizar(1, requestProduto);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void removerProdutoComSucesso() {

        when(produtoRepository.existsById(anyInt())).thenReturn(true);

        produtoController.remover(1);

        verify(produtoRepository).deleteById(anyInt());
    }

    @Test
    public void removerProdutoComExcecaoNoSuchElement() {
        
        when(produtoRepository.existsById(anyInt())).thenReturn(false);

        try {
            produtoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void removerProdutoComExcecaoDataIntegrityViolation() {

        when(produtoRepository.existsById(anyInt())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            produtoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void removerProdutoComExcecaoIllegalArgument() {

        when(produtoRepository.existsById(anyInt())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            produtoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void removerProdutoComExcecao() {

        when(produtoRepository.existsById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            produtoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }
}
