package json;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import domain.Customer;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerJsonMarshaller
		implements
			MessageBodyReader<Customer>,
			MessageBodyWriter<Customer> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type == Customer.class;
	}

	@Override
	public long getSize(Customer t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Customer t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream)
			throws IOException, WebApplicationException {
		
//		JsonObject jsonObject = Json.createObjectBuilder()
//			.add("id", t.getId()).add("userName", t.getUserName()).add("firstName", t.getFirstName())
//			.add("lastName", t.getLastName()).add("shippingAddress", Json.t.getShippingAddress())
//			.build();
//
//	DataOutputStream outputStream = new DataOutputStream(entityStream);
//	outputStream.writeBytes(jsonObject.toString());
		
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type == Customer.class;
	}

	@Override
	public Customer readFrom(Class<Customer> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream)
			throws IOException, WebApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
