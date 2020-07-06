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
import pesrona.HibernateUtil;
import pesrona.model.Client;
import pesrona.model.Role;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ClientsBean implements Serializable {

    private List<Client> clients;

    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        clients = session.createQuery("select o from Client o").getResultList();
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setRoles(List<Client> clients) {
        this.clients = clients;
    }
}
