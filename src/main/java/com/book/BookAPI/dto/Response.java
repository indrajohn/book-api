package com.book.BookAPI.dto;

import lombok.Data;

@Data
public class Response<T> {
	private String statusCode;
	private String message;
	private T results;
}
