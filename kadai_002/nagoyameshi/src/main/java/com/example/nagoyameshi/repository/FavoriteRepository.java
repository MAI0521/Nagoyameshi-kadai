package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Favorite findByRestaurantAndUserOrderByCreatedAtDesc(Restaurant restaurant, User user);

	List <Favorite> findByRestaurantAndUser(Restaurant restaurant, User user);

	Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

	void deleteByRestaurantId(Integer id);
}
