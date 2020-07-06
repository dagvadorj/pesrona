package pesrona.bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pesrona.HibernateUtil;
import pesrona.model.Resource;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ResourcesNewBean implements Serializable {

    private String name;

    @PostConstruct
    public void init() {

    }

    public String save() {

        System.out.println(name);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Resource resource = new Resource();
        resource.setName(name);
        session.save(resource);
        transaction.commit();
        return "resources";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
