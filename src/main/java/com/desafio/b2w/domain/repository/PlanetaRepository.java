package com.desafio.b2w.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio.b2w.domain.model.Planeta;

@Repository
public interface PlanetaRepository extends JpaRepository<Planeta, Long> {

	List<Planeta> findTodasByNameContainingIgnoreCase(String name);	

}
