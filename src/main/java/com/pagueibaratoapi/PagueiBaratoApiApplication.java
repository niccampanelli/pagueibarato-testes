package com.pagueibaratoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * <H1>PagueiBaratoAPI</H1>
 * <P>API RESTful para facilitar a comparação de preços de produtos no varejo. Construída com Spring Boot.</P>
 * made with ❤ in 🇧🇷
 * @author Lucas Campanelli de Souza
 * @author Nicholas Campanelli de Souza
 * @version 1.0.0
 * @see <a href="https://github.com/lucascampanelli/paguei-barato-api">Repositório da API no GitHub</a>
 */
@EnableCaching
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PagueiBaratoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagueiBaratoApiApplication.class, args);
	}

	@CacheEvict(value = "categorias", allEntries = true)
	@Scheduled(fixedDelay = 7200000, initialDelay = 7200000)
	public void limparCacheCategoria(){}

	@CacheEvict(value = "estoques", allEntries = true)
	@Scheduled(fixedDelay = 7200000, initialDelay = 7200000)
	public void limparCacheEstoque(){}

	@CacheEvict(value = "mercados", allEntries = true)
	@Scheduled(fixedDelay = 7200000, initialDelay = 7200000)
	public void limparCacheMercado(){}

	@CacheEvict(value = "mercadoProdutos", allEntries = true)
	@Scheduled(fixedDelay = 3600000, initialDelay = 3600000)
	public void limparCacheMercadoProdutos(){}

	@CacheEvict(value = "mercadoSugestoes", allEntries = true)
	@Scheduled(fixedDelay = 600000, initialDelay = 600000)
	public void limparCacheMercadoSugestoes(){}

	@CacheEvict(value = "produtos", allEntries = true)
	@Scheduled(fixedDelay = 600000, initialDelay = 600000)
	public void limparCacheProdutos(){}

	@CacheEvict(value = "ramos", allEntries = true)
	@Scheduled(fixedDelay = 7200000, initialDelay = 7200000)
	public void limparCacheRamo(){}

	@CacheEvict(value = "sugestoes", allEntries = true)
	@Scheduled(fixedDelay = 7200000, initialDelay = 7200000)
	public void limparCacheSugestao(){}
}
