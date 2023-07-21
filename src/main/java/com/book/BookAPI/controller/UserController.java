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
import org.springframework.web.bind.annotation.RestController;

import com.book.BookAPI.entity.User;
import com.book.BookAPI.exception.ApiRequestException;
import com.book.BookAPI.services.AuthServices;
import com.book.BookAPI.services.UserServices;
import com.book.BookAPI.utils.Utils;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/v1/user")
@Api(tags = "User Controller")
public class UserController {

	@Autowired
	private UserServices userServices;

	@Autowired
	private AuthServices authServices;

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
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(userServices.get(id)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@GetMapping("/{currentPage}/{rowLimit}")
	public ResponseEntity<?> getPagination(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue,
			@PathVariable("currentPage") Integer currentPage, @PathVariable("rowLimit") Integer rowLimit
		) {

		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			authServices.validateToken(token);
			Page<User> pageable = userServices.getPagination(currentPage, rowLimit);
			return ResponseEntity.ok(Utils.setStatusMessageSuccessPagination(pageable.getContent(),
					pageable.getTotalPages(), pageable.getTotalElements(), rowLimit, currentPage++));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

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
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(userServices.delete(id)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<?> save(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue, @RequestBody final User user) {
		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			authServices.validateToken(token);

			user.setCreatedBy(user.getId());
			long unixTime = System.currentTimeMillis() / 1000L;
			user.setCreatedDate(unixTime);
			user.setActive(1);
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(userServices.save(user)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> edit(
			@RequestHeader(value = Utils.AUTHORIZATION, required = false) String authorizationHeader,
			@CookieValue(value = "__HID", required = false) String cookieValue, @PathVariable("id") Integer id,
			@RequestBody final User borrower) {
		if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) && cookieValue == null) {
			throw new ApiRequestException(Utils.NOT_AUTHORIZED);
		}
		String token = authorizationHeader != null ? authorizationHeader.substring(7) : cookieValue;

		try {
			User user = authServices.validateToken(token);

			
			borrower.setUpdatedBy(user.getId());
			long unixTime = System.currentTimeMillis() / 1000L;
			borrower.setUpdatedDate(unixTime);
			return ResponseEntity.ok(Utils.setStatusMessageSuccess(userServices.save(borrower)));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
}
