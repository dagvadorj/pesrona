/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pesrona.bean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Role;

/**
 *
 * @author dagva
 */
@Named(value = "rolesBean")
@ViewScoped
public class RolesBean implements Serializable {

    private List<Role> roles;

    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        roles = session.createQuery("select o from Role o").getResultList();
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
