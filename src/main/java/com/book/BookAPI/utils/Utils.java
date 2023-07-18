package com.book.BookAPI.utils;

import com.book.BookAPI.dto.Response;
import com.book.BookAPI.dto.ResponsePagination;

public class Utils {
	public static final String DATA_NOT_FOUND = "Data Not Found";
	public static final String DATA_EXIST = "Data Exist";
	public static final String TOKEN_EXPIRED = "Token Expired";
	public static final String ZONE_LOCATION = "Australia/Sydney";
	public static final String NOT_AUTHORIZED = "Not Authorized";
	public static final String AUTHORIZATION = "Authorization";
	public static final String SUCCESS = "Success";
	public static final String USERNAME_HAS_BEEN_TAKEN = "Username Has Been Taken";
	public static final String INVALID_USERNAME_OR_PASSWORD = "Invalid Username Or Password";
	public static final String CODE_INVALID_USERNAME_OR_PASSWORD = "1001";
	public static final String CODE_DATA_NOT_FOUND = "0000";
	public static final String CODE_TOKEN_EXPIRED = "0001";
	public static final String CODE_DATA_EXIST = "0002";
	public static final String CODE_CODE_MUST_UNIQUE = "0003";
	public static final String CODE_ANY_ERROR = "00000";
	public static final String CODE_SUCCESS = "1000";
	
	
	public static <T> Response<T> setStatusMessageSuccess(T object) {
		Response<T> response = new Response<T>();
		response.setMessage(SUCCESS);
		response.setStatusCode(CODE_SUCCESS);
		response.setResults(object);
		return response;
	}

	public static <T> ResponsePagination<T> setStatusMessageSuccessPagination(T object, long totalPages, long totalData,
			int per_page, int current_page) {
		ResponsePagination<T> response = new ResponsePagination<T>();
		response.setTotalPages(totalPages);
		response.setTotalData(totalData);
		response.setPer_page(per_page);
		response.setCurrent_page(current_page);
		response.setMessage("Success");
		response.setStatusCode("1000");
		response.setResults(object);
		return response;
	}
}
