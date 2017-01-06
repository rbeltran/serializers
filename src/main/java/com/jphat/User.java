package com.jphat;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7576837746114486888L;
	
	Address homeAddress;
	Address businessAddress;
	String firstName;
	String lastName;
	String phone;
	HashMap<String, String> properties;
	
	public User() {
	}
	
	public User(Address homeAddress, Address businessAddress, String firstName, String lastName, String phone,
			HashMap<String, String> properties) {
		super();
		this.homeAddress = homeAddress;
		this.businessAddress = businessAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.properties = properties;
	}
	public Address getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}
	public Address getBusinessAddress() {
		return businessAddress;
	}
	public void setBusinessAddress(Address businessAddress) {
		this.businessAddress = businessAddress;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public HashMap<String, String> getProperties() {
		return properties;
	}
	public void setProperties(HashMap<String, String> itemMap) {
		this.properties = itemMap;
	}
	public boolean equals(Object obj) {
		if( obj != null && obj instanceof User ) {
			User other = (User) obj;
			return homeAddress.equals(other.homeAddress ) &&
					businessAddress.equals(other.businessAddress) &&
					firstName.equals(other.firstName) &&
					lastName.equals(other.lastName) &&
					phone.equals(other.phone) &&
					properties.equals(other.properties);
		}
		return false;
	}
}
