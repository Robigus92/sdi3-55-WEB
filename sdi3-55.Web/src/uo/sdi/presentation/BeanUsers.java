package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import alb.util.log.Log;
import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import uo.sdi.infrastructure.Factories;
import uo.sdi.presentation.util.Mensajes;


@ManagedBean(name = "controller")
@SessionScoped
public class BeanUsers implements Serializable {
	private static final long serialVersionUID = 55555L;
	// Se añade este atributo de entidad para recibir el usuario concreto
	// selecionado de la tabla o de un formulario
	// Es necesario inicializarlo para que al entrar desde el formulario de
	// AltaForm.xml se puedan
	// dejar los valores en un objeto existente.

	// uso de inyección de dependencia
	@ManagedProperty(value = "#{user}")
	private BeanUser user;
	
	private String message = "";
	private String result = "generic_result";

	public BeanUser getUser() {
		return user;
	}

	public void setUser(BeanUser user) {
		this.user = user;
	}

	private List<User> users = null;

	public List<User> getUsers() {
		return (users);
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	// Se inicia correctamente el MBean inyectado si JSF lo hubiera crea
	// y en caso contrario se crea. (hay que tener en cuenta que es un Bean
	// de sesión)
	// Se usa @PostConstruct, ya que en el contructor no se sabe todavía si
	// el ManagedBean ya estaba construido y en @PostConstruct SI.
	@PostConstruct
	public void init() {
		Log.info("BeanUsers - PostConstruct");
		// Buscamos el alumno en la sesión. Esto es un patrón factoría
		// claramente.
		user = (BeanUser) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get(new String("user"));
		// si no existe lo creamos e inicializamos
		if (user == null) {
			Log.info("BeanUsers - No existia");
			user = new BeanUser();
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("user", user);
		}
		
		listado();
	}

	@PreDestroy
	public void end() {
		Log.info("BeanUsers - PreDestroy");
	}

	public void iniciaUser(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		// Obtenemos el archivo de propiedades correspondiente al idioma que
		// tengamos seleccionado y que viene envuelto en facesContext
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		user.setId(null);
		user.setLogin(bundle.getString("valorDefectoNombre"));
		user.setEmail(bundle.getString("valorDefectoCorreo"));
		user.setPassword(bundle.getString("valorDefectoPassword"));
		Log.info("Initiating User with default values.");
	}
	
	private void updateListado() throws BusinessException{
		Services s = Factories.services;
		AdminService service = s.getAdminService();	
		
		users = service.findAllUsers();
		
		List<User> temp = new ArrayList<User>();
		temp.addAll(users);
		for (User user : users) {
			if(user.getIsAdmin()){
				temp.remove(user);
			}
		}
		
		users = temp;
	}

	public String listado() {
		try {
			updateListado();

			Log.info("Logged Admin asked for the Users lists.");
			return "exito"; // Nos vamos a la vista listado.xhtml

		} catch (Exception e) {
			Log.error("Error listing the User.");
			return "error"; // Nos vamos la vista de error
		}

	}

	public String baja(User user) {
		AdminService service;
		Services s = Factories.services;
		try {
			service = s.getAdminService();

			service.deepDeleteUser(user.getId());

			updateListado();
			
			Log.info("User "+user.getLogin()+" was successfully deleted.");
			Mensajes.info("Éxito", "mensaje_exito_user_eliminado");
			return "exito"; // Nos vamos a la vista de listado.

		} catch (Exception e) {
			Log.error("Error during User "+user.getLogin()+" deleting.");
			Mensajes.info("Error", "mensaje_error_user_eliminado");
			return "error"; // Nos vamos a la vista de error
		}

	}

	public String salva() {
		UserService service;
		Services s = Factories.services;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = s.getUserService();
			// Salvamos o actualizamos el alumno segun sea una operacion de alta
			// o de edici��n
			if (user.getId() == null) {
				service.registerUser(user.getUserBase());
				Log.info("User "+user.getLogin()+" was successfull saved.");
				Mensajes.info("Éxito", "mensaje_exito_user_creado");
			} else {
				service.updateUserDetails(user.getUserBase());
				Log.info("User "+user.getLogin()+" was successfull updated.");
				Mensajes.info("Éxito", "mensaje_exito_user_editado");
			}

			updateListado();
			return "exito"; // Nos vamos a la vista de listado.

		} catch (Exception e) {
			Log.error("Error trying to save-update the User "+user.getLogin()+".");
			return "error"; // Nos vamos a la vista de error.
		}

	}
	
	public String changeStatus(User user) {
		AdminService service;
		Services s = Factories.services;
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			service = s.getAdminService();
			
			if (user.getStatus() == UserStatus.ENABLED) {
				service.disableUser(user.getId());
				Mensajes.info("Éxito", "mensaje_exito_user_deshabilitado");
			} else {
				service.enableUser(user.getId());
				Mensajes.info("Éxito", "mensaje_exito_user_habilitado");
			}

			updateListado();
			
			Log.info("The status of user "+user.getLogin()+" was changed.");
			return "exito"; // Nos vamos a la vista de listado.
			

		} catch (Exception e) {
			Log.error("Error trying to change the status of user "+user.getLogin()+".");
			Mensajes.error("Error", "mensaje_error_user_estado");
			return "error"; // Nos vamos a la vista de error
		}		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String resetAndPopolateDB(){
		Services s = Factories.services;
		AdminService service  = s.getAdminService();
		Log.info("The Logged Admin asked for a DB Reset. Starting reset.");
		try {
			service.resetDB();
			updateListado();
			Mensajes.info("Éxito", "mensaje_exito_user_resetdb");
		} catch (Exception e) {
			Log.error("Error resetting the DB. ");
			Mensajes.error("Error", "mensaje_error_user_resetdb");
			return "error";
		}
		
		return "exito";
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
