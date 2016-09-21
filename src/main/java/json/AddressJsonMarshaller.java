package json;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import domain.Address;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressJsonMarshaller
		implements
			MessageBodyWriter<Address>,
			MessageBodyReader<Address> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type == Address.class;
	}

	@Override
	public long getSize(Address t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Address t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream)
			throws IOException, WebApplicationException {

		JsonObject jsonObject = Json.createObjectBuilder()
				.add("number", t.getNumber()).add("street", t.getStreet())
				.add("suburb", t.getSuburb()).add("city", t.getCity())
				.add("country", t.getCountry()).add("zipCode", t.getZipCode())
				.build();

		DataOutputStream outputStream = new DataOutputStream(entityStream);
		outputStream.writeBytes(jsonObject.toString());
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type == Address.class;
	}

	@Override
	public Address readFrom(Class<Address> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream)
			throws IOException, WebApplicationException {
		
		JsonReader reader = Json.createReader(entityStream);
		return (Address) reader.readObject();
	}

}
