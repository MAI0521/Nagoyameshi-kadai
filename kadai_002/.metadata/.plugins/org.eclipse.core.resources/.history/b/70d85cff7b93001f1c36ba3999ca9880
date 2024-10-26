package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{

	public Page<Review> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant, Pageable pageable);
//	public List<Review> findTop6ByOrderByCreatedAtDesc();
	
	@Query("SELECT r.id, COUNT(rv.id) FROM Restaurant r LEFT JOIN Review rv ON r.id = rv.restaurant.id GROUP BY r.id")
	List<Object[]> countReviewsPerRestaurant();
	
	@Query("SELECT r.id, COALESCE(AVG(rv.starId), 0) FROM Restaurant r LEFT JOIN Review rv ON r.id = rv.restaurant.id GROUP BY r.id")
	List<Object[]> averageReviewsPerRestaurant(); 
}
