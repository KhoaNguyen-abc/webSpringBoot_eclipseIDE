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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="VEGETABLE")
public class Vegetable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="VegetableID")
	public int vegetableID;
	@Column(name="CatagoryID")
	public int catagoryID;
	@Column(name="VegetableName")
	public String vegetableName;
	@Column(name="Unit")
	public String unit;
	@Column(name="Amount")
	public int amount;
	@Column(name="Image")
	public String image;
	@Column(name="Price")
	public float price;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CatagoryID", referencedColumnName = "CatagoryID", insertable = false, updatable = false)
	public Category category;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="ORDERDETAIL",
			joinColumns ={@JoinColumn(name="VegetableID", referencedColumnName = "VegetableID", nullable = false)},
			inverseJoinColumns = {@JoinColumn(name="OrderID", referencedColumnName = "OrderID", nullable = false)})
	public Set<Order> orderVegetableList = new HashSet<Order>();
	
	public Vegetable() {
		this.vegetableID = 0;
		this.catagoryID = 0;
		this.vegetableName = null;
		this.unit = null;
		this.amount = 0;
		this.image = null;
		this.price = 0f;
		this.category = null;
		this.orderVegetableList = null;
	}


	public Vegetable(int vegetableID, int catagoryID, String vegetableName, String unit, int amount, String image,
			float price, Category category, Set<Order> orderVegetableList) {
		this.vegetableID = vegetableID;
		this.catagoryID = catagoryID;
		this.vegetableName = vegetableName;
		this.unit = unit;
		this.amount = amount;
		this.image = image;
		this.price = price;
		this.category = category;
		this.orderVegetableList = orderVegetableList;
	}


	public int getVegetableID() {
		return vegetableID;
	}


	public void setVegetableID(int vegetableID) {
		this.vegetableID = vegetableID;
	}


	public int getCatagoryID() {
		return catagoryID;
	}


	public void setCatagoryID(int catagoryID) {
		this.catagoryID = catagoryID;
	}


	public String getVegetableName() {
		return vegetableName;
	}


	public void setVegetableName(String vegetableName) {
		this.vegetableName = vegetableName;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public Set<Order> getOrderVegetableList() {
		return orderVegetableList;
	}


	public void setOrderVegetableList(Set<Order> orderVegetableList) {
		this.orderVegetableList = orderVegetableList;
	}

	public String getAllOrdersVegetable() {
		String str = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for(Order item: orderVegetableList) {
			String outDate = dateFormat.format(item.getDate()); 
			str +="{\"orderID\":" + item.getOrderID() + ", \"customerID\":" + item.getCustomerID() + ", \"date\":\"" + outDate +
					"\", \"total\":" + item.getTotal() + ", \"note\":\"" + item.getNote() + "\"},";
		}
		return str;
	}

	@Override
	public String toString() {
		String strLast = getAllOrdersVegetable().substring(0, getAllOrdersVegetable().length()-1);
		return "{\"vegetableID\":"+ vegetableID +", \"catagoryID\":"+ catagoryID +", \"vegetableName\":\""+ vegetableName +"\", \"unit\":\""+ unit +"\", \"amount\":"+ amount +", \"image\":\""+ image +"\","
				+ " \"price\":"+ price +", \"category\":"+ category +", \"orderVegetableList\":["+ strLast +"]}";
	}
	
}
