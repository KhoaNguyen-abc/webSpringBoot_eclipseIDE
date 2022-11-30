package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	@Column(name="nameStudent")
	private String name;
	@Column(name="phoneNumber")
	private String phone;
	@Column(name="create_at")
	private String createAt;
	@Column(name="updated_at")
	private String updateAt;
	
	public Student(Integer id, String name, String phone, String createAt, String updateAt) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.createAt = createAt;
		this.updateAt = updateAt;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public String getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", phone=" + phone + ", createAt=" + createAt + ", updateAt="
				+ updateAt + "]";
	}
	
}
