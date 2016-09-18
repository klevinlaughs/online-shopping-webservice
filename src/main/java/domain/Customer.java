package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

import jaxb.PurchaseHistoryAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String userName;
	private String firstName;
	private String lastName;
	private Address shippingAddress;
	@AttributeOverrides({
		@AttributeOverride(name="number", column=@Column(name="BILLING_NUMBER", nullable=false)),
		@AttributeOverride(name="street", column=@Column(name="BILLING_STREET", nullable=false)),
		@AttributeOverride(name="suburb", column=@Column(name="BILLING_SUBURB", nullable=false)),
		@AttributeOverride(name="city", column=@Column(name="BILLING_CITY", nullable=false)),
		@AttributeOverride(name="country", column=@Column(name="BILLING_COUNTRY", nullable=false)),
		@AttributeOverride(name="zipCode", column=@Column(name="BILLING_ZIPCODE", nullable=false))
	})
	private Address billingAddress;
	private CreditCard creditCard;
//	@XmlJavaTypeAdapter(PurchaseHistoryAdapter.class)
//	@XmlElementWrapper(name = "PurchaseHistory")
//	@XmlElement(name = "Item")
	@ElementCollection
	@OrderColumn
	@OneToMany(fetch = FetchType.LAZY)
	private List<Item> purchaseHistory = new ArrayList<Item>();
	private DateTime joinDate;
	private Image profilePic;

	/**
	 * Default constructor needed for persistent classes
	 */
	protected Customer() {
	}

	/**
	 * Creates a user with the required username field
	 * 
	 * @param userName
	 *            the username of the customer
	 */
	public Customer(String userName) {
		this.userName = userName;
		this.joinDate = new DateTime();
	}

	/**
	 * Adds an item to the purchase history
	 * 
	 * @param item
	 *            the item to add
	 */
	public void addToPurchaseHistory(Item item) {
		purchaseHistory.add(item);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public List<Item> getPurchaseHistory() {
		return purchaseHistory;
	}

	public void setPurchaseHistory(List<Item> purchaseHistory) {
		this.purchaseHistory = purchaseHistory;
	}

	public DateTime getJoinDate() {
		return joinDate;
	}
	
	public Image getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(Image profilePic) {
		this.profilePic = profilePic;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Customer:{ id:" + id)
		.append(", userName:" + userName)
		.append(", firstName:" + firstName)
		.append(", lastName:" + lastName)
		.append(", shippingAddress:" + shippingAddress)
		.append(", billingAddress:" + billingAddress)
		.append(", creditCard:" + creditCard)
		.append(", purchaseHistory:{");
		for (int i = 0; i < purchaseHistory.size() - 1; i++){
			sb.append("Item:" + purchaseHistory.get(i).getName() + ", ");
		}
		sb.append("Item:" + purchaseHistory.get(purchaseHistory.size() - 1))
		.append("}, joinDate:" + joinDate)
		.append(" }");
		return sb.toString();
	}

}
