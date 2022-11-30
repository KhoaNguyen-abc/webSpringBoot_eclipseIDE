package com.example.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class RelationshipIdOrderDetails implements Serializable{
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="OrderID")
	public int orderID;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="VegetableID")
	public int vegetableID;
	
	public RelationshipIdOrderDetails() {
		this.orderID = 0;
		this.vegetableID = 0;
	}

	public RelationshipIdOrderDetails(int orderID, int vegetableID) {
		this.orderID = orderID;
		this.vegetableID = vegetableID;
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

	@Override
	public String toString() {
		return "RelationshipIdOrderDetails [orderID=" + orderID + ", vegetableID=" + vegetableID + "]";
	}
	 
}
