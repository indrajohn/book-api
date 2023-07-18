package com.book.BookAPI.dto;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class ApiException {
	private String statusCode;
	private String message;
	private ZonedDateTime timestamp;
}
