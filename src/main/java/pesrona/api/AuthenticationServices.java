package pesrona.api;

import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.listener.NewSettingsListener;
import pesrona.model.Client;
import pesrona.model.Setting;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Path("/authentication")
public class AuthenticationServices {

    /**
     * 
     * @param authorization Bearer authorization header with client token
     * @param username
     * @param password needs to be plain in order to check LDAP login
     * @return 
     */
    @POST
    @Path("/signin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signin(@HeaderParam("Authorization") String authorization,
            @FormParam("username") String username, @FormParam("password") String password) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Response.status(400, "Token is required").build();
        }

        if (username == null || username.trim().length() == 0 || password == null || password.trim().length() == 0) {
            return Response.status(400, "Bad token").build();
        }
        
        String token = authorization.substring("Bearer".length()).trim();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Client client = (Client) session.createQuery("select o from Client o where o.token = :token").
                setParameter("token", token).getSingleResult();

        if (client == null) {
            session.close();
            return Response.status(400, "Bad token").build();
        }

        User user = session.get(User.class, username);

        if (user == null) {
            // User does not exist
            session.close();
            return Response.status(400, "Bad parameter").build();
        }

        if (user.getActive() == null || !user.getActive()) {
            // User is not active
            session.close();
            return Response.status(400, "Bad parameter").build();
        }

        if (user.getType() == null) {
            session.close();
            return Response.status(400, "Bad parameter").build();
        }

        if (user.getType().equals("normal") && user.getPassword() == null) {
            session.close();
            return Response.status(400, "Bad parameter").build();
        }
        
        if (user.getType().equals("normal") && user.getPassword().equals(Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString())) {
            session.close();
            ResponseDto responseDto = new ResponseDto();
            responseDto.setResponse(true);
            return Response.ok(responseDto).build();
        }

        List<Setting> settings = session.createQuery("select o from Setting o").getResultList();

        Map<String, String> settingValues = new HashMap<>();

        settings.forEach(setting -> {
            settingValues.put(setting.getCode(), setting.getValue());
        });

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.SECURITY_AUTHENTICATION, settingValues.get(NewSettingsListener.LDAP_ENCRYPTION));
        env.put(Context.SECURITY_PRINCIPAL, "uid=" + username + "," + settingValues.get(NewSettingsListener.LDAP_BASEDN));
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {

            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://" + settingValues.get(NewSettingsListener.LDAP_HOST) + ":" + settingValues.get(NewSettingsListener.LDAP_PORT));
            new InitialDirContext(env);

            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            session.close();

            ResponseDto responseDto = new ResponseDto();
            responseDto.setResponse(true);
            return Response.ok(responseDto).build();
        } catch (NamingException e) {
            e.printStackTrace();
            session.close();
            return Response.status(400, "Bad parameter").build();
        }
    }

    public class ResponseDto implements Serializable {

        private Boolean response;
        private String expiryDate;

        public Boolean getResponse() {
            return response;
        }

        public void setResponse(Boolean response) {
            this.response = response;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }
    }
}
