package com.example.nagoyameshi.controller;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserRepository userRepository;        
    
    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;                
    }    
    
    @GetMapping
    public String index(@RequestParam(name = "email", required = false) String email, 
    		@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        Page<User> userPage;
        
        if (email != null && !email.isEmpty()) {
//            userPage = userRepository.findByFullNameLikeOrKanaLike("%" + keyword + "%", "%" + keyword + "%", pageable); 
        	User user = userRepository.findByEmail(email);
        	userPage = new PageImpl<>(Collections.singletonList(user)); 
        } else {
            userPage = userRepository.findAll(pageable);
        }   
        
        
        model.addAttribute("userPage", userPage); 
        model.addAttribute("email", email); 
//        model.addAttribute("keyword", keyword);                
        
        return "admin/users/index";
    }
    
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        User user = userRepository.getReferenceById(id);
        
        model.addAttribute("user", user);
        
        return "admin/users/show";
    }  
}
