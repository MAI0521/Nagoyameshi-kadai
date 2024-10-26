package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.RestaurantService;
 
@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController  {
     private final RestaurantRepository restaurantRepository;  
     private final CategoryRepository categoryRepository;
     private final RestaurantService restaurantService;  
     private final CategoryService categoryService;
     private final FavoriteRepository favoriteRepository; 
     private final ReservationRepository reservationRepository; 
     private final ReviewRepository reviewRepository; 
     
     public AdminRestaurantController(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, RestaurantService restaurantService, CategoryService categoryService,
    		 FavoriteRepository favoriteRepository, ReservationRepository reservationRepository, ReviewRepository reviewRepository) {
         this.restaurantRepository = restaurantRepository;  
         this.categoryRepository = categoryRepository;
         this.restaurantService = restaurantService;
         this.categoryService = categoryService;
		 this.reviewRepository = reviewRepository;
		 this.reservationRepository = reservationRepository;
		 this.favoriteRepository = favoriteRepository;
     }	
     
     @GetMapping
     public String index(Model model, 
    		 @PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.ASC) Pageable pageable, 
//    		 It goes to HTML's line where it sees name = "keyword" and receives th:value="${keyword} in String type
    		 @RequestParam(name = "keyword", required = false) String keyword, 
     		 @RequestParam(name = "category", required = false) Integer categoryId) { 
         Page<Restaurant> restaurantPage;   
         
         if (categoryId != null) {
             restaurantPage = restaurantRepository.findByCategoryId(categoryId, pageable); 

         } else if (keyword != null && !keyword.isEmpty()) {
        	 restaurantPage = restaurantRepository.findByVenueNameLike("%" + keyword + "%", pageable);                
         } else {
        	 restaurantPage = restaurantRepository.findAll(pageable);
         }  
         
         List<Category> categories = categoryRepository.findAll();
      
         
         model.addAttribute("categories", categories);
         model.addAttribute("selectedCategoryId", categoryId);
//       sending the result back to the HTML (with return "admin/restaurants/index" and it is trying to show <tr th:each="restaurant : ${restaurantPage}">)
         model.addAttribute("restaurantPage", restaurantPage);
         model.addAttribute("keyword", keyword);   
 
	 return "admin/restaurants/index";
	 }  
     
     @GetMapping("/{id}")
     public String show(@PathVariable(name = "id") Integer id, Model model) {
         Restaurant restaurant = restaurantRepository.getRestaurantById(id);
         List<Category> categories = categoryRepository.findAll();
         
         model.addAttribute("categories", categories);
         model.addAttribute("restaurant", restaurant);
         
         return "admin/restaurants/show";
     }  
     
     @GetMapping("/register")
     public String register(Model model) {
    	 List<Category> categories = categoryRepository.findAll();
         
         model.addAttribute("categories", categories);
         model.addAttribute("restaurantRegisterForm", new RestaurantRegisterForm());
         
         return "admin/restaurants/register";
     } 
     
     @PostMapping("/create")
     public String create(@ModelAttribute @Validated RestaurantRegisterForm restaurantRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/restaurants/register";
         }
         
         restaurantService.create(restaurantRegisterForm);
         redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");    
         
         return "redirect:/admin/restaurants";
     } 
     
     @GetMapping("/{id}/edit")
     public String edit(@PathVariable(name = "id") Integer id, Model model) {
         Restaurant restaurant = restaurantRepository.getReferenceById(id);
         String imageName = restaurant.getImageName();
         RestaurantEditForm restaurantEditForm = new RestaurantEditForm(
        		 restaurant.getId(), 
        		 restaurant.getVenueName(), 
        		 restaurant.getCategory(), 
        		 null, 
        		 restaurant.getDescription(), 
        		 restaurant.getPostalCode(),
        		 restaurant.getAddress(), 
        		 restaurant.getOpeningHour(), 
        		 restaurant.getClosingHour(), 
        		 restaurant.getReservationCapacity(),
         		 restaurant.getBudgetRange());
         List<Category> categories = categoryRepository.findAll();
         
         model.addAttribute("categories", categories);
         model.addAttribute("imageName", imageName);
         model.addAttribute("restaurantEditForm", restaurantEditForm);
         
         return "admin/restaurants/edit";
     } 
     
     @PostMapping("/{id}/update")
     public String update(@ModelAttribute @Validated RestaurantEditForm restaurantEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/restaurants/edit";
         }
         
         restaurantService.update(restaurantEditForm);
         redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
         
         return "redirect:/admin/restaurants";
     }   
     
     @DeleteMapping("/{id}/delete")
     public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
    	 restaurantService.deleteRestaurant(id);
                 
         redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
         
         return "redirect:/admin/restaurants";
     }  
}
