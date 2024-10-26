package com.example.nagoyameshi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Controller

public class ReservationController {
	private final ReservationRepository reservationRepository;    
	 private final RestaurantRepository restaurantRepository;
     private final ReservationService reservationService; 
     
     @Autowired
     public ReservationController(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, ReservationService reservationService) {        
        this.reservationRepository = reservationRepository; 
        this.restaurantRepository = restaurantRepository;
        this.reservationService = reservationService;
    }  
     
     @GetMapping("/reservations")     
     public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
    	 Integer user = userDetailsImpl.getUser().getId();
         Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
         
         model.addAttribute("reservationPage", reservationPage);         
         
         return "reservations/index";
     }

    
     @GetMapping("/restaurants/{id}/reservations/input")
     public String input(@PathVariable(name = "id") Integer id,
                         @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {   
         Restaurant restaurant = restaurantRepository.getReferenceById(id);
         try {
             restaurant = restaurantRepository.getReferenceById(id);
         } catch (EntityNotFoundException e) {
             model.addAttribute("errorMessage", "指定されたレストランが見つかりません。");
             return "error"; // Redirect to a custom error page
         }
         
         Integer reservationCount = reservationInputForm.getReservationCount();   
         Integer capacity = restaurant.getReservationCapacity();
         
         // Check if reservationCount is provided
         if (reservationCount != null && !reservationService.isWithinCapacity(reservationCount, capacity)) {
             FieldError fieldError = new FieldError(bindingResult.getObjectName(), "reservationCount", "予約人数が定員を超えています。");
             bindingResult.addError(fieldError);                
         }

         // Try parsing the date-time string
         try {
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
             LocalDateTime reservationDateTime = LocalDateTime.parse(reservationInputForm.getFromReservationDateTime().trim(), formatter);
             reservationInputForm.setReservationDateTime(reservationDateTime); // Save parsed date-time back to the form
         } catch (DateTimeParseException e) {
             bindingResult.rejectValue("fromReservationDateTime", "invalid.date", "日付の形式が正しくありません。入力例: 2024-12-31 18:30");
         }

         // Check for binding errors
         if (bindingResult.hasErrors()) {            
             model.addAttribute("restaurant", restaurant);            
             model.addAttribute("errorMessage", "予約内容に不備があります。"); 
             return "restaurants/show"; // Return to show page with errors
         }
         
         redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
         
         return "redirect:/restaurants/{id}/reservations/confirm"; // Redirect to confirmation page
     }  
    
    @GetMapping("/restaurants/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
                          HttpServletRequest httpServletRequest,
                          Model model) 
    {        
    	if (userDetailsImpl == null) {
            return "redirect:/login"; // Redirect to login if the user is not authenticated
        }
    	
    	Restaurant restaurant = restaurantRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser(); 
                
        LocalDateTime reservationDateTime = reservationInputForm.getReservationDateTime();
        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(restaurant.getId(), user.getId(), reservationDateTime, reservationInputForm.getReservationCount());
        
        model.addAttribute("restaurant", restaurant);  
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);
        System.out.println("Raw date-time value: " + reservationDateTime);
        String reservationText = reservationInputForm.getReservationDateTimeAsText();
        Integer reservationCountText = reservationInputForm.getReservationCount();
        
        return "reservations/confirm";
    } 
       
    @PostMapping("/restaurants/{id}/reservations/create")
    public String create(@PathVariable(name = "id") Integer id, 
    		@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		@ModelAttribute ReservationRegisterForm reservationRegisterForm) {                
        reservationService.create(userDetailsImpl, reservationRegisterForm);        
        return "redirect:/reservations?reserved";
    }
    
    @PostMapping("/restaurants/{id}/reservations/{reservationId}/cancel")
    public String cancel(@PathVariable(name = "id") Integer id,
                         @PathVariable("reservationId") Integer reservationId,
                         @ModelAttribute ReservationRegisterForm reservationRegisterForm) {
      
        reservationService.cancelReservationById(reservationId); 

        return "redirect:/reservations?canceled";
    }
    
}