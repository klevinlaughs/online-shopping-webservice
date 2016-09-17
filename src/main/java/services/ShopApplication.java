package services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * ShopApplication for JAX-RS webservice, subclass of Application
 * 
 * @author Kelvin Lau
 *
 */
@ApplicationPath("/services")
public class ShopApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public ShopApplication() {
		// Register the ParoleeResource singleton to handle HTTP requests.
		ItemResource resource = new ItemResource();
		singletons.add(resource);

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
