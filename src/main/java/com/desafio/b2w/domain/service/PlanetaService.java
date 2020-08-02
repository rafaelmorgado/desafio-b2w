package com.desafio.b2w.domain.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.desafio.b2w.domain.exception.EntidadeNaoEncontradaException;
import com.desafio.b2w.domain.model.Planeta;
import com.desafio.b2w.domain.repository.PlanetaRepository;

@Service
public class PlanetaService {

	@Autowired
	private PlanetaRepository planetaRepository;
	
	@Transactional
	public Planeta salvar(Planeta planeta) {
		Planeta planetaAtual = planetaRepository.save(planeta);
		List<Planeta> planetas = new ArrayList<Planeta>();
		planetas.add(planetaAtual);
		buscaTotalFilmes(planetas);
		return planetas.get(0);
	}
	
	@Transactional
	public void excluir(Long planetaId) {
		try {
			planetaRepository.deleteById(planetaId);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de planeta com código %d.", planetaId));
		}
	}

	
	private void buscaTotalFilmes(List<Planeta> planetas) throws JSONException {
		
		for (Planeta planeta: planetas) {
		
			RestTemplate restTemplate = new RestTemplate();
			String swapiUrl
			  = "https://swapi.dev/api/planets";
			String response
			  = restTemplate.getForEntity(swapiUrl + "/" + planeta.getId() + '/', String.class).getBody();
			
			JSONArray filmes = new JSONObject(response).getJSONArray("films");
			planeta.setTotalFilmes(filmes.length());
		}	
	}
	
}
