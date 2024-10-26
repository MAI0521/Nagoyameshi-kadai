package com.example.nagoyameshi.form;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm { 
	 
	private Integer restaurantId;

    private Integer userId;     
        
    private LocalDateTime reservationDateTime;   
    
    private Integer reservationCount;
}

