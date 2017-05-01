package uo.sdi.presentation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import alb.util.log.Log;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.dto.Category;
import uo.sdi.dto.User;
import uo.sdi.infrastructure.Factories;

@ManagedBean(name = "categorias")
@SessionScoped
public class BeanCategories implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Category> categories;
	private Category categoria = null;
	private User user;
	private HashMap<Long,Category> category_map = new HashMap<Long,Category>(); 
	

	@PostConstruct
	public void init() {
		Log.info("BeanCategorias - PostConstruct");
		listado();
	}

	/*
	private void iniciaCategoria() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		categoria.setId(null);
		categoria.setName(bundle.getString("valorDefectoNombre"));
		categoria.setUserId(null);
		
	} */
	

	public String listado() {
		TaskService service;
		Services s = Factories.services;
		try {
			service = s.getTaskService();
			user = (User) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap()
					.get(new String("LOGGEDIN_USER"));
			
			categories = service.findCategoriesByUserId(user.getId());
			fullfillCategoryMap();
			
		} catch (Exception e) {
			Log.error("Error listando las categorías");
			return "error";
		}
		return "exito";
	}
	
	private void fullfillCategoryMap(){
		for(Category var: categories){
			category_map.put(var.getId(), var);
		}
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Category getCategoria() {
		return categoria;
	}

	public void setCategoria(Category categoria) {
		this.categoria = categoria;
	}
	
	public String getCategoryNameById(Long t){
		
		if(t==null) return "";
		for (Category c : categories) {
			if(c.getId().compareTo(t) == 0)
				return c.getName();
		}
		return "";
		
	}
	
}
