package pesrona.resources;

import com.google.common.hash.Hashing;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.hibernate.Session;
import pesrona.HibernateUtil;
import pesrona.listener.NewSettingsListener;
import pesrona.model.Client;
import pesrona.model.Permission;
import pesrona.model.Resource;
import pesrona.model.Scope;
import pesrona.model.Setting;
import pesrona.model.User;

/**
 *
 * @author Dagvadorj
 */
@Path("permission")
public class PermissionService {

    @POST
    @Path("single")
    public Response signin(@HeaderParam("Authorization") String authorization,
            @FormParam("username") String username, @FormParam("password") String password) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Response.status(400, "Token is required").build();
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
        env.put(Context.SECURITY_PRINCIPAL, "uid:" + username + "," + settingValues.get(NewSettingsListener.LDAP_BASEDN));
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://" + settingValues.get(NewSettingsListener.LDAP_HOST) + ":" + settingValues.get(NewSettingsListener.LDAP_PORT));
            DirContext context = new InitialDirContext(env);
            
            session.close();
            
            ResponseDto responseDto = new ResponseDto();
            responseDto.setResponse(true);
            return Response.ok(responseDto).build();
        } catch (NamingException e) {
            session.close();
            return Response.status(400, "Bad parameter").build();
        }
    }

    @POST
    @Path("single")
    public Response single(@HeaderParam("Authorization") String authorization,
            @FormParam("username") String username, @FormParam("scopeCode") String scopeCode,
            @FormParam("resourceCode") String resourceCode) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Response.status(400, "Token is required").build();
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
        Scope scope = session.get(Scope.class, scopeCode);
        Resource resource = session.get(Resource.class, resourceCode);

        if (user == null || scope == null || resource == null) {
            session.close();
            return Response.status(400, "Bad parameter").build();
        }

        List<Permission> permissions = session.createQuery("select o from Permission o, Assignment o1 where o.role = o1.role and o.client = :client and o.scope = :scope and o1.resource = :resource and o1.user = :user")
                .setParameter("client", client)
                .setParameter("user", user)
                .setParameter("resource", resource)
                .setParameter("scope", scope)
                .getResultList();
        if (permissions.isEmpty()) {
            session.close();
            return Response.status(400, "No access").build();
        }

        session.close();

        ResponseDto responseDto = new ResponseDto();
        responseDto.setResponse(true);

        return Response.ok(responseDto).build();
    }

    @POST
    public Response all(@HeaderParam("Authorization") String authorization,
            @FormParam("username") String username) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Response.status(400, "Token is required").build();
        }

        String token = authorization.substring("Basic".length()).trim();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Client client = (Client) session.createQuery("select o from Client o where o.token = :token").
                setParameter("token", token).getSingleResult();
        if (client == null) {
            session.close();
            return Response.status(400, "Bad token").build();
        }

        User user = session.get(User.class, username);

        if (user == null) {
            session.close();
            return Response.status(400, "Bad parameter").build();
        }

        List<PermissionDto> permissionDtos = session.createQuery("select o.scope.code as scopeCode, o.scope.name as scope, o1.resource.code as resourceCode, o1.resource.name as resource from Permission o, Assignment o1 where o.role = o1.role and o.client = :client and o1.user = :user")
                .setParameter("client", client)
                .setParameter("user", user)
                .getResultList();
        if (permissionDtos.isEmpty()) {
            session.close();
            return Response.status(400, "No access").build();
        }

        session.close();

        return Response.ok(permissionDtos).build();
    }

    public class PermissionDto implements Serializable {

        private String scopeCode;
        private String scope;
        private String resourceCode;
        private String resource;

        public String getScopeCode() {
            return scopeCode;
        }

        public void setScopeCode(String scopeCode) {
            this.scopeCode = scopeCode;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getResourceCode() {
            return resourceCode;
        }

        public void setResourceCode(String resourceCode) {
            this.resourceCode = resourceCode;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
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

    public class CheckDto implements Serializable {

        private String username;
        private String scopeCode;
        private String resourceCode;

        /**
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username the username to set
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * @return the scopeCode
         */
        public String getScopeCode() {
            return scopeCode;
        }

        /**
         * @param scopeCode the scopeCode to set
         */
        public void setScopeCode(String scopeCode) {
            this.scopeCode = scopeCode;
        }

        /**
         * @return the resourceCode
         */
        public String getResourceCode() {
            return resourceCode;
        }

        /**
         * @param resourceCode the resourceCode to set
         */
        public void setResourceCode(String resourceCode) {
            this.resourceCode = resourceCode;
        }

    }
}
