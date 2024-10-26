package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Autowired
    public  FavoriteService(FavoriteRepository favoriteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Favorite getFavoritesForRestaurant(Restaurant restaurant, User user) {
    	return favoriteRepository.findByRestaurantAndUserOrderByCreatedAtDesc(restaurant,user);
    }

    @Transactional
    public void addFavorite(Integer restaurantId, User user) {
    	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
    	

        // Create a new Favorites entity
        Favorite favorite = new Favorite();
        favorite.setRestaurant(restaurant);
        favorite.setUser(user);
       

        // Save the Favorites entity
        favoriteRepository.save(favorite);
    }
    
    @Transactional
	public void deleteFavoritesByRestaurantIdAndUser(Integer restaurantId, User user) {
    	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
    	
    	List<Favorite> favorites = favoriteRepository.findByRestaurantAndUser(restaurant, user);
        for (Favorite favorite : favorites) {
            favoriteRepository.delete(favorite);
		
	}
}
}