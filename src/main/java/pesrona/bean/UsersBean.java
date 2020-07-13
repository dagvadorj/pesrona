package pesrona.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.listener.NewSettingsListener;
import pesrona.model.Setting;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class UsersBean implements Serializable {

    private List<User> users;
    private Map<String, String> settingValues;
    private Session session;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        users = session.createQuery("select o from User o").getResultList();
        List<Setting> settings = session.createQuery("select o from Setting o").getResultList();

        settingValues = new HashMap<>();

        for (Setting setting : settings) {
            settingValues.put(setting.getCode(), setting.getValue());
        }
    }

    public void activate(User user) {
        session.getTransaction().begin();
        if (user.getActive() == null) {
            user.setActive(true);
        } else {
            user.setActive(!user.getActive());
        }
        session.save(user);
        session.getTransaction().commit();
    }

    public void sync() {

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.SECURITY_AUTHENTICATION, settingValues.get(NewSettingsListener.LDAP_ENCRYPTION));
        env.put(Context.SECURITY_PRINCIPAL, settingValues.get(NewSettingsListener.LDAP_ACCOUNT));
        env.put(Context.SECURITY_CREDENTIALS, settingValues.get(NewSettingsListener.LDAP_PASSWORD));

        DirContext context;

        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://" + settingValues.get(NewSettingsListener.LDAP_HOST) + ":" + settingValues.get(NewSettingsListener.LDAP_PORT));
            context = new InitialDirContext(env);
        } catch (NamingException e) {
            if (e.getMessage() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
            return;
        }

        NamingEnumeration<SearchResult> results;

        try {

            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = context.search(settingValues.get(NewSettingsListener.LDAP_BASEDN), "(" + settingValues.get(NewSettingsListener.LDAP_LOGIN) + "=*)", new Object[]{}, controls);

            session.getTransaction().begin();

            String loginAttribute = settingValues.get(NewSettingsListener.LDAP_LOGIN);
            String emailAttribute = settingValues.get(NewSettingsListener.LDAP_EMAIL);
            String nameAttribute = settingValues.get(NewSettingsListener.LDAP_FNAME);

            while (results.hasMoreElements()) {
                SearchResult binding = results.nextElement();
                Attributes attributes = binding.getAttributes();

                if (attributes == null) {
                    continue;
                }

                if (attributes.get(loginAttribute) == null || attributes.get(emailAttribute) == null
                        || attributes.get(nameAttribute) == null) {
                    continue;
                }

                if (attributes.get(loginAttribute).get() == null || attributes.get(emailAttribute).get() == null
                        || attributes.get(nameAttribute).get() == null) {
                    continue;
                }

                String username = attributes.get(loginAttribute).get().toString();

                User user = session.get(User.class, username);

                if (user == null) {
                    user = new User();
                    user.setUsername(username);
                    user.setActive(true);
                }

                user.setEmail(attributes.get(emailAttribute).get().toString());
                user.setName(attributes.get(nameAttribute).get().toString());
                user.setType("ldap");

                session.save(user);
            }

            session.getTransaction().commit();

            users = session.createQuery("select o from User o").getResultList();

        } catch (NamingException e) {
            if (e.getMessage() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
            }
        }
    }

    /**
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
