package com.book.BookAPI.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePagination<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private String statusCode;
	private String message;
	private long totalPages;
	private long totalData;
	private int per_page;
	private int current_page;
	private T results;

}