package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.controllers.ProdutoController;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseProduto;
import com.pagueibaratoapi.pagueibaratoapi.PagueiBaratoApiApplicationTests;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;



@RunWith(SpringRunner.class)
public class ProdutoControllerIntegrationTest extends PagueiBaratoApiApplicationTests {

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

    @Test
    public void listarProdutoComCorpoNulo() {

        try {
            produtoController.listar(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void listarProdutoComIdFornecido() {

        Produto produtoListar = new Produto();
        produtoListar.setId(1);

        try {
            produtoController.listar(produtoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void listarProdutoComCorInvalida() {

        Produto produtoListar = new Produto();
        produtoListar.setCor("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.listar(produtoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("cor_invalido"));
        }
    }

    @Test
    public void listarProdutoComNomeInvalido() {

        Produto produtoListar = new Produto();
        produtoListar.setNome("uva");

        try {
            produtoController.listar(produtoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("nome_invalido"));
        }
    }

    @Test
    public void listarProdutoComMarcaInvalida() {

        Produto produtoListar = new Produto();
        produtoListar.setNome("banana pera");
        produtoListar.setMarca("a");

        try {
            produtoController.listar(produtoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("marca_invalido"));
        }
    }

    @Test
    public void listarProdutoComTamanhoInvalido() {

        Produto produtoListar = new Produto();
        produtoListar.setNome("banana pera");
        produtoListar.setMarca("banana");
        produtoListar.setTamanho("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.listar(produtoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("tamanho_invalido"));
        }
    }

    @Test
    public void listarProdutoComUsuarioInvalido() {

        Produto produtoListar = new Produto();
        produtoListar.setNome("banana pera");
        produtoListar.setMarca("banana");
        produtoListar.setTamanho("pequeno");
        produtoListar.setCriadoPor(0);

        try {
            produtoController.listar(produtoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void listarProdutoComCategoriaInvalida() {

        Produto produtoListar = new Produto();
        produtoListar.setNome("banana pera");
        produtoListar.setMarca("banana");
        produtoListar.setTamanho("pequeno");
        produtoListar.setCriadoPor(1);
        produtoListar.setCategoriaId(0);

        try {
            produtoController.listar(produtoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("categoria_invalido"));
        }
    }

    @Test
    public void editarProdutoComSucesso() {

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

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("Produto Teste Editado");
        produtoEditar.setCategoriaId(idCategoria);

        ResponseProduto responseProduto = produtoController.editar(idProduto, produtoEditar);

        assertTrue(responseProduto != null && responseProduto.getId() != null && responseProduto.getId() > 0);
    }

    @Test
    public void editarProdutoComCorpoNulo() {

        try {
            produtoController.editar(1, null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void editarProdutoComIdFornecido() {

        Produto produtoEditar = new Produto();
        produtoEditar.setId(1);

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void editarProdutoComCorInvalida() {

        Produto produtoEditar = new Produto();
        produtoEditar.setCor("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("cor_invalido"));
        }
    }

    @Test
    public void editarProdutoComNomeInvalido() {

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("uva");

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("nome_invalido"));
        }
    }

    @Test
    public void editarProdutoComMarcaInvalida() {

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("banana pera");
        produtoEditar.setMarca("a");

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("marca_invalido"));
        }
    }

    @Test
    public void editarProdutoComTamanhoInvalido() {

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("banana pera");
        produtoEditar.setMarca("banana");
        produtoEditar.setTamanho("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("tamanho_invalido"));
        }
    }

    @Test
    public void editarProdutoComUsuarioInvalido() {

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("banana pera");
        produtoEditar.setMarca("banana");
        produtoEditar.setTamanho("pequeno");
        produtoEditar.setCriadoPor(0);

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void editarProdutoComCategoriaInvalida() {

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("banana pera");
        produtoEditar.setMarca("banana");
        produtoEditar.setTamanho("pequeno");
        produtoEditar.setCriadoPor(1);
        produtoEditar.setCategoriaId(0);

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("categoria_invalido"));
        }
    }

    @Test
    public void editarProdutoComProdutoExistente() {

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

        produtoRepository.save(produtoCriar).getId();

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("Produto Teste");
        produtoEditar.setMarca("Marca Teste");
        produtoEditar.setCor("Cor Teste");
        produtoEditar.setTamanho("Tamanho Teste");
        produtoEditar.setCriadoPor(idUsuario);
        produtoEditar.setCategoriaId(idCategoria);

        try {
            produtoController.editar(1, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("produto_existente"));
        }
    }

    @Test
    public void editarProdutoComExcecaoCategoriaNaoEncontrado() {

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

        Produto produtoEditar = new Produto();
        produtoEditar.setNome("Produto Teste 2");
        produtoEditar.setMarca("Marca Teste 2");
        produtoEditar.setCor("Cor Teste 2");
        produtoEditar.setTamanho("Tamanho Teste 2");
        produtoEditar.setCriadoPor(idUsuario);
        produtoEditar.setCategoriaId(1);

        try {
            produtoController.editar(idProduto, produtoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("categoria_nao_encontrado"));
        }
    }

    @Test
    public void atualizarProdutoComSucesso() {

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

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("Produto Teste 2");
        produtoAtualizar.setMarca("Marca Teste 2");
        produtoAtualizar.setCor("Cor Teste 2");
        produtoAtualizar.setTamanho("Tamanho Teste 2");
        produtoAtualizar.setCriadoPor(idUsuario);
        produtoAtualizar.setCategoriaId(idCategoria);

        ResponseProduto responseProduto = produtoController.atualizar(idProduto, produtoAtualizar);

        assertTrue(responseProduto != null && responseProduto.getId() != null && responseProduto.getId() > 0);
    }

    @Test
    public void atualizarProdutoComCorpoNulo() {

        try {
            produtoController.atualizar(1, null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void atualizarProdutoComIdFornecido() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setId(1);

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void atualizarProdutoComCorInvalida() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setCor("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("cor_invalido"));
        }
    }

    @Test
    public void atualizarProdutoComNomeInvalido() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("uva");

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("nome_invalido"));
        }
    }

    @Test
    public void atualizarProdutoComMarcaInvalida() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("banana pera");
        produtoAtualizar.setMarca("a");

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("marca_invalido"));
        }
    }

    @Test
    public void atualizarProdutoComTamanhoInvalido() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("banana pera");
        produtoAtualizar.setMarca("banana");
        produtoAtualizar.setTamanho("lorem ipsum dolor sit amet consectetur adipiscing elit");

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("tamanho_invalido"));
        }
    }

    @Test
    public void atualizarProdutoComUsuarioInvalido() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("banana pera");
        produtoAtualizar.setMarca("banana");
        produtoAtualizar.setTamanho("pequeno");
        produtoAtualizar.setCriadoPor(0);

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void atualizarProdutoComCategoriaInvalida() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("banana pera");
        produtoAtualizar.setMarca("banana");
        produtoAtualizar.setTamanho("pequeno");
        produtoAtualizar.setCriadoPor(1);
        produtoAtualizar.setCategoriaId(0);

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("categoria_invalido"));
        }
    }

    @Test
    public void atualizarProdutoComUsuarioNaoEncontrado() {

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("banana pera");
        produtoAtualizar.setMarca("banana");
        produtoAtualizar.setTamanho("pequeno");
        produtoAtualizar.setCriadoPor(1);
        produtoAtualizar.setCategoriaId(1);

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_nao_encontrado"));
        }
    }

    @Test
    public void atualizarProdutoComCategoriaNaoEncontrada() {

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

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("banana pera");
        produtoAtualizar.setMarca("banana");
        produtoAtualizar.setTamanho("pequeno");
        produtoAtualizar.setCriadoPor(idUsuario);
        produtoAtualizar.setCategoriaId(1);

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("categoria_nao_encontrado"));
        }
    }

    @Test
    public void atualizarProdutoComProdutoExistente() {

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
        produtoCriar.setNome("Produto Teste 1");
        produtoCriar.setMarca("Marca Teste 1");
        produtoCriar.setCor("Cor Teste 1");
        produtoCriar.setTamanho("Tamanho Teste 1");
        produtoCriar.setCriadoPor(idUsuario);
        produtoCriar.setCategoriaId(idCategoria);

        produtoRepository.save(produtoCriar);

        Produto produtoAtualizar = new Produto();
        produtoAtualizar.setNome("Produto Teste 1");
        produtoAtualizar.setMarca("Marca Teste 1");
        produtoAtualizar.setCor("Cor Teste 1");
        produtoAtualizar.setTamanho("Tamanho Teste 1");
        produtoAtualizar.setCriadoPor(idUsuario);
        produtoAtualizar.setCategoriaId(idCategoria);

        try {
            produtoController.atualizar(1, produtoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("produto_existente"));
        }
    }

    @Test
    public void removerProdutoComSucesso() {

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

        int quantidadeProdutosAntes = produtoRepository.findAll().size();

        produtoController.remover(idProduto);

        int quantidadeProdutosDepois = produtoRepository.findAll().size();
        
        assertTrue(quantidadeProdutosAntes == quantidadeProdutosDepois + 1);
    }

    @Test
    public void removerProdutoComExcecaoNoSuchElement() {

        try {
            produtoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("NoSuchElementException"));
        }
    }
}
