package pesrona.bean;

import java.io.Serializable;
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
public class UsersNewBean implements Serializable {

    private String name;
    private String username;
    private String email;

    @PostConstruct
    public void init() {

    }

    public String save() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = new User();
            user.setName(name);
            user.setUsername(username);
            user.setType("normal");
            user.setEmail(email);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
