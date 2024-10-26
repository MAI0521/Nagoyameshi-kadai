package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;

 @Controller
public class HomeController {
	 private final RestaurantService restaurantService;
	 private final ReviewService reviewService;
	 private final CategoryRepository categoryRepository;

    public HomeController(RestaurantService restaurantService, ReviewService reviewService, CategoryRepository categoryRepository) {
        this.restaurantService = restaurantService;
        this.reviewService = reviewService;
        this.categoryRepository = categoryRepository;
    }
	    
     @GetMapping("/")
     public String index(Model model) {
    	 List<Restaurant> newRestaurants = restaurantService.findTop8RestaurantsByOrderByCreatedAtDesc();
         List<Object[]> reviewCounts = reviewService.getReviewCountPerRestaurant();
         List<Object[]> averageReviews = reviewService.getAverageReviewsPerRestaurant();
         List<Restaurant> popularRestaurants = restaurantService.findTop3RestaurantsByOrderByReviewCountDesc();
         List<Category> categories = categoryRepository.findAll();

         model.addAttribute("categories", categories);
         model.addAttribute("newRestaurants", newRestaurants);
         model.addAttribute("popularRestaurants", popularRestaurants);
         model.addAttribute("reviewCounts", reviewCounts);
         model.addAttribute("averageReviews", averageReviews);
         return "index";
     }   
}

