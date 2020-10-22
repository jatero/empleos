package net.itinajero.service;

import net.itinajero.model.Perfil;

public interface IPerfilesService{
	Perfil buscarPorId(Integer idPerfil);
	Perfil buscarPorNombre(String nombre);
}
