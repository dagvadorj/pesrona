/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pesrona.bean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pesrona.HibernateUtil;
import pesrona.model.Client;
import pesrona.model.Role;
import pesrona.model.Scope;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ScopesNewBean implements Serializable {

    private Session session;
    
    private String name;
    private Long clientId;
    private List<Client> clients;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        clients = (List<Client>) session.createQuery("select o from Client o").getResultList();
    }

    public String save() {

        Transaction transaction = session.beginTransaction();
        Scope scope = new Scope();
        scope.setName(name);
        scope.setClient(session.get(Client.class, clientId));
        session.save(scope);
        transaction.commit();
        return "scopes";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the clients
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     * @return the clientId
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


}
