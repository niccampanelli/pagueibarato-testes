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
import com.pagueibaratoapi.models.requests.Mercado;
import com.pagueibaratoapi.models.requests.Usuario;
import com.pagueibaratoapi.models.responses.ResponseMercado;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.RamoRepository;
import com.pagueibaratoapi.repository.SugestaoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;
import com.pagueibaratoapi.utils.EditaRecurso;
import com.pagueibaratoapi.utils.Tratamento;

/**
* Classe responsável por controlar as requisições de Mercado.
*/
@RestController
@RequestMapping("/mercado")
public class MercadoController {

    // Iniciando as variáveis de instância dos repositórios.
    private final MercadoRepository mercadoRepository;
    private final RamoRepository ramoRepository;
    private final UsuarioRepository usuarioRepository;

    // Construtor do controller do estoque, que realiza a injeção de dependência dos repositórios.
    public MercadoController(
        CategoriaRepository categoriaRepository,
        EstoqueRepository estoqueRepository,
        MercadoRepository mercadoRepository,
        ProdutoRepository produtoRepository,
        RamoRepository ramoRepository,
        SugestaoRepository sugestaoRepository,
        UsuarioRepository usuarioRepository
    ) {
        this.mercadoRepository = mercadoRepository;
        this.ramoRepository = ramoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Método responsável por criar um novo mercado.
     * @param requestMercado - Objeto do tipo Mercado que contém os dados do novo mercado.
     * @return ResponseMercado - Objeto do tipo ResponseMercado que contém os dados do novo mercado, já com o Id criado.
     */
    @PostMapping
    @CacheEvict(value = "mercados", allEntries = true)
    public ResponseMercado criar(@RequestBody Mercado requestMercado) {
        try {

            // Validando os dados do mercado enviados pelo cliente.
            Tratamento.validarMercado(requestMercado, false);

            // Verificando se o usuário informado existe.
            if(!usuarioRepository.existsById(requestMercado.getCriadoPor()))
                // Se não existir, lança exceção com mensagem de erro.
                throw new DadosInvalidosException("usuario_nao_encontrado");

            Usuario usuario = usuarioRepository.findById(requestMercado.getCriadoPor()).get();

            // Verificando se o usuário informado não foi deletado.
            if(!Tratamento.usuarioExiste(usuario))
                // Se o usuário foi deletado, lança exceção com mensagem de erro.
                throw new NoSuchElementException("usuario_nao_encontrado");

            // Verificando se o ramo informado existe.
            if(!ramoRepository.existsById(requestMercado.getRamoId()))
                // Se não existirm, lança exceção com mensagem de erro.
                throw new DadosInvalidosException("ramo_nao_encontrado");

            // Verificando se o nome do mercado informado já existe
            if(mercadoRepository.existsByNomeIgnoreCase(requestMercado.getNome()))
                throw new DadosConflitantesException("mercado_existente");

            // Verificando se o endereço do mercado informado já existe
            if(mercadoRepository.findByEndereco(
                requestMercado.getLogradouro(), 
                requestMercado.getNumero(),
                requestMercado.getComplemento(), 
                requestMercado.getBairro(), 
                requestMercado.getCidade(),
                requestMercado.getUf(), 
                requestMercado.getCep()) != null
            )
                // Se já existir, lança exceção com mensagem de erro, 
                // haja vista que não pode haver dois mercados no mesmo local.
                throw new DadosConflitantesException("mercado_existente");

            // Cria o mercado e armazena o retorno num objeto do tipo ResponseMercado.
            ResponseMercado responseMercado = new ResponseMercado(mercadoRepository.save(requestMercado));

            // Adiciona ao retorno um link para ler o mercado criado
            responseMercado.add(
                linkTo(
                    methodOn(MercadoController.class).ler(responseMercado.getId())
                )
                .withSelfRel()
            );

            // Retorna o objeto do tipo ResponseMercado com o mercado criado e o link para sua leitura.
            return responseMercado;

        } catch (DadosConflitantesException e) {
            // Lança uma exceção informando que os dados estão em conflito com outros dados no banco de dados.
            throw new ResponseStatusException(409, e.getMessage(), e);
        } catch(NoSuchElementException e) {
            // Lança uma exceção informando que algum registro informado não existe.
            throw new ResponseStatusException(404, e.getMessage(), e);
        } catch (DadosInvalidosException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são inválidos.
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são inválidos.
            throw new ResponseStatusException(500, "erro_insercao", e);
        } catch (IllegalArgumentException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são inválidos.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            // Lança uma exceção informando que ocorreu um erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Método responsável por ler um mercado com o id informado.
     * @param id - Id do mercado que será lido.
     * @return ResponseMercado - Objeto do tipo ResponseMercado que contém os dados do mercado lido.
     */
    @GetMapping("/{id}")
    public ResponseMercado ler(@PathVariable("id") Integer id) {
        try {
            // Busca o mercado com o id informado e armazena num objeto do tipo ResponseMercado.
            ResponseMercado responseMercado = new ResponseMercado(mercadoRepository.findById(id).get());

            // Se houver um mercado com o id informado
            if(responseMercado != null) {

                // Adiciona à resposta um link para listar todos os mercados
                responseMercado.add(
                    linkTo(
                        methodOn(MercadoController.class).listar(new Mercado())
                    )
                    .withRel("collection")
                );

            }

            // Retorna o objeto do tipo ResponseMercado com o mercado lido e o link para a listagem dos mercados
            return responseMercado;

        } catch (NoSuchElementException e) {
            // Lança uma exceção informando que o mercado com o id informado não foi encontrado.
            throw new ResponseStatusException(404, "nao_encontrado", e);
        } catch (Exception e) {
            // Lança uma exceção informando que ocorreu um erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Método responsável por listar todos os mercados.
     * @param mercado - Objeto do tipo Mercado que será usado como parâmetro para filtrar os resultados.
     * @return <b>List < ResponseMercado ></b> - Lista de mercados.
     */
    @GetMapping
    @Cacheable("mercados")
    public List<ResponseMercado> listar(Mercado requestMercado) {
        try {

            // Validando o mercado enviado como parâmetro pelo cliente.
            Tratamento.validarMercado(requestMercado, true);

            // Buscando todos os mercados de acordo com o filtro enviado por parâmetro e salvando na lista de mercados
            List<Mercado> mercados = mercadoRepository.findAll(
                Example.of(
                    requestMercado, 
                    ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                )
            );

            // Criando uma lista de respostas de mercados vazia.
            List<ResponseMercado> responseMercado = new ArrayList<ResponseMercado>();

            // Para cada mercado encontrado
            for(Mercado mercado : mercados) {
                // Cria um objeto do tipo ResponseMercado, convertendo o objeto Mercado para o objeto ResponseMercado
                responseMercado.add(new ResponseMercado(mercado));
            }

            // Se a lista de marcados a serem retornados não for vazia
            if(!responseMercado.isEmpty()) {
                // Para cada marcado da resposta
                for(ResponseMercado mercado : responseMercado) {
                    // Adiciona à resposta um link para a leitura do marcado em questão.
                    mercado.add(
                        linkTo(
                            methodOn(MercadoController.class).ler(mercado.getId())
                        )
                        .withSelfRel()
                    );
                }
            }

            // Retorna a lista de marcados encontrados.
            return responseMercado;

        } catch (DadosInvalidosException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são inválidos.
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (NullPointerException e) {
            // Lança uma exceção caso algum registro seja nulo
            throw new ResponseStatusException(404, "nao_encontrado", e);
        } catch (UnsupportedOperationException e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Método responsável por atualizar alguns atributos específicos de um mercado.
     * @param id - Id do mercado que será atualizado.
     * @param requestMercado - Parâmetros que serão atualizados no mercado.
     * @return <b>ResponseMercado</b> - Mercado atualizado.
     */
    @PatchMapping("/{id}")
    public ResponseMercado editar(@PathVariable int id, @RequestBody Mercado requestMercado) {
        try {

            // Validando os parâmetros enviados pelo cliente.
            Tratamento.validarMercado(requestMercado, true);

            // Buscando um mercado que possua o mesmo endereço informado
            if(mercadoRepository.findByEndereco(
                requestMercado.getLogradouro(), 
                requestMercado.getNumero(),
                requestMercado.getComplemento(), 
                requestMercado.getBairro(), 
                requestMercado.getCidade(),
                requestMercado.getUf(), 
                requestMercado.getCep()) != null
            )
                // Lança uma exceção informando que o endereço já existe.
                throw new DadosConflitantesException("mercado_existente");
            
            // Buscando o mercado que será atualizado e guardando seu estado atual.
            Mercado mercadoAtual = mercadoRepository.findById(id).get();

            // Se o ramo informado não existir
            if(!ramoRepository.existsById(requestMercado.getRamoId()))
                // Lança uma exceção informando que o ramo informado não existe.
                throw new DadosInvalidosException("ramo_nao_encontrado");

            // Se o nome do mercado já existir
            if(mercadoRepository.existsByNomeIgnoreCase(requestMercado.getNome()))
                // Lança uma exceção informando que o nome do mercado já existe.
                throw new DadosConflitantesException("mercado_existente");

            // Atualizando o mercado com os dados enviados pelo cliente.
            ResponseMercado responseMercado = new ResponseMercado(
                mercadoRepository.save(
                    // Chamando método que retornará o mercado com os dados atualizados de acordo com os parâmetros recebidos.
                    EditaRecurso.editarMercado(
                        mercadoAtual, 
                        requestMercado
                    )
                )
            );

            // Adiciona à resposta um link para a leitura do mercado em questão.
            responseMercado.add(
                linkTo(
                    methodOn(MercadoController.class).ler(responseMercado.getId())
                )
                .withSelfRel()
            );

            // Retorna o mercado atualizado.
            return responseMercado;

        } catch (DadosConflitantesException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são conflitantes/já existem no banco de dados.
            throw new ResponseStatusException(409, e.getMessage(), e);
        } catch (DadosInvalidosException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são inválidos.
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            // Lança uma exceção informando que ocorreu um erro de integridade de dados.
            throw new ResponseStatusException(500, "erro_insercao", e);
        } catch (IllegalArgumentException e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (NoSuchElementException e) {
            // Lança uma exceção caso algum registro não seja encontrado.
            throw new ResponseStatusException(404, "nao_encontrado", e);
        } catch (Exception e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Método responsável por atualizar todos os atributos de um mercado.
     * @param id - Id do mercado que será atualizado.
     * @param requestMercado - Parâmetros que serão atualizados no mercado.
     * @return <b>ResponseMercado</b> - Mercado atualizado.
     */
    @PutMapping("/{id}")
    public ResponseMercado atualizar(@PathVariable int id, @RequestBody Mercado requestMercado) {
        try {

            // Validando os parâmetros enviados pelo cliente.
            Tratamento.validarMercado(requestMercado, false);

            // Se o id do mercado informado não existir
            if(!mercadoRepository.existsById(id))
                // Lança uma exceção informando que o mercado não existe.
                throw new NoSuchElementException("nao_encontrado");

            // Se o id do usuário informado não existir
            if(!usuarioRepository.existsById(requestMercado.getCriadoPor()))
                // Lança uma exceção informando que o usuário não existe.
                throw new DadosInvalidosException("usuario_nao_encontrado");

            // Se o id do ramo informado não existir
            if(!ramoRepository.existsById(requestMercado.getRamoId()))
                // Lança uma exceção informando que o ramo informado não existe.
                throw new DadosInvalidosException("ramo_nao_encontrado");

            // Se o nome do mercado já existir no banco de dados
            if(mercadoRepository.existsByNomeIgnoreCase(requestMercado.getNome()))
                // Lança uma exceção informando que o nome do mercado já existe.
                throw new DadosConflitantesException("mercado_existente");

            // Se o endereço do mercado já existir no banco de dados
            if(mercadoRepository.findByEndereco(
                requestMercado.getLogradouro(), 
                requestMercado.getNumero(),
                requestMercado.getComplemento(), 
                requestMercado.getBairro(), 
                requestMercado.getCidade(),
                requestMercado.getUf(), 
                requestMercado.getCep()) != null
            )
                // Lança uma exceção informando que o endereço já existe.
                throw new DadosConflitantesException("mercado_existente");

            Mercado mercado = mercadoRepository.findById(id).get();

            // Adiciona ao objeto da requisição o Id informado pelo cliente.
            requestMercado.setId(id);

            // Adiciona ao objeto da requisição o Id do usuário que criou o mercado.
            requestMercado.setCriadoPor(mercado.getCriadoPor());

            // Atualizando o mercado com os dados enviados pelo cliente e armazenando no objeto responseMercado.
            ResponseMercado responseMercado = new ResponseMercado(mercadoRepository.save(requestMercado));

            // Adiciona à resposta um link para a leitura do mercado em questão.
            responseMercado.add(
                linkTo(
                    methodOn(MercadoController.class).ler(responseMercado.getId())
                )
                .withSelfRel()
            );

            // Retorna o mercado atualizado.
            return responseMercado;

        } catch (DadosConflitantesException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são conflitantes/já existem no banco de dados.
            throw new ResponseStatusException(409, e.getMessage(), e);
        } catch (DadosInvalidosException e) {
            // Lança uma exceção informando que os dados enviados pelo cliente são inválidos.
            throw new ResponseStatusException(400, e.getMessage(), e);
        } catch (NoSuchElementException e) {
            // Lança uma exceção informando que algum registro não foi encontrado.
            throw new ResponseStatusException(404, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            // Lança uma exceção informando que ocorreu um erro de integridade de dados.
            throw new ResponseStatusException(500, "erro_insercao", e);
        } catch (IllegalArgumentException e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }

    /**
     * Método responsável por deletar um mercado.
     * @param id - Id do mercado que será deletado.
     * @return Object - Link para a listagem de mercados.
     */
    @DeleteMapping("/{id}")
    @CacheEvict(value = "mercados", allEntries = true)
    public Object remover(@PathVariable int id) {
        try {

            // Se o id do mercado informado não existir
            if(!mercadoRepository.existsById(id))
                // Lança uma exceção informando que o mercado não existe.
                throw new NoSuchElementException("nao_encontrado");

            // Deleta o mercado com o id informado.
            mercadoRepository.deleteById(id);

            // Retorna o link para a listagem de mercados.
            return linkTo(
                        methodOn(MercadoController.class).listar(new Mercado())
                    )
                    .withRel("collection");

        } catch (NoSuchElementException e) {
            // Lança uma exceção informando que o mercado não existe.
            throw new ResponseStatusException(404, e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            // Lança uma exceção informando que ocorreu um erro de integridade de dados.
            throw new ResponseStatusException(500, "erro_remocao", e);
        } catch (IllegalArgumentException e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        } catch (Exception e) {
            // Lança uma exceção caso ocorra algum erro inesperado.
            throw new ResponseStatusException(500, "erro_inesperado", e);
        }
    }
}
