package domain;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Customer {

	private Long id;

	private String userName;
	private String firstName;
	private String lastName;
	private Address shippingAddress;
	private Address billingAddress;
	@XmlElementWrapper(name = "PurchaseHistory")
	@XmlElement(name = "Item")
	@ElementCollection
	@OneToMany(fetch=FetchType.LAZY)
	private Set<Item> purchaseHistory;
	private DateTime joinDate;
	private Image profilePic;

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

	public Set<Item> getPurchaseHistory() {
		return purchaseHistory;
	}

	public void setPurchaseHistory(Set<Item> purchaseHistory) {
		this.purchaseHistory = purchaseHistory;
	}

	public DateTime getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(DateTime joinDate) {
		this.joinDate = joinDate;
	}

	public Image getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(Image profilePic) {
		this.profilePic = profilePic;
	}

}
