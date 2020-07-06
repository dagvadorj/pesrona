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
import pesrona.model.Client;
import pesrona.model.Role;
import pesrona.model.Scope;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ScopesBean implements Serializable {

    private List<Scope> scopes;

    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        scopes = session.createQuery("select o from Scope o").getResultList();
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
    }
}
