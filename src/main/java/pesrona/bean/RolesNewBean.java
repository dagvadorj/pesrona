package pesrona.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.Transaction;

import pesrona.HibernateUtil;
import pesrona.model.Role;

/**
 *
 * @author dagva
 */
@Named
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
