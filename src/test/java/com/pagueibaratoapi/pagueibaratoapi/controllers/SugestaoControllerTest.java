package com.pagueibaratoapi.pagueibaratoapi.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}
