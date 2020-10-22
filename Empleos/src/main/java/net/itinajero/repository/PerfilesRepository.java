package net.itinajero.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.itinajero.model.Perfil;

public interface PerfilesRepository extends JpaRepository<Perfil, Integer> {
	
	Optional<Perfil> findByPerfil(String perfil);

}
