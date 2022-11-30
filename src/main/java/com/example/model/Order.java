package com.example.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="`ORDER`")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="OrderID")
	public int orderID;
	@Column(name="CustomerID")
	public int customerID;
	@Column(name="Date")
	public Date date;
	@Column(name="Total")
	public float total;
	@Column(name="Note")
	public String note;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CustomerID", referencedColumnName = "CustomerID", insertable = false, updatable = false)
	public Customer customer;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="ORDERDETAIL",
			joinColumns ={@JoinColumn(name="OrderID", referencedColumnName = "OrderID", nullable = false)},
			inverseJoinColumns = {@JoinColumn(name="VegetableID", referencedColumnName = "VegetableID", nullable = false)})
	public Set<Vegetable> vegetableOrderList = new HashSet<Vegetable>();

	public Order() {
		this.customerID = 0;
		this.date = null;
		this.total = 0f;
		this.note = null;
		this.customer = null;
		this.vegetableOrderList = null;
	}

	public Order(int customerID, Date date, float total, String note, Customer customer,
			Set<Vegetable> vegetableOrderList) {
		this.customerID = customerID;
		this.date = date;
		this.total = total;
		this.note = note;
		this.customer = customer;
		this.vegetableOrderList = vegetableOrderList;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Vegetable> getVegetableOrderList() {
		return vegetableOrderList;
	}

	public void setVegetableOrderList(Set<Vegetable> vegetableOrderList) {
		this.vegetableOrderList = vegetableOrderList;
	}
	
	public String getAllVegetablesOrder() {
		String str = "";
		for(Vegetable item: vegetableOrderList) {
			str +="{\"vegetableID\":" + item.getVegetableID() + ", \"catagoryID\":" + item.getCatagoryID() + ", \"vegetableName\":\""
					+ item.getVegetableName() + "\", \"unit\":" + item.getUnit() + "\", \"amount\":" + item.getAmount() + ", \"image\":\"" + item.getImage()
					+"\", \"price\":" + item.getPrice() + "},";
		}
		return str;
	}

	@Override
	public String toString() {
		String strLast = getAllVegetablesOrder().substring(0, getAllVegetablesOrder().length()-1);
		return "{\"orderID\":" + orderID + ", \"customerID\":" + customerID + ", \"date\":\"" + date + "\", \"total\":" + total
				+ ", \"note\":\"" + note + "\", \"customer\":" + customer + ", \"vegetableOrderList\":[" + strLast + "]}";
	}
}
