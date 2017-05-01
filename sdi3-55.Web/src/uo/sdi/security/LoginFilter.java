package uo.sdi.security;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import alb.util.log.Log;
import uo.sdi.dto.User;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(
		dispatcherTypes = {DispatcherType.REQUEST }
					, 
		urlPatterns = { "/user/*" }, 
		initParams = { 
				@WebInitParam(name = "LoginParam", value = "/index.xhtml")
		})
public class LoginFilter implements Filter {
	
	FilterConfig config = null;
	
	//private String pathignored1 = "/sdi2-55/index.xhtml";
	//private String pathignored2 = "/sdi2-55/altaForm.xhtml";

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		
		User logged_user = new User();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		
		try{
		// If it is not an Http Request, the Filter returns. 
		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}

		Log.info("LoginFilter working");
		// Read his session context.
		HttpSession session = req.getSession();
		logged_user = (User) session.getAttribute("LOGGEDIN_USER");

		// If the user is not logged, is necessary to redirect him to the index
		// page.
		if (logged_user == null) {
			Log.warn("Anonymous User redirect to index page."
					+ " Session " + session.getId()+".");
			res.sendRedirect(req.getContextPath() +"/index.xhtml");
		}

		// continue with the chain.
		chain.doFilter(request, response);
		
		} catch(Exception e){
			Log.info("Login Filter Error." + e.getMessage());
		}
	}
	
	/*
	private boolean isIgnoredPath(String path){
		
		if(path.equals(pathignored1)) return true;
		if(path.equals(pathignored2)) return true;
		
		return false;
	}*/

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		config = fConfig;
	}

}
