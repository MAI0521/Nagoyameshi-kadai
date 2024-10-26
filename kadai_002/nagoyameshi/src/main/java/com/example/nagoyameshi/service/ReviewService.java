package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewEditForm;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;  
    private final UserRepository userRepository;
    
    
    public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;  
        this.restaurantRepository = restaurantRepository;  
        this.userRepository = userRepository;  
    }  

    public Page<Review> getReviewsForRestaurant(Restaurant restaurant, int page, int size) {
        return reviewRepository.findByRestaurantOrderByCreatedAtDesc(restaurant, PageRequest.of(page, size));
    }
 
    
    @Transactional
    public void create(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, ReviewForm reviewForm) {
//        Review review = new Review();
        Review review = new Review();
        Restaurant restaurant = restaurantRepository.getReferenceById(reviewForm.getId());
        User user = userDetailsImpl.getUser();
        
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setStarId(reviewForm.getStarId());
        review.setComment(reviewForm.getComment());
        
        reviewRepository.save(review);
    }
    
    @Transactional
    public void update(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,ReviewEditForm reviewEditForm) {
        Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
        Restaurant restaurant = restaurantRepository.getReferenceById(reviewEditForm.getId());
    	User user = userDetailsImpl.getUser();
        
        review.setRestaurant(restaurant);                
        review.setUser(user);
        review.setStarId(reviewEditForm.getStarId());
        review.setComment(reviewEditForm.getComment());
                    
        reviewRepository.save(review);
    }

//  コントローラーから直接レポジトリを呼び出すよりもサービスからの呼び出しをしたほうが管理しやすい。
	public List<Object[]> getReviewCountPerRestaurant() {
		return reviewRepository.countReviewsPerRestaurant();
	}

	public List<Object[]>getAverageReviewsPerRestaurant() {
		return reviewRepository.averageReviewsPerRestaurant();
	}

}
