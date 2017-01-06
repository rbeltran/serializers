package com.jphat;

import java.io.Serializable;

public class Address implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7556991401568607891L;
	
	String street;
	String city;
	String state;
	String country;	
	
	public Address() {
		
	}
	
	public Address( String street, String city, String state, String country ) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.country = country;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean equals(Object obj) {
		if( obj != null && obj instanceof User) {
			Address other = (Address)obj;
			return street.equals(other.street) && 
					city.equals(other.city) && 
					state.equals(other.state) && 
					country.equals(other.country);
		}
		return false;
	}
}
