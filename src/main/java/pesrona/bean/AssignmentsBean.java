package pesrona.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Assignment;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class AssignmentsBean implements Serializable {

    private Session session;
    private List<Assignment> assignments;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        assignments = session.createQuery("select o from Assignment o").getResultList();
    }

    public void expire(Assignment assignment) {
        session.getTransaction().begin();
        assignment.setExpiryDate(new Date());
        session.save(assignment);
        session.getTransaction().commit();
    }
    
    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> scopes) {
        this.assignments = scopes;
    }
}
