package br.com.e2etreinamentos.meu.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.e2etreinamentos.meu.sistema.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
