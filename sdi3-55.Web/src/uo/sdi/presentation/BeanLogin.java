package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import alb.util.log.Log;
import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.dto.User;
import uo.sdi.infrastructure.Factories;


@ManagedBean(name = "login")
@SessionScoped
public class BeanLogin implements Serializable {
	private static final long serialVersionUID = 6L;
	private String name = "";
	private String password = "";
	private String result = "login_form_result_valid";
	private boolean logged = false;
	private boolean adminLogged = false;

	public BeanLogin() {
		Log.info("BeanLogin - No existia");
	}

	public String verify() {
		UserService service;
		Services s = Factories.services;
		try {
			service = s.getUserService();
			User user = service.findLoggableUser(name, password);
			if (user != null) {
				putUserInSession(user);
				
				if(user.getIsAdmin()){
					Log.info("LOGIN. Admin "+user.getLogin()+" successfully logged in.");
					return "exito_admin";
				} else {
					Log.info("LOGIN. User "+user.getLogin()+" successfully logged in.");
					return "exito_user";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("CRITICAL ERROR. Error doing login. ");
		}
		setResult("login_form_result_error");
		return "error";
	}

	private void putUserInSession(User user) {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		session.put("LOGGEDIN_USER", user);
		setLogged(true);
		setAdminLogged(user.getIsAdmin());
	}
	
	public String closeSession() {
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		session.remove("LOGGEDIN_USER");
		setLogged(false);
		setAdminLogged(false);
		session.clear();
		return "exito";
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public boolean isAdminLogged() {
		return adminLogged;
	}

	public void setAdminLogged(boolean adminLogged) {
		this.adminLogged = adminLogged;
	}

}