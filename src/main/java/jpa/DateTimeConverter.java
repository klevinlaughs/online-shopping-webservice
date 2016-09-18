package jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Converter for org.joda.time.DateTime to be stored in db
 * 
 * @author Kelvin
 *
 */
@Converter(autoApply = true)
public class DateTimeConverter implements AttributeConverter<DateTime, String> {

	@Override
	public String convertToDatabaseColumn(DateTime attribute) {
		return attribute.toString(DateTimeFormat.forPattern("[dd/MM/yyyy HH:mm:ss]"));
	}

	@Override
	public DateTime convertToEntityAttribute(String dbData) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("[dd/MM/yyyy HH:mm:ss]");
		return dtf.parseDateTime(dbData);
	}

}
