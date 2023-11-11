package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.MercadoController;
import com.pagueibaratoapi.models.requests.Mercado;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseMercado;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.RamoRepository;
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
    private Optional<Usuario> optionalUsuario;

    private Mercado mercado;

    private Usuario usuario;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.inicializarMercado();
        this.inicializarUsuario();
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

    // @Test
    // public void criarMercadoComEnderecoExistente() {

    //     when(usuarioRepository.existsById(anyInt())).thenReturn(true);

    //     when(ramoRepository.existsById(anyInt())).thenReturn(true);

    //     when(mercadoRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);

    //     when(mercadoRepository.findByEndereco(
    //             requestMercado.getLogradouro(), 
    //             requestMercado.getNumero(),
    //             requestMercado.getComplemento(), 
    //             requestMercado.getBairro(), 
    //             requestMercado.getCidade(),
    //             requestMercado.getUf(), 
    //             requestMercado.getCep())).thenReturn(new Mercado());

    //     try {
    //         mercadoController.criar(requestMercado);
    //     } catch (Exception e) {
    //         assertEquals(e.getCause().getMessage(), "mercado_existente");
    //         assertEquals(((ResponseStatusException) e).getStatus(), HttpStatus.CONFLICT);
    //     }
    // }

    // @Test
    // public void criarMercadoComDadosInvalidos() {

    //     Mercado requestMercado = new Mercado();
    //     requestMercado.setCriadoPor(1);
    //     requestMercado.setNome(null);
    //     requestMercado.setLogradouro("Rua Teste");
    //     requestMercado.setNumero("123");
    //     requestMercado.setBairro("Bairro Teste");
    //     requestMercado.setCidade("Cidade Teste");
    //     requestMercado.setUf("UF");
    //     requestMercado.setCep("12345-678");
    //     requestMercado.setRamoId(1);

    //     when(usuarioRepository.existsById(requestMercado.getCriadoPor())).thenReturn(true);

    //     when(ramoRepository.existsById(requestMercado.getRamoId())).thenReturn(true);

    //     try {
    //         mercadoController.criar(requestMercado);
    //     } catch (Exception e) {
    //         assertEquals(e.getCause().getMessage(), "nome_invalido");
    //         assertEquals(((ResponseStatusException) e).getStatus(), HttpStatus.BAD_REQUEST);
    //     }
    // }

    // @Test
    // public void criarMercadoComErroInesperado() {

    //     Mercado requestMercado = new Mercado();
    //     requestMercado.setCriadoPor(1);
    //     requestMercado.setNome("Mercado Teste");
    //     requestMercado.setLogradouro("Rua Teste");
    //     requestMercado.setNumero("123");
    //     requestMercado.setBairro("Bairro Teste");
    //     requestMercado.setCidade("Cidade Teste");
    //     requestMercado.setUf("UF");
    //     requestMercado.setCep("12345-678");
    //     requestMercado.setRamoId(1);

    //     when(usuarioRepository.existsById(requestMercado.getCriadoPor())).thenReturn(true);

    //     when(ramoRepository.existsById(requestMercado.getRamoId())).thenReturn(true);

    //     when(mercadoRepository.existsByNomeIgnoreCase(requestMercado.getNome())).thenReturn(false);

    //     when(mercadoRepository.findByEndereco(
    //             requestMercado.getLogradouro(), 
    //             requestMercado.getNumero(),
    //             requestMercado.getComplemento(), 
    //             requestMercado.getBairro(), 
    //             requestMercado.getCidade(),
    //             requestMercado.getUf(), 
    //             requestMercado.getCep())).thenReturn(null);

    //     when(mercadoRepository.save(requestMercado)).thenThrow(new DataIntegrityViolationException(""));

    //     try {
    //         mercadoController.criar(requestMercado);
    //     } catch (Exception e) {
    //         assertEquals(e.getCause().getMessage(), "erro_inesperado");
    //         assertEquals(((ResponseStatusException) e).getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // @Test
    // public void criarMercadoComExcecaoInesperada() {

    //     Mercado requestMercado = new Mercado();
    //     requestMercado.setCriadoPor(1);
    //     requestMercado.setNome("Mercado Teste");
    //     requestMercado.setLogradouro("Rua Teste");
    //     requestMercado.setNumero("123");
    //     requestMercado.setBairro("Bairro Teste");
    //     requestMercado.setCidade("Cidade Teste");
    //     requestMercado.setUf("UF");
    //     requestMercado.setCep("12345-678");
    //     requestMercado.setRamoId(1);

    //     when(usuarioRepository.existsById(requestMercado.getCriadoPor())).thenReturn(true);

    //     when(ramoRepository.existsById(requestMercado.getRamoId())).thenReturn(true);

    //     when(mercadoRepository.existsByNomeIgnoreCase(requestMercado.getNome())).thenReturn(false);

    //     when(mercadoRepository.findByEndereco(
    //             requestMercado.getLogradouro(), 
    //             requestMercado.getNumero(),
    //             requestMercado.getComplemento(), 
    //             requestMercado.getBairro(), 
    //             requestMercado.getCidade(),
    //             requestMercado.getUf(), 
    //             requestMercado.getCep())).thenReturn(null);

    //     when(mercadoRepository.save(requestMercado)).thenThrow(new RuntimeException());

    //     try {
    //         mercadoController.criar(requestMercado);
    //     } catch (Exception e) {
    //         assertEquals(e.getCause().getMessage(), "erro_inesperado");
    //         assertEquals(((ResponseStatusException) e).getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    /* -------------------------------------------------------------------------- */
    
}