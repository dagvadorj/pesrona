package pesrona.bean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Assignment;
import pesrona.model.Client;
import pesrona.model.Role;
import pesrona.model.Scope;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class AssignmentsBean implements Serializable {

    private List<Assignment> assignments;

    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        assignments = session.createQuery("select o from Assignment o").getResultList();
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> scopes) {
        this.assignments = scopes;
    }
}
