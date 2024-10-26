package com.example.nagoyameshi.form;

import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRegisterForm {
    @NotBlank(message = "店舗名を入力してください。")
    private String venueName;
    
    @NotNull(message = "カテゴリーを選択してください。")
    private Category category;
        
    private MultipartFile imageFile;
    
    @NotBlank(message = "説明を入力してください。")
    private String description;   
    
    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;
    
    @NotBlank(message = "住所を入力してください。")
    private String address;
    
    @NotNull(message = "営業時間（開始）を選択してください。")
    @Min(value = 0, message = "0時から24時の間で指定してください。")
    @Max(value = 24, message = "0時から24時の間で指定してください。")
    private Integer openingHour;
    
    @NotNull(message = "営業時間（終了）を選択してください。")
    @Min(value = 0, message = "0時から24時の間で指定してください。")
    @Max(value = 24, message = "0時から24時の間で指定してください。")
    private Integer closingHour;
    
    @NotNull(message = "席数を入力してください。")
    @Min(value = 1, message = "席数は1人以上に設定してください。")
    private Integer reservationCapacity;  
    
    @NotNull(message = "予算を選択してください。")
    private Integer budgetRange; 
}
