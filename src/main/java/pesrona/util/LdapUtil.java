package pesrona.util;

import java.util.Objects;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LdapUtil {

	private final static String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

	private static DirContext context = null;

	public static DirContext getContext(String providerUrl, String encryption, String account, String password)
			throws NamingException {
		if (!Objects.isNull(context)) {
			return context;
		}

		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		properties.put(Context.PROVIDER_URL, providerUrl);
		properties.put(Context.SECURITY_AUTHENTICATION, encryption);
		properties.put(Context.SECURITY_PRINCIPAL, account);
		properties.put(Context.SECURITY_CREDENTIALS, password);
		if (providerUrl.startsWith("ldaps:")) {
			properties.put(Context.SECURITY_PROTOCOL, "ssl");
		}
		DirContext context = new InitialDirContext(properties);
		return context;
	}
}
