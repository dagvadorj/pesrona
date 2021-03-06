package pesrona.bean;

import java.io.Serializable;
import java.util.ArrayList;
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
import pesrona.model.Client;
import pesrona.model.Permission;
import pesrona.model.Role;
import pesrona.model.Scope;

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
    private String scopeCode;
    private Date expiryDate;

    private List<Role> roles;
    private List<Client> clients;
    private List<Scope> scopes;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        roles = session.createQuery("select o from Role o").getResultList();
        clients = session.createQuery("select o from Client o").getResultList();
        scopes = new ArrayList<>();
        expiryDate = new Date();
    }

    public void refreshScopes() {
        scopes = session.createQuery("select o from Scope o where o.client.id = :clientId").setParameter("clientId", clientId).getResultList();
    }

    public String save() {

        try {
            Transaction transaction = session.beginTransaction();
            Permission permission = new Permission();
            permission.setRole(session.get(Role.class, roleId));
            permission.setClient(session.get(Client.class, clientId));
            permission.setScope(session.get(Scope.class, scopeCode));
            permission.setCreatedDate(new Date());
            permission.setExpiryDate(expiryDate);
            session.save(permission);
            transaction.commit();
            return "permissions";
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

    public String getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(String scopeCode) {
        this.scopeCode = scopeCode;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
