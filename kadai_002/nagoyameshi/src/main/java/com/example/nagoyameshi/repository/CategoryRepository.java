package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Category;

 public interface CategoryRepository extends JpaRepository<Category, Integer> {
	 List<Category> findAll();
	 public Category getCategoryById(Integer id);
 }