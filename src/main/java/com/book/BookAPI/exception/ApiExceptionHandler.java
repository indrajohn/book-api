package com.book.BookAPI.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.book.BookAPI.dto.ApiException;
import com.book.BookAPI.utils.Utils;

@ControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(value= {ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
		HttpStatus requestStatus = checkRequestStatus(e.getMessage());
		
		ApiException apiException = new ApiException();
		apiException.setMessage(e.getMessage());
		String statusCode = checkStatusCode(e.getMessage());
		apiException.setStatusCode(statusCode);
		ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(Utils.ZONE_LOCATION));
		apiException.setTimestamp(zonedDateTime);
		return new ResponseEntity<>(apiException,requestStatus);
	};
	
	private HttpStatus checkRequestStatus(String message) {
		if(message.equals(Utils.DATA_NOT_FOUND)) {
			return HttpStatus.NOT_FOUND;
		}
		
		if(message.equals(Utils.NOT_AUTHORIZED)) {
			return HttpStatus.UNAUTHORIZED;
		}
		
		return HttpStatus.BAD_REQUEST;
	}
	
	private String checkStatusCode(String message) {
		if(message.equals(Utils.DATA_NOT_FOUND)) {
			return Utils.CODE_DATA_NOT_FOUND;
		}
		if(message.equals(Utils.INVALID_USERNAME_OR_PASSWORD)) {
			return Utils.CODE_INVALID_USERNAME_OR_PASSWORD;
		}
		if(message.equals(Utils.TOKEN_EXPIRED)) {
			return Utils.CODE_TOKEN_EXPIRED;
		}
		if(message.equals(Utils.DATA_EXIST)) {
			return Utils.CODE_DATA_EXIST;
		}
		
		return Utils.CODE_ANY_ERROR;
	}
	
	
}
