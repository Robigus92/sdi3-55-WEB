package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import alb.util.date.DateUtil;
import alb.util.log.Log;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Task;
import uo.sdi.infrastructure.Factories;

@ManagedBean(name = "task")
@SessionScoped
public class BeanTask extends Task implements Serializable {
	private static final long serialVersionUID = 55551L;
	
	public BeanTask() {
		iniciaTask(null);
	}

	// Este método es necesario para copiar el alumno a editar cuando
	// se pincha el enlace Editar en la vista listado.xhtml. Podría sustituirse
	// por un método editar en BeanAlumnos.
	public void setTask(Task task) {
		setId(task.getId());
		setCategoryId(task.getCategoryId());
		setComments(task.getComments());
		setCreated(task.getCreated());
		setFinished(task.getFinished());
		setPlanned(task.getPlanned());
		setTitle(task.getTitle());
		setUserId(task.getUserId());
		
	}

	// Iniciamos los datos del alumno con los valores por defecto
	// extraídos del archivo de propiedades correspondiente
	public void iniciaTask(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		setCreated(DateUtil.today());
		setCategoryId(null);
		setComments(null);
		setFinished(null);
		setPlanned(DateUtil.today());
		setTitle(bundle.getString("task_title"));
		setUserId(null);
		setId(null);
	}
	
	private void updateTask() throws BusinessException{
		Services s = Factories.services;
		TaskService ts = s.getTaskService();
		ts.updateTask(this.getTaskBase());
		
	}
	
	public String editTask(){
		try {
			updateTask();
		} catch (BusinessException e) {
			Log.error("Error editing task ["+getTitle()+"]");
			return "error";
		}
		return "exito";
	}
	
	@Override
	public void setPlanned(Date planned){
		if(planned==null) super.setPlanned(DateUtil.today());
		else super.setPlanned(planned);
	}

	
	public boolean beforeOfToday(Date d1){
		if(d1==null) return false;
		return DateUtil.isBefore(d1, DateUtil.today());
	}
	
	public Task getTaskBase() {
		Task t = new Task();
		t.setCategoryId(getCategoryId());
		t.setComments(getComments());
		t.setCreated(getCreated());
		t.setFinished(getFinished());
		t.setId(getId());
		t.setPlanned(getPlanned());
		t.setTitle(getTitle());
		t.setUserId(getUserId());
		return t;
	}
}
