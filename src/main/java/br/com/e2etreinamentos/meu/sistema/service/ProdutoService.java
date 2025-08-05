package br.com.e2etreinamentos.meu.sistema.service;

import org.springframework.stereotype.Service;

import br.com.e2etreinamentos.meu.sistema.model.Produto;
import br.com.e2etreinamentos.meu.sistema.repository.ProdutoRepository;

// Lógica de negócio da camada controller
@Service
public class ProdutoService {

	private final ProdutoRepository repository;

	public ProdutoService(ProdutoRepository repository) {
		this.repository = repository;
	}

	public Produto salvar(Produto produto) {
		return repository.save(produto);
	}
}