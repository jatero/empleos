package net.itinajero.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.itinajero.model.Solicitud;
import net.itinajero.service.ISolicitudesService;
import net.itinajero.service.IUsuariosService;
import net.itinajero.service.IVacantesService;
import net.itinajero.util.Utileria;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {
	
	@Autowired
	private IVacantesService serviceVancates;
	
	@Autowired
	private ISolicitudesService serviceSolicitudes;
	
	@Autowired
	private IUsuariosService serviceUsuarios;
	
	/**
	 * EJERCICIO: Declarar esta propiedad en el archivo application.properties. El valor sera el directorio
	 * en donde se guardarán los archivos de los Curriculums Vitaes de los usuarios.
	 */
	@Value("${empleosapp.ruta.cv}")
	private String ruta;
		
    /**
	 * Metodo que muestra la lista de solicitudes sin paginacion
	 * Seguridad: Solo disponible para un usuarios con perfil ADMINISTRADOR/SUPERVISOR.
	 * @return
	 */
    @GetMapping("/index") 
	public String mostrarIndex(Model model) {
    	// EJERCICIO
    	model.addAttribute("solicitudes", serviceSolicitudes.buscarTodas());
		return "solicitudes/listSolicitudes";
		
	}
    
    /**
	 * Metodo que muestra la lista de solicitudes con paginacion
	 * Seguridad: Solo disponible para usuarios con perfil ADMINISTRADOR/SUPERVISOR.
	 * @return
	 */
	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		model.addAttribute("solicitudes", serviceSolicitudes.buscarTodas(page));
		// EJERCICIO
		return "solicitudes/listSolicitudes";
		
	}
    
	/**
	 * Método para renderizar el formulario para aplicar para una Vacante
	 * Seguridad: Solo disponible para un usuario con perfil USUARIO.
	 * @return
	 */
	@GetMapping("/create/{idVacante}")
	public String crear(Solicitud solicitud, @PathVariable("idVacante") int idVacante, Model model) {
		model.addAttribute("vacante", serviceVancates.buscarPorId(idVacante));
		// EJERCICIO
		return "solicitudes/formSolicitud";
		
	}
	
	/**
	 * Método que guarda la solicitud enviada por el usuario en la base de datos
	 * Seguridad: Solo disponible para un usuario con perfil USUARIO.
	 * @return
	 */
	@PostMapping("/save")
	public String guardar(Solicitud solicitud, Authentication auth, BindingResult result, RedirectAttributes attributes, @RequestParam("archivoCV") MultipartFile multiPart) {	
		// EJERCICIO
		if (result.hasErrors()) {
			for (ObjectError error: result.getAllErrors()){
				System.out.println("Ocurrio un error: "+ error.getDefaultMessage());
			}			
			return "solicitudes/formSolicitud";
		}
		
		if (!multiPart.isEmpty()) {
			String nombreCV = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreCV != null) {
				solicitud.setArchivo(nombreCV);
			}
		}else {
			attributes.addFlashAttribute("msg", "No se ha podido recuperar el curriculum!");
			return "solicitudes/formSolicitud";
		}

		//solicitud.setVacante(serviceVancates.buscarPorId(idVacante));
		solicitud.setFecha(new Date());
		solicitud.setUsuario(serviceUsuarios.buscarPorUsername(auth.getName()));
		serviceSolicitudes.guardar(solicitud);
		attributes.addFlashAttribute("msg", "Gracias por enviar tu CV!");
		return "redirect:/";	
		
	}
	
	/**
	 * Método para eliminar una solicitud
	 * Seguridad: Solo disponible para usuarios con perfil ADMINISTRADOR/SUPERVISOR. 
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idSolicitud, RedirectAttributes attributes) {
		
		// Eliminamos la solicitud.
		serviceSolicitudes.eliminar(idSolicitud);
			
		attributes.addFlashAttribute("msg", "La solicitud fue eliminada!.");
		//return "redirect:/solicitudes/index";
		return "redirect:/solicitudes/indexPaginate";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idSolicitud, Model model) {
		Solicitud solicitud = serviceSolicitudes.buscarPorId(idSolicitud);
		model.addAttribute("solicitud", solicitud);
		return "solicitudes/formSolicitud";
	}
	
	/**
	 * Personalizamos el Data Binding para todas las propiedades de tipo Date
	 * @param webDataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
