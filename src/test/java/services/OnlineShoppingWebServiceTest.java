package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.Customer;
import domain.Image;
import domain.Item;
import domain.Review;

/**
 * Uses a bit of ParoleeWebServiceTest as scaffolding
 * 
 * @author Kelvin Lau
 *
 */
public class OnlineShoppingWebServiceTest {
	private static final String WEB_SERVICE_URI = "http://localhost:10000/services";

	private static final Logger _logger = LoggerFactory
			.getLogger(OnlineShoppingWebServiceTest.class);

	private static Client _client;

	/**
	 * One-time setup method that creates a Web service client.
	 */
	@BeforeClass
	public static void setUpClient() {
		_client = ClientBuilder.newClient();
	}

	/**
	 * Runs before each unit test to restore Web service database. This ensures
	 * that each test is independent; each test runs on a Web service that has
	 * been initialised with a common set of Parolees.
	 */
	// @Before
	public void reloadServerData() {
		Response response = _client.target(WEB_SERVICE_URI).request().put(null);
		response.close();

		// Pause briefly before running any tests. Test addParoleeMovement(),
		// for example, involves creating a timestamped value (a movement) and
		// having the Web service compare it with data just generated with
		// timestamps. Joda's Datetime class has only millisecond precision,
		// so pause so that test-generated timestamps are actually later than
		// timestamped values held by the Web service.
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * One-time finalisation method that destroys the Web service client.
	 */
	@AfterClass
	public static void destroyClient() {
		_client.close();
	}

	// ------------------------------ ITEMS TEST ------------------------------

	/**
	 * Tests /services/items with query parameters, minimal HATEOS testing.
	 */
	@Test
	public void getItemsDefault() {
		_logger.info("TEST: getItemsDefault");
		Response response = _client.target(WEB_SERVICE_URI + "/item")
				.queryParam("start", 2).queryParam("size", 3).request()
				.accept(MediaType.APPLICATION_XML).get();

		int status = response.getStatus();

		if (status != 200) {
			response.close();
			fail("Could not get items for some reason: " + status);
		}

		List<Item> items = response.readEntity(new GenericType<List<Item>>() {
		});

		_logger.info("Items retrieved: " + items);

		assertEquals(3, items.size());
		assertEquals(Long.valueOf(3), items.get(0).getId());

		Link previous = response.getLink("previous");
		_logger.info("previous link: " + previous);
		Link next = response.getLink("next");
		_logger.info("next link: " + next);

		response.close();

		assertNotNull(previous);
		assertNotNull(next);
	}

	/**
	 * Tests the default of /services/items, and also HATEOAS functionality
	 * testing
	 */
	@Test
	public void getItemsDefaultWithHATEOAS() {
		_logger.info("TEST: getItemsDefaultWithHATEOAS");
		Response response = _client.target(WEB_SERVICE_URI + "/item").request()
				.accept(MediaType.APPLICATION_XML).get();

		int status = response.getStatus();

		if (status != 200) {
			response.close();
			fail("Could not get items for some reason: " + status);
		}

		List<Item> items = response.readEntity(new GenericType<List<Item>>() {
		});

		_logger.info("Items retrieved: " + items);

		assertEquals(items.size(), 5);
		assertEquals(Long.valueOf(1), items.get(0).getId());

		Link previous = response.getLink("previous");
		_logger.info("previous link: " + previous);
		Link next = response.getLink("next");
		_logger.info("next link: " + next);

		response.close();

		assertNull(previous);
		assertNotNull(next);

		URI nextUri = next.getUri();
		assertEquals(WEB_SERVICE_URI + "/item?start=5&size=2",
				nextUri.toString());

		response = _client.target(nextUri).request()
				.accept(MediaType.APPLICATION_XML).get();

		if (response.getStatus() != 200) {
			fail("Could not get items from next link for some reason: "
					+ response.getStatus());
		}

		items = response.readEntity(new GenericType<List<Item>>() {
		});

		assertEquals(items.size(), 2);
		assertEquals(Long.valueOf(6), items.get(0).getId());

		previous = response.getLink("previous");
		_logger.info("previous link: " + previous);
		next = response.getLink("next");
		_logger.info("next link: " + next);

		response.close();

		assertNotNull(previous);
		assertNull(next);
	}

	/**
	 * Test whether you can get an item by id
	 */
	@Test
	public void getItemsByCategory() {
		_logger.info("TEST: getItemByCategory");
		Response response = _client.target(WEB_SERVICE_URI + "/item")
				.queryParam("category", "Food_And_Drink").request()
				.accept(MediaType.APPLICATION_XML).get();

		int status = response.getStatus();

		if (status != 200) {
			response.close();
			fail("Could not get items for some reason: " + status);
		}

		List<Item> items = response.readEntity(new GenericType<List<Item>>() {
		});

		_logger.info("Items retrieved: " + items);

		assertEquals(2, items.size());

		Link previous = response.getLink("previous");
		_logger.info("previous link: " + previous);
		Link next = response.getLink("next");
		_logger.info("next link: " + next);

		response.close();

		assertNull(previous);
		assertNull(next);
	}

	/**
	 * Test to get an item by id
	 */
	@Test
	public void getItemById() {
		_logger.info("TEST: getItemById");
		Item item = _client.target(WEB_SERVICE_URI + "/item/{id}")
				.resolveTemplate("id", 5).request()
				.accept(MediaType.APPLICATION_XML).get(Item.class);

		assertEquals(Long.valueOf(5), item.getId());
		assertEquals("Pepsi 2.5L", item.getName());
	}

	/**
	 * Test to get non existent item by id
	 */
	@Test
	public void getBadItem() {
		_logger.info("TEST: getBadItem");

		Response response = _client.target(WEB_SERVICE_URI + "/item/{id}")
				.resolveTemplate("id", 99).request()
				.accept(MediaType.APPLICATION_XML).get();
		int status = response.getStatus();
		response.close();

		assertEquals(404, status);

	}

	/**
	 * Test to get images for an item
	 */
	@Test
	public void getImagesForItem() {
		_logger.info("TEST: getImagesForItem");
		List<Image> images = _client
				.target(WEB_SERVICE_URI + "/item/{id}/image")
				.resolveTemplate("id", 1).request()
				.accept(MediaType.APPLICATION_XML)
				.get(new GenericType<List<Image>>() {
				});

		assertEquals(2, images.size());
		assertEquals("ps4.jpg", images.get(0).getName());
		assertEquals("xbox.png", images.get(1).getName());
		assertEquals(Integer.valueOf(1024), images.get(0).getWidth());
		assertEquals(Integer.valueOf(768), images.get(0).getHeight());
		assertNull(images.get(1).getHeight());
		assertNull(images.get(1).getWidth());
	}

	/**
	 * Test adding image to item
	 */
	@Test
	public void addImageToItem() {
		Image image = new Image("test.png");

		Response response = _client.target(WEB_SERVICE_URI + "/item/{id}/image")
				.resolveTemplate("id", 2).request()
				.accept(MediaType.APPLICATION_XML).post(Entity.xml(image));

		int status = response.getStatus();
		response.close();

		if (status != 204) {
			fail("Couldn't add image to item");
		}

		Item item = _client.target(WEB_SERVICE_URI + "/item/{id}")
				.resolveTemplate("id", 2).request()
				.accept(MediaType.APPLICATION_XML).get(Item.class);

		assertEquals(1, item.getImages().size());
		assertEquals("test.png", item.getImages().iterator().next().getName());
	}

	/**
	 * Test to get reviews for an item
	 */
	@Test
	public void getReviewsForItem() {
		_logger.info("TEST: getReviewsForItem");
		Set<Review> reviews = _client
				.target(WEB_SERVICE_URI + "/item/{id}/review")
				.resolveTemplate("id", 1).request()
				.accept(MediaType.APPLICATION_XML)
				.get(new GenericType<Set<Review>>() {
				});

		assertEquals(2, reviews.size());
	}

	/**
	 * Test to delete an item
	 */
	@Test
	public void deleteItem() {
		_logger.info("TEST: deleteItem");

		Response response = _client.target(WEB_SERVICE_URI + "/item/{id}")
				.resolveTemplate("id", 4).request().delete();

		int status = response.getStatus();
		response.close();

		if (status != 204) {
			fail("Could not delete item 1");
		}

		response = _client.target(WEB_SERVICE_URI + "/item/{id}")
				.resolveTemplate("id", 4).request().get();
		
		assertEquals(404, response.getStatus());
		response.close();
	}

	/**
	 * Test for async responses
	 */
	@Test
	public void testAsyncWatchList() {
		_logger.info("TEST: testAsyncWatchList");
		Client watchClient = ClientBuilder.newClient();
		final WebTarget target = watchClient
				.target(WEB_SERVICE_URI + "/item/{id}/watch")
				.resolveTemplate("id", 1);
		target.request().async().get(new InvocationCallback<Item>() {

			@Override
			public void completed(Item response) {
				_logger.info("Notified of item set to be deal : " + response);
			}

			@Override
			public void failed(Throwable throwable) {
			}

		});

		Response response = _client.target(WEB_SERVICE_URI + "/item/{id}/deal")
				.resolveTemplate("id", 1).request().put(Entity.xml("true"));

		assertEquals(204, response.getStatus());

		response.close();
	}

	// ----------------------------- CUSTOMER TESTS ----------------------------

	/**
	 * Test to login and logout
	 */
	@Test
	public void loginAndLogout() {
		_logger.info("TEST: login");

		String username = "x_sniper360noscope_x";

		Response response = _client.target(WEB_SERVICE_URI + "/customer/login")
				.request().post(Entity.xml(username));

		Map<String, NewCookie> cookies = response.getCookies();
		response.close();

		assertFalse(cookies.isEmpty());

		NewCookie usernameCookie = cookies.get("username");
		assertEquals(username, usernameCookie.getValue());

		response = _client.target(WEB_SERVICE_URI + "/customer/logout")
				.request().cookie(usernameCookie).get();
		cookies = response.getCookies();
		response.close();

		// Max age is 0 means deleted
		assertEquals(0, cookies.get("username").getMaxAge());

	}

	/**
	 * Testing a bad login (400)
	 */
	@Test
	public void badLogin() {
		_logger.info("TEST: badLogin");

		String username = "does-not-exist";

		Response response = _client.target(WEB_SERVICE_URI + "/customer/login")
				.request().post(Entity.xml(username));

		int status = response.getStatus();
		response.close();

		assertEquals(400, status);
	}

	/**
	 * Test to create customer
	 */
	@Test
	public void createCustomer() {
		Customer customer = new Customer("Bob");
		Response response = _client.target(WEB_SERVICE_URI + "/customer")
				.request().post(Entity.xml(customer));

		int status = response.getStatus();
		Map<String, NewCookie> cookies = response.getCookies();
		response.close();

		assertEquals("Bob", cookies.get("username").getValue());
		assertEquals(201, status);

		// logout since automatic login
		response = _client.target(WEB_SERVICE_URI + "/customer/logout")
				.request().get();
		cookies = response.getCookies();
		response.close();

		// Max age is 0 means deleted
		assertEquals(0, cookies.get("username").getMaxAge());
	}
}
