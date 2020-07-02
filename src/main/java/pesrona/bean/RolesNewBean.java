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
import org.hibernate.Transaction;
import pesrona.HibernateUtil;
import pesrona.model.Role;

/**
 *
 * @author dagva
 */
@Named(value = "rolesNewBean")
@ViewScoped
public class RolesNewBean implements Serializable {

    private String name;

    @PostConstruct
    public void init() {

    }

    public String save() {

        System.out.println(name);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Role role = new Role();
            role.setName(name);
            session.save(role);
            transaction.commit();
            return "roles";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Yes");
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
