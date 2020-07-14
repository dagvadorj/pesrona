package pesrona.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Client;
import pesrona.model.Permission;
import pesrona.model.Role;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class PermissionsBean implements Serializable {

    private Session session;
    private List<Permission> permissions;
    private List<Role> roles;
    private List<Client> clients;

    public List<Role> getRoles() {
        return roles;
    }

    public List<Client> getClients() {
        return clients;
    }

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        permissions = session.createQuery("select o from Permission o").getResultList();
        roles = session.createQuery("select o from Role o").getResultList();
        clients = session.createQuery("select o from Client o").getResultList();
    }

    public void expire(Permission permission) {
        session.getTransaction().begin();
        permission.setExpiryDate(new Date());
        session.save(permission);
        session.getTransaction().commit();
        permissions = session.createQuery("select o from Permission o").getResultList();
    }
    
    public List<Permission> getPermissions() {
        return permissions;
    }
}
