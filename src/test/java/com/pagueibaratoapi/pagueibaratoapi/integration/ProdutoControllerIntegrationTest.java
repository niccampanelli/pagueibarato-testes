package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.ProdutoController;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseProduto;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ProdutoControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    @Autowired
    private ProdutoController produtoController;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Before
    public void setUp() {
        produtoRepository.deleteAll();
        usuarioRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void criarProdutoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Usuario Teste");
        usuarioCriar.setEmail("teste@email.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Logradouro Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("12345-678");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descricao Testeeeeeee");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCriadoPor(idUsuario);
        produtoCriar.setCategoriaId(idCategoria);

        ResponseProduto responseProduto = produtoController.criar(produtoCriar);

        assertTrue(responseProduto.getId() != null && responseProduto.getId() > 0);
    }

    @Test
    public void criarProdutoComCorpoNulo() {

        try {
            produtoController.criar(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void criarProdutoComIdFornecido() {

        Produto produtoCriar = new Produto();
        produtoCriar.setId(1);

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void criarProdutoComCorInvalida() {

        Produto produtoCriar = new Produto();
        produtoCriar.setCor("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("cor_invalido"));
        }
    }

    @Test
    public void criarProdutoComNomeInvalido() {

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("uva");

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("nome_invalido"));
        }
    }

    @Test
    public void criarProdutoComMarcaInvalida() {

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("banana pera");
        produtoCriar.setMarca("a");

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("marca_invalido"));
        }
    }

    @Test
    public void criarProdutoComTamanhoInvalido() {

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("banana pera");
        produtoCriar.setMarca("banana");
        produtoCriar.setTamanho("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("tamanho_invalido"));
        }
    }

    @Test
    public void criarProdutoComUsuarioInvalido() {

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("banana pera");
        produtoCriar.setMarca("banana");
        produtoCriar.setTamanho("pequeno");
        produtoCriar.setCriadoPor(0);

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void criarProdutoComCategoriaInvalida() {

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("banana pera");
        produtoCriar.setMarca("banana");
        produtoCriar.setTamanho("pequeno");
        produtoCriar.setCriadoPor(1);
        produtoCriar.setCategoriaId(0);

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("categoria_invalido"));
        }
    }

    @Test
    public void criarProdutoComUsuarioNaoEncontrado() {

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("banana pera");
        produtoCriar.setMarca("banana");
        produtoCriar.setTamanho("pequeno");
        produtoCriar.setCriadoPor(1);
        produtoCriar.setCategoriaId(1);

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_nao_encontrado"));
        }
    }

    @Test
    public void crairProdutoComCategoriaNaoEncontrada() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Usuario Teste");
        usuarioCriar.setEmail("teste@email.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Logradouro Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("12345-678");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("banana pera");
        produtoCriar.setMarca("banana");
        produtoCriar.setTamanho("pequeno");
        produtoCriar.setCriadoPor(idUsuario);
        produtoCriar.setCategoriaId(1);

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("categoria_nao_encontrado"));
        }
    }

    @Test
    public void criarProdutoComProdutoExistente() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Usuario Teste");
        usuarioCriar.setEmail("teste@email.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Logradouro Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("12345-678");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descricao Testeeeeeee");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCriadoPor(idUsuario);
        produtoCriar.setCategoriaId(idCategoria);

        produtoRepository.save(produtoCriar);

        try {
            produtoCriar.setId(null);

            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("produto_existente"));
        }
    }

    @Test
    public void criarProdutoComExcecaoNoSuchElement() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Usuario Teste");
        usuarioCriar.setEmail("");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Logradouro Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("12345-678");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descricao Testeeeeeee");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCriadoPor(idUsuario);
        produtoCriar.setCategoriaId(idCategoria);

        try {
            produtoController.criar(produtoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("NoSuchElementException"));
        }
    }

    @Test
    public void lerProdutoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Usuario Teste");
        usuarioCriar.setEmail("teste@email.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Logradouro Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("12345-678");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descricao Testeeeeeee");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCriadoPor(idUsuario);
        produtoCriar.setCategoriaId(idCategoria);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        ResponseProduto responseProduto = produtoController.ler(idProduto);

        assertTrue(responseProduto != null && responseProduto.getId() != null && responseProduto.getId() > 0);
    }

    @Test
    public void lerProdutoComExcecaoNoSuchElement() {

        try {
            produtoController.ler(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("NoSuchElementException"));
        }
    }

    @Test
    public void lerProdutoComExcecao() {

        try {
            produtoController.ler(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }
    }

    @Test
    public void listarProdutoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Usuario Teste");
        usuarioCriar.setEmail("teste@email.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Logradouro Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("12345-678");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descricao Testeeeeeee");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCriadoPor(idUsuario);
        produtoCriar.setCategoriaId(idCategoria);

        produtoRepository.save(produtoCriar);

        List<ResponseProduto> responseProdutos = produtoController.listar(new Produto());

        assertTrue(responseProdutos != null && responseProdutos.size() == 1);
    }
}
