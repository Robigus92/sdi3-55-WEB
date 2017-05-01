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
		urlPatterns = { "/admin/*" }, 
		initParams = { 
				@WebInitParam(name = "AdminParam", value = "/index.xhtml")
		})
public class AdminFilter implements Filter {
	
	FilterConfig config = null;

    /**
     * Default constructor. 
     */
    public AdminFilter() {
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
			FilterChain chain) {

		User logged_user = new User();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		try {
			// If it is not an Http Request, the Filter returns.
			if (!(request instanceof HttpServletRequest)) {
				chain.doFilter(request, response);
				return;
			}

			Log.info("AdminFiter working");

			// Read his session context.
			HttpSession session = req.getSession();
			logged_user = (User) session.getAttribute("LOGGEDIN_USER");

			// If the user is not logged, is necessary to redirect him to the
			// index
			// page.
			if (logged_user == null) {
				Log.warn("Anonymous User redirect to index page." + " Session "
						+ session.getId() + ".");
				res.sendRedirect(req.getContextPath() + "/index.xhtml");
			}

			// If the user is not an admin is necessary to redirect him to the
			// index
			// page.
			if (!logged_user.getIsAdmin()) {
				Log.warn("User " + logged_user.getLogin()
						+ " tried to accede to admin resources.");
				res.sendRedirect(req.getContextPath() +"/index.xhtml");
			}

			// continue with the chain.
			chain.doFilter(request, response);
		} catch (Exception e) {
			Log.info("Admin Filter Error." + e.getMessage());
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
