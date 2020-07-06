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
import pesrona.model.Scope;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class PermissionsNewBean implements Serializable {

    private Session session;
    
    private Long roleId;
    private Long clientId;
    private Long scopeId;
    private List<Role> roles;
    private List<Client> clients;
    private List<Scope> scopes;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        setRoles((List<Role>) session.createQuery("select o from Role o").getResultList());
        setClients((List<Client>) session.createQuery("select o from Client o").getResultList());
        setScopes((List<Scope>) session.createQuery("select o from Scope o").getResultList());
    }

    public String save() {

        Transaction transaction = session.beginTransaction();
        Assignment assignment = new Assignment();
        assignment.setRole(session.get(Role.class, getRoleId()));
        assignment.setUser(session.get(User.class, getClientId()));
        session.save(assignment);
        transaction.commit();
        return "permissions";
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

    /**
     * @return the scopeId
     */
    public Long getScopeId() {
        return scopeId;
    }

    /**
     * @param scopeId the scopeId to set
     */
    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * @return the clients
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     * @param clients the clients to set
     */
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    /**
     * @return the scopes
     */
    public List<Scope> getScopes() {
        return scopes;
    }

    /**
     * @param scopes the scopes to set
     */
    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
    }

}
