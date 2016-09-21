package services;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
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
import domain.Item;
import domain.Review;

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
	public Response createCustomer(Customer customer) {
		_logger.info("Creating user...");
		em.getTransaction().begin();
		em.persist(customer);
		em.getTransaction().commit();

		return Response.created(URI.create("/parolees/" + customer.getId()))
				.cookie(new NewCookie("username", customer.getUserName()))
				.build();
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
	public Customer getCustomerById(@CookieParam("username") String cookie,
			@PathParam("id") long id) {

		if (cookie == null) {
			throw new NotAuthorizedException("Must login to view profiles");
		}

		em.getTransaction().begin();
		Customer customer = em.find(Customer.class, id);
		em.getTransaction().commit();

		if (customer == null) {
			throw new NotFoundException();
		}

		if (!customer.getUserName().equals(cookie)) {
			throw new ForbiddenException("Not allowed to view another user");
		}

		return customer;
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
			@CookieParam("username") String cookie,
			@PathParam("username") String username) {

		if (cookie == null) {
			throw new NotAuthorizedException("Must login to view profiles");
		}

		if (!username.equals(cookie)) {
			throw new ForbiddenException("Not allowed to view another user");
		}

		em.getTransaction().begin();
		Customer customer = em
				.createQuery(
						"SELECT c FROM Customer c WHERE c.userName = :username",
						Customer.class)
				.setParameter("username", username).getSingleResult();
		em.getTransaction().commit();

		if (customer == null) {
			throw new NotFoundException();
		}

		return customer;
	}

	/**
	 * Gets reviews made by a logged in customer
	 * 
	 * @param username
	 *            the username from cookie
	 * @return
	 */
	@GET
	@Path("review")
	public List<Review> getReviewsForCustomer(
			@CookieParam("username") String username) {

		if (username == null) {
			throw new NotAuthorizedException("Login to see your reviews");
		}

		em.getTransaction().begin();

		List<Review> reviews = em
				.createQuery(
						"SELECT r FROM Review r WHERE r.reviewer.userName = :username",
						Review.class)
				.setParameter(username, username).getResultList();

		em.getTransaction().commit();

		// return even if empty
		return reviews;
	}

	/**
	 * Gets the purchase history for a logged in customer
	 * 
	 * @param username
	 *            the username from cookie
	 * @return
	 */
	@GET
	@Path("purchase-history")
	public List<Item> getPurchaseHistoryForCustomer(
			@CookieParam("username") String username) {

		if (username == null) {
			throw new NotAuthorizedException(
					"Login to see your purchase history");
		}

		em.getTransaction().begin();

		List<Item> purchaseHistory = em
				.createQuery(
						"SELECT c.purchaseHistory from Customer c where c.username = :username",
						Item.class)
				.setParameter("username", username).getResultList();

		em.getTransaction().commit();

		// return even if empty
		return purchaseHistory;
	}
}
