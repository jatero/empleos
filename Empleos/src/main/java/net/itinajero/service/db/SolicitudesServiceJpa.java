package net.itinajero.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.itinajero.model.Solicitud;
import net.itinajero.repository.SolicitudesRepository;
import net.itinajero.service.ISolicitudesService;

@Service
public class SolicitudesServiceJpa implements ISolicitudesService {
	
	@Autowired
	private SolicitudesRepository solicitudesRepo;

	@Override
	public void guardar(Solicitud solicitud) {
		// TODO Auto-generated method stub
		solicitudesRepo.save(solicitud);
	}

	@Override
	public void eliminar(Integer idSolicitud) {
		// TODO Auto-generated method stub
		solicitudesRepo.deleteById(idSolicitud);
	}

	@Override
	public List<Solicitud> buscarTodas() {
		// TODO Auto-generated method stub
		return solicitudesRepo.findAll();
	}

	@Override
	public Solicitud buscarPorId(Integer idSolicitud) {
		// TODO Auto-generated method stub
		Optional<Solicitud> solicitud = solicitudesRepo.findById(idSolicitud);
		return solicitud.isPresent() ? solicitud.get() : null;
	}

	@Override
	public Page<Solicitud> buscarTodas(Pageable page) {
		// TODO Auto-generated method stub
		return solicitudesRepo.findAll(page);
	}	

}
