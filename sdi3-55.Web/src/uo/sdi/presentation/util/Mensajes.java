package uo.sdi.presentation.util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensajes {
	     
	    public static void info(String titulo, String mensaje) {
	    	FacesContext facesContext = FacesContext.getCurrentInstance();
			ResourceBundle bundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "msgs");
			
	        facesContext.addMessage(null, 
	        	new FacesMessage(FacesMessage.SEVERITY_INFO
	        			, titulo
	        			, bundle.getString(mensaje)
    			)
	        );
	    }
	     
	    public static void warn(String titulo, String mensaje) {
	    	FacesContext facesContext = FacesContext.getCurrentInstance();
			ResourceBundle bundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "msgs");
			
	        facesContext.addMessage(null, 
	        	new FacesMessage(FacesMessage.SEVERITY_WARN
	        			, titulo
	        			, bundle.getString(mensaje)
    			)
	        );
	    }
	     
	    public static void error(String titulo, String mensaje) {
	    	FacesContext facesContext = FacesContext.getCurrentInstance();
			ResourceBundle bundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "msgs");
			
	        facesContext.addMessage(null, 
	        	new FacesMessage(FacesMessage.SEVERITY_ERROR
	        			, titulo
	        			, bundle.getString(mensaje)
    			)
	        );
	    }
	     
	    public static void fatal(String titulo, String mensaje) {
	    	FacesContext facesContext = FacesContext.getCurrentInstance();
			ResourceBundle bundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "msgs");
			
	        facesContext.addMessage(null, 
	        	new FacesMessage(FacesMessage.SEVERITY_FATAL
	        			, titulo
	        			, bundle.getString(mensaje)
    			)
	        );
	    }
}
