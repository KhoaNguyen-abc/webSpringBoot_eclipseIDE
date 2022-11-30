package com.example.model;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="CUSTOMERS")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CustomerID")
	public int customerID;
	@Column(name="Password")
	public String password;
	@Column(name="Fullname")
	public String fullname;
	@Column(name="Address")
	public String address;
	@Column(name="City")
	public String city;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="customer", cascade = CascadeType.ALL)
	public Set<Order> orderList = new HashSet<Order>();
	
	public Customer() {
		this.customerID = 0;
		this.password = null;
		this.fullname = null;
		this.address = null;
		this.city = null;
		this.orderList = null;
	}

	public Customer(int customerID, String password, String fullname, String address, String city,
			Set<Order> orderList) {
		this.customerID = customerID;
		this.password = password;
		this.fullname = fullname;
		this.address = address;
		this.city = city;
		this.orderList = orderList;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Set<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(Set<Order> orderList) {
		this.orderList = orderList;
	}
	
	public String getAllOrders() {
		String str = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for(Order item: orderList) {
			String outDate = dateFormat.format(item.getDate()); 
			str += "{\"orderID\":" + item.getOrderID() + ", \"customerID\":" + item.getCustomerID() + ", \"date\":\"" + outDate +
					"\", \"total\":" + item.getTotal() + ", \"note\":\"" + item.getNote() + "\"},";
		}
		return str;
	}

	@Override
	public String toString() {
		String strLast = getAllOrders().substring(0, getAllOrders().length()-1);
		return "{\"Customer\":{\"customerID\":" + customerID + ", \"password\":\"" + password + "\", \"fullname\":\"" + fullname + "\", \"address\":\""
				+ address + "\", \"city\":\"" + city + "\", \"orderList\":[" + strLast + "]";
	}
		
}
