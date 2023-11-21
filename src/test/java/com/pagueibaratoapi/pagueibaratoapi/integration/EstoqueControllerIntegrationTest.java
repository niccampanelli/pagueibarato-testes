package com.pagueibaratoapi.pagueibaratoapi.integration;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagueibaratoapi.controllers.EstoqueController;
import com.pagueibaratoapi.models.requests.Categoria;
import com.pagueibaratoapi.models.requests.Estoque;
import com.pagueibaratoapi.models.requests.Mercado;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Ramo;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseEstoque;
import com.pagueibaratoapi.pagueibaratoapi.PagueibaratoapiApplicationTests;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.RamoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class EstoqueControllerIntegrationTest extends PagueibaratoapiApplicationTests {

    @Autowired
    private EstoqueController estoqueController;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    @Autowired
    private RamoRepository ramoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Before
    public void setUp() throws Exception {
        estoqueRepository.deleteAll();
        estoqueRepository.deleteAll();
        usuarioRepository.deleteAll();
        produtoRepository.deleteAll();
        mercadoRepository.deleteAll();
        ramoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void criarEstoqueComSucesso() throws Exception {

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

        ResponseEstoque response = estoqueController.criar(estoqueCriar);

        estoqueRepository.delete(estoqueCriar);

        assertTrue(response.getId() != null && response.getId() > 0);
    }

    @Test
    public void criarEstoqueComExcecaoDadosInvalidos() {

        try {
            estoqueController.criar(null);
        } catch (ResponseStatusException e) {
            assertTrue(e.getStatus().value() == 400);
        }
    }

    @Test
    public void criarEstoqueComUsuarioInvalido() {

        try {
            Estoque estoqueCriar = new Estoque();
            estoqueCriar.setCriadoPor(0);
            estoqueCriar.setMercadoId(1);
            estoqueCriar.setProdutoId(1);

            estoqueController.criar(estoqueCriar);
        } catch (ResponseStatusException e) {
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void criarEstoqueComUsuarioInexistente() {

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

        try {
            Estoque estoqueCriar = new Estoque();
            estoqueCriar.setCriadoPor(idUsuario);
            estoqueCriar.setMercadoId(1);
            estoqueCriar.setProdutoId(1);

            estoqueController.criar(estoqueCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("usuario_invalido"));
        }
    }

    @Test
    public void criarEstoqueComProdutoInvalido() {

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

        try {
            Estoque estoqueCriar = new Estoque();
            estoqueCriar.setCriadoPor(idUsuario);
            estoqueCriar.setMercadoId(1);
            estoqueCriar.setProdutoId(1);

            estoqueController.criar(estoqueCriar);
        } catch (ResponseStatusException e) {
            assertTrue(e.getCause().getMessage().equals("produto_invalido"));
        }
    }

    @Test
    public void criarEstoqueComMercadoInvalido() {

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

        try {
            Estoque estoqueCriar = new Estoque();
            estoqueCriar.setCriadoPor(idUsuario);
            estoqueCriar.setMercadoId(1);
            estoqueCriar.setProdutoId(idProduto);

            estoqueController.criar(estoqueCriar);
        } catch (ResponseStatusException e) {
            assertTrue(e.getCause().getMessage().equals("mercado_invalido"));
        }
    }

    @Test
    public void criarEstoqueComEstoqueExistente() {

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

        estoqueRepository.save(estoqueCriar);

        try {
            estoqueCriar.setId(null);

            estoqueController.criar(estoqueCriar);
        } catch (ResponseStatusException e) {
            System.out.println(e.getCause().getMessage());
            assertTrue(e.getCause().getMessage().equals("estoque_existente"));
        }

        estoqueRepository.delete(estoqueCriar);
    }

    @Test
    public void lerEstoqueComSucesso() {

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

        estoqueRepository.save(estoqueCriar);

        ResponseEstoque response = estoqueController.ler(estoqueCriar.getId());

        assertTrue(response != null && response.getId() != null && response.getId() > 0);

        estoqueRepository.delete(estoqueCriar);
    }

    @Test
    public void lerEstoqueComExcecaoNoSuchElement() {

        try {
            estoqueController.ler(1534);
        } catch (ResponseStatusException e) {
            assertTrue(e.getStatus().value() == 404);
        }
    }

    @Test
    public void lerEstoqueComExcecao() {

        try {
            estoqueController.ler(null);
        } catch (ResponseStatusException e) {
            assertTrue(e.getStatus().value() == 500);
        }
    }

    @Test
    public void listarEstoqueComSucesso() {

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

        estoqueRepository.save(estoqueCriar);

        List<ResponseEstoque> response = estoqueController.listar(new Estoque());

        assertTrue(response != null && response.size() > 0);
    }

    @Test
    public void listarEstoqueComExcecaoDadosInvalidos() {

        Estoque estoque = new Estoque();
        estoque.setId(1);

        try {
            estoqueController.listar(estoque);
        } catch (ResponseStatusException e) {
            assertTrue(e.getStatus().value() == 400);
        }
    }
    
    @Test
    public void removerEstoqueComSucesso() {

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

        estoqueRepository.save(estoqueCriar);

        int quantidadeEstoqueAntes = estoqueRepository.findAll().size();

        estoqueController.remover(estoqueCriar.getId());

        int quantidadeEstoqueDepois = estoqueRepository.findAll().size();

        assertTrue(quantidadeEstoqueAntes == quantidadeEstoqueDepois + 1);
    }
    
    @Test
    public void removerEstoqueComExcecaoNoSuchElement() {
        
        try {
            estoqueController.remover(1534);
        } catch (ResponseStatusException e) {
            assertTrue(e.getStatus().value() == 404);
        }
    }
}
