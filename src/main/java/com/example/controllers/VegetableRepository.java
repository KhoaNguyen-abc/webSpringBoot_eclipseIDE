package com.example.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Vegetable;

@Repository
public interface VegetableRepository extends JpaRepository<Vegetable, Long>{
	
	@Query(value = "SELECT * FROM Vegetable "
			+ "WHERE Vegetable.catagoryID IN (?1) AND Vegetable.vegetableName LIKE ?2 AND Vegetable.vegetableID IN"
			+ "(SELECT Orderdetail.VegetableID FROM Orderdetail WHERE Orderdetail.quantity > ?3)", nativeQuery = true)
	Page<Vegetable> findAllByCategoryIDAndNameAndQuantity(List<Integer> idList, String search, int sold, Pageable pageable);
	
	@Query(value = "SELECT * FROM Vegetable WHERE Vegetable.vegetableID = ?1", nativeQuery = true)
	Vegetable findById(int id);
}
