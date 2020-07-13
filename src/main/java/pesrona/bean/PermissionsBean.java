package pesrona.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Assignment;
import pesrona.model.Permission;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class PermissionsBean implements Serializable {

    private Session session;
    private List<Permission> permissions;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        permissions = session.createQuery("select o from Permission o").getResultList();
    }

    public void expire(Assignment assignment) {
        session.getTransaction().begin();
        assignment.setExpiryDate(new Date());
        session.save(assignment);
        session.getTransaction().commit();
    }
    
    public List<Permission> getPermissions() {
        return permissions;
    }
}
