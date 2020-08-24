package pesrona.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dagva
 */
public class Report implements Serializable {

    private User user;
    private Role role;
    private Resource resource;
    private Client client;
    private Scope scope;
    private Date assignmentCreatedDate;
    private Date assignmentExpiryDate;
    private Date permissionCreatedDate;
    private Date permissionExpiryDate;

    public Report(User user, Role role, Resource resource, Client client, Scope scope, Date assignmentCreatedDate, Date assignmentExpiryDate, Date permissionCreatedDate, Date permissionExpiryDate) {
        this.user = user;
        this.role = role;
        this.resource = resource;
        this.client = client;
        this.scope = scope;
        this.assignmentCreatedDate = assignmentCreatedDate;
        this.assignmentExpiryDate = assignmentExpiryDate;
        this.permissionCreatedDate = permissionCreatedDate;
        this.permissionExpiryDate = permissionExpiryDate;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Date getPermissionCreatedDate() {
        return permissionCreatedDate;
    }

    public void setPermissionCreatedDate(Date permissionCreatedDate) {
        this.permissionCreatedDate = permissionCreatedDate;
    }

    public Date getPermissionExpiryDate() {
        return permissionExpiryDate;
    }

    public void setPermissionExpiryDate(Date permissionExpiryDate) {
        this.permissionExpiryDate = permissionExpiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Date getAssignmentCreatedDate() {
        return assignmentCreatedDate;
    }

    public void setAssignmentCreatedDate(Date assignmentCreatedDate) {
        this.assignmentCreatedDate = assignmentCreatedDate;
    }

    public Date getAssignmentExpiryDate() {
        return assignmentExpiryDate;
    }

    public void setAssignmentExpiryDate(Date assignmentExpiryDate) {
        this.assignmentExpiryDate = assignmentExpiryDate;
    }
}
