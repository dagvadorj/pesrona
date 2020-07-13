package pesrona.bean;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pesrona.HibernateUtil;
import pesrona.model.Resource;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ResourcesEditBean implements Serializable {

    private Session session;
    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        String resourceCode = params.get("resourceCode");
        resource = session.get(Resource.class, resourceCode);
    }

    public String save() {

        try {
            Transaction transaction = session.beginTransaction();
            session.save(resource);
            transaction.commit();
            return "resources";
        } catch (Exception e) {
            if (e.getMessage() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
        }
        return null;
    }

    public String remove() {

        try {
            Transaction transaction = session.beginTransaction();
            session.delete(resource);
            transaction.commit();
            return "resources";
        } catch (Exception e) {
            if (e.getMessage() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
        }
        return null;
    }

}
