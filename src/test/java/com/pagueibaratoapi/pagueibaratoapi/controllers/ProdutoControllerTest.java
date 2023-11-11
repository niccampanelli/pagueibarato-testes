package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.ProdutoController;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseProduto;
import com.pagueibaratoapi.repository.CategoriaRepository;
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
}
