package com.example.nagoyameshi.form;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
    @NotBlank(message = "予約日時を入力してください。")
    private String fromReservationDateTime;    
    
    @NotNull(message = "来店人数を入力してください。")
    @Min(value = 1, message = "来店人数は1人以上に設定してください。")
    private Integer reservationCount; 

    // Keep a field to store the parsed LocalDateTime
    private LocalDateTime reservationDateTime;

    // Get the LocalDateTime, parsing the string and handling format
    public LocalDateTime getReservationDateTime() {
//        if (reservationDateTime == null) {
//            try {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//                reservationDateTime = LocalDateTime.parse(fromReservationDateTime, formatter);
//            } catch (DateTimeParseException e) {
                // Handle parsing error if needed
//                throw new RuntimeException("Invalid date format for reservation date and time", e);
//            }
//        }
        return reservationDateTime;
    }

    // Optionally, set the reservationDateTime directly if needed
    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }
    
    public String getReservationDateTimeAsText() {
        LocalDateTime formatRsvpDateTime = getReservationDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"); // Change this pattern as needed
        return formatRsvpDateTime.format(formatter);
    }
    
    public Integer getReservationCount() { 
    	return reservationCount;
    }
    
    public void setReservationCount() { 
    	this.reservationCount = reservationCount;
    }
    
    
}