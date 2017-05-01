package uo.sdi.presentation;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import alb.util.log.Log;
import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import uo.sdi.infrastructure.Factories;
import uo.sdi.presentation.util.Mensajes;

@ManagedBean(name = "register")
@SessionScoped
public class BeanRegister implements Serializable  {
	private static final long serialVersionUID = 7L;
	private String name = "";
	private String password = "";
	private String password2 = "";
	private String email = "";
	private String result = "register_form_result_valid";
	
	public BeanRegister(){
		Log.info("BeanRegister - No existia");
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String save(){
		return salva();
	}
	
	public String salva() {
		UserService service;
		User user = new User();
		Services s = Factories.services;
		
		user.setLogin(name);
		user.setIsAdmin(false);
		user.setPassword(password);
		user.setStatus(UserStatus.ENABLED);
		user.setEmail(email);
		Log.info("Starting Creation User '"+name+"' process");
		
		if(!password.equals(password2)) {
			setResult("register_form_result_incorrect");
			Log.error("Passwords are different");
			return "error";
		}
		
		try {
			service = s.getUserService();
			if (user.getId() == null) {
				service.registerUser(user);
			} else {
				service.updateUserDetails(user);
			}
			// Actualizamos el javabean de alumnos inyectado en la tabla
			// users = Services.getAdminService().findAllUsers();
			
			Log.info("User successfully created");
			Mensajes.info("Éxito", "mensaje_exito_registro");
			return "exito"; // Nos vamos a la vista de listado.

		} catch (BusinessException be) {
			Log.error(be.getMessage());
			if(be.getMessage().equals("The login is already used"))
				setResult("login_already_used");
			else
				setResult("register_form_result_incorrect");
			return "error";
		} catch (Exception e) {
			Log.error("User creation failed");
			setResult("register_form_result_incorrect");
			Mensajes.error("Error", "mensaje_error_registro");
			return "error"; // Nos vamos a la vista de error.
		}

	}
}
