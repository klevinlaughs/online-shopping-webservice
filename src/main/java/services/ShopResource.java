package services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web service resource implementation for the Parolee application. An instance
 * of this class handles all HTTP requests for the Parolee Web service.
 * 
 * @author Ian Warren
 *
 */
@Path("/shop")
public class ShopResource {
	private static final Logger _logger = LoggerFactory.getLogger(ShopResource.class);
	
	public ShopResource() {
		reloadDatabase();
	}

	/**
    * Convenience method for testing the Web service. This method reinitialises 
    * the state of the Web service to hols three Parolee objects.
    */
	@PUT
	public void reloadData() {
		reloadDatabase();
	}
	/*
	*//**
	 * Returns a view of the Parolee database, represented as a List of
	 * nz.ac.auckland.parolee.dto.Parolee objects.
	 * 
	 *//*
	@GET
	@Produces("application/xml")
	public Response getParolees(@DefaultValue("1") @QueryParam("start") int start, 
			@DefaultValue("1") @QueryParam("size")int size,
			@Context UriInfo uriInfo) {
		URI uri = uriInfo.getAbsolutePath();
		
		Link previous = null;
		Link next = null;
		
		if(start > 1) {
			// There are previous Parolees - create a previous link.
			previous = Link.fromUri(uri + "?start={start}&size={size}")
					.rel("prev")
					.build(start - 1, size);
		}
		if(start + size <= _paroleeDB.size()) {
			// There are successive parolees - create a next link.
			_logger.info("Making NEXT link");
			next = Link.fromUri(uri + "?start={start}&size={size}")
					.rel("next")
					.build(start + 1, size);
		}

		// Create list of Parolees to return.
		List<nz.ac.auckland.parolee.dto.Parolee> parolees = 
				new ArrayList<nz.ac.auckland.parolee.dto.Parolee>();
		long paroleeId = start;
		for(int i = 0; i < size; i++) {
			Parolee parolee = _paroleeDB.get(paroleeId);
			parolees.add(ParoleeMapper.toDto(parolee));
		}
		
		// Create a GenericEntity to wrap the list of Parolees to return. This
		// is necessary to preserve generic type data when using any
		// MessageBodyWriter to handle translation to a particular data format.
		GenericEntity<List<nz.ac.auckland.parolee.dto.Parolee>> entity = 
				new GenericEntity<List<nz.ac.auckland.parolee.dto.Parolee>>(parolees) {};
		
		// Build a Response that contains the list of Parolees plus the link 
		// headers.
 		ResponseBuilder builder = Response.ok(entity);
 		if(previous != null) {
 			builder.links(previous);
 		}
 		if(next != null) {
 			builder.links(next);
 		}
 		Response response = builder.build();
 		
 		// Return the custom Response. The JAX-RS run-time will process this,
 		// extracting the List of Parolee objects and marshalling them into the
 		// HTTP response message body. In addition, since the Response object
 		// contains headers (previous and/or next), these will be added to the 
 		// HTTP response message. The Response object was created with the 200
 		// Ok status code, and this too will be added for the status header.
 		return response;
	}*/
	
//	protected Parolee findParolee(long id) {
//		return _paroleeDB.get(id);
//	}

	protected void reloadDatabase() {/*
		_paroleeDB = new ConcurrentHashMap<Long, Parolee>();
		_idCounter = new AtomicLong();

		// === Initialise Parolee #1
		long id = _idCounter.incrementAndGet();
		Address address = new Address("15", "Bermuda road", "St Johns", "Auckland", "1071");
		Parolee parolee = new Parolee(id,
				"Sinnen", 
				"Oliver", 
				Gender.MALE,
				new LocalDate(1970, 5, 26),
				address,
				new Curfew(address, new LocalTime(20, 00),new LocalTime(06, 30)));
		_paroleeDB.put(id, parolee);

		CriminalProfile profile = new CriminalProfile();
		profile.addConviction(new CriminalProfile.Conviction(new LocalDate(
				1994, 1, 19), "Crime of passion", Offence.MURDER,
				Offence.POSSESION_OF_OFFENSIVE_WEAPON));
		parolee.setCriminalProfile(profile);

		DateTime now = new DateTime();
		DateTime earlierToday = now.minusHours(1);
		DateTime yesterday = now.minusDays(1);
		GeoPosition position = new GeoPosition(-36.852617, 174.769525);

		parolee.addMovement(new Movement(yesterday, position));
		parolee.addMovement(new Movement(earlierToday, position));
		parolee.addMovement(new Movement(now, position));
		
		// === Initialise Parolee #2
		id = _idCounter.incrementAndGet();
		address = new Address("22", "Tarawera Terrace", "St Heliers", "Auckland", "1071");
		parolee = new Parolee(id,
				"Watson", 
				"Catherine", 
				Gender.FEMALE,
				new LocalDate(1970, 2, 9),
				address,
				null);
		_paroleeDB.put(id, parolee);
		
		// === Initialise Parolee #3
		id = _idCounter.incrementAndGet();
		address = new Address("67", "Drayton Gardens", "Oraeki", "Auckland", "1071");
		parolee = new Parolee(id,
				"Giacaman", 
				"Nasser", 
				Gender.MALE,
				new LocalDate(1980, 10, 19),
				address,
				null);
		_paroleeDB.put(id, parolee);*/
	}
}
