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
import pesrona.model.Client;
import pesrona.model.Role;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class UsersNewBean implements Serializable {

    private String name;

    @PostConstruct
    public void init() {

    }

    public String save() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = new User();
            user.setName(name);
            session.save(user);
            transaction.commit();
            return "users";
        } catch (Exception e) {
            e.printStackTrace();
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
