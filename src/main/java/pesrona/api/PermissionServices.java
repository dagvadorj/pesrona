package pesrona.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import pesrona.model.Client;
import pesrona.model.Permission;
import pesrona.model.Resource;
import pesrona.model.Scope;
import pesrona.model.User;

/**
 *
 * @author Dagvadorj
 */
@Path("/permission")
public class PermissionServices {

    @POST
    @Path("/single")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
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

        System.out.println("yoyo");

        return Response.ok(responseDto).build();
    }

    @POST
    @Path("all")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response all(@HeaderParam("Authorization") String authorization,
            @FormParam("username") String username) {

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

        List<Object[]> permissions = session.createQuery("select o.scope.code as scopeCode, o.scope.name as scope, o1.resource.code as resourceCode, o1.resource.name as resource from Permission o, Assignment o1 where o.role = o1.role and o.client = :client and o1.user = :user")
                .setParameter("client", client)
                .setParameter("user", user)
                .getResultList();
        if (permissions.isEmpty()) {
            session.close();
            return Response.status(400, "No access").build();
        }

        session.close();

        List<PermissionDto> permissionDtos = new ArrayList<>();

        permissions.stream().map(object -> {
            PermissionDto permissionDto = new PermissionDto();
            permissionDto.setScopeCode((String) object[0]);
            permissionDto.setScope((String) object[1]);
            permissionDto.setResourceCode((String) object[2]);
            permissionDto.setResource((String) object[3]);
            return permissionDto;
        }).forEachOrdered(permissionDto -> {
            permissionDtos.add(permissionDto);
        });

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
