package com.example.controllers;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Category;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.Vegetable;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	@Query(value = "SELECT * FROM Customers WHERE Customers.customerID = ?1", nativeQuery = true)
	Customer findById(int customerID);
	
	@Query(value = "SELECT * FROM Customers WHERE Customers.customerID = ?1 AND Customers.password = ?2", nativeQuery = true)
	Customer findById(int customerID, String password);
}
