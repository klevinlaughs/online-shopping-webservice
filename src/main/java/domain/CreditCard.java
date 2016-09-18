package domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Embeddable
public class CreditCard {

	@Column(nullable = false)
	private String cardNumber;
	@Column(nullable = false)
	private String expiryMonth;
	@Column(nullable = false)
	private String expiryYear;

	/**
	 * Default constructor needed for persistent classes
	 */
	protected CreditCard() {
	}

	/**
	 * Initializes all fields
	 * 
	 * @param cardNumber
	 * @param expiryMonth
	 * @param expiryYear
	 */
	public CreditCard(String cardNumber, String expiryMonth, String expiryYear) {
		this.cardNumber = cardNumber;
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ cardNumber:" + cardNumber)
		.append(", expiryMonth:" + expiryMonth)
		.append(", expiryYear:" + expiryYear)
		.append(" }");
		return sb.toString();
	}

}
