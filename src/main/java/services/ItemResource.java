package services;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.Address;
import domain.Category;
import domain.CreditCard;
import domain.Customer;
import domain.Image;
import domain.Item;
import domain.Review;

/**
 * The resource by which the online shopping web service is accessed
 * 
 * @author Kelvin Lau
 *
 */
@Path("/item")
public class ItemResource {
	private static final Logger _logger = LoggerFactory
			.getLogger(ItemResource.class);

	private static EntityManager em = PersistenceManager.instance()
			.createEntityManager();

	public ItemResource() {
		reloadDatabase();
	}

	/**
	 * Convenience method for testing the Web service. This method reinitialises
	 * the state of the Web service to holds three Parolee objects.
	 */
	// @PUT
	// public void reloadData() {
	// reloadDatabase();
	// }

	/**
	 * HATEOS for getting items, only want 5 items by default, starting at index
	 * 0, and a category is included
	 * 
	 * @param start
	 * @param size
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getItems(@DefaultValue("0") @QueryParam("start") int start,
			@DefaultValue("5") @QueryParam("size") int size,
			@DefaultValue("none") @QueryParam("category") String category,
			@Context UriInfo uriInfo) {

		_logger.info("Getting items");

		em.getTransaction().begin();

		_logger.info("Getting item count");

		Long itemCount = em.createQuery("select count(*) from Item item",
				Long.class).getSingleResult();

		_logger.info("Item count: " + itemCount);

		em.getTransaction().commit();
		em.getTransaction().begin();

		URI uri = uriInfo.getAbsolutePath();

		_logger.info("URI: " + uri.toString());

		Link previous = null;
		Link next = null;

		if (start > 0) {
			_logger.info("Making previous link...");
			// There are previous Items
			int newStart;
			if (start > size) {
				// Start is greater than size, even more previous items
				// afterwards
				newStart = start - size;
			} else {
				// Start is equal to or less than size, no more previous items
				// afterwards
				newStart = 0;
			}
			// Use default size if reached the end :(
			int newSize = start + size >= itemCount ? 5 : size;
			previous = Link.fromUri(uri + "?start={start}&size={size}")
					.rel("previous").build(newStart, newSize);
		}
		if (start + size < itemCount) {
			// There are more items
			_logger.info("Making next link...");
			int nextSize;
			if (start + 2 * size <= itemCount) {
				// Next step has even more items after
				nextSize = 5;
			} else {
				// Next step doesn't have anymore after
				nextSize = (int) (itemCount - start - size);
			}
			next = Link.fromUri(uri + "?start={start}&size={size}").rel("next")
					.build(start + size, nextSize);
		}

		_logger.info("Querying items...");

		List<Item> items = em.createQuery("select i from Item i", Item.class)
				.setFirstResult(start).setMaxResults(size).getResultList();

		em.getTransaction().commit();

		for (Item item : items) {
			_logger.info("Item retrieved: " + item);
		}

		GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(items) {
		};

		ResponseBuilder builder = Response.ok(entity);
		if (previous != null) {
			builder.links(previous);
		}
		if (next != null) {
			builder.links(next);
		}
		Response response = builder.build();

		_logger.info("Returning response...");

		return response;
	}

	/**
	 * Creates a new item (TODO add some sort of auth or admin check)
	 * 
	 * @param item
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public void createItem(Item item) {

	}

	/**
	 * Gets item by id
	 * 
	 * @param id
	 *            the id of the item
	 * @return the item
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Item getItemById(@PathParam("id") long id) {
		em.getTransaction().begin();
		Item item = em.find(Item.class, id);
		em.getTransaction().commit();

		return item;
	}

	/**
	 * Gets the images for an item
	 * 
	 * @param id
	 *            the id of the item
	 * @return Set<Image> of images
	 */
	@GET
	@Path("{id}/images")
	@Produces(MediaType.APPLICATION_XML)
	public Set<Image> getImagesForItem(@PathParam("id") long id) {

		em.getTransaction().begin();
		Set<Image> images = em.find(Item.class, id).getImages();
		em.getTransaction().commit();

		return images;
	}

	@GET
	@Path("{id}/review")
	@Produces(MediaType.APPLICATION_XML)
	public List<Review> getReviewsForItem(@PathParam("id") long id) {

		em.getTransaction().begin();
		List<Review> reviews = em
				.createQuery("SELECT r FROM Review r WHERE r.item.id = :id",
						Review.class).setParameter("id", id).getResultList();
		em.getTransaction().commit();

		return reviews;
	}

	/**
	 * Gets the deals of the day
	 * 
	 * @param start
	 *            - the starting index (default 1)
	 * @param size
	 *            - the amount of deals to get (default 3)
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Path("deals")
	@Produces(MediaType.APPLICATION_XML)
	public Response getItemsToday(
			@DefaultValue("0") @QueryParam("start") int start,
			@DefaultValue("3") @QueryParam("size") int size,
			@DefaultValue("none") @QueryParam("category") String category,
			@Context UriInfo uriInfo) {

		URI uri = uriInfo.getAbsolutePath();

		Link previous = null;
		Link next = null;

		ResponseBuilder builder = Response.ok();
		return builder.build();
	}

	/**
	 * Method to load up the database (for testing)
	 */
	protected void reloadDatabase() {
		// DatabaseUtility.openDatabase();
		// DatabaseUtility.clearDatabase(false);
		// DatabaseUtility.closeDatabase();

		// ---- ITEMS SET UP ----
		// TODO set more stock levels?
		Item item1 = new Item("PS4", new BigDecimal("599"));
		item1.addImage(new Image("ps4.jpg", 1024, 768));
		item1.addImage(new Image("xbox.png"));
		item1.setStockLevel(100);

		Item item2 = new Item("Gatorade", new BigDecimal("3.99"));
		item2.setStockLevel(30);

		Item item3 = new Item("7.1 Surround Sound Gaming Headset",
				new BigDecimal("240"));
		item3.setStockLevel(0);

		Item item4 = new Item("Box of Tissues", new BigDecimal("2.49"));
		item4.setDealItem(true);

		Item item5 = new Item("Pepsi 2.5L", new BigDecimal("1.99"));
		item5.setDealItem(true);

		Item item6 = new Item("Pawpaw Ointment", new BigDecimal("7.99"));
		item6.setDealItem(true);

		Item item7 = new Item("AWP", new BigDecimal("4000"));
		item7.setDealItem(true);
		item7.setStockLevel(1);

		// ---- CATEGORIES SET UP ----
		Category catElec = new Category("Electronics");
		Category catAudio = new Category("Audio", catElec);
		Category catGaming = new Category("Gaming", catElec);
		Category catFoodAndDrink = new Category("Food and Drink");
		Category catGuns = new Category("Guns");
		// TODO more categories?

		item1.setCategory(catGaming);
		item2.setCategory(catFoodAndDrink);
		item3.setCategory(catAudio);

		item5.setCategory(catFoodAndDrink);

		item7.setCategory(catGuns);

		// ---- CUSTOMERS SET UP ----
		Customer customer1 = new Customer("x_sniper360noscope_x");
		customer1.setFirstName("John");
		customer1.setLastName("Doe");
		customer1.setProfilePic(new Image("this-is-me.jpg"));
		customer1.setShippingAddress(new Address(123, "Sneaky Road",
				"Plum Hill", "BoomCity", "Japanda", "20580"));
		customer1.setBillingAddress(customer1.getShippingAddress());
		customer1
				.setCreditCard(new CreditCard("123456789", "November", "2020"));
		customer1.addToPurchaseHistory(item7);
		customer1.addToPurchaseHistory(item5);
		customer1.addToPurchaseHistory(item3);
		customer1.addToPurchaseHistory(item1);

		Customer customer2 = new Customer("chuggachuggachoochoo");

		Customer customer3 = new Customer("trainee");
		customer3.setFirstName("Jim");
		customer3.setLastName("Bob");
		customer3.addToPurchaseHistory(item1);
		customer3.addToPurchaseHistory(item2);

		// ---- REVIEWS SET UP ----
		Review review1 = new Review(customer1, item7, 5.0);
		Review review2 = new Review(customer1, item1, 1.0,
				"Fake!! Don't buy! You have been warned!");
		Review review3 = new Review(customer3, item1, 5.0,
				"It workes pretty well, did its job. :)");

		// ---- PERSISTENCE ----
		em.getTransaction().begin();

		em.persist(item1);
		em.persist(item2);
		em.persist(item3);
		em.persist(item4);
		em.persist(item5);
		em.persist(item6);
		em.persist(item7);

		// Also persists catElec (parent... cascade persistence)
		em.persist(catGaming);
		em.persist(catFoodAndDrink);
		em.persist(catAudio);
		em.persist(catGuns);

		em.persist(customer1);
		em.persist(customer2);
		em.persist(customer3);

		em.persist(review1);
		em.persist(review2);
		em.persist(review3);

		em.getTransaction().commit();
	}
}
