package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.UsuarioController;
import com.pagueibaratoapi.models.exceptions.DadosConflitantesException;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseUsuario;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;
import com.pagueibaratoapi.repository.UsuarioRepository;



@RunWith(SpringRunner.class)
public class UsuarioControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    private List<Usuario> usuarios;



    @Before
    public void setUp() {
        this.usuarioRepository.deleteAll();

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

        ResponseUsuario responseUsuario = this.usuarioController.criar(usuario);

        usuarioRepository.deleteById(responseUsuario.getId());

        assertNotNull(responseUsuario);
        assertNotNull(responseUsuario.getId());
        assertEquals("Usuário Teste", responseUsuario.getNome());
        assertEquals("fulano@email.com", responseUsuario.getEmail());
    }

    @Test
    public void criarUsuarioComCorpoNulo() throws Exception {

        try {

            this.usuarioController.criar(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("corpo_nulo", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComIdFornecido() throws Exception {

        usuario.setId(1);

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("id_fornecido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComComplementoInvalido() throws Exception {

        usuario.setComplemento("Complemento Inválido!");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("complemento_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComNomeInvalido() throws Exception {

        usuario.setNome("An");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComEmailInvalido() throws Exception {

        usuario.setEmail("emailinvalido");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("email_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComSenhaInvalida() throws Exception {

        usuario.setSenha("senhainvalida");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("senha_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComLogradouroInvalido() throws Exception {

        usuario.setLogradouro("Rua");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("logradouro_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComNumeroInvalido() throws Exception {

        usuario.setNumero(0);

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("numero_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComBairroInvalido() throws Exception {

        usuario.setBairro("Bair");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("bairro_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComCidadeInvalida() throws Exception {

        usuario.setCidade("São");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("cidade_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComUfInvalida() throws Exception {

        usuario.setUf("São Paulo");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("uf_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComCepInvalido() throws Exception {

        usuario.setCep("123456789");

        try {

            this.usuarioController.criar(usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("cep_invalido", e.getReason());
        }
    }

    @Test
    public void criarUsuarioComEmailExistente() throws Exception {

        Usuario usuarioCriado = this.usuarioRepository.save(usuario);

        Usuario novoUsuario = new Usuario();

        novoUsuario.setNome("Usuário Teste 2");
        novoUsuario.setEmail(this.usuario.getEmail());
        novoUsuario.setLogradouro("Rua Teste 2");
        novoUsuario.setNumero(123);
        novoUsuario.setComplemento("Casa");
        novoUsuario.setBairro("Bairro Teste");
        novoUsuario.setCidade("Cidade Teste");
        novoUsuario.setUf("SP");
        novoUsuario.setCep("12345-678");
        novoUsuario.setSenha("123456UsuarioT3ST3!");

        try {

            this.usuarioController.criar(novoUsuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("email_em_uso", e.getReason());
        }
        finally {
            this.usuarioRepository.deleteById(usuarioCriado.getId());
        }
    }

    /* -------------------------------------------------------------------------- */





    /* ----------------------  LEITURA DE USUÁRIO POR ID  ----------------------- */

    @Test
    public void lerUsuarioPorIdComSucesso() throws Exception {

        Usuario usuarioCriado = this.usuarioRepository.save(usuario);

        ResponseUsuario responseUsuario = this.usuarioController.ler(usuarioCriado.getId());

        this.usuarioRepository.deleteById(usuarioCriado.getId());

        assertNotNull(responseUsuario);
        assertNotNull(responseUsuario.getId());
        assertEquals("Usuário Teste", responseUsuario.getNome());
        assertEquals("fulano@email.com", responseUsuario.getEmail());
        assertEquals("Rua Teste", responseUsuario.getLogradouro());
    }

    @Test
    public void lerUsuarioPorIdInexistente() throws Exception {

        try {

            this.usuarioController.ler(1986);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("usuario_nao_encontrado", e.getReason());
        }
    }

    @Test
    public void lerUsuarioPorIdComExcecao() {

        try {

            this.usuarioController.ler(null);

        }
        catch (ResponseStatusException e) {
            assertEquals(500, e.getRawStatusCode());
            assertEquals("erro_inesperado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */





    /* -------------------------  LISTAGEM DE USUÁRIOS  ------------------------- */

    @Test
    public void listarUsuariosComSucesso() throws Exception {

        this.usuarioRepository.saveAll(usuarios);

        List<ResponseUsuario> responseUsuarios = this.usuarioController.listar();

        this.usuarioRepository.deleteAll();

        assertNotNull(responseUsuarios);
        assertEquals(2, responseUsuarios.size());
        assertEquals("Usuário Teste", responseUsuarios.get(0).getNome());
        assertEquals("Usuário Teste 2", responseUsuarios.get(1).getNome());
    }

    /* -------------------------------------------------------------------------- */





    /* --------------------------  EDIÇÃO DE USUÁRIOS  -------------------------- */

    @Test
    public void editarUsuarioComSucesso() throws Exception {

        Usuario usuarioCriado = this.usuarioRepository.save(usuario);

        Usuario usuarioEditado = new Usuario();

        usuarioEditado.setNome("Usuário Teste Editado");

        ResponseUsuario responseUsuario = this.usuarioController.editar(usuarioCriado.getId(), usuarioEditado);

        this.usuarioRepository.deleteById(usuarioCriado.getId());

        assertNotNull(responseUsuario);
        assertNotNull(responseUsuario.getId());
        assertEquals("Usuário Teste Editado", responseUsuario.getNome());
        assertEquals("fulano@email.com", responseUsuario.getEmail());
        assertEquals("Rua Teste", responseUsuario.getLogradouro());

    }

    @Test
    public void editarUsuarioComCorpoNulo() throws Exception {

        try {

            this.usuarioController.editar(1, null);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("corpo_nulo", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComIdFornecido() throws Exception {

        usuario.setId(1);

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("id_fornecido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComComplementoInvalido() throws Exception {

        usuario.setComplemento("Complemento Inválido!");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("complemento_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComNomeInvalido() throws Exception {

        usuario.setNome("An");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("nome_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComEmailInvalido() throws Exception {

        usuario.setEmail("emailinvalido");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("email_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComSenhaInvalida() throws Exception {

        usuario.setSenha("senhainvalida");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("senha_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComLogradouroInvalido() throws Exception {

        usuario.setLogradouro("Rua");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("logradouro_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComNumeroInvalido() throws Exception {

        usuario.setNumero(0);

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("numero_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComBairroInvalido() throws Exception {

        usuario.setBairro("Bair");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("bairro_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComCidadeInvalida() throws Exception {

        usuario.setCidade("São");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("cidade_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComUfInvalida() throws Exception {

        usuario.setUf("São Paulo");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("uf_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComCepInvalido() throws Exception {

        usuario.setCep("123456789");

        try {

            this.usuarioController.editar(1, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(400, e.getRawStatusCode());
            assertEquals("cep_invalido", e.getReason());
        }

    }

    @Test
    public void editarUsuarioComEmailExistente() throws Exception {

        Usuario usuarioCriado = this.usuarioRepository.save(usuario);

        Usuario usuarioEditado = new Usuario();

        usuarioEditado.setNome("Usuário Teste 2");
        usuarioEditado.setEmail(this.usuarios.get(1).getEmail());
        usuarioEditado.setLogradouro("Rua Teste 2");
        usuarioEditado.setNumero(123);
        usuarioEditado.setComplemento("Casa");
        usuarioEditado.setBairro("Bairro Teste");
        usuarioEditado.setCidade("Cidade Teste");
        usuarioEditado.setUf("SP");
        usuarioEditado.setCep("12345-678");
        usuarioEditado.setSenha("123456UsuarioT3ST3!");

        try {

            this.usuarioController.editar(usuarioCriado.getId(), usuarioEditado);

        }
        catch (ResponseStatusException e) {
            assertEquals(409, e.getRawStatusCode());
            assertEquals("email_em_uso", e.getReason());
        }
        finally {
            this.usuarioRepository.deleteById(usuarioCriado.getId());
        }

    }

    @Test
    public void editarUsuarioDeletado() throws Exception {

        Usuario usuarioCriado = this.usuarioRepository.save(usuario);


        Usuario usuarioEditado = new Usuario();

        usuarioEditado.setNome("Usuário Teste Editado");


        this.usuarioController.remover(usuarioCriado.getId());

        try {

            this.usuarioController.editar(usuarioCriado.getId(), usuarioEditado);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }
        finally {
            this.usuarioRepository.deleteById(usuarioCriado.getId());
        }

    }

    @Test
    public void editarUsuarioComExcecaoNoSuchElement() throws Exception {

        try {

            this.usuarioController.editar(1986, usuario);

        }
        catch (ResponseStatusException e) {
            assertEquals(404, e.getRawStatusCode());
            assertEquals("nao_encontrado", e.getReason());
        }

    }

    /* -------------------------------------------------------------------------- */

}