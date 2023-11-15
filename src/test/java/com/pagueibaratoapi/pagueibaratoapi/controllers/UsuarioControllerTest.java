package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.UsuarioController;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseUsuario;
import com.pagueibaratoapi.repository.UsuarioRepository;



public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Optional<Usuario> optionalUsuario;

    private Usuario usuario;

    private List<Usuario> usuarios;



    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.inicializarUsuario();
    }

    private void inicializarUsuario() {
        usuario = new Usuario();

        usuario.setNome("Usuário Teste");
        usuario.setEmail("fulano@email.com");
        usuario.setLogradouro("Rua Teste");
        usuario.setNumero(123);
        usuario.setComplemento("Casa");
        usuario.setBairro("Bairro Teste");
        usuario.setCidade("Cidade Teste");
        usuario.setUf("SP");
        usuario.setCep("12345-678");
        usuario.setSenha("123456UsuarioT3ST3!");

        Usuario usuario2 = new Usuario();

        usuario2.setNome("Usuário Teste 2");
        usuario2.setEmail("ciclano@email.com");
        usuario2.setLogradouro("Rua Teste 2");
        usuario2.setNumero(456);
        usuario2.setComplemento("Casa 2");
        usuario2.setBairro("Bairro Teste 2");
        usuario2.setCidade("Cidade Teste 2");
        usuario2.setUf("SP");
        usuario2.setCep("12345-678");
        usuario2.setSenha("123456UsuarioT3ST3!");

        this.usuarios = List.of(usuario, usuario2);
        
    }



    /* --------------------------  CRIAÇÃO DE USUÁRIO  -------------------------- */

    @Test
    public void criarUsuarioComSucesso() throws Exception {

        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);

        when(usuarioRepository.save(any())).thenReturn(usuario);

        ResponseUsuario response = usuarioController.criar(usuario);

        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getEmail(), response.getEmail());
    }

    @Test
    public void criarUsuarioComEmailExistente() throws Exception {

        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);

        try {

            usuarioController.criar(usuario);

        } catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("email_em_uso", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComDadosInvalidos() throws Exception {

        usuario.setSenha("123456");

        try {

            usuarioController.criar(usuario);

        } catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("senha_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComExcecaoDataViolation() throws Exception {

        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);
        when(usuarioRepository.save(any())).thenThrow(new DataIntegrityViolationException(""));

        try {

            usuarioController.criar(usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_insercao", e.getReason());
        }

    }

    @Test
    public void criarUsuarioComExcecaoIllegalArgument() throws Exception {

        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);
        when(usuarioRepository.save(any())).thenThrow(new IllegalArgumentException(""));

        try {

            usuarioController.criar(usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void criarUsuarioComExcecao() throws Exception {

        when(usuarioRepository.findByEmail(anyString())).thenThrow(new RuntimeException());

        try {

            usuarioController.criar(usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -----------------------  LEITURA DE USUÁRIO POR ID ----------------------- */

    @Test
    public void lerUsuarioComSucesso() throws Exception {

        usuario.setId(1);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        ResponseUsuario responseUsuario = usuarioController.ler(1);

        assertEquals(usuario.getId(), responseUsuario.getId());
        assertEquals(usuario.getNome(), responseUsuario.getNome());
        assertEquals(usuario.getEmail(), responseUsuario.getEmail());

    }

    @Test
    public void lerUsuarioInexistente() throws Exception {

        usuario.setEmail("");

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);

        try {

            usuarioController.ler(1);

        } 
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }

    }

    @Test
    public void lerUsuarioComExcecao() throws Exception {

        when(usuarioRepository.findById(anyInt())).thenThrow(new RuntimeException());

        try {

            usuarioController.ler(1);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */





    /* --------------------------  LISTAGEM DE USUÁRIOS ------------------------- */

    @Test
    public void listarComSucesso() {

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        
        List<ResponseUsuario> responseUsuarios = usuarioController.listar();

        assertNotNull(responseUsuarios);
        assertEquals(usuarios.get(0).getNome(), responseUsuarios.get(0).getNome());
        assertEquals(usuarios.get(0).getEmail(), responseUsuarios.get(0).getEmail());
        assertEquals(usuarios.get(1).getNome(), responseUsuarios.get(1).getNome());
        assertEquals("ciclano@email.com", responseUsuarios.get(1).getEmail());

    }

    @Test
    public void listarComExcecaoNullPointer() {

        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

        try {

            usuarioController.listar();

        } 
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }
        
    }

    @Test
    public void listarComExcecaoUnsupportedOperation() {

        when(usuarioRepository.findAll()).thenThrow(new UnsupportedOperationException());

        try {

            usuarioController.listar();

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("UnsupportedOperationException"));
        }

    }

    @Test
    public void listarComExcecao() {

        when(usuarioRepository.findAll()).thenThrow(new RuntimeException());

        try {

            usuarioController.listar();

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause() instanceof RuntimeException);
        }
    }

    /* -------------------------------------------------------------------------- */





    /* --------------------------  EDIÇÃO DE USUÁRIOS  -------------------------- */

    @Test
    public void editarUsuarioComSucesso() throws Exception {

        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);
        
        when(usuarioRepository.save(any())).thenReturn(usuarios.get(1));

        usuarios.get(1).setSenha(null);

        ResponseUsuario responseUsuario = usuarioController.editar(1, usuarios.get(1));

        assertEquals(usuarios.get(1).getNome(), responseUsuario.getNome());
        assertEquals(usuarios.get(1).getEmail(), responseUsuario.getEmail());

    }

    @Test
    public void editarUsuarioComEmailExistente() throws Exception {

        usuario.setSenha(null);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);

        try {

            usuarioController.editar(1, usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("email_em_uso", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComDadosInvalidos() throws Exception {
        
        usuario.setSenha("123");

        try {

            usuarioController.editar(1, usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("senha_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComExcecaoDataViolation() throws Exception {

        this.usuario.setSenha(null);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);
        
        when(usuarioRepository.save(any())).thenThrow(new DataIntegrityViolationException(""));

        try {

            usuarioController.editar(1, usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_insercao", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComExcecaoIllegalArgument() throws Exception {
        
        usuario.setSenha(null);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(optionalUsuario);
        when(optionalUsuario.get()).thenReturn(usuario);
        
        when(usuarioRepository.save(any())).thenThrow(new IllegalArgumentException());

        try {

            usuarioController.editar(1, usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("java.lang.IllegalArgumentException"));
        }

    }

    @Test
    public void editarUsuarioComExcecaoNoSuchElement() throws Exception {

        usuario.setSenha(null);

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.empty());

        try {

            usuarioController.editar(1, usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }
        
    }

    @Test
    public void editarUsuarioComExcecao() throws Exception {

        this.usuario.setSenha(null);

        when(usuarioRepository.findById(anyInt())).thenThrow(new RuntimeException());

        try {

            usuarioController.editar(1, usuario);

        } 
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
            assertTrue(e.getCause().toString().contains("RuntimeException"));
        }

    }

    /* -------------------------------------------------------------------------- */

}