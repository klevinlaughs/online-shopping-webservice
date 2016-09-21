package services;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

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

	// multivaluedmap for async responses
	private Map<Long, List<AsyncResponse>> watchListResponses = new HashMap<Long, List<AsyncResponse>>();

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
	 * @param category
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getItems(@DefaultValue("0") @QueryParam("start") int start,
			@DefaultValue("5") @QueryParam("size") int size,
			@DefaultValue("none") @QueryParam("category") String category,
			@Context UriInfo uriInfo) {

		_logger.info("Getting items");

		String categoryQuery = "";
		if (!category.equals("none")) {
			categoryQuery = " WHERE item.category.name = '" + category + "'";
		}

		em.getTransaction().begin();

		_logger.info("Getting item count");

		Long itemCount = em
				.createQuery("select count(*) from Item item" + categoryQuery,
						Long.class)
				.getSingleResult();

		_logger.info("Item count: " + itemCount);

		em.getTransaction().commit();
		em.getTransaction().begin();

		URI uri = uriInfo.getAbsolutePath();

		Link previous = createPreviousHATEOAS(start, size, 5, itemCount, uri);
		Link next = createNextHATEOAS(start, size, 5, itemCount, uri);

		_logger.info("Querying items...");

		List<Item> items = em
				.createQuery("select item from Item item" + categoryQuery,
						Item.class)
				.setFirstResult(start).setMaxResults(size).getResultList();

		em.getTransaction().commit();

		for (Item item : items) {
			_logger.info("Item retrieved: " + item);
		}

		GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(
				items) {
		};

		ResponseBuilder builder = Response.ok(entity);
		if (previous != null) {
			builder.links(previous);
		}
		if (next != null) {
			builder.links(next);
		}
		return builder.build();
	}

	/**
	 * Creates a new item
	 * 
	 * @param item
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public void createItem(Item item) {

		em.getTransaction().begin();
		em.persist(item);
		em.getTransaction().commit();

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
		_logger.info("Item found: " + item);
		em.getTransaction().commit();

		if (item == null) {
			throw new NotFoundException(
					"Item with id:" + id + " does not exist");
		}

		return item;
	}

	/**
	 * Deletes an item along with its reviews
	 * 
	 * @param id
	 */
	@DELETE
	@Path("{id}")
	public void deleteItemById(@PathParam("id") long id) {
		em.getTransaction().begin();
		List<Review> reviewsForItem = em
				.createQuery("SELECT r FROM Review r WHERE r.item.id = :id",
						Review.class)
				.setParameter("id", id).getResultList();
		if (!reviewsForItem.isEmpty()) {
			for (Review review : reviewsForItem) {
				em.remove(review);
			}
		}

		Item item = em.find(Item.class, id);
		em.remove(item);
		em.getTransaction().commit();
	}

	/**
	 * Gets the images for an item
	 * 
	 * @param id
	 *            the id of the item
	 * @return Set<Image> of images
	 */
	@GET
	@Path("{id}/image")
	@Produces(MediaType.APPLICATION_XML)
	public List<Image> getImagesForItem(@PathParam("id") long id) {

		em.getTransaction().begin();
		Set<Image> images = em.find(Item.class, id).getImages();
		em.getTransaction().commit();

		List<Image> imageList = new ArrayList<Image>(images);

		return imageList;
	}

	/**
	 * Adds image to specified item
	 * 
	 * @param id
	 * @param image
	 */
	@POST
	@Path("{id}/image")
	@Consumes(MediaType.APPLICATION_XML)
	public void addImageForItem(@PathParam("id") long id, Image image) {
		em.getTransaction().begin();
		Item item = em.find(Item.class, id);
		item.addImage(image);
		em.persist(item);
		em.getTransaction().commit();
	}

	/**
	 * 
	 * Gets the reviews for an item with HATEOAS
	 * 
	 * @param id
	 *            item id
	 * @param start
	 * @param size
	 * @param uriInfo
	 * @return
	 */
	@GET
	@Path("{id}/review")
	@Produces(MediaType.APPLICATION_XML)
	public Response getReviewsForItem(@PathParam("id") long id,
			@QueryParam("start") int start, @QueryParam("size") int size,
			@Context UriInfo uriInfo) {

		URI uri = uriInfo.getAbsolutePath();

		em.getTransaction().begin();

		Long count = em.createQuery(
				"select count(*) from Review r where r.item.id = :id",
				Long.class).setParameter("id", id).getSingleResult();

		em.getTransaction().commit();

		Link previous = createPreviousHATEOAS(start, size, 3, count, uri);
		Link next = createNextHATEOAS(start, size, 3, count, uri);

		em.getTransaction().begin();

		List<Review> reviews = em
				.createQuery("SELECT r FROM Review r WHERE r.item.id = :id",
						Review.class)
				.setFirstResult(start).setMaxResults(size)
				.setParameter("id", id).getResultList();
		em.getTransaction().commit();

		GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(
				reviews) {
		};

		ResponseBuilder builder = Response.ok(entity);
		if (previous != null) {
			builder.links(previous);
		}
		if (next != null) {
			builder.links(next);
		}
		return builder.build();
	}

	/**
	 * Creates a review for an item
	 * 
	 * @param cookie
	 * @param id
	 * @param review
	 */
	@POST
	@Path("/{id}/review")
	public void createReviewForItem(@CookieParam("username") String cookie,
			@PathParam("id") long id, Review review) {

		if (cookie == null) {
			throw new NotAuthorizedException("Must login to review");
		}
		em.getTransaction().begin();
		em.persist(review);
		em.getTransaction().commit();
	}

	/**
	 * Gets categories with HATEOAS
	 * 
	 * @param start
	 * @param size
	 * @return
	 */
	@GET
	@Path("category")
	public Response getCategories(
			@DefaultValue("0") @QueryParam("start") int start,
			@DefaultValue("5") @QueryParam("size") int size,
			@Context UriInfo uriInfo) {

		em.getTransaction().begin();
		URI uri = uriInfo.getAbsolutePath();

		Long count = em
				.createQuery("select count(*) from Category c", Long.class)
				.getSingleResult();

		Link previous = createPreviousHATEOAS(start, size, 5, count, uri);
		Link next = createNextHATEOAS(start, size, 5, count, uri);

		List<Category> categories = em
				.createQuery("SELECT c FROM Category c", Category.class)
				.setFirstResult(start).setMaxResults(size).getResultList();

		em.getTransaction().commit();

		GenericEntity<List<Category>> entity = new GenericEntity<List<Category>>(
				categories) {
		};

		ResponseBuilder builder = Response.ok(entity);
		if (previous != null) {
			builder.links(previous);
		}
		if (next != null) {
			builder.links(next);
		}
		return builder.build();
	}

	/**
	 * Creates a category
	 * 
	 * @param category
	 */
	@POST
	@Path("category")
	@Consumes(MediaType.APPLICATION_XML)
	public void createCategory(Category category) {
		em.getTransaction().begin();
		em.persist(category);
		em.getTransaction().commit();
	}

	/**
	 * 
	 * @param start
	 *            the starting index (default 1)
	 * @param size
	 *            the amount of deals to get (default 3)
	 * @param category
	 *            the category
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

		_logger.info("Getting items");

		em.getTransaction().begin();

		_logger.info("Getting item count");

		String categoryQuery = "";
		if (!category.equals("none")) {
			categoryQuery = " AND item.category.name = '" + category + "'";
		}

		Long itemCount = em.createQuery(
				"select count(*) from Item item WHERE item.dealItem = true"
						+ categoryQuery,
				Long.class).getSingleResult();

		_logger.info("Item count: " + itemCount);

		em.getTransaction().commit();
		em.getTransaction().begin();

		URI uri = uriInfo.getAbsolutePath();

		Link previous = createPreviousHATEOAS(start, size, 5, itemCount, uri);
		Link next = createNextHATEOAS(start, size, 5, itemCount, uri);

		_logger.info("Querying items...");

		List<Item> items = em
				.createQuery(
						"select item from Item item WHERE item.dealItem = true"
								+ categoryQuery,
						Item.class)
				.setFirstResult(start).setMaxResults(size).getResultList();

		em.getTransaction().commit();

		for (Item item : items) {
			_logger.info("Item retrieved: " + item);
		}

		GenericEntity<List<Item>> entity = new GenericEntity<List<Item>>(
				items) {
		};

		ResponseBuilder builder = Response.ok(entity);
		if (previous != null) {
			builder.links(previous);
		}
		if (next != null) {
			builder.links(next);
		}
		return builder.build();
	}

	/**
	 * Sets specified item to deal. It also responds to the async responses that
	 * are watching the item for a deal
	 * 
	 * @param id
	 *            item id
	 * @param isDeal
	 */
	@PUT
	@Path("{id}/deal")
	public void setDealItem(@PathParam("id") long id, String isDeal) {
		_logger.info("Setting deal on item:" + id + " as " + isDeal);
		em.getTransaction().begin(); 
		Item item = em.find(Item.class, id);
		item.setDealItem(Boolean.valueOf(isDeal));
		em.persist(item);
		em.getTransaction().commit();

		if (Boolean.valueOf(isDeal)) {
			_logger.info("DEAL ALERT!!!");
			List<AsyncResponse> watchers = watchListResponses.get(id);
			if (watchers != null) {
				for (AsyncResponse toRespond : watchers) {
					_logger.info("Alerting watchers...");
					toRespond.resume(item);
				}
				watchers.clear();
			}
		}
	}

	/**
	 * Adds an item to a client's watch list, async response to notify
	 * 
	 * @param id
	 * @param asyncResponse
	 */
	@GET
	@Path("{id}/watch")
	public void addToWatchList(@PathParam("id") long id,
			@Suspended AsyncResponse asyncResponse) {
		_logger.info("Adding an item to a client's watchlist...");
		List<AsyncResponse> listOfResponses = watchListResponses.get(id);
		
		try {
			listOfResponses.add(asyncResponse);
		} catch (NullPointerException e){
			listOfResponses = new ArrayList<AsyncResponse>();
			listOfResponses.add(asyncResponse);
			watchListResponses.put(id, listOfResponses);
		}
	}

	/**
	 * Helper method for previous HATEOAS links
	 * 
	 * @param start
	 *            the starting index
	 * @param size
	 *            the interval size
	 * @param defaultSize
	 *            the default interval size
	 * @param count
	 *            the total number of elements available
	 * @param uri
	 *            the original uri from which the new one will be made
	 * @return
	 */
	private Link createPreviousHATEOAS(int start, int size, int defaultSize,
			Long count, URI uri) {
		if (start > 0) {
			_logger.info("Making previous link for " + uri);
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
			int newSize = start + size >= count ? defaultSize : size;
			return Link.fromUri(uri + "?start={start}&size={size}")
					.rel("previous").build(newStart, newSize);
		}

		return null;
	}

	/**
	 * Helper method for next HATEOAS links
	 * 
	 * @param start
	 *            the starting index
	 * @param size
	 *            the interval size
	 * @param defaultSize
	 *            the default interval size
	 * @param count
	 *            the total number of elements available
	 * @param uri
	 *            the original uri from which the new one will be made
	 * @return
	 */
	private Link createNextHATEOAS(int start, int size, int defaultSize,
			Long count, URI uri) {
		if (start + size < count) {
			// There are more items
			_logger.info("Making next link...");
			int nextSize;
			if (start + 2 * size <= count) {
				// Next step has even more items after
				nextSize = size;
			} else {
				// Next step doesn't have anymore after
				nextSize = (int) (count - start - size);
			}
			return Link.fromUri(uri + "?start={start}&size={size}").rel("next")
					.build(start + size, nextSize);
		}
		return null;
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
		// Category catElec = new Category("Electronics");
		Category catAudio = new Category("Audio"/* , catElec */);
		Category catGaming = new Category("Gaming"/* , catElec */);
		Category catFoodAndDrink = new Category("Food_And_Drink");
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
