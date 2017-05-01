package uo.sdi.security;

import java.io.IOException;

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
		urlPatterns = { "/snippets/*" }, 
		initParams = { 
				@WebInitParam(name = "SniParam", value = "/index.xhtml")
		})

/*NarFilter = Not Allowed Resource Filter. 
 * It redirect to the index page and signal the behavior. */
public class NarFilter implements Filter {
	
	FilterConfig config = null;
	
	//private String pathignored1 = "/sdi2-55/index.xhtml";
	//private String pathignored2 = "/sdi2-55/altaForm.xhtml";

    /**
     * Default constructor. 
     */
    public NarFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		User logged_user = new User();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		try {
			// If it is not an Http Request, the Filter returns.
			if (!(request instanceof HttpServletRequest)) {
				chain.doFilter(request, response);
				return;
			}

			Log.info("SnippetFilter working");
			// Read his session context.
			HttpSession session = req.getSession();
			logged_user = (User) session.getAttribute("LOGGEDIN_USER");

			// If the user is not logged, is necessary to redirect him to the
			// index
			// page.
			if (logged_user == null) {
				Log.warn("WARNING. Anonymous User redirect to index page. "
						+ "Tried to access to a not allowed folder. "
						+ " Session " + session.getId() + ".");
			} else {
				Log.warn("WARNING. User " + logged_user.getLogin()
						+ " redirect to index page. "
						+ "Tried to access to a not allowed folder. "
						+ " Session " + session.getId() + ".");
			}

			// if the user is going to the snippet folder is redirect to the
			// index.
			res.sendRedirect(req.getContextPath() +"/index.xhtml");

		} catch (Exception e) {
			Log.info("Nar Filter Error." + e.getMessage());

		}
	}
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		config = fConfig;
	}

}
