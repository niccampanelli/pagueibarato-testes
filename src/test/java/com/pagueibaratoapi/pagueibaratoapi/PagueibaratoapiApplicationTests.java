package com.pagueibaratoapi.pagueibaratoapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pagueibaratoapi.PagueiBaratoApiApplication;
import com.pagueibaratoapi.repository.CategoriaRepository;
import com.pagueibaratoapi.repository.EstoqueRepository;
import com.pagueibaratoapi.repository.MercadoRepository;
import com.pagueibaratoapi.repository.ProdutoRepository;
import com.pagueibaratoapi.repository.RamoRepository;
import com.pagueibaratoapi.repository.SugestaoRepository;
import com.pagueibaratoapi.repository.UsuarioRepository;

@SpringBootTest(classes = PagueiBaratoApiApplication.class)
public class PagueibaratoapiApplicationTests {

	@Test
	void contextLoads() {
	}

}
