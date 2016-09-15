package domain;

import java.util.Set;

import org.joda.time.DateTime;

public class Customer {
	
	private String userName;
	private String firstName;
	private String lastName;
	private Address shippingAddress;
	private Address billingAddress; 
	private Set<Item> purchaseHistory;
	private DateTime joinDate;
	
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
	
}
