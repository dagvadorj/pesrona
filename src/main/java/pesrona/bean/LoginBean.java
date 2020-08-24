package pesrona.bean;

import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Session session;

    private String username;
    private String password;

    @PostConstruct
    public void init() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public String login() {
        List<User> users = session.createQuery("select o from User o where o.username = :username and o.password = :password and o.active = true and o.administrator = true", User.class)
                .setParameter("username", username)
                .setParameter("password", Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString())
                .getResultList();
        if (users.size() != 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Authentication is unsuccessful!"));
            return null;
        }

        User user = users.get(0);
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.setAttribute("user", user);
        return "index";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
