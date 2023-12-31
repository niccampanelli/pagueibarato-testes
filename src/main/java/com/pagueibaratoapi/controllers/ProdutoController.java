package com.pagueibaratoapi.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pagueibaratoapi.models.exceptions.DadosConflitantesException;
import com.pagueibaratoapi.models.exceptions.DadosInvalidosException;
import com.pagueibaratoapi.models.requests.Produto;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseProduto;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;
import com.pagueibaratoapi.utils.EditaRecurso;
import com.pagueibaratoapi.utils.Tratamento;

/**
 * Classe responsável por controlar as requisições de produtos.
 */
@RestController
@RequestMapping("/produto")
public class ProdutoController {

    // Repositórios responsável pelos métodos JPA dp banco de dados.
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    // Construtor
    public ProdutoController(
        CategoriaRepository categoriaRepository,
        ProdutoRepository produtoRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Rota responsável por criar um produto.
     * @param categoria - Dados do produto a ser criado.
     * @return Dados e id do produto criado.
     */
    @PostMapping
    @CacheEvict(value = "produtos", allEntries = true)
    public ResponseProduto criar(@RequestBody Produto requestProduto) {
        try {

            // Valida os dados fornecidos.
            Tratamento.validarProduto(requestProduto, false);

            // Se o usuário fornecido não existir,
            if(!usuarioRepository.existsById(requestProduto.getCriadoPor()))
                // Retorna um erro.
                throw new DadosInvalidosException("usuario_nao_encontrado");
            
            // Se a categoria fornecida não existir,
            if(!categoriaRepository.existsById(requestProduto.getCategoriaId()))
                // Retorna um erro.
                throw new DadosInvalidosException("categoria_nao_encontrado");

            // Se as características fornecidas pertencer a um produto já existente,
            if(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(), 
                requestProduto.getMarca(),
                requestProduto.getTamanho(), 
                requestProduto.getCor()) != null
            )
                // Retorna um erro.
                throw new DadosConflitantesException("produto_existente");

            // Buscando o usuário informado como criador do produto.
            Usuario usuario = usuarioRepository.findById(requestProduto.getCriadoPor()).get();

            // Verifica se o usuário informado como criador não foi deletado anteriormente.
            if(!Tratamento.usuarioExiste(usuario))
                // Retorna um erro.
                throw new NoSuchElementException("usuario_nao_encontrado");

            // Criando uma nova instância do produto para tratar o nome dele e criá-lo no banco.
            Produto produtoTratado = requestProduto;

            // Pegando cada palavra do nome do produto em minúsculas separado por espaço.
            String[] nomeProduto = requestProduto.getNome().toLowerCase().split(" ");

            // Percorrendo cada palavra do nome do produto
            for(int i = 0; i < nomeProduto.length; i++) {
                // Transformando a palavra atual em uma array
                char[] palavraArr = nomeProduto[i].toCharArray();

                // Transformando a primeira letra da palavra em maiúscula
                palavraArr[0] = nomeProduto[i].toUpperCase().charAt(0);

                // Reescreve a palavra atual com a primeira letra tratada
                nomeProduto[i] = String.valueOf(palavraArr);

                // Se for a primeira palavra sendo tratada,
                // substitui o nome do produto pelo nome tratado
                if(i < 1)
                    produtoTratado.setNome(nomeProduto[i]);
                // Se não, concatena a palavra atual ao nome do produto
                else
                    produtoTratado.setNome(produtoTratado.getNome() + " " + nomeProduto[i]);
            }

            // Insere o produto e transforma os dados obtidos em modelo de resposta.
            ResponseProduto responseProduto = new ResponseProduto(produtoRepository.save(produtoTratado));

            // Adiciona o link para o produto.
            responseProduto.add(
                linkTo(
                    methodOn(ProdutoController.class).ler(responseProduto.getId())
                )
                .withSelfRel()
            );

            // Retorna os dados do produto criado.
            return responseProduto;

        } catch (DadosConflitantesException e) {
            throw new ResponseStatusException(409, e.getMessage(), e);
        } catch(NoSuchElementException e) {
            throw new ResponseStatusException(404, e.getMessage(), e);
        } catch (DadosInvalidosException e) {
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(500, "erro_insercao", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Rota responsável por ler um produto.
     * @param id - Id do produto a ser lido.
     * @return Dados do produto.
     */
    @GetMapping("/{id}")
    public ResponseProduto ler(@PathVariable("id") Integer id) {
        try {

            // Busca o produto no banco e transforma os dados obtidos em modelo de resposta.
            ResponseProduto responseProduto = new ResponseProduto(produtoRepository.findById(id).get());

            // Adiciona o link para a rota de listagem de produtos.
            if(responseProduto != null) {
                responseProduto.add(
                    linkTo(
                        methodOn(ProdutoController.class).listar(new Produto())
                    )
                    .withRel("collection")
                );
            }

            // Retorna os dados do produto.
            return responseProduto;

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(404, "nao_encontrado", e);
        } catch (Exception e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Rota responsável por listar todos os produtos.
     * @param requestProduto - Dados de pesquisa para filtragem.
     * @return Lista de produtos.
     */
    @GetMapping
    @Cacheable("produtos")
    public List<ResponseProduto> listar(Produto requestProduto) {
        try {

            // Valida os dados de pesquisa.
            Tratamento.validarProduto(requestProduto, true);

            // Busca os produtos no banco semelhantes aos dados de pesquisa.
            // Se não houver dados de pesquisa, busca todos os produtos.
            List<Produto> produtos = produtoRepository.findAll(
                Example.of(
                    requestProduto, 
                    ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                )
            );

            // Lista de produtos que será retornada.
            List<ResponseProduto> responseProduto = new ArrayList<ResponseProduto>();

            // Adiciona os prdutos à lista.
            for(Produto produto : produtos) {
                responseProduto.add(new ResponseProduto(produto));
            }

            // Se houver produtos,
            if(responseProduto != null) {
                // Percorre os produtos,
                for(ResponseProduto produto : responseProduto) {
                    // Adiciona o link para a rota de detalhamento de produto.
                    produto.add(
                        linkTo(
                            methodOn(ProdutoController.class).ler(produto.getId())
                        )
                        .withSelfRel()
                    );
                }
            }

            return responseProduto;

        } catch (DadosInvalidosException e) {
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new ResponseStatusException(404, "nao_encontrado", e);
        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Rota responsável por editar um produto.
     * @param id - Id do produto a ser editado.
     * @param requestProduto - Dados modificados do produto.
     * @return Dados novos do produto editado.
     */
    @PatchMapping("/{id}")
    public ResponseProduto editar(@PathVariable int id, @RequestBody Produto requestProduto) {
        try {

            // Valida os dados fornecidos.
            Tratamento.validarProduto(requestProduto, true);

            // Se as características fornecidas pertencer a um produto já existente,
            if(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(), 
                requestProduto.getMarca(),
                requestProduto.getTamanho(), 
                requestProduto.getCor()) != null
            )
                // Retorna um erro.
                throw new DadosConflitantesException("produto_existente");

            // Busca o produto a ser editado.
            Produto produtoAtual = produtoRepository.findById(id).get();
            
            // Se a categoria informada não existir,
            if(!categoriaRepository.existsById(requestProduto.getCategoriaId()))
                // Retorna um erro.
                throw new DadosInvalidosException("categoria_nao_encontrado");

            // Chama o recurso de tratamento de edição de produto.
            // Insere os dados tratados no banco de dados.
            // Transforma os dados inseridos em resposta.
            ResponseProduto responseProduto = new ResponseProduto(
                produtoRepository.save(
                    EditaRecurso.editarProduto(
                        produtoAtual, 
                        requestProduto
                    )
                )
            );
    
            // Adiciona o link para a rota de detalhamento de produto.
            responseProduto.add(
                linkTo(
                    methodOn(ProdutoController.class).ler(responseProduto.getId())
                )
                .withSelfRel()
            );

            // Retorna a resposta.
            return responseProduto;

        } catch (DadosConflitantesException e) {
            throw new ResponseStatusException(409, e.getMessage(), e);
        } catch (DadosInvalidosException e) {
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(500, "erro_insercao", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(404, "nao_encontrado", e);
        } catch (Exception e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Rota responsável por substituir um produto.
     * @param id - Id do produto a ser substituído.
     * @param requestProduto - Dados do novo produto.
     * @return Dados do novo produto.
     */
    @PutMapping("/{id}")
    public ResponseProduto atualizar(@PathVariable int id, @RequestBody Produto requestProduto) {
        try {

            // Valida os dados fornecidos.
            Tratamento.validarProduto(requestProduto, false);

            // Se o usuário informado não existir,
            if(!usuarioRepository.existsById(requestProduto.getCriadoPor()))
                // Retorna um erro.
                throw new DadosInvalidosException("usuario_nao_encontrado");

            // Se a categoria informada não existir,
            if(!categoriaRepository.existsById(requestProduto.getCategoriaId()))
                // Retorna um erro.
                throw new DadosInvalidosException("categoria_nao_encontrado");

            // Se as características fornecidas pertencer a um produto já existente,
            if(produtoRepository.findByCaracteristicas(
                requestProduto.getNome(), 
                requestProduto.getMarca(),
                requestProduto.getTamanho(), 
                requestProduto.getCor()) != null
            )
                // Retorna um erro.
                throw new DadosConflitantesException("produto_existente");

            // Busca o produto a ser substituído.
            Produto produto = produtoRepository.findById(id).get();

            // Define o id do produto como o id fornecido.
            requestProduto.setId(id);

            // Define o id do usuário criador como o id atual do banco dedos, haja vista que não pode ser alterado.
            requestProduto.setCriadoPor(produto.getCriadoPor());
            
            // Insere o novo produto e transforma os dados inseridos em resposta.
            ResponseProduto responseProduto = new ResponseProduto(produtoRepository.save(requestProduto));

            // Adiciona o link para a rota de detalhamento de produto.
            responseProduto.add(
                linkTo(
                    methodOn(ProdutoController.class).ler(responseProduto.getId())
                )
                .withSelfRel()
            );

            // Retorna a resposta.
            return responseProduto;

        } catch (DadosConflitantesException e) {
            throw new ResponseStatusException(409, e.getMessage(), e);
        } catch (DadosInvalidosException e) {
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(404, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(500, "erro_insercao", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Rota responsável por excluir um produto.
     * @param id - Id do produto a ser excluído.
     */
    @DeleteMapping("/{id}")
    @CacheEvict(value = "produtos", allEntries = true)
    public Object remover(@PathVariable int id) {
        try {

            // Se o produto informado não existir,
            if(!produtoRepository.existsById(id))
                // Retorna um erro.
                throw new NoSuchElementException("nao_encontrado");

            // Exclui o produto.
            produtoRepository.deleteById(id);

            // Retorna um link para a rota de listagem de produtos.
            return linkTo(
                        methodOn(ProdutoController.class).listar(new Produto())
                    )
                    .withRel("collection");

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(404, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(500, "erro_remocao", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }
}
