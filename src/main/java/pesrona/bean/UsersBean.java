package pesrona.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;

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

	private static final long serialVersionUID = 1L;

	private List<User> users;
	private Map<String, String> settingValues;
	private Session session;

	@PostConstruct
	public void init() {
		session = HibernateUtil.getSessionFactory().openSession();
		users = session.createQuery("select o from User o", User.class).getResultList();
		List<Setting> settings = session.createQuery("select o from Setting o", Setting.class).getResultList();

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

		System.out.println("Syncing");

		Logger logger = Logger.getLogger(UsersBean.class.getName());

		logger.info("logging");

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.SECURITY_AUTHENTICATION, settingValues.get(NewSettingsListener.LDAP_ENCRYPTION));
		env.put(Context.SECURITY_PRINCIPAL, settingValues.get(NewSettingsListener.LDAP_ACCOUNT));
		env.put(Context.SECURITY_CREDENTIALS, settingValues.get(NewSettingsListener.LDAP_PASSWORD));

		System.out.println(env.toString());

		LdapContext context;

		try {
			env.put(Context.REFERRAL, "follow");
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, settingValues.get(NewSettingsListener.LDAP_HOST));
			context = new InitialLdapContext(env, null);
			System.out.println("context");
			logger.info(context.toString());
		} catch (NamingException e) {
			logger.info(e.toString());
			if (e.getMessage() == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			}
			return;
		}
//
//		int pageSize = 500;
//		byte[] cookie = null;
//
//		try {
//
//			context.setRequestControls(new Control[] { new PagedResultsControl(pageSize, Control.NONCRITICAL) });
//			int total = 0;
//			
//			logger.info(Integer.valueOf(total).toString());
//
//			do {
//				/* perform the search */
//				NamingEnumeration<SearchResult> results = context.search(settingValues.get(NewSettingsListener.LDAP_BASEDN),
//						"(" + settingValues.get(NewSettingsListener.LDAP_LOGIN) + "=*)", new SearchControls());
//
//				Logger.getLogger(UsersBean.class.getName()).log(Level.INFO, results.toString());
//				Logger.getLogger(UsersBean.class.getName()).log(Level.INFO, Boolean.valueOf(results != null).toString());
//				Logger.getLogger(UsersBean.class.getName()).log(Level.INFO, Boolean.valueOf(results.hasMoreElements()).toString());
//				
//				session.getTransaction().begin();
//
//				/* for each entry print out name + all attrs and values */
//				while (results != null && results.hasMoreElements()) {
//					SearchResult entry = (SearchResult) results.nextElement();
//					System.out.println(entry.getName());
//					Logger.getLogger(UsersBean.class.getName()).log(Level.INFO, entry.getName());
//
//					String loginAttribute = settingValues.get(NewSettingsListener.LDAP_LOGIN);
//					String emailAttribute = settingValues.get(NewSettingsListener.LDAP_EMAIL);
//					String nameAttribute = settingValues.get(NewSettingsListener.LDAP_FNAME);
//
//					Attributes attributes = entry.getAttributes();
//
//					if (attributes == null) {
//						continue;
//					}
//
//					if (attributes.get(loginAttribute) == null || attributes.get(loginAttribute).get() == null) {
//						continue;
//					}
//
//					String username = attributes.get(loginAttribute).get().toString();
//
//					User user = session.get(User.class, username);
//
//					if (user == null) {
//						user = new User();
//						user.setUsername(username);
//						user.setActive(true);
//					}
//
//					if (attributes.get(emailAttribute) != null && attributes.get(emailAttribute).get() != null)
//						user.setEmail(attributes.get(emailAttribute).get().toString());
//					if (attributes.get(nameAttribute) != null && attributes.get(nameAttribute).get() != null)
//						user.setName(attributes.get(nameAttribute).get().toString());
//					user.setType("ldap");
//
//					session.save(user);
//
//				}
//
//				// Examine the paged results control response
//				Control[] controls = context.getResponseControls();
//				if (controls != null) {
//					for (int i = 0; i < controls.length; i++) {
//						if (controls[i] instanceof PagedResultsResponseControl) {
//							PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
//							total = prrc.getResultSize();
//							if (total != 0) {
//								System.out.println("***************** END-OF-PAGE " + "(total : " + total
//										+ ") *****************\n");
//
//							} else {
//								System.out.println(
//										"***************** END-OF-PAGE " + "(total: unknown) ***************\n");
//							}
//							cookie = prrc.getCookie();
//						}
//					}
//				} else {
//					System.out.println("No controls were sent from the server");
//				}
//				// Re-activate paged results
//				context.setRequestControls(
//						new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });
//
//			} while (cookie != null);
//
//			session.getTransaction().commit();
//
//			users = session.createQuery("select o from User o", User.class).getResultList();
//
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Good " + Integer.valueOf(total)));
//			
//		} catch (Exception e) {
//			if (e.getMessage() == null) {
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown error"));
//			} else {
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
//			}
//		}

		try {

			NamingEnumeration<SearchResult> results = context.search(settingValues.get(NewSettingsListener.LDAP_BASEDN),
					"(" + settingValues.get(NewSettingsListener.LDAP_LOGIN) + "=*)", new SearchControls());

			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			context.setRequestControls(new Control[] { new PagedResultsControl(10000, Control.CRITICAL) });

			results = context.search(settingValues.get(NewSettingsListener.LDAP_BASEDN),
					"(" + settingValues.get(NewSettingsListener.LDAP_LOGIN) + "=*)", new Object[] {}, controls);

			session.getTransaction().begin();

			String loginAttribute = settingValues.get(NewSettingsListener.LDAP_LOGIN);
			String emailAttribute = settingValues.get(NewSettingsListener.LDAP_EMAIL);
			String nameAttribute = settingValues.get(NewSettingsListener.LDAP_FNAME);

			try {

				while (results.hasMore()) {
					SearchResult binding = results.next();
					Attributes attributes = binding.getAttributes();

					if (attributes == null) {
						continue;
					}

					if (attributes.get(loginAttribute) == null || attributes.get(loginAttribute).get() == null) {
						continue;
					}

					String username = attributes.get(loginAttribute).get().toString();

					User user = session.get(User.class, username);

					if (user == null) {
						user = new User();
						user.setUsername(username);
						user.setActive(true);
					}

					if (attributes.get(emailAttribute) != null && attributes.get(emailAttribute).get() != null)
						user.setEmail(attributes.get(emailAttribute).get().toString());
					if (attributes.get(nameAttribute) != null && attributes.get(nameAttribute).get() != null)
						user.setName(attributes.get(nameAttribute).get().toString());
					user.setType("ldap");

					session.save(user);
				}

			} catch (PartialResultException ex) {

			}

			session.getTransaction().commit();

			users = session.createQuery("select o from User o", User.class).getResultList();

		} catch (Exception e) {
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
