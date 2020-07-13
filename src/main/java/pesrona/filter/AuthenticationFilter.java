package pesrona.filter;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author dagva
 */
@WebFilter(filterName = "AuthenticationFilter", servletNames = {"Faces Servlet"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.FORWARD})
public class AuthenticationFilter implements Filter {

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getSession().getAttribute("user") == null) {
            if (!httpRequest.getPathInfo().equals("/login.xhtml")) {
                httpResponse.sendRedirect("login.xhtml");
                return;
            } else {
                chain.doFilter(httpRequest, httpResponse);
                return;
            }
        } else {
            if (httpRequest.getPathInfo().equals("/login.xhtml")) {
                httpResponse.sendRedirect("index.xhtml");
                return;
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

}
