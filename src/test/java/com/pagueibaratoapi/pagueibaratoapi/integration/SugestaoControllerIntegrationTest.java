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

import com.pagueibaratoapi.controllers.SugestaoController;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.requests.Estoque;
import com.pagueibaratoapi.models.requests.Mercado;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Ramo;
import com.pagueibaratoapi.models.requests.Sugestao;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseSugestao;
import com.pagueibaratoapi.pagueibaratoapi.PagueiBaratoApiApplicationTests;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.RamoRepository;
import com.pagueibaratoapi.repository.SugestaoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;



@RunWith(SpringRunner.class)
public class SugestaoControllerIntegrationTest extends PagueiBaratoApiApplicationTests {

    @Autowired
    private SugestaoController sugestaoController;

    @Autowired
    private SugestaoRepository sugestaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RamoRepository ramoRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Before
    public void setUp() {
        sugestaoRepository.deleteAll();
        usuarioRepository.deleteAll();
        categoriaRepository.deleteAll();
        produtoRepository.deleteAll();
        ramoRepository.deleteAll();
        mercadoRepository.deleteAll();
        estoqueRepository.deleteAll();
    }

    @Test
    public void criarSugestaoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setCriadoPor(idUsuario);
        sugestaoCriar.setEstoqueId(idEstoque);
        sugestaoCriar.setPreco(10.0f);

        ResponseSugestao responseSugestao = sugestaoController.criar(sugestaoCriar);

