package pesrona.bean;

import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class LogoutBean implements Serializable {

    @PostConstruct
    public void init() {
    }
    
    public String logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().invalidate();
        return "login";
    }
}
