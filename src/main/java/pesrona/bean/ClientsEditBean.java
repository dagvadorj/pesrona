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
import pesrona.model.Client;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ClientsEditBean implements Serializable {

    private Session session;
    private Client client;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Long clientId = Long.parseLong(params.get("clientId"));
        client = session.get(Client.class, clientId);
    }

    public String save() {

        try {
            Transaction transaction = session.beginTransaction();
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

    public String remove() {

        try {
            Transaction transaction = session.beginTransaction();
            session.delete(client);
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
