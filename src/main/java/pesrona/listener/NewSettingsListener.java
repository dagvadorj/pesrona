package pesrona.listener;

import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Setting;

/**
 * Web application lifecycle listener.
 *
 * @author poc
 */
public class NewSettingsListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Setting> settings = session.createQuery("select o from Setting o").getResultList();
        if (settings.isEmpty()) {
            
            session.getTransaction().begin();
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_HOST");
                setting.setName("LDAP host");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_PORT");
                setting.setName("LDAP port");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_ENCRYPTION");
                setting.setName("Encryption");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_ACCOUNT");
                setting.setName("Account");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_PASSWORD");
                setting.setName("Password");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_BASEDN");
                setting.setName("Base DN");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_LOGIN");
                setting.setName("Login");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_FNAME");
                setting.setName("First Name");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_LNAME");
                setting.setName("Last Name");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode("LDAP_EMAIL");
                setting.setName("E-mail");
                session.save(setting);
            }
            
            session.getTransaction().commit();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
