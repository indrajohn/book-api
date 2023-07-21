package com.book.BookAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.book.BookAPI.entity.User;
import com.book.BookAPI.exception.ApiRequestException;
import com.book.BookAPI.repo.UserRepo;
import com.book.BookAPI.utils.Utils;


@Service
public class UserServices {
	
	@Autowired
	private UserRepo userRepo;
	
	
	public Page<User> getPagination(Integer currentPage, Integer rowLimit) {
		currentPage--;
		if (currentPage < 0) {
			throw new ApiRequestException(Utils.DATA_NOT_FOUND);
		}
		Order order = new Sort.Order(Direction.DESC, "createdDate");
		Pageable pagination = PageRequest.of(currentPage, rowLimit, Sort.by(order));

		Page<User> pagable = userRepo.findAllByActive(pagination, 1);

		return pagable;
	}
	
	public User get(Integer id) throws Exception {
		User user = userRepo.findByIdAndActive(id,1).orElse(new User());
		if(user.getId() == null) {
			throw new ApiRequestException(Utils.DATA_NOT_FOUND);
		}
		return user;
	};
	
	public User getByEmail(String email) throws Exception {
		User user = userRepo.findByEmailAndActive(email,1).orElse(new User());
		if(user.getId() == null) {
			throw new ApiRequestException(Utils.DATA_NOT_FOUND);
		}
		return user;
	};
	
	public User getByEmailRegister(String email) throws Exception {
		User user = userRepo.findByEmailAndActive(email,1).orElse(new User());
		if(user.getId() != null) {
			throw new ApiRequestException(Utils.DATA_EXIST);
		}
		return user;
	};
	
	public User delete(Integer id) throws Exception {
		User user = userRepo.findById(id).orElse(new User());
		if(user.getId() == null) {
			throw new ApiRequestException(Utils.DATA_NOT_FOUND);
		}
		user.setActive(0);
		return userRepo.saveAndFlush(user);
	};
	
	public User save(User user) {
		try {
			user.setActive(1);
			return userRepo.saveAndFlush(user);
		}catch (Exception e) {
			throw new ApiRequestException(e.getMessage());
		}
		
	};
}
