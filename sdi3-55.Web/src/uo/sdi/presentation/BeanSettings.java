package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import alb.util.log.Log;

@ManagedBean(name = "settings")
@SessionScoped
public class BeanSettings implements Serializable {
	private static final long serialVersionUID = 2L;
	private static final Locale ENGLISH = new Locale("en");
	private static final Locale SPANISH = new Locale("es");
	private static final Locale ITALIAN = new Locale("it");
	private Locale locale = new Locale("es");
	private String result = "generic_result";

	// uso de inyección de dependencia
	@ManagedProperty(value = "#{user}")
	private BeanUser user;

	public BeanUser getUser() {
		return user;
	}

	public void setUser(BeanUser user) {
		this.user = user;
	}

	public Locale getLocale() {
		// Aqui habria que cambiar algo de código para coger locale del
		// navegador
		// la primera vez que se accede a getLocale(), de momento dejamos como
		// idioma de partida “es”
		return (locale);
	}

	public void setSpanish(ActionEvent event) {
		locale = SPANISH;
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
//			if (user != null)
//				user.iniciaUser(null);
		} catch (Exception ex) {
			Log.error(ex.getMessage());
		}
	}

	public void setEnglish(ActionEvent event) {
		locale = ENGLISH;
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
//			if (user != null)
//				user.iniciaUser(null);
		} catch (Exception ex) {
			Log.error(ex.getMessage());
		}
	}
	
	public void setItalian(ActionEvent event){
		locale = ITALIAN;
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
//			if (user != null)
//				user.iniciaUser(null);
		} catch (Exception ex) {
			Log.error(ex.getMessage());
		}		
	}

	// Se inicia correctamente el Managed Bean inyectado si JSF lo hubiera
	// creado
	// y en caso contrario se crea.
	// (hay que tener en cuenta que es un Bean de sesión)
	// Se usa @PostConstruct, ya que en el contructor no se sabe todavía si
	// el MBean ya estaba construido y en @PostConstruct SI.
	@PostConstruct
	public void init() {
		Log.info("BeanSettings - PostConstruct");
		// Buscamos el alumno en la sesión. Esto es un patrón factoría
		// claramente.
		user = (BeanUser) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get(new String("user"));
		// si no existe lo creamos e inicializamos
		if (user == null) {
			Log.info("BeanSettings - No existia");
			user = new BeanUser();
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("user", user);
		}
	}

	// Es sólo a modo de traza.
	@PreDestroy
	public void end() {
		Log.info("BeanSettings - PreDestroy");
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
		
	
}