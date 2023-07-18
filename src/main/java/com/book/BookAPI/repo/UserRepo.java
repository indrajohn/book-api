package com.book.BookAPI.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.BookAPI.entity.User;


public interface UserRepo extends JpaRepository<User, Integer> {
	public Optional<User> findByIdAndActive(Integer id,Integer active);
	public Optional<User> findByUsernameAndActive(String username,Integer active);
	public Optional<User> findByEmailAndActive(String email,Integer active);
	public Optional<User> findByActiveAndUsernameOrEmail(Integer active,String username,String email);
}
