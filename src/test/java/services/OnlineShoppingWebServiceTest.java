package services;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import domain.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses a bit of ParoleeWebServiceTest as scaffolding
 * 
 * @author Kelvin Lau
 *
 */
public class OnlineShoppingWebServiceTest {
	private static final String WEB_SERVICE_URI = "http://localhost:10000/services/item";

	private static final Logger _logger = LoggerFactory.getLogger(OnlineShoppingWebServiceTest.class);

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

	@Test
	public void getItemsDefault() {
		Response response = _client.target(WEB_SERVICE_URI).request().accept("application/xml").get();

		List<Item> items = response.readEntity(new GenericType<List<Item>>() {
		});

		_logger.info("Items retrieved: " + items);

		// List<Item> items = _client.target(WEB_SERVICE_URI)
		// .request()
		// .accept("application/xml")
		// .get(new GenericType<List<Item>>(){});

		assertEquals(items.size(), 5);
		assertEquals(Long.valueOf(1), items.get(0).getId());

		// TODO check links, also implement equals and hashcode for images, maybe for items?
		
		Link previous = response.getLink("previous");
		_logger.info("previous link: " + previous);
		Link next = response.getLink("next");
		_logger.info("next link: " + next);
				
		response.close();
		
		assertNull(previous);
		assertNotNull(next);

	}

}
