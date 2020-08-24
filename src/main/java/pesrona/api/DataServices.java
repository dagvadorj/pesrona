package pesrona.api;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InvalidAttributeValueException;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.NoSuchAttributeException;
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
import pesrona.util.LdapUtil;

/**
 *
 * @author dagva
 */
@Path("/data")
public class DataServices {

	@POST
	@Path("/users/add/ldap")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addLdapUser(@HeaderParam("Authorization") String authorization,
			@FormParam("username") String username, @FormParam("email") String email,
			@FormParam("password") String password, @FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName) {

		String token = authorization.substring("Bearer".length()).trim();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Client client = (Client) session.createQuery("select o from Client o where o.token = :token")
				.setParameter("token", token).getSingleResult();

		if (client == null) {
			session.close();
			return Response.status(400, "Bad token").build();
		}

		List<Setting> settings = session.createQuery("select o from Setting o", Setting.class).getResultList();

		Map<String, String> settingValues = new HashMap<>();

		settings.forEach(setting -> {
			settingValues.put(setting.getCode(), setting.getValue());
		});

		DirContext context;

		try {
			context = LdapUtil.getContext(settingValues.get(NewSettingsListener.LDAP_HOST),
					settingValues.get(NewSettingsListener.LDAP_ENCRYPTION),
					settingValues.get(NewSettingsListener.LDAP_ACCOUNT),
					settingValues.get(NewSettingsListener.LDAP_PASSWORD));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(400).build();
		}

		String distinguishedName = "cn=" + username + ",ou=allusers, "
				+ settingValues.get(NewSettingsListener.LDAP_BASEDN);

		Attributes entry = new BasicAttributes(true);

		Attribute oc = new BasicAttribute("objectClass");
		oc.add("top");
		oc.add("person");
		oc.add("organizationalPerson");
		oc.add("user"); // , , , user
//		
		entry.put(oc);
		entry.put(settingValues.get(NewSettingsListener.LDAP_FNAME), firstName);
//		entry.put(settingValues.get(NewSettingsListener.LDAP_EMAIL), email); // IMPORTANT Do not add this
		entry.put("sn", lastName);
//		entry.put(settingValues.get(NewSettingsListener.LDAP_LOGIN), username); // IMPORTANT Do not add this
//		entry.put("givenName", firstName); // TODO
		// entry.put(new BasicAttribute("password", password));
		// entry.put("msDS-UserAccountDisabled", "FALSE");

		System.out.println(distinguishedName);
		System.out.println(entry);

		try {
			context.createSubcontext(distinguishedName, entry);
		} catch (NameAlreadyBoundException e) {
			e.printStackTrace();
		} catch (NoSuchAttributeException e) {
			System.out.println(e.getExplanation());
			System.out.println(e.getMessage());
			System.out.println(e.getRootCause());
			System.out.println(e.getRemainingName());
			System.out.println(e.getResolvedName());
			System.out.println(e.getResolvedObj());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).build();
		} catch (NamingException e) {
			System.out.println(e.getExplanation());
			System.out.println(e.getMessage());
			System.out.println(e.getRootCause());
			System.out.println(e.getResolvedName());
			System.out.println(e.getResolvedObj());
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(500).build();
		}

		return Response.ok().build();
	}

	@POST
	@Path("/users/update_password/ldap")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateLdapUserPassword(@HeaderParam("Authorization") String authorization,
			@FormParam("username") String username, @FormParam("current_password") String currentPassword,
			@FormParam("password") String password) {

		String token = authorization.substring("Bearer".length()).trim();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Client client = (Client) session.createQuery("select o from Client o where o.token = :token")
				.setParameter("token", token).getSingleResult();

		if (client == null) {
			session.close();
			return Response.status(400, "Bad token").build();
		}

		List<Setting> settings = session.createQuery("select o from Setting o", Setting.class).getResultList();

		Map<String, String> settingValues = new HashMap<>();

		settings.forEach(setting -> {
			settingValues.put(setting.getCode(), setting.getValue());
		});

		DirContext context;

		try {
			context = LdapUtil.getContext(settingValues.get(NewSettingsListener.LDAP_HOST),
					settingValues.get(NewSettingsListener.LDAP_ENCRYPTION),
					settingValues.get(NewSettingsListener.LDAP_ACCOUNT),
					settingValues.get(NewSettingsListener.LDAP_PASSWORD));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(400).build();
		}

		String distinguishedName = "cn=" + username + ",ou=allusers, "
				+ settingValues.get(NewSettingsListener.LDAP_BASEDN);

		try {

			ModificationItem[] mods = new ModificationItem[2];

			mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
					new BasicAttribute("unicodePwd", p(currentPassword)));
			mods[1] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
					new BasicAttribute("unicodePwd", p(password)));

			context.modifyAttributes(distinguishedName, mods);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(400).build();
		}

		return Response.ok().build();
	}

	private byte[] p(String password) {
		String quotedPassword = "\"" + password + "\"";
		char unicodePwd[] = quotedPassword.toCharArray();
		byte pwdArray[] = new byte[unicodePwd.length * 2];
		for (int i = 0; i < unicodePwd.length; i++) {
			pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
			pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
		}
		System.out.print("encoded password: ");
		for (int i = 0; i < pwdArray.length; i++) {
			System.out.print(pwdArray[i] + " ");
		}
		return pwdArray;
	}

	public class RoleDto {

		private Long id;
		private String name;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public class ScopeDto {

	}
}
