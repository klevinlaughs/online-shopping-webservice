package services;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import domain.*;

/**
 * Adapted from the ParoleeResolver from parolee-dto
 * 
 * 
 * ContextResolver implementation to return a customised JAXBContext for the
 * Parolee Web service. 
 * 
 * The JAX-RS run-time will create a default JAXBContext. To use a default
 * JAXBContext, this class isn't required and shouldn't be registered.
 * 
 * This class is included to show how a customised JAXBContext can be created. 
 * For the customised JAXBContext to be used, this class must be registered 
 * with JAX-RS, by class ParoleeApplication. As with registering any component,
 * the Application class (i.e. ParoleeApplication for this Web service), should
 * return this class from its getClasses() method.   
 * 
 * @author Ian Warren
 *
 */
public class ShopResolver implements ContextResolver<JAXBContext> {
	private JAXBContext _context;

	public ShopResolver() {
		try {
			// The JAXB Context should be able to marshal and unmarshal the
			// specified classes.
			_context = JAXBContext.newInstance(Customer.class, Category.class, Item.class, Address.class, Image.class, Review.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JAXBContext getContext(Class<?> type) {
		if (type.equals(Customer.class) || type.equals(Category.class)
				|| type.equals(Item.class) || type.equals(Address.class)
				|| type.equals(Image.class) || type.equals(Review.class)) {
			return _context;
		} else {
			return null;
		}
	}
}
