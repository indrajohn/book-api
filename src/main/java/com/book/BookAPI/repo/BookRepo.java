package com.book.BookAPI.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.book.BookAPI.entity.Book;
import com.book.BookAPI.entity.User;

public interface BookRepo extends JpaRepository<Book, Integer> {
	Page<Book> findAllByActive(Pageable pageable,Integer active);
	Page<Book> findAllByIsbnAndActive(Pageable pageable,String isbn,Integer active);
	Page<Book> findAllByNameAndActive(Pageable pageable,String name,Integer active);
	Page<Book> findAllByNameAndIsbnAndActive(Pageable pageable,String name,String isbn,Integer active);
	Page<Book> findAllByActiveAndBorrower(Pageable pageable,Integer active,User user);
	
	
	
}
