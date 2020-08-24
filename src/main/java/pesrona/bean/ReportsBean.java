package pesrona.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.hibernate.Session;

import pesrona.HibernateUtil;
import pesrona.model.Client;
import pesrona.model.Report;
import pesrona.model.Resource;
import pesrona.model.Role;
import pesrona.model.User;

/**
 *
 * @author dagva
 */
@Named
@ViewScoped
public class ReportsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Session session;
	private List<Report> reports;
	private List<User> users;
	private List<Resource> resources;
	private List<Role> roles;
	private List<Client> clients;

	@PostConstruct
	public void init() {
		session = HibernateUtil.getSessionFactory().openSession();
		reports = session.createQuery(
				"select new pesrona.model.Report(o1.user, o.role, o1.resource, o.client, o.scope, o1.createdDate, o1.expiryDate, o.createdDate, o.expiryDate) from Permission o, Assignment o1 where o.role = o1.role",
				Report.class).getResultList();
		users = session.createQuery("select o from User o", User.class).getResultList();
		roles = session.createQuery("select o from Role o", Role.class).getResultList();
		clients = session.createQuery("select o from Client o", Client.class).getResultList();
		resources = session.createQuery("select o from Resource o", Resource.class).getResultList();
	}

	public List<Resource> getResources() {
		return resources;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public List<Client> getClients() {
		return clients;
	}

	public List<Report> getReports() {
		return reports;
	}
}
