package com.example.nagoyameshi.service;
 
 import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
 
 @Service
 public class RestaurantService {
     private final RestaurantRepository restaurantRepository;    
     
     public RestaurantService(RestaurantRepository restaurantRepository) {
         this.restaurantRepository = restaurantRepository;        
     }    
     
     @Transactional
     public void create(RestaurantRegisterForm restaurantRegisterForm) {
    	 Restaurant restaurant = new Restaurant();        
         MultipartFile imageFile = restaurantRegisterForm.getImageFile();
         
         if (!imageFile.isEmpty()) {
             String imageName = imageFile.getOriginalFilename(); 
             String hashedImageName = generateNewFileName(imageName);
             Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
             copyImageFile(imageFile, filePath);
             restaurant.setImageName(hashedImageName);
         }
         
         restaurant.setVenueName(restaurantRegisterForm.getVenueName()); 
         restaurant.setCategory(restaurantRegisterForm.getCategory());
         restaurant.setDescription(restaurantRegisterForm.getDescription());
         restaurant.setPostalCode(restaurantRegisterForm.getPostalCode());
         restaurant.setAddress(restaurantRegisterForm.getAddress());
         restaurant.setOpeningHour(restaurantRegisterForm.getOpeningHour());
         restaurant.setClosingHour(restaurantRegisterForm.getClosingHour());
         restaurant.setReservationCapacity(restaurantRegisterForm.getReservationCapacity());
         restaurant.setBudgetRange(restaurantRegisterForm.getBudgetRange());
                     
         restaurantRepository.save(restaurant);
     }  
     
     @Transactional
     public void update(RestaurantEditForm restaurantEditForm) {
    	 Restaurant restaurant = restaurantRepository.getReferenceById(restaurantEditForm.getId());
         MultipartFile imageFile = restaurantEditForm.getImageFile();
         
         if (!imageFile.isEmpty()) {
             String imageName = imageFile.getOriginalFilename(); 
             String hashedImageName = generateNewFileName(imageName);
             Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
             copyImageFile(imageFile, filePath);
             restaurant.setImageName(hashedImageName);
         }
         
         restaurant.setVenueName(restaurantEditForm.getVenueName()); 
         restaurant.setCategory(restaurantEditForm.getCategory());
         restaurant.setDescription(restaurantEditForm.getDescription());
         restaurant.setPostalCode(restaurantEditForm.getPostalCode());
         restaurant.setAddress(restaurantEditForm.getAddress());
         restaurant.setOpeningHour(restaurantEditForm.getOpeningHour());
         restaurant.setClosingHour(restaurantEditForm.getClosingHour());
         restaurant.setReservationCapacity(restaurantEditForm.getReservationCapacity());
         restaurant.setBudgetRange(restaurantEditForm.getBudgetRange());
                     
         restaurantRepository.save(restaurant);
     }    
     
     // UUIDを使って生成したファイル名を返す
     public String generateNewFileName(String fileName) {
         String[] fileNames = fileName.split("\\.");                
         for (int i = 0; i < fileNames.length - 1; i++) {
             fileNames[i] = UUID.randomUUID().toString();            
         }
         String hashedFileName = String.join(".", fileNames);
         return hashedFileName;
     }     
     
     // 画像ファイルを指定したファイルにコピーする
     public void copyImageFile(MultipartFile imageFile, Path filePath) {           
         try {
             Files.copy(imageFile.getInputStream(), filePath);
         } catch (IOException e) {
             e.printStackTrace();
         }          
     }

     public Page<Restaurant> findAllByOrderByReviewCountDesc(Pageable pageable) {
    	 return restaurantRepository.findAllSortedByReviewCount(pageable);
    	}

    public Page<Restaurant> findAllByOrderByAverageScoreDesc(Pageable pageable) {
    	return restaurantRepository.findAllSortedByAverageScore(pageable);
		}
   
 }