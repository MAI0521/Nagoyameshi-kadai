package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Category;

 public interface CategoryRepository extends JpaRepository<Category, Integer> {
	 List<Category> findAll();
	 @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
	 List<Category> findByKeyword(@Param("keyword") String keyword);
	 public Category getCategoryById(Integer id);
 }