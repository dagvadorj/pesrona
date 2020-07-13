package pesrona.bean;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Client;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ClientsBean implements Serializable {

    private Session session;
    private List<Client> clients;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        clients = session.createQuery("select o from Client o").getResultList();
    }

    public void retokenize(Client client) {
        client.setToken(UUID.randomUUID().toString());
        session.save(client);
    }
    
    public List<Client> getClients() {
        return clients;
    }

    public void setRoles(List<Client> clients) {
        this.clients = clients;
    }
}
