package br.com.e2etreinamentos.meu.sistema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.e2etreinamentos.meu.sistema.model.Produto;
import br.com.e2etreinamentos.meu.sistema.repository.ProdutoRepository;
import jakarta.validation.Validator;


@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

	@Mock
	private ProdutoRepository repository;
	
	@InjectMocks
	private ProdutoService service;
	
	private Validator validator;
	
	@Test
	void deveCadastrarProduto() {
		Produto produto = new Produto();
		produto.setNome("Iphone");
		produto.setQuantidade(10);
		produto.setPreco(1000.0);
		
		when(repository.save(produto)).thenReturn(produto);
				
		Produto salvo = service.salvar(produto);
		assertEquals("Iphone", salvo.getNome());	
	}
	
	
}
