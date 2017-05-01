package uo.sdi.presentation;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import alb.util.date.DateUtil;
import alb.util.log.Log;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.infrastructure.Factories;
import uo.sdi.presentation.util.*;

@ManagedBean(name = "tareas")
@SessionScoped
public class BeanTasks implements Serializable {
	private static final long serialVersionUID = 55550L;

	@ManagedProperty(value = "#{task}")
	private BeanTask task = null;

	public BeanTask getTask() {
		return task;
	}

	public void setTask(BeanTask task) {
		this.task = task;
	}

	@ManagedProperty(value = "#{user}")
	private BeanUser user;

	public BeanUser getUser() {
		return user;
	}

	public void setUser(BeanUser user) {
		this.user = user;
	}

	
	@ManagedProperty(value = "#{categorias}")
	private BeanCategories categorias;
	
	
	public BeanCategories getCategorias() {
		return categorias;
	}

	public void setCategorias(BeanCategories categorias) {
		this.categorias = categorias;
	}

	private String tipoListado = "inbox";
	
	public String getTipoListado() {
		return tipoListado;
	}

	public void setTipoListado(String tipoListado) {
		this.tipoListado = tipoListado;
	}

	
	
	@PostConstruct
	public void init() {
		Log.info("BeanTasks - PostConstruct");
		task = (BeanTask) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get(new String("task"));

		if (task == null) {
			Log.info("BeanTask - No existía en sesión");
			task = new BeanTask();
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("task", task);
		}

		user = (BeanUser) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get(new String("user"));

		if (user == null) {
			Log.info("BeanUser- No existía en sesión");
			user = new BeanUser();
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("user", user);
		}

		try {
			user.setUser((User) FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap()
					.get(new String("LOGGEDIN_USER")));

			prepareInboxTasks(); // Empiezo a cargar un litado de task.
			
			
		} catch (BusinessException e) {
			Log.error(e.getMessage());
		}

		categorias = (BeanCategories) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get(new String("categorias"));

		if (categorias == null) {
			Log.info("BeanCategorias- No existía en sesión");
			categorias = new BeanCategories();
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("categorias", categorias);
		}
	}

	@PreDestroy
	public void end() {
		Log.info("BeanUsers - PreDestroy");
	}

	private List<Task> tasks = null;

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	

	private List<Task> filteredTasks = null;
	
	public List<Task> getFilteredTasks() {
		return filteredTasks;
	}

	public void setFilteredTasks(List<Task> filteredTasks) {
		this.filteredTasks = filteredTasks;
	}
	

	private void prepareInboxTasks() throws BusinessException {
		TaskService service;
		Services s = Factories.services;

		service = s.getTaskService();
		tasks = service.findInboxTasksByUserId(user.getId());

	}

	private void prepareAllInboxTasks() throws BusinessException {
		TaskService service;
		Services s = Factories.services;

		service = s.getTaskService();
		tasks = service.findInboxTasksByUserId(user.getId());
		tasks.addAll((service.findFinishedInboxTasksByUserId(user.getId())));

	}

	private void prepareTodayTasks() throws BusinessException {
		TaskService service;
		Services s = Factories.services;

		service = s.getTaskService();
		tasks = service.findTodayTasksByUserId(user.getId());

	}

	private void prepareWeekTasks() throws BusinessException {
		TaskService service;
		Services s = Factories.services;

		service = s.getTaskService();
		tasks = service.findWeekTasksByUserId(user.getId());

	}

	private void prepareCategoryTasks(Category category) 
		throws BusinessException {
		
		TaskService service;
		Services s = Factories.services;

		service = s.getTaskService();
		tasks = service.findTasksByCategoryId(category.getId());
	}
	
	
	public String listadoInboxTasks() {
		try {
			categorias.setCategoria(null);
			if(todo)
				prepareAllInboxTasks();
			else
				prepareInboxTasks();
			
			setTipoListado("inbox");
		} catch (Exception e) {
			Log.error("The User "+user.getLogin()+" got an error asking for Inbox Tasks list.");
			return "error";
		}
		Log.info("The User "+user.getLogin()+" got his Inbox Tasks list.");
		return "exito";
	}

	public String listadoAllInboxTasks() {
		try {
			categorias.setCategoria(null);
			prepareAllInboxTasks();
			setTipoListado("inbox");
		} catch (Exception e) {
			Log.error("The User "+user.getLogin()+" got an error asking for "
					+ "his all Inbox Tasks list.");
			return "error";
		}
		Log.info("The User "+user.getLogin()+" got his All Inbox Tasks list.");
		return "exito";
	}

