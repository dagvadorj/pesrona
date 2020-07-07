package pesrona.bean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Resource;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ResourcesNewBean implements Serializable {

    private String code;
    private String name;

    @PostConstruct
    public void init() {

    }

    public String save() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        
        Resource resource = new Resource();
        resource.setCode(code);
        resource.setName(name);
        session.save(resource);
        
        session.getTransaction().commit();
        return "resources";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
