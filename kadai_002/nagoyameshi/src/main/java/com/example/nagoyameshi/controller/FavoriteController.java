package com.example.nagoyameshi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;
 
 @Controller
 @RequestMapping
 
public class FavoriteController {
	private final RestaurantRepository restaurantRepository; 
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteService favoriteService;
    
    @Autowired
    public FavoriteController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository, FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
        this.restaurantRepository = restaurantRepository;     
        this.reviewRepository = reviewRepository;
        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
    }
    
    @GetMapping("/favorites")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetails, 
    		@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.ASC) Pageable pageable, Model model) {
        User user = userDetails.getUser();
        Page<Favorite> favoritesPage = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        
        model.addAttribute("favoritesPage", favoritesPage);         
        
        return "favorites/index";
    }

    @PostMapping ("/restaurants/{restaurantId}/favorites")
    public String addFavorite(
    		@PathVariable("restaurantId") Integer restaurantId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
    		Model model) {
//    		Favorite favorite = favoriteRepository.findById(userId).orElse(null);
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String currentUserName = authentication.getName();

    	 System.out.println("Current User Name: " + currentUserName);
    	 System.out.println("Current Restaurant Name: " + restaurantId);
    	 
    	 favoriteService.addFavorite(restaurantId, userDetails.getUser());

        return "redirect:/restaurants/{restaurantId}";
    }
    
   
    @PostMapping ("/restaurants/{restaurantId}/favorites-delete")
    public String deleteFavorite(
    		@PathVariable("restaurantId") Integer restaurantId,
    		@AuthenticationPrincipal UserDetailsImpl userDetails,
            RedirectAttributes redirectAttributes) {
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentUserName = authentication.getName();
    		
			favoriteService.deleteFavoritesByRestaurantIdAndUser(restaurantId, userDetails.getUser());
        return "redirect:/restaurants/{restaurantId}";
    }

}
