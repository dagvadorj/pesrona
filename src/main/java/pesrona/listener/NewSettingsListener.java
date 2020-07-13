package pesrona.listener;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Setting;
import pesrona.model.User;

/**
 * Web application lifecycle listener.
 *
 * @author poc
 */
public class NewSettingsListener implements ServletContextListener {

    public final static String LDAP_HOST = "LDAP_HOST";
    public final static String LDAP_PORT = "LDAP_PORT";
    public final static String LDAP_ENCRYPTION = "LDAP_ENCRYPTION";
    public final static String LDAP_ACCOUNT = "LDAP_ACCOUNT";
    public final static String LDAP_PASSWORD = "LDAP_PASSWORD";
    public final static String LDAP_BASEDN = "LDAP_BASEDN";
    public final static String LDAP_LOGIN = "LDAP_LOGIN";
    public final static String LDAP_FNAME = "LDAP_FNAME";
    public final static String LDAP_LNAME = "LDAP_LNAME";
    public final static String LDAP_EMAIL = "LDAP_EMAIL";
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Setting> settings = session.createQuery("select o from Setting o").getResultList();
        if (settings.isEmpty()) {
            
            session.getTransaction().begin();
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_HOST);
                setting.setName("LDAP host");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_PORT);
                setting.setName("LDAP port");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_ENCRYPTION);
                setting.setName("Encryption");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_ACCOUNT);
                setting.setName("Account");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_PASSWORD);
                setting.setName("Password");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_BASEDN);
                setting.setName("Base DN");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_LOGIN);
                setting.setName("Login");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_FNAME);
                setting.setName("First Name");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_LNAME);
                setting.setName("Last Name");
                session.save(setting);
            }
            
            {
                Setting setting = new Setting();
                setting.setCode(LDAP_EMAIL);
                setting.setName("E-mail");
                session.save(setting);
            }
            
            session.getTransaction().commit();
        }
        
        session.close();
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
