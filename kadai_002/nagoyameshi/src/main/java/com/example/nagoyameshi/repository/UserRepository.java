package com.example.nagoyameshi.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.User;

 public interface UserRepository extends JpaRepository<User, Integer> {
	 public User findByEmail(String email);
//	 public Page<User> findByFullNameLikeOrKanaLike(String fullNameKeyword, String kanaKeyword, Pageable pageable);
//	 public Page<User> findByEmail(String email, Pageable pageable);
 }