        assertTrue(responseSugestao != null && responseSugestao.getId() > 0);
    }

    @Test
    public void criarSugestaoComCorpoNulo() {

        try {
            sugestaoController.criar(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void criarSugestaoComIdFornecido() {

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setId(1);

        try {
            sugestaoController.criar(sugestaoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void criarSugestaoComPrecoInvalido() {

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setPreco(-1.0f);

        try {
            sugestaoController.criar(sugestaoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("preco_invalido"));
        }
    }

    @Test
    public void criarSugestaoComUsuarioInvalido() {

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setPreco(10.0f);
        sugestaoCriar.setCriadoPor(0);

        try {
            sugestaoController.criar(sugestaoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void criarSugestaoComEstoqueInvalido() {

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setPreco(10.0f);
        sugestaoCriar.setCriadoPor(1);
        sugestaoCriar.setEstoqueId(0);

        try {
            sugestaoController.criar(sugestaoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("estoque_invalido"));
        }
    }

    @Test
    public void criarSugestaoComEstoqueNaoEncontrado() {

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setPreco(10.0f);
        sugestaoCriar.setCriadoPor(1);
        sugestaoCriar.setEstoqueId(1);

        try {
            sugestaoController.criar(sugestaoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("estoque_nao_encontrado"));
        }
    }

    @Test
    public void criarSugestaoComUsuarioNaoEncontrado() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setCriadoPor(1);
        sugestaoCriar.setEstoqueId(idEstoque);
        sugestaoCriar.setPreco(10.0f);

        try {
            sugestaoController.criar(sugestaoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_nao_encontrado"));
        }
    }

    @Test
    public void criarSugestaoComExcecaoNoSuchElement() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setCriadoPor(idUsuario);
        sugestaoCriar.setEstoqueId(idEstoque);
        sugestaoCriar.setPreco(10.0f);

        try {
            sugestaoController.criar(sugestaoCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("NoSuchElementException"));
        }
    }

    @Test
    public void lerSugestaoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setCriadoPor(idUsuario);
        sugestaoCriar.setEstoqueId(idEstoque);
        sugestaoCriar.setPreco(10.0f);

        int idSugestao = sugestaoRepository.save(sugestaoCriar).getId();

        ResponseSugestao responseSugestao = sugestaoController.ler(idSugestao);

        assertTrue(responseSugestao != null && responseSugestao.getId() > 0);
    }

    @Test
    public void lerSugestaoComExcecaoNoSuchElement() {

        try {
            sugestaoController.ler(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("NoSuchElementException"));
        }
    }

    @Test
    public void lerSugestaoComExcecao() {

        try {
            sugestaoController.ler(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getStatus());
            assertTrue(e.getStatus().toString().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }
    }

    @Test
    public void listarSugestaoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setCriadoPor(idUsuario);
        sugestaoCriar.setEstoqueId(idEstoque);
        sugestaoCriar.setPreco(10.0f);

        sugestaoRepository.save(sugestaoCriar).getId();

        List<ResponseSugestao> responseSugestao = sugestaoController.listar(new Sugestao());

        assertTrue(responseSugestao != null && responseSugestao.size() > 0);
    }

    @Test
    public void listarSugestaoComCorpoNulo() {

        try {
            sugestaoController.listar(null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void listarSugestaoComIdFornecido() {

        Sugestao sugestaoListar = new Sugestao();
        sugestaoListar.setId(1);

        try {
            sugestaoController.listar(sugestaoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void listarSugestaoComPrecoInvalido() {

        Sugestao sugestaoListar = new Sugestao();
        sugestaoListar.setPreco(-1.0f);

        try {
            sugestaoController.listar(sugestaoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("preco_invalido"));
        }
    }

    @Test
    public void listarSugestaoComUsuarioInvalido() {

        Sugestao sugestaoListar = new Sugestao();
        sugestaoListar.setPreco(10.0f);
        sugestaoListar.setCriadoPor(0);

        try {
            sugestaoController.listar(sugestaoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void listarSugestaoComEstoqueInvalido() {

        Sugestao sugestaoListar = new Sugestao();
        sugestaoListar.setPreco(10.0f);
        sugestaoListar.setCriadoPor(1);
        sugestaoListar.setEstoqueId(0);

        try {
            sugestaoController.listar(sugestaoListar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("estoque_invalido"));
        }
    }

    @Test
    public void editarSugestaoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setCriadoPor(idUsuario);
        sugestaoCriar.setEstoqueId(idEstoque);
        sugestaoCriar.setPreco(10.0f);

        int idSugestao = sugestaoRepository.save(sugestaoCriar).getId();

        Sugestao sugestaoEditar = new Sugestao();
        sugestaoEditar.setPreco(20f);

        ResponseSugestao responseSugestao = sugestaoController.editar(idSugestao, sugestaoEditar);

        assertTrue(responseSugestao != null && responseSugestao.getId() > 0);
    }

    @Test
    public void editarSugestaoComCorpoNulo() {

        try {
            sugestaoController.editar(1, null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void editarSugestaoComIdFornecido() {

        Sugestao sugestaoEditar = new Sugestao();
        sugestaoEditar.setId(1);

        try {
            sugestaoController.editar(1, sugestaoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void editarSugestaoComPrecoInvalido() {

        Sugestao sugestaoEditar = new Sugestao();
        sugestaoEditar.setPreco(-1.0f);

        try {
            sugestaoController.editar(1, sugestaoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("preco_invalido"));
        }
    }

    @Test
    public void editarSugestaoComUsuarioInvalido() {

        Sugestao sugestaoEditar = new Sugestao();
        sugestaoEditar.setPreco(10.0f);
        sugestaoEditar.setCriadoPor(0);

        try {
            sugestaoController.editar(1, sugestaoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void editarSugestaoComEstoqueInvalido() {

        Sugestao sugestaoEditar = new Sugestao();
        sugestaoEditar.setPreco(10.0f);
        sugestaoEditar.setCriadoPor(1);
        sugestaoEditar.setEstoqueId(0);

        try {
            sugestaoController.editar(1, sugestaoEditar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("estoque_invalido"));
        }
    }

    @Test
    public void atualizarSugestaoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestaoCriar = new Sugestao();
        sugestaoCriar.setCriadoPor(idUsuario);
        sugestaoCriar.setEstoqueId(idEstoque);
        sugestaoCriar.setPreco(10.0f);

        int idSugestao = sugestaoRepository.save(sugestaoCriar).getId();

        Sugestao sugestaoAtualizar = new Sugestao();
        sugestaoAtualizar.setCriadoPor(idUsuario);
        sugestaoAtualizar.setEstoqueId(idEstoque);
        sugestaoAtualizar.setPreco(20.0f);

        ResponseSugestao responseSugestao = sugestaoController.atualizar(idSugestao, sugestaoAtualizar);

        assertTrue(responseSugestao != null && responseSugestao.getId() > 0);
    }

    @Test
    public void atualizarSugestaoComCorpoNulo() {

        try {
            sugestaoController.atualizar(1, null);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("corpo_nulo"));
        }
    }

    @Test
    public void atualizarSugestaoComIdFornecido() {

        Sugestao sugestaoAtualizar = new Sugestao();
        sugestaoAtualizar.setId(1);

        try {
            sugestaoController.atualizar(1, sugestaoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("id_fornecido"));
        }
    }

    @Test
    public void atualizarSugestaoComPrecoInvalido() {

        Sugestao sugestaoAtualizar = new Sugestao();
        sugestaoAtualizar.setPreco(-1.0f);

        try {
            sugestaoController.atualizar(1, sugestaoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("preco_invalido"));
        }
    }

    @Test
    public void atualizarSugestaoComUsuarioInvalido() {

        Sugestao sugestaoAtualizar = new Sugestao();
        sugestaoAtualizar.setPreco(10.0f);
        sugestaoAtualizar.setCriadoPor(0);

        try {
            sugestaoController.atualizar(1, sugestaoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void atualizarSugestaoComEstoqueInvalido() {

        Sugestao sugestaoAtualizar = new Sugestao();
        sugestaoAtualizar.setPreco(10.0f);
        sugestaoAtualizar.setCriadoPor(1);
        sugestaoAtualizar.setEstoqueId(0);

        try {
            sugestaoController.atualizar(1, sugestaoAtualizar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("estoque_invalido"));
        }
    }

    @Test
    public void atualizarSugestaoComExcecaoNoSuchElement() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestao = new Sugestao();
        sugestao.setCriadoPor(idUsuario);
        sugestao.setEstoqueId(idEstoque);
        sugestao.setPreco(10.0f);

        try {
            sugestaoController.atualizar(1, sugestao);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("NoSuchElementException"));
        }
    }

    @Test
    public void removerSugestaoComSucesso() {

        Usuario usuarioCriar = new Usuario();
        usuarioCriar.setNome("Teste");
        usuarioCriar.setEmail("teste@teste.com");
        usuarioCriar.setSenha("Senha!123456");
        usuarioCriar.setLogradouro("Rua Teste");
        usuarioCriar.setNumero(123);
        usuarioCriar.setBairro("Bairro Teste");
        usuarioCriar.setCidade("Cidade Teste");
        usuarioCriar.setUf("SP");
        usuarioCriar.setCep("00000-000");

        int idUsuario = usuarioRepository.save(usuarioCriar).getId();

        Ramo ramoCriar = new Ramo();
        ramoCriar.setNome("Ramo Teste");
        ramoCriar.setDescricao("Descrição Teste");

        int idRamo = ramoRepository.save(ramoCriar).getId();

        Mercado mercadoCriar = new Mercado();
        mercadoCriar.setNome("Mercado Teste");
        mercadoCriar.setRamoId(idRamo);
        mercadoCriar.setLogradouro("Rua Teste");
        mercadoCriar.setNumero(123);
        mercadoCriar.setBairro("Bairro Teste");
        mercadoCriar.setCidade("Cidade Teste");
        mercadoCriar.setUf("SP");
        mercadoCriar.setCep("00000-000");
        mercadoCriar.setCriadoPor(idUsuario);

        int idMercado = mercadoRepository.save(mercadoCriar).getId();

        Categoria categoriaCriar = new Categoria();
        categoriaCriar.setNome("Categoria Teste");
        categoriaCriar.setDescricao("Descrição Teste");

        int idCategoria = categoriaRepository.save(categoriaCriar).getId();

        Produto produtoCriar = new Produto();
        produtoCriar.setNome("Produto Teste");
        produtoCriar.setMarca("Marca Teste");
        produtoCriar.setCategoriaId(idCategoria);
        produtoCriar.setTamanho("Tamanho Teste");
        produtoCriar.setCor("Cor Teste");
        produtoCriar.setCriadoPor(idUsuario);

        int idProduto = produtoRepository.save(produtoCriar).getId();

        Estoque estoqueCriar = new Estoque();
        estoqueCriar.setCriadoPor(idUsuario);
        estoqueCriar.setMercadoId(idMercado);
        estoqueCriar.setProdutoId(idProduto);

        int idEstoque = estoqueRepository.save(estoqueCriar).getId();

        Sugestao sugestao = new Sugestao();
        sugestao.setCriadoPor(idUsuario);
        sugestao.setEstoqueId(idEstoque);
        sugestao.setPreco(10.0f);

        int idSugestao = sugestaoRepository.save(sugestao).getId();

        int quantidadeSugestoesAntes = sugestaoRepository.findAll().size();

        sugestaoController.remover(idSugestao);

        int quantidadeSugestoesDepois = sugestaoRepository.findAll().size();

        assertTrue(quantidadeSugestoesAntes == quantidadeSugestoesDepois + 1);
    }

    @Test
    public void removerSugestaoComExcecaoNoSuchElement() {

        try {
            sugestaoController.remover(1);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().toString());
            assertTrue(e.getCause().toString().contains("NoSuchElementException"));
        }
    }
}
