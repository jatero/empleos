package net.itinajero.service.db;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import net.itinajero.model.Perfil;
import net.itinajero.repository.PerfilesRepository;
import net.itinajero.service.IPerfilesService;

public class PerfilesServiceJpa implements IPerfilesService{
	
	@Autowired
	private PerfilesRepository perfilesRepo;

	@Override
	public Perfil buscarPorId(Integer idPerfil) {
		Optional<Perfil> perfil = perfilesRepo.findById(idPerfil);
		return perfil.isPresent() ? perfil.get() : null;
	}

	@Override
	public Perfil buscarPorNombre(String nombre) {
		Optional<Perfil> perfil = perfilesRepo.findByPerfil(nombre);
		return perfil.isPresent() ? perfil.get() : null;
	}

}
