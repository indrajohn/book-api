package com.book.BookAPI.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.book.BookAPI.entity.Book;
import com.book.BookAPI.exception.ApiRequestException;
import com.book.BookAPI.repo.BookRepo;
import com.book.BookAPI.utils.Utils;

@Service
public class BookServices {

	private BookRepo bookRepo;
	
	public Page<Book> getPagination(Integer currentPage, Integer rowLimit, Book searchingBook) {
		currentPage--;
		if (currentPage < 0) {
			throw new ApiRequestException(Utils.DATA_NOT_FOUND);
		}
		Order order = new Sort.Order(Direction.DESC, "createdDate");
		
		Pageable pagination = searchingBook.getIsbn() != null || searchingBook.getName() != null ? 
				PageRequest.of(currentPage, rowLimit) :
					PageRequest.of(currentPage, rowLimit,Sort.by(order));
		
		if(searchingBook.getIsbn() != null && searchingBook.getName() == null) {
			return bookRepo.findAllByIsbnAndActive(pagination, searchingBook.getIsbn() , 1);
		}
		if(searchingBook.getIsbn() == null && searchingBook.getName() != null) {
			return bookRepo.findAllByNameAndActive(pagination, searchingBook.getName() , 1);
		}
		
		if(searchingBook.getIsbn() != null && searchingBook.getName() != null) {
			return bookRepo.findAllByNameAndIsbnAndActive(pagination, searchingBook.getName(),searchingBook.getIsbn() , 1);
		}
		
			
		return bookRepo.findAllByActive(pagination, 1);
	}
	
	public Book getOne(Integer id) throws Exception {
		Optional<Book> bookExist = bookRepo.findById(id);
		if(!bookExist.isPresent()) {
			throw new ApiRequestException(Utils.DATA_NOT_FOUND); 
		}
		return bookExist.get();
	}
	
	public Book delete(Integer id) throws Exception {
		Optional<Book> bookExist = bookRepo.findById(id);
		if(!bookExist.isPresent()) {
			throw new ApiRequestException(Utils.DATA_NOT_FOUND); 
		}
		bookExist.get().setActive(0);
		return bookRepo.saveAndFlush(bookExist.get());
	}
	
	public Book save(Book book) {
		try {
			book.setActive(1);
			return bookRepo.saveAndFlush(book);
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
		
	};
	
	
}
