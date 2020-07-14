package pesrona.bean;

import pesrona.model.Report;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Client;
import pesrona.model.Resource;
import pesrona.model.Role;
import pesrona.model.Scope;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ReportsBean implements Serializable {

    private Session session;
    private List<Report> reports;
    private List<User> users;
    private List<Resource> resources;

    public List<Resource> getResources() {
        return resources;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Client> getClients() {
        return clients;
    }
    private List<Role> roles;
    private List<Client> clients;

    public List<Report> getReports() {
        return reports;
    }

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        reports = session.createQuery("select new pesrona.model.Report(o1.user, o.role, o1.resource, o.client, o.scope, o1.createdDate, o1.expiryDate, o.createdDate, o.expiryDate) from Permission o, Assignment o1 where o.role = o1.role").getResultList();
        users = session.createQuery("select o from User o").getResultList();
        roles = session.createQuery("select o from Role o").getResultList();
        clients = session.createQuery("select o from Client o").getResultList();
        resources = session.createQuery("select o from Resource o").getResultList();
    }
}
