package uo.sdi.presentation;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import uo.sdi.dto.User;

@ManagedBean(name = "user")
@SessionScoped
public class BeanUser extends User implements Serializable {
	private static final long serialVersionUID = 55556L;
	
	public BeanUser() {
		iniciaUser(null);
	}

	// Este método es necesario para copiar el alumno a editar cuando
	// se pincha el enlace Editar en la vista listado.xhtml. Podría sustituirse
	// por un método editar en BeanAlumnos.
	public void setUser(User user) {
		setId(user.getId());
		setLogin(user.getLogin());
		setEmail(user.getEmail());
		setPassword(user.getPassword());
	}

	// Iniciamos los datos del alumno con los valores por defecto
	// extraídos del archivo de propiedades correspondiente
	public void iniciaUser(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		setId(null);
		setLogin(bundle.getString("valorDefectoNombre"));
		setEmail(bundle.getString("valorDefectoCorreo"));
		setPassword(bundle.getString("valorDefectoPassword"));
	}
	
	public User getUserBase() {
		User u = new User();
		u.setEmail(getEmail());
		u.setId(getId());
		u.setIsAdmin(getIsAdmin());
		u.setLogin(getLogin());
		u.setPassword(getPassword());
		u.setStatus(getStatus());
		return u;
	}
}
