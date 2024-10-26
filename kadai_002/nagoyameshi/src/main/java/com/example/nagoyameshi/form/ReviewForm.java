package com.example.nagoyameshi.form;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
 
 @Data
 @AllArgsConstructor

public class ReviewForm {
     private Integer id;
	 
	 private Integer restaurant;

     private Integer user;    
     
     @NotNull(message = "評価を選択してください。")
     private Integer starId = 5;    
         
     @NotBlank(message = "コメントを入力してください。")
     private String comment;
	}