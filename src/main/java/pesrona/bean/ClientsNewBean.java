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
import pesrona.model.Client;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ClientsNewBean implements Serializable {

    private Session session;
    private String name;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public String save() {

        try {
            Transaction transaction = session.beginTransaction();
            Client client = new Client();
            client.setName(name);
            session.save(client);
            transaction.commit();
            return "clients";
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

}
