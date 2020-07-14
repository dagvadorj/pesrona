package pesrona.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
    private Date expiryDate;

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
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
        roles = session.createQuery("select o from Role o").getResultList();
        users = session.createQuery("select o from User o").getResultList();
        resources = session.createQuery("select o from Resource o").getResultList();
        expiryDate = new Date();
    }

    public String save() {

        try {

            Transaction transaction = session.beginTransaction();
            Assignment assignment = new Assignment();
            assignment.setCreatedDate(new Date());
            assignment.setExpiryDate(expiryDate);
            assignment.setRole(session.get(Role.class, roleId));
            assignment.setUser(session.get(User.class, username));
            if (resourceCode != null && resourceCode.trim().length() > 0) {
                assignment.setResource(session.get(Resource.class, resourceCode));
            }
            session.save(assignment);
            transaction.commit();
            return "assignments";
        } catch (Exception e) {
            if (e.getMessage() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
        }
        return null;
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
