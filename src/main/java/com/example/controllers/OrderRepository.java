package com.example.controllers;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Category;
import com.example.model.Customer;
import com.example.model.Order;
import com.example.model.Vegetable;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}