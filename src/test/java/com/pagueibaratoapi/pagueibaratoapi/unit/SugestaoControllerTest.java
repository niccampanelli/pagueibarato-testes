package com.pagueibaratoapi.pagueibaratoapi.unit;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
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

import com.pagueibaratoapi.controllers.SugestaoController;
import com.pagueibaratoapi.models.requests.Sugestao;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseSugestao;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.SugestaoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;



public class SugestaoControllerTest {

    @InjectMocks
    private SugestaoController sugestaoController;

    @Mock
    private SugestaoRepository sugestaoRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Optional<Usuario> optionalUsuario;

    @Mock
    private Optional<Sugestao> optionalSugestao;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void criarSugestaoComSucesso() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Usuario Teste");
        usuario.setEmail("teste@email.com");

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        when(sugestaoRepository.save(any())).thenReturn(requestSugestao);

        ResponseSugestao responseSugestao = sugestaoController.criar(requestSugestao);

        assertTrue(responseSugestao.getEstoqueId() != null);
    }

    @Test
    public void criarSugestaoComExcecaoDadosInvalidos() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setId(1);

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void criarSugestaoComEstoqueInvalido() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(false);

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void criarSugestaoComUsuarioInvalido() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void criarSugestaoComExcecaoNoSuchElement() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void criarSugestaoComUsuarioNaoEncontrado() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("");
        usuario.setEmail("");

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void criarSugestaoComExcecaoDataIntegrityViolation() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void criarSugestaoComExcecaoIllegalArgument() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void criarSugestaoComExcecao() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            sugestaoController.criar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void lerSugestaoComSucesso() {

        Sugestao sugestao = new Sugestao();
        sugestao.setId(1);
        sugestao.setPreco(10f);
        sugestao.setEstoqueId(1);
        sugestao.setTimestamp(Calendar.getInstance());
        sugestao.setCriadoPor(1);

        when(sugestaoRepository.findById(anyInt())).thenReturn(optionalSugestao);
        when(optionalSugestao.get()).thenReturn(sugestao);

        ResponseSugestao responseSugestao = sugestaoController.ler(1);

        assertTrue(responseSugestao.getId() != null);
    }

    @Test
    public void lerSugestaoComExcecaoNoSuchElement() {
            
        when(sugestaoRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            sugestaoController.ler(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void lerSugestaoComExcecao() {
            
        when(sugestaoRepository.findById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            sugestaoController.ler(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void listarSugestaoComSucesso() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        Sugestao sugestao = new Sugestao();
        sugestao.setId(2);
        sugestao.setPreco(10f);
        sugestao.setEstoqueId(1);
        sugestao.setTimestamp(Calendar.getInstance());
        sugestao.setCriadoPor(1);

        List<Sugestao> sugestoes = List.of(sugestao);

        when(sugestaoRepository.findAll(isA(Example.class))).thenReturn(sugestoes);

        List<ResponseSugestao> responseSugestoes = sugestaoController.listar(requestSugestao);

        assertTrue(responseSugestoes.size() == 1);
    }

    @Test
    public void listarSugestaoComExcecaoDadosInvalidos() {

        Sugestao requestSugestao = null;

        try {
            sugestaoController.listar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void listarSugestaoComExcecaoNullPointer() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        List<Sugestao> sugestoes = List.of();

        when(sugestaoRepository.findAll(isA(Example.class))).thenReturn(sugestoes);

        try {
            sugestaoController.listar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void listarSugestaoComExcecaoUnsupportedOperation() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(sugestaoRepository.findAll(isA(Example.class)))
                .thenThrow(new UnsupportedOperationException("erro_inesperado"));

        try {
            sugestaoController.listar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.UnsupportedOperationException"));
        }
    }

    @Test
    public void listarSugestaoComExcecao() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(sugestaoRepository.findAll(isA(Example.class))).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            sugestaoController.listar(requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void editarSugestaoComSucesso() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        Sugestao sugestaoAtual = new Sugestao();
        sugestaoAtual.setId(1);
        sugestaoAtual.setPreco(10f);
        sugestaoAtual.setEstoqueId(1);
        sugestaoAtual.setTimestamp(Calendar.getInstance());
        sugestaoAtual.setCriadoPor(1);

        when(sugestaoRepository.findById(anyInt())).thenReturn(optionalSugestao);
        when(optionalSugestao.get()).thenReturn(sugestaoAtual);

        Sugestao sugestaoEditada = new Sugestao();
        sugestaoEditada.setId(1);
        sugestaoEditada.setPreco(20f);
        sugestaoEditada.setEstoqueId(1);
        sugestaoEditada.setTimestamp(Calendar.getInstance());
        sugestaoEditada.setCriadoPor(1);

        when(sugestaoRepository.save(any())).thenReturn(sugestaoEditada);

        ResponseSugestao responseSugestao = sugestaoController.editar(1, requestSugestao);

        System.out.println(responseSugestao.getPreco());

        assertTrue(responseSugestao.getPreco() == 0.2f);
    }

    @Test
    public void editarSugestaoComExcecaoDadosInvalidos() {

        Sugestao requestSugestao = null;

        try {
            sugestaoController.editar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void editarSugestaoComExcecaoDataIntegrityViolation() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(sugestaoRepository.findById(anyInt())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            sugestaoController.editar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void editarSugestaoComExcecaoIllegalArgument() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(sugestaoRepository.findById(anyInt())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            sugestaoController.editar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void editarSugestaoComExcecaoNoSuchElement() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(sugestaoRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            sugestaoController.editar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void editarSugestaoComExcecao() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(sugestaoRepository.findById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            sugestaoController.editar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void atualizarSugestaoComSucesso() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        Sugestao sugestao = new Sugestao();
        sugestao.setId(1);
        sugestao.setPreco(10f);
        sugestao.setEstoqueId(1);
        sugestao.setTimestamp(Calendar.getInstance());
        sugestao.setCriadoPor(1);

        when(sugestaoRepository.findById(anyInt())).thenReturn(optionalSugestao);
        when(optionalSugestao.get()).thenReturn(sugestao);

        Sugestao sugestaoEditada = new Sugestao();
        sugestaoEditada.setId(1);
        sugestaoEditada.setPreco(20f);
        sugestaoEditada.setEstoqueId(1);
        sugestaoEditada.setTimestamp(Calendar.getInstance());
        sugestaoEditada.setCriadoPor(1);

        when(sugestaoRepository.save(any())).thenReturn(sugestaoEditada);

        ResponseSugestao responseSugestao = sugestaoController.atualizar(1, requestSugestao);

        System.out.println(responseSugestao.getPreco());

        assertTrue(responseSugestao.getPreco() == 0.2f);
    }

    @Test
    public void atualizarSugestaoComExcecaoDadosInvalidos() {

        Sugestao requestSugestao = null;

        try {
            sugestaoController.atualizar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void atualizarSugestaoComEstoqueInvalido() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setId(1);
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(false);

        try {
            sugestaoController.atualizar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void atualizarSugestaoComUsuarioInvalido() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setId(1);
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        try {
            sugestaoController.atualizar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.BAD_REQUEST.toString()));
        }
    }

    @Test
    public void atualizarSugestaoComExcecaoNoSuchElement() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(sugestaoRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            sugestaoController.atualizar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }
    }

    @Test
    public void atualizarSugestaoComExcecaoDataIntegrityViolation() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(1);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(sugestaoRepository.findById(anyInt())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            sugestaoController.atualizar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }
    }

    @Test
    public void atualizarSugestaoComExcecaoIllegalArgument() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(2);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(sugestaoRepository.findById(anyInt())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            sugestaoController.atualizar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }
    }

    @Test
    public void atualizarSugestaoComExcecao() {

        Sugestao requestSugestao = new Sugestao();
        requestSugestao.setPreco(10f);
        requestSugestao.setEstoqueId(1);
        requestSugestao.setTimestamp(Calendar.getInstance());
        requestSugestao.setCriadoPor(2);

        when(estoqueRepository.existsById(anyInt())).thenReturn(true);

        when(usuarioRepository.existsById(anyInt())).thenReturn(true);

        when(sugestaoRepository.findById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            sugestaoController.atualizar(1, requestSugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }
    }

    @Test
    public void removerSugestaoComSucesso() {

        when(sugestaoRepository.existsById(anyInt())).thenReturn(true);

        sugestaoController.remover(1);

        verify(sugestaoRepository).deleteById(anyInt());
    }

    @Test
    public void removerSugestaoComExcecaoNoSuchElement() {

        when(sugestaoRepository.existsById(anyInt())).thenReturn(false);

        try {
            sugestaoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.NOT_FOUND.toString()));
        }

        verify(sugestaoRepository).existsById(anyInt());
    }

    @Test
    public void removerSugestaoComExcecaoDataIntegrityViolation() {

        when(sugestaoRepository.existsById(anyInt())).thenThrow(new DataIntegrityViolationException("erro_insercao"));

        try {
            sugestaoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("org.springframework.dao.DataIntegrityViolationException"));
        }

        verify(sugestaoRepository).existsById(anyInt());
    }

    @Test
    public void removerSugestaoComExcecaoIllegalArgument() {

        when(sugestaoRepository.existsById(anyInt())).thenThrow(new IllegalArgumentException("erro_inesperado"));

        try {
            sugestaoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

        verify(sugestaoRepository).existsById(anyInt());
    }

    @Test
    public void removerSugestaoComExcecao() {

        when(sugestaoRepository.existsById(anyInt())).thenThrow(new RuntimeException("erro_inesperado"));

        try {
            sugestaoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("java.lang.RuntimeException"));
        }

        verify(sugestaoRepository).existsById(anyInt());
    }
}