	public String listadoTodayTasks() {
		try {
			categorias.setCategoria(null);
			prepareTodayTasks();
			setTipoListado("today");
		} catch (Exception e) {
			Log.error("The User "+user.getLogin()+" got an error asking for his"
					+ " Today Tasks list.");
			return "error";
		}
		Log.info("The User "+user.getLogin()+" got his Today Tasks list.");
		return "exito";
	}

	public String listadoWeekTasks() {
		try {
			categorias.setCategoria(null);
			prepareWeekTasks();
			setTipoListado("week");
		} catch (Exception e) {
			Log.error("The User "+user.getLogin()+" got an Error "
					+ "asking for his Week Tasks list.");
			return "error";
		}
		Log.info("The User "+user.getLogin()+" got his Week Tasks list.");
		return "exito";
	}
	
	public String listadoCategoryTasks(Category category) {
		try {
			categorias.setCategoria(category);
			prepareCategoryTasks(category);
			setTipoListado("category");
			Log.info("User ["+user.getLogin()+"] asked successfully for his category task list.");
		} catch (Exception e) {
			Log.error("The User "+user.getLogin()+" got an error"
					+ "asking for his Categories list.");
			return "error";
		}
		Log.info("The User "+user.getLogin()+" got his Categories list.");
		return "exito";
	}

	public String eliminar(Task task) {
		TaskService service;
		Services s = Factories.services;
		try {
			service = s.getTaskService();
			service.deleteTask(task.getId());
			
			reloadTasks();
			
		} catch (Exception e) {
			Mensajes.error("Error", "mensaje_error_task_eliminada");
			Log.error("The User "+user.getLogin()+" got an error deleting a task.");
			return "error";
		}
		Mensajes.info("Éxito", "mensaje_exito_task_eliminada");
		Log.info("The User "+user.getLogin()+" delete his own task.");
		return "exito";
	}

	private void reloadTasks() throws BusinessException {
		switch (tipoListado) {
			case "category":
				prepareCategoryTasks(categorias.getCategoria());
				break;
			case "inbox":
				listadoInboxTasks();
				break;
			case "today":
				prepareTodayTasks();
				break;
			case "week":
				prepareWeekTasks();
				break;
		}
		
	}

	public String finalizar(Task task) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		try {
			markAsDone(task);
			reloadTasks();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
							bundle.getString("task_finalized_confirmation")));
		} catch (BusinessException e) {
			Log.error("User " + user.getLogin()
					+ ". Erroring marking as done a Task");
		}
		Log.info("User " + user.getLogin()
				+ ". 'Task marked' finished successfully.");
		return "exito";
	}
	
	private void markAsDone(Task task) throws BusinessException{
		TaskService service;
		Services s = Factories.services;

		service = s.getTaskService();
		service.markTaskAsFinished(task.getId());
		
	};

	private boolean todo = false;
	
	public boolean isTodo() {
		return todo;
	}

	public void setTodo(boolean todo) {
		this.todo = todo;
	}
	
	private void addTask() throws BusinessException{
		TaskService service;
		Services s = Factories.services;

		service = s.getTaskService();
		task.setUserId(user.getId());
		service.createTask(task.getTaskBase());
	}
	
	public String saveTask(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		
		try {
			addTask();
			goToList();
			reloadTasks();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
							bundle.getString("task_created_confirmation")));
		} catch (BusinessException e) {
			
			Log.error("User ["+user.getLogin()+"]. Error adding a new task.");
			return "error";
		}
		
		return "exito";
	}
	
	private void goToList(){
		
		if(task.getCategoryId()==null) 
			tipoListado="inbox";
		else if(task.getPlanned()!=null){
			if(MyDateUtil.sameDay(task.getPlanned(),DateUtil.today())) 
				tipoListado="today";
		
			else if(DateUtil.isAfter(task.getPlanned(), DateUtil.today())) 
				tipoListado="week";
		}
	}
	
	public String goToEditTask(Task toEdit){
		
		if(toEdit==null) return "error";
		
		Log.info("Preparing to edit a task. ");
		task.setTask(toEdit);
		return "exito";
	}
	
	public String saveEditedTask(){
		String result = "exito";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		try {
			result = task.editTask();
			task.setTask(new Task());
			reloadTasks();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
							bundle.getString("task_edited_confirmation")));
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			Log.error("Error editing task ["+task.getTitle()+"] of User ["+user.getLogin()+"].");
			result="error";
		}
		return result;
	}
	
	/*
	private boolean sameDay(Date d1, Date d2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(d1);
		c2.setTime(d2);
		
		return (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))
				&& (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR));
		
		
	}*/
}
