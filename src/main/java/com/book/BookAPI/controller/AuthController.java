package com.book.BookAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.BookAPI.dto.CredentialLogin;
import com.book.BookAPI.dto.Response;
import com.book.BookAPI.dto.UserInformation;
import com.book.BookAPI.entity.Role;
import com.book.BookAPI.entity.User;
import com.book.BookAPI.exception.ApiRequestException;
import com.book.BookAPI.services.AuthServices;
import com.book.BookAPI.services.UserServices;
import com.book.BookAPI.utils.Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/auth")
@Api(tags = "Authorization")
public class AuthController {

	@Autowired
	private AuthServices authServices;

	@Autowired
	private UserServices userServices;

	@ApiOperation(value = "This method is used to get the specific user information with token")
	@GetMapping("/user-information/{token}")
	public ResponseEntity<Response<?>> validateToken(@PathVariable final String token) {
		try {
			User user = authServices.validateToken(token);
			UserInformation userInformation = new UserInformation();
			userInformation.setFirstName(user.getFirstName());
			userInformation.setLastName(user.getLastName());
			userInformation.setEmail(user.getEmail());
			userInformation.setPhoneNumber(user.getPhoneNumber());
			userInformation.setUsername(user.getUsername());
			userInformation.setRole(user.getRole().getName());

			return ResponseEntity.ok(Utils.setStatusMessageSuccess(userInformation));
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@ApiOperation(value = "This method is used to sign in and add token in the cookie")
	@PostMapping("/sign-in")
	public ResponseEntity<Response<?>> signIn(@RequestBody CredentialLogin credentialLogin) {
		Response<User> response = Utils.setStatusMessageSuccess(authServices.signIn(credentialLogin));

		Response<UserInformation> newResponse = new Response<>();
		if (response.getResults() != null) {
			HttpHeaders headers = new HttpHeaders();

			// headers.add("Set-Cookie","__NEW="+response.getResults().getIs_new_user()+";
			// Max-Age=604800; domain=localhost; Path=/; HttpOnly;");
			// headers.add("Set-Cookie","__HID="+response.getResults().getToken()+";
			// Max-Age="+24 * 60 * 60 * 1000+"; SameSite=None;
			// domain="+domainHeaderCookie+"; Path=/; HttpOnly; Secure;");
			headers.add("Set-Cookie", "__HID=" + response.getResults().getToken() + "; Max-Age=" + (60 * 60 * 24 * 1)
					+ "; SameSite=None; domain=localhost; Path=/; HttpOnly; Secure;");

			newResponse.setMessage(response.getMessage());
			newResponse.setStatusCode(response.getStatusCode());

			User signInAuth = response.getResults();

			UserInformation userInformation = new UserInformation();
			userInformation.setFirstName(signInAuth.getFirstName());
			userInformation.setLastName(signInAuth.getLastName());
			userInformation.setEmail(signInAuth.getEmail());
			userInformation.setPhoneNumber(signInAuth.getPhoneNumber());
			userInformation.setUsername(signInAuth.getUsername());
			userInformation.setRole(signInAuth.getRole().getName());

			newResponse.setResults(userInformation);
			return ResponseEntity.ok().headers(headers).body(newResponse);
		}
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "This method is used to sign up")
	@PostMapping("/sign-up")
	public ResponseEntity<Response<User>> signUp(@RequestBody User user) {
		try {
			userServices.getByEmailRegister(user.getEmail());
			Role role = new Role();
			role.setId(2);
			user.setRole(role);
			user.setActive(1);
			authServices.saveAuth(user);
		} catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
		return ResponseEntity.ok(Utils.setStatusMessageSuccess(null));
	}

	@ApiOperation(value = "This method is used to sign out")
	@PostMapping("/sign-out")
	public ResponseEntity<Response<User>> signOut() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie",
				"__HID=''; expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=None; domain=localhost; Path=/; HttpOnly; Secure;");
		return ResponseEntity.ok().headers(headers).body(null);
	}

}