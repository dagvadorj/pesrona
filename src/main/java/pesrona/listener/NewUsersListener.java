package pesrona.listener;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.User;

/**
 * Web application lifecycle listener.
 *
 * @author dagva
 */
public class NewUsersListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = session.createQuery("select o from User o").getResultList();
        if (users.isEmpty()) {
            
            session.getTransaction().begin();
            User user = new User();
            user.setUsername("admin");
            user.setPassword(Hashing.sha512().hashString("admin", StandardCharsets.UTF_8).toString());
            user.setName("Admin");
            user.setType("normal");
            user.setActive(true);
            user.setAdministrator(true);
            session.save(user);
            session.getTransaction().commit();
        }
        session.close();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
