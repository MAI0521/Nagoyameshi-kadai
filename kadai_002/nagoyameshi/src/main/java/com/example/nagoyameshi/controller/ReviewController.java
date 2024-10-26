package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/restaurants/{id}/reviews")
public class ReviewController {
	 private static final UserDetailsImpl userDetailsImpl = null;
	 private final ReviewRepository reviewRepository;    
	 private final RestaurantRepository restaurantRepository;
	 private final UserRepository userRepository;
     private final ReviewService reviewService; 
     
    
     public ReviewController(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository, UserRepository userRepository, ReviewService reviewService) {        
        this.reviewRepository = reviewRepository; 
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.reviewService = reviewService;
    }
     
     @GetMapping
     public String getReviews(@PathVariable(name="id") Integer id,
    		 @RequestParam(name = "page", defaultValue = "0") int page,
    		 @PageableDefault(page = 0, size = 10) Pageable pageable, Model model) {
    	 Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String currentUserName = authentication.getName(); 
      
         
    	 if (restaurant != null) {
    		 Page<Review> reviewPage = reviewRepository.findByRestaurantOrderByCreatedAtDesc(restaurant, PageRequest.of(page, 10));
    	        model.addAttribute("restaurant", restaurant);
    	        model.addAttribute("reviewPage", reviewPage);
    	        model.addAttribute("review",reviewPage);
    	        model.addAttribute("currentUserName", currentUserName);
    	        return "reviews/index";		
    	    } else {
    	        return "error/404"; 
    	    }
     }
     
     @GetMapping("/input")
     public String input(
    		 @PathVariable("id") Integer id,
             @ModelAttribute @Validated ReviewForm reviewForm,
             BindingResult bindingResult,
    		 Model model) { 
    	 Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
    	 if (restaurant != null) {
           model.addAttribute("restaurant", restaurant);
           model.addAttribute("reviewForm", reviewForm);
           return "reviews/input";
       } else {
           return "error/404";
       }
     }   
     
     
     @PostMapping("/input")
     public String update(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		 @PathVariable("id") Integer id,
    		 @RequestBody @ModelAttribute @Validated ReviewForm reviewForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    	 if (bindingResult.hasErrors()) {
             return "reviews/input";
         }
         reviewService.create(userDetailsImpl, reviewForm);
         redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
         
         return "redirect:/restaurants/{id}";
     }  
     
     @GetMapping("/{reviewId}/edit")
     public String edit(@PathVariable("reviewId") Integer reviewId, 
             @PathVariable("id") Integer id, 
             Model model) {
		Review review = reviewRepository.findById(reviewId).orElse(null);
		if (review != null) {
		  ReviewEditForm reviewEditForm = new ReviewEditForm(
		      reviewId, review.getStarId(), review.getComment());
		  Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
		  model.addAttribute("reviewEditForm", reviewEditForm);
		  model.addAttribute("review", review);
		  model.addAttribute("restaurant", restaurant);
		  return "reviews/edit";
		} else {
		  return "error/404";
		}
	}
     
     @PostMapping("/{reviewId}/update")
     public String update(@PathVariable("reviewId") Integer reviewId,
                          @PathVariable("id") Integer id,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                          @ModelAttribute @Validated ReviewEditForm reviewEditForm,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "reviews/edit";
         }
         
         Review review = reviewRepository.findById(reviewId).orElse(null);
         Restaurant restaurant= restaurantRepository.findById(id).orElse(null);
         reviewService.update(userDetailsImpl, reviewEditForm);
         redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
         return "redirect:/restaurants/{id}";
     }  
     
     @PostMapping("/{reviewId}/delete")
     public String delete(@PathVariable("reviewId") Integer reviewId,
                          @PathVariable("id") Integer id,
                          RedirectAttributes redirectAttributes) {        
         reviewRepository.deleteById(reviewId);
         redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
         return "redirect:/restaurants/{id}";
     }
 }
