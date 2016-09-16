package services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Application subclass for the Parolee Web service.
 * 
 * @author Ian Warren
 *
 */
@ApplicationPath("/services")
public class ShopApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public ShopApplication() {
		singletons.add(new ItemResource());

		// Register the ContextResolver class for JAXB.
		classes.add(ShopResolver.class);
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
}
