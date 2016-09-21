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

import domain.CreditCard;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CreditCardJsonMarshaller
		implements
			MessageBodyWriter<CreditCard>,
			MessageBodyReader<CreditCard> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type == CreditCard.class;
	}

	@Override
	public CreditCard readFrom(Class<CreditCard> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream)
			throws IOException, WebApplicationException {
		JsonReader reader = Json.createReader(entityStream);
		return (CreditCard) reader.readObject();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type == CreditCard.class;
	}

	@Override
	public long getSize(CreditCard t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(CreditCard t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream)
			throws IOException, WebApplicationException {

		JsonObject jsonObject = Json.createObjectBuilder()
				.add("cardNumber", t.getCardNumber())
				.add("expiryMonth", t.getExpiryMonth())
				.add("expiryYear", t.getExpiryYear()).build();

		DataOutputStream outputStream = new DataOutputStream(entityStream);
		outputStream.writeBytes(jsonObject.toString());

	}

}
