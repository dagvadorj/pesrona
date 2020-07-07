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
import pesrona.model.Resource;
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
    private String username;
    private String resourceCode;
    private List<Client> roles;
    private List<User> users;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }
    private List<Resource> resources;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        setRoles((List<Client>) session.createQuery("select o from Role o").getResultList());
        setUsers((List<User>) session.createQuery("select o from User o").getResultList());
        resources = session.createQuery("select o from Resource o").getResultList();
    }

    public String save() {

        Transaction transaction = session.beginTransaction();
        Assignment assignment = new Assignment();
        assignment.setRole(session.get(Role.class, roleId));
        assignment.setUser(session.get(User.class, username));
        assignment.setResource(session.get(Resource.class, resourceCode));
        session.save(assignment);
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

    /**
     * @return the resources
     */
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }


}
