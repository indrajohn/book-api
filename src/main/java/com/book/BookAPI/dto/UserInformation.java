package com.book.BookAPI.dto;

import lombok.Data;

@Data
public class UserInformation {
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String role;
	
}
