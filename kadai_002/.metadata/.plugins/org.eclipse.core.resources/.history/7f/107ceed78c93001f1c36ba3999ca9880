package com.example.nagoyameshi.security;

 import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;
 
 @Service
 public class UserDetailsServiceImpl implements UserDetailsService {
     private final UserRepository userRepository;    
     
     public UserDetailsServiceImpl(UserRepository userRepository) {
         this.userRepository = userRepository;        
     }
     
     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {  
    	 try {
    	        User user = userRepository.findByEmail(email);
    	        
    	        // Check if the user exists
    	        if (user == null) {
    	            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
    	        }
    	        
    	        // Create a list of authorities based on user roles
    	        Collection<GrantedAuthority> authorities = new ArrayList<>();
    	        if (user.isAdmin()) {
    	            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // Add admin role
    	        }
    	        if (user.paidLicense) {
    	            authorities.add(new SimpleGrantedAuthority("ROLE_PAID")); // Add paid license role
    	        }
    	        if (!user.isAdmin() && !user.paidLicense) {
    	            authorities.add(new SimpleGrantedAuthority("ROLE_GENERAL")); // Add general role if not admin or paid
    	        }
    	        // If you have more roles, you can check and add them here

    	        // Return the UserDetailsImpl object with the user and authorities
    	        return new UserDetailsImpl(user, authorities);
    	    } catch (Exception e) {
    	        throw new UsernameNotFoundException("ユーザーが見つかりませんでした。", e);
    	    }
     }   
}