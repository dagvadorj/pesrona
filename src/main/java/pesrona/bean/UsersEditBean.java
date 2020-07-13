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
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class UsersEditBean implements Serializable {

    private Session session;
    private User user;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        String username = params.get("username");
        user = session.get(User.class, username);
    }

    public String save() {

        try {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return "users";
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
            session.delete(user);
            transaction.commit();
            return "users";
        } catch (Exception e) {
            if (e.getMessage() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
        }
        return null;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
