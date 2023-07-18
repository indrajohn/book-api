package com.book.BookAPI.services;

import java.nio.CharBuffer;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.book.BookAPI.dto.CredentialLogin;
import com.book.BookAPI.entity.User;
import com.book.BookAPI.exception.ApiRequestException;
import com.book.BookAPI.repo.UserRepo;
import com.book.BookAPI.utils.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class AuthServices {
	
	@Autowired
	private UserRepo userRepo;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	private Key hmacKey;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
	}
	
	public String createToken(User auth) {
		Claims claims = Jwts.claims().setSubject(auth.getUsername());
		Date now = new Date();
		// Date validity = new Date(now.getTime() + 600000); // 10 minutes
		// Date validity = new Date(now.getTime() + 3000); // 10 minutes
		Date validity = new Date(now.getTime() + 1800000); // 30 minutes
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity).signWith(hmacKey).compact();
	}
	
	
	
	public User validateToken(String token) throws Exception {
		String username = "";
		User auth = null;
		try {
			username = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(token).getBody().getSubject();
			Optional<User> authOptional = userRepo.findByUsernameAndActive(username,1);

			if (!authOptional.isPresent()) {
				throw new ApiRequestException(Utils.DATA_NOT_FOUND);
			}

			auth = authOptional.get();
			auth.setRefreshToken("");
			String refresh_token = createToken(auth);
			auth.setRefreshToken(refresh_token);
			userRepo.save(auth);
		} catch (ExpiredJwtException e) {
			if (e.toString().contains("expired")) {
				username = e.getClaims().getSubject();

				Optional<User> authOptional = userRepo.findByUsernameAndActive(username,1);

				if (!authOptional.isPresent()) {
					throw new ApiRequestException(Utils.INVALID_USERNAME_OR_PASSWORD);
				}
				auth = authOptional.get();

				if (auth.getRefreshToken() != null && !auth.getRefreshToken().equals("")) {
					String refresh_token = auth.getRefreshToken();
					try {
						username = Jwts.parserBuilder().setSigningKey(hmacKey).build().parseClaimsJws(refresh_token)
								.getBody().getSubject();
						auth.setRefreshToken("");
						auth.setRefreshToken(createToken(auth));
						userRepo.save(auth);

					} catch (ExpiredJwtException ex) {
						throw new ApiRequestException(Utils.TOKEN_EXPIRED);
					}
				} else {
					throw new ApiRequestException(Utils.TOKEN_EXPIRED);
				}
			}

		} catch (Exception e) {
			throw new ApiRequestException(Utils.TOKEN_EXPIRED);
		}
		return auth;
	}
	
	public User setStatusSignIn(User auth, CredentialLogin credential) {
		if (passwordEncoder.matches(CharBuffer.wrap(credential.getPassword()), auth.getPassword())) {
			String token = createToken(auth);
			auth.setToken("");
			auth.setToken(token);
			userRepo.save(auth);
		} else {
			throw new ApiRequestException(Utils.INVALID_USERNAME_OR_PASSWORD);
		}
		return auth;
	}

	public boolean checkPassword(User auth, User credential) {
		return passwordEncoder.matches(CharBuffer.wrap(credential.getPassword()), auth.getPassword());
	}
	
	public User signIn(CredentialLogin credential) {
		Optional<User> authWithUsername = userRepo.findByUsernameAndActive(credential.getUsername(),1);
		Optional<User> authWithEmail = userRepo.findByEmailAndActive(credential.getUsername(),1);
		if (!authWithUsername.isPresent() && !authWithUsername.isPresent() && !authWithUsername.isPresent()) {
			throw new ApiRequestException(Utils.INVALID_USERNAME_OR_PASSWORD);
		}

		if (authWithUsername.isPresent()) {
			return setStatusSignIn(authWithUsername.get(), credential);
		}

		return setStatusSignIn(authWithEmail.get(), credential);
	}
	
	public User saveAuth(User auth) throws Exception {
		Long unixTime = System.currentTimeMillis() / 1000L;
		Optional<User> authOptional = userRepo.findByUsernameAndActive(auth.getUsername(),1);
		
		
		if (authOptional.isPresent()) {
			throw new ApiRequestException(Utils.USERNAME_HAS_BEEN_TAKEN);
		}
		
		String password = passwordEncoder.encode(auth.getPassword());
		auth.setPassword(password);
		auth.setCreatedDate(unixTime);

		userRepo.save(auth);
		return auth;
	}



}
