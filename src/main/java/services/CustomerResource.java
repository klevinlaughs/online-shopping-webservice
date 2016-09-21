package services;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.Customer;

@Path("/customer")
public class CustomerResource {

	private static final Logger _logger = LoggerFactory
			.getLogger(ItemResource.class);

	private static EntityManager em = PersistenceManager.instance()
			.createEntityManager();

	public CustomerResource() {
	}

	/**
	 * Creates a user and persists in the database
	 * 
	 * @param customer
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public void createCustomer(Customer customer) {
		_logger.info("Creating user...");
		em.getTransaction().begin();
		em.persist(customer);
		em.getTransaction().commit();
	}

	/**
	 * Login method, creates a cookie for client
	 * 
	 * @param username
	 * @return
	 */
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_XML)
	public Response doLogin(String username) {
		_logger.info("Attempting login...");

		try {
			em.getTransaction().begin();

			// No exception thrown, then user exists, continue on
			em.createQuery(
					"SELECT c FROM Customer c WHERE c.userName =  :username")
					.setParameter("username", username).getSingleResult();

		} catch (NoResultException e) {
			throw new BadRequestException("User does not exist");
		} finally {
			em.getTransaction().commit();
		}

		return Response.ok().cookie(new NewCookie("username", username))
				.build();
	}

	/**
	 * Logout method, deletes user cookie
	 * 
	 * @param cookie
	 * @return
	 */
	@GET
	@Path("logout")
	public Response doLogout(@CookieParam("username") Cookie cookie) {
		if (cookie != null) {
			_logger.info("deleting cookie...");
			NewCookie newCookie = new NewCookie(cookie, "deleting cookie", 0,
					false);
			return Response.ok().cookie(newCookie).build();
		}
		return Response.ok().build();
	}

	/**
	 * Gets a customer by id. Only the customer themselves can get their own
	 * data
	 * 
	 * @param cookie
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	public Customer getCustomerById(@CookieParam("username") Cookie cookie,
			@PathParam("id") long id) {

		return null;
	}

	/**
	 * Gets a customer by username. Only the customer themselves can get their
	 * own data
	 * 
	 * @param cookie
	 * @param username
	 * @return
	 */
	@GET
	@Path("{username}")
	public Customer getCustomerByUsername(
			@CookieParam("username") Cookie cookie,
			@PathParam("username") String username) {
		return null;
	}

}
