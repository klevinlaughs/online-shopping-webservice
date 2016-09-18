package domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class Address {

	@Column(nullable = false)
	private int number;
	@Column(nullable = false)
	private String street;
	@Column(nullable = false)
	private String suburb;
	@Column(nullable = false)
	private String city;
	@Column(nullable = false)
	private String country;
	@Column(nullable = false)
	private String zipCode;

	/**
	 * Default constructor needed for persistent classes
	 */
	protected Address() {
	}

	/**
	 * Initializes all fields
	 * 
	 * @param number
	 * @param street
	 * @param suburb
	 * @param city
	 * @param country
	 * @param zipCode
	 */
	public Address(int number, String street, String suburb, String city, String country, String zipCode) {
		this.number = number;
		this.street = street;
		this.suburb = suburb;
		this.city = city;
		this.country = country;
		this.zipCode = zipCode;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ")
		.append(number + " ")
		.append(street + " ")
		.append(suburb + " ")
		.append(city + " ")
		.append(country + " ")
		.append(zipCode + " ")
		.append("}");
		return sb.toString();
	}
}
