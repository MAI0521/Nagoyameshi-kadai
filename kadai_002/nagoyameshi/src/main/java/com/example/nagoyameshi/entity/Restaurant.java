package com.example.nagoyameshi.entity;

 import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
 
 @Entity
 @Table(name = "restaurants")
 @Data
public class Restaurant {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")
     private Integer id;
     
     @ManyToOne
     @JoinColumn(name = "category_id")
     private Category category; 
 
     @Column(name = "venue_name")
     private String venueName;
 
     @Column(name = "image_name")
     private String imageName;
 
     @Column(name = "description")
     private String description;
 
     @Column(name = "postal_code")
     private String postalCode;
 
     @Column(name = "address")
     private String address;
 
     @Column(name = "opening_hour")
     private Integer openingHour;
 
     @Column(name = "closing_hour")
     private Integer closingHour;
     
     @Column(name = "reservation_capacity")
     private Integer reservationCapacity;
     
     @Column(name = "budget_range")
     private Integer budgetRange;
 
     @Column(name = "created_at", insertable = false, updatable = false)
     private Timestamp createdAt;
 
     @Column(name = "updated_at", insertable = false, updatable = false)
     private Timestamp updatedAt;
     
     @OneToMany(mappedBy = "restaurant")
     private List<Review> reviews; // One restaurant can have many reviews
}
