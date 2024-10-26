package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;  
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final RestaurantService restaurantService;         
    
    public RestaurantController(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, ReviewRepository reviewRepository, FavoriteRepository favoriteRepository, RestaurantService restaurantService) {
        this.restaurantRepository = restaurantRepository;  
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
        this.restaurantService = restaurantService;
    }
    
    @Autowired
    private ReviewService reviewService;
    
    @GetMapping
    public String index(@RequestParam(name = "keywords", required = false) String keywords,  
    		 			@RequestParam(name = "category", required = false) Integer categoryId,
                        @RequestParam(name = "budgetRange", required = false) Integer budgetRange, 
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
    	Page<Restaurant> restaurantPage; 
    	 
        List<Category> categories = categoryRepository.findAll();  
//        Objectとして返すと、中身の参照が
        List<Object[]> reviewCounts = reviewService.getReviewCountPerRestaurant();
        List<Object[]> averageReviews = reviewService.getAverageReviewsPerRestaurant();
        
        
//        reviewCounts.sort(Comparator.comparingDouble((Object[] arr) -> (Long) arr[1]).reversed());
                
    	if (categoryId != null) {
    		if (order != null && order.equals("budgetRangeAsc")) {
    			restaurantPage = restaurantRepository.findByCategoryIdOrderByBudgetRangeAsc(categoryId, pageable); 
    		} else if (order != null && order.equals("budgetRangeDesc")) {
    			restaurantPage = restaurantRepository.findByCategoryIdOrderByBudgetRangeDesc(categoryId, pageable);
    		} else if (order != null && order.equals("reviewCountDesc")) {
        		restaurantPage = restaurantRepository.findByCategoryIdSortedByReviewCount(categoryId, pageable);
        	} else if (order != null && order.equals("averageScoreDesc")) {
            		restaurantPage = restaurantRepository.findByCategoryIdSortedByAverageScore(categoryId, pageable);
    		} else {
    			restaurantPage = restaurantRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable); 
    		}
    		
        } else if (keywords != null && !keywords.isEmpty()) {
        	if (order != null && order.equals("budgetRangeAsc")) {
        		restaurantPage = restaurantRepository.findByVenueNameOrDescriptionLikeOrderByBudgetRangeAsc(keywords, pageable);  
        	} else if (order != null && order.equals("budgetRangeDesc")) {
        		restaurantPage = restaurantRepository.findByVenueNameOrDescriptionLikeOrderByBudgetRangeDesc(keywords, pageable); 
        	} else if (order != null && order.equals("reviewCountDesc")) {
        		restaurantPage = restaurantRepository.findByVenueNameOrDescriptionLikeSortedByReviewCount(keywords, pageable);
        	} else if (order != null && order.equals("averageScoreDesc")) {
        		restaurantPage = restaurantRepository.findByVenueNameOrDescriptionLikeSortedByAverageScore(keywords, pageable);
        	} else {
        		restaurantPage = restaurantRepository.findByVenueNameOrDescriptionLikeOrderByCreatedAtDesc(keywords, pageable); 
        	}
        } else if (budgetRange != null){
        	if (order != null && order.equals("budgetRangeAsc")) {
        		restaurantPage = restaurantRepository.findByBudgetRangeOrderByBudgetRangeAsc(budgetRange, pageable);
        	} else if (order != null && order.equals("budgetRangeDesc")) {
        		restaurantPage = restaurantRepository.findByBudgetRangeOrderByBudgetRangeDesc(budgetRange, pageable);
        	} else if (order != null && order.equals("reviewCountDesc")) {
        		restaurantPage = restaurantRepository.findByBudgetRangeSortedByReviewCount(budgetRange, pageable);
        	} else if (order != null && order.equals("averageScoreDesc")) {
        		restaurantPage = restaurantRepository.findByBudgetRangeSortedByAverageScore(budgetRange, pageable);
        	} else {
        		restaurantPage = restaurantRepository.findByBudgetRangeOrderByCreatedAtDesc(budgetRange, pageable);
        	}
        } else {
        	if (order != null && order.equals("budgetRangeAsc")) {
        		restaurantPage = restaurantRepository.findAllByOrderByBudgetRangeAsc(pageable);
        	} else if (order != null && order.equals("budgetRangeDesc")) {
        		restaurantPage = restaurantRepository.findAllByOrderByBudgetRangeDesc(pageable);
        	} else if (order != null && order.equals("reviewCountDesc")) {
        		restaurantPage = restaurantRepository.findAllSortedByReviewCount(pageable);
        	} else if (order != null && order.equals("averageScoreDesc")) {
        		restaurantPage = restaurantRepository.findAllSortedByAverageScore(pageable);
        	} else {
        		restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);
        	}
        }
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keywords", keywords);
        model.addAttribute("budgetRange", budgetRange);
        model.addAttribute("order", order);
        model.addAttribute("reviewCounts", reviewCounts);
        model.addAttribute("averageReviews", averageReviews);
        
        return "restaurants/index";
    }
   
    
    @GetMapping("/{restaurantId}")
    public String getRestaurantById(@PathVariable("restaurantId") Integer restaurantId,
   		 @PageableDefault(size = 10) Pageable pageable, 
         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
         Model model) {
    	Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
   	 	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName(); 
   	 	Page<Review> review = reviewRepository.findByRestaurantOrderByCreatedAtDesc(restaurant, PageRequest.of(0, 6));
		 
	     model.addAttribute("restaurant", restaurant); 
	     model.addAttribute("review",review);
	     model.addAttribute("currentUserName", currentUserName);
	     if (userDetailsImpl != null) {
	    	 com.example.nagoyameshi.entity.User user = userDetailsImpl.getUser();
	         Favorite favorite = favoriteRepository.findByRestaurantAndUserOrderByCreatedAtDesc(restaurant, user);
	         model.addAttribute("favorite", favorite);
	     } else {
	         model.addAttribute("favorite", null); // Handle the case where userDetailsImpl is null
	     }
	     
	     if (restaurant != null) {
	         model.addAttribute("restaurant", restaurant);
	     } else {
	         // Handle the case where the house is not found
	         return "redirect:/error";
	     }
	     
	     model.addAttribute("reservationInputForm", new ReservationInputForm());
	     return "restaurants/show";
    }

}
