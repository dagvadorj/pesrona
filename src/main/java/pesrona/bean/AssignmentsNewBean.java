package pesrona.bean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pesrona.HibernateUtil;
import pesrona.model.Assignment;
import pesrona.model.Client;
import pesrona.model.Role;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class AssignmentsNewBean implements Serializable {

    private Session session;
    
    private Long roleId;
    private Long userId;
    private List<Client> roles;
    private List<User> users;

    @PostConstruct
    public void init() {
        setSession(HibernateUtil.getSessionFactory().openSession());
        setRoles((List<Client>) getSession().createQuery("select o from Role o").getResultList());
        setUsers((List<User>) getSession().createQuery("select o from User o").getResultList());
    }

    public String save() {

        Transaction transaction = getSession().beginTransaction();
        Assignment assignment = new Assignment();
        assignment.setRole(session.get(Role.class, roleId));
        assignment.setUser(session.get(User.class, userId));
        getSession().save(assignment);
        transaction.commit();
        return "assignments";
    }

    /**
     * @return the roles
     */
    public List<Client> getClients() {
        return getRoles();
    }

    /**
     * @return the roleId
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the roles
     */
    public List<Client> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Client> roles) {
        this.roles = roles;
    }

    /**
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }


}
