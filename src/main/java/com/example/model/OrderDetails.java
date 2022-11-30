package com.example.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="ORDERDETAIL")
@IdClass(value= RelationshipIdOrderDetails.class)
public class OrderDetails implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="OrderID")
	public int orderID;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="VegetableID")
	public int vegetableID;
	@Column(name="Quantity")
	public int quantity;
	@Column(name="Price")
	public float price;
	
	public OrderDetails() {
		this.orderID = 0;
		this.vegetableID = 0;
		this.quantity = 0;
		this.price = 0f;
	}
	
	public OrderDetails(int orderID, int vegetableID, int quantity, float price) {
		this.orderID = orderID;
		this.vegetableID = vegetableID;
		this.quantity = quantity;
		this.price = price;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getVegetableID() {
		return vegetableID;
	}

	public void setVegetableID(int vegetableID) {
		this.vegetableID = vegetableID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "{\"OrderDetails\":{\"orderID:" + orderID + ", \"vegetableID\":" + vegetableID + ", \"quantity\":" + quantity
				+ ", \"price\":" + price + "}}";
	}
}
