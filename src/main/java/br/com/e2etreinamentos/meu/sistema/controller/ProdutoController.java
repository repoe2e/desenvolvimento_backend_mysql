package br.com.e2etreinamentos.meu.sistema.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.e2etreinamentos.meu.sistema.model.Produto;
import br.com.e2etreinamentos.meu.sistema.service.ProdutoService;

// Indica que está class é um controller que lida com requisições HTTP
@RestController

// Define a URL base para todos os endpoints deste controller. 
@RequestMapping("/api/produtos")
public class ProdutoController {
	
	private final ProdutoService service;
	
	public ProdutoController(ProdutoService service){
		this.service = service;
	}
		
	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Produto produto){
		
		return ResponseEntity.ok(service.salvar(produto));
	}	
}