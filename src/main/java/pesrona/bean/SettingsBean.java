package pesrona.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.Client;
import pesrona.model.Role;
import pesrona.model.Setting;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class SettingsBean implements Serializable {

    private Session session;
    
    private List<Setting> settings;
    private Map<Long, String> settingValues;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
        settings = session.createQuery("select o from Setting o").getResultList();
        settingValues = new HashMap<>();
        for (Setting setting : settings) {
            settingValues.put(setting.getId(), setting.getValue());
        }
    }

    public void update() {

        session.getTransaction().begin();
        for (Map.Entry<Long, String> settingValue : settingValues.entrySet()) {
            Setting setting = session.get(Setting.class, settingValue.getKey());
            setting.setValue(settingValue.getValue());
            session.update(setting);
        }
        session.getTransaction().commit();
        
    }
    
    /**
     * @return the users
     */
    public List<Setting> getSettings() {
        return settings;
    }

    /**
     * @param settings the users to set
     */
    public void setUsers(List<Setting> settings) {
        this.settings = settings;
    }

    /**
     * @return the settingValues
     */
    public Map<Long, String> getSettingValues() {
        return settingValues;
    }

    /**
     * @param settingValues the settingValues to set
     */
    public void setSettingValues(Map<Long, String> settingValues) {
        this.settingValues = settingValues;
    }
}
