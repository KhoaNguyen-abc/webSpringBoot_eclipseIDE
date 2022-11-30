package com.example.model;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Generated;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="CATEGORY")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="CatagoryID")
	public int catagoryID;
	@Column(name="Name")
	public String name;
	@Column(name="Description")
	public String description;
	
	@OneToMany(fetch= FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
	public Set<Vegetable> vegetableList = new HashSet<Vegetable>();

	public Category() {
		this.catagoryID = 0;
		this.name = null;
		this.description = null;
		this.vegetableList = null;
	}

	public Category(int catagoryID, String name, String description, Set<Vegetable> vegetableList) {
		this.catagoryID = catagoryID;
		this.name = name;
		this.description = description;
		this.vegetableList = vegetableList;
	}

	public int getCatagoryID() {
		return catagoryID;
	}

	public void setCatagoryID(int catagoryID) {
		this.catagoryID = catagoryID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Vegetable> getVegetableList() {
		return vegetableList;
	}

	public void setVegetableList(Set<Vegetable> vegetableList) {
		this.vegetableList = vegetableList;
	}
	
	public String getAllVegetables() {
		String str = "";
		for(Vegetable item: vegetableList) {
			str += "{\"vegetableID\":" + item.getVegetableID() + ", \"catagoryID\":" + item.getCatagoryID() + ", \"vegetableName\":\""+
					item.getVegetableName() + "\", \"unit\":\"" + item.getUnit() + "\", \"amount\":" + item.getAmount() +
					", \"image\":\"" + item.getImage() + "\", \"price\":" + item.getPrice() + "},";
		}
		return str;
	}

	@Override
	public String toString() {
		String strLast = getAllVegetables().substring(0, getAllVegetables().length()-1);
		return "{\"Category\": {\"catagoryID\":"+ catagoryID +", \"name\":\""+ name +"\", \"description\":\""+ description +"\"}, \"vegetableList\":["
				+ strLast + "]";
	}
	
}
