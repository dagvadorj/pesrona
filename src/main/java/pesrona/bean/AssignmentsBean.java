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
import pesrona.model.Resource;
import pesrona.model.Role;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class AssignmentsBean implements Serializable {

    private Session session;
    private List<Assignment> assignments;
    private List<Role> roles;
    private List<User> users;
    private List<Resource> resources;

    public List<User> getUsers() {
        return users;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        assignments = session.createQuery("select o from Assignment o").getResultList();
        roles = session.createQuery("select o from Role o").getResultList();
        users = session.createQuery("select o from User o").getResultList();
        resources = session.createQuery("select o from Resource o").getResultList();
    }

    public void expire(Assignment assignment) {
        session.getTransaction().begin();
        assignment.setExpiryDate(new Date());
        session.save(assignment);
        session.getTransaction().commit();
        assignments = session.createQuery("select o from Assignment o").getResultList();
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
}
