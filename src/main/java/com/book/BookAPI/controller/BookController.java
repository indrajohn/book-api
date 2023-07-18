package com.book.BookAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.BookAPI.entity.Book;
import com.book.BookAPI.entity.User;
import com.book.BookAPI.exception.ApiRequestException;
import com.book.BookAPI.services.AuthServices;
import com.book.BookAPI.services.BookServices;
import com.book.BookAPI.utils.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/book")
@Api(tags = "Book Controller")
public class BookController {
	
	@Autowired
	private BookServices bookServices;
	
	@Autowired
	private AuthServices authServices;
	
	@ApiOperation(value = "This method is used to get specific book")
	@GetMapping("/{id}")
	public ResponseEntity<?> get(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue, @PathVariable final Integer id) {
		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			authServices.validateToken(token);
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(bookServices.getOne(id)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@ApiOperation(value = "This method is used to get book with pagination")
	@GetMapping("/{currentPage}/{rowLimit}")
	public ResponseEntity<?> getPagination(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue,
			@PathVariable("currentPage") Integer currentPage, @PathVariable("rowLimit") Integer rowLimit,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "isbn", required = false) String isbn) {

		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			authServices.validateToken(token);
			
			Book searchBook = new Book();
			searchBook.setIsbn(isbn);
			searchBook.setName(name);
			Page<Book> pageable = bookServices.getPagination(currentPage, rowLimit, searchBook);
			return ResponseEntity.ok(Utils.setStatusMessageSuccessPagination(pageable.getContent(),
					pageable.getTotalPages(), pageable.getTotalElements(), rowLimit, currentPage++));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@ApiOperation(value = "This method is used to delete book")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue, @PathVariable final Integer id)
			throws Exception {
		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			authServices.validateToken(token);
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(bookServices.delete(id)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@ApiOperation(value = "This method is used to save book")
	@PostMapping
	public ResponseEntity<?> save(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue, @RequestBody final Book book) {
		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			User user = authServices.validateToken(token);

			book.setCreatedBy(user.getId());
			long unixTime = System.currentTimeMillis() / 1000L;
			book.setCreatedDate(unixTime);
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(bookServices.save(book)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@ApiOperation(value = "This method is used to edit book")
	@PutMapping("/{id}")
	public ResponseEntity<?> edit(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue, @PathVariable("id") Integer id,
			@RequestBody final Book book) {
		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			User user = authServices.validateToken(token);

			book.setUpdatedBy(user.getId());
			long unixTime = System.currentTimeMillis() / 1000L;
			book.setUpdatedDate(unixTime);
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(bookServices.save(book)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
}


