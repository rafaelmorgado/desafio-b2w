package com.desafio.b2w.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.b2w.domain.exception.EntidadeNaoEncontradaException;
import com.desafio.b2w.domain.model.Planeta;
import com.desafio.b2w.domain.repository.PlanetaRepository;
import com.desafio.b2w.domain.service.PlanetaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/planetas")
public class PlanetaController {
	
	@Autowired
	private PlanetaRepository planetaRepository;
	
	@Autowired
	private PlanetaService planetaService;
	
	
	@GetMapping
	public List<Planeta> listar() {
		return planetaRepository.findAll();
	}
	
	@GetMapping("/{planetaId}")
	public ResponseEntity<?> buscar(@PathVariable Long planetaId) {
		
		Optional<Planeta> planeta = planetaRepository.findById(planetaId);
		
		if (planeta.isPresent() ) {
			planetaRepository.findById(planetaId);
			return ResponseEntity.ok(planeta.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Não existe um cadastro de planeta com código %d.", planetaId));
	}
	
	@GetMapping("/por-nome/{planetaName}") 
	public List<Planeta> listarPlanetasPorNome(@PathVariable("planetaName") String name) { 
		return planetaRepository.findTodasByNameContainingIgnoreCase(name); 
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Planeta adicionar(@RequestBody Planeta planeta) {
		return planetaService.salvar(planeta);
	}
	
	@PutMapping("/{planetaId}")
	public ResponseEntity<Planeta> atualizar(@PathVariable Long planetaId, @RequestBody Planeta planeta) {
		
		Optional<Planeta> planetaAtual = planetaRepository.findById(planetaId);
		
		if (planetaAtual.isPresent()) {
			BeanUtils.copyProperties(planeta, planetaAtual.get(), "id");
			
			Planeta planetaSalvo = planetaService.salvar(planetaAtual.get());
			return ResponseEntity.ok(planetaSalvo);
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{planetaId}")
	public ResponseEntity<?> remover(@PathVariable Long planetaId) {
		try {
			planetaService.excluir(planetaId);
			return ResponseEntity.noContent().build();
		} catch (EntidadeNaoEncontradaException e) {
		    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PatchMapping("/{planetaId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long planetaId, 
			@RequestBody Map<String, Object> campos) {
		
		Optional<Planeta> planetaAtual = planetaRepository.findById(planetaId);
		
		if (planetaAtual.get() == null) {
			return ResponseEntity.notFound().build();
		}
		
		merge(campos, planetaAtual.get());
		
		return atualizar(planetaId, planetaAtual.get());	
	}

	private void merge(Map<String, Object> dadosOrigem, Planeta planetaDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Planeta planetaOrigem = objectMapper.convertValue(dadosOrigem, Planeta.class);
		
		System.out.println(planetaOrigem);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			
			Field field = ReflectionUtils.findField(Planeta.class, nomePropriedade);
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, planetaOrigem);
			
			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			ReflectionUtils.setField(field, planetaDestino, novoValor); 
		});
	}
}