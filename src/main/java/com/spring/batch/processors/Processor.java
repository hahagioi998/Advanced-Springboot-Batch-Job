package com.spring.batch.processors;

import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.batch.models.User;
import com.spring.batch.repositories.UserRepository;

@Component
public class Processor implements ItemProcessor<User, User> {

	@Autowired
	UserRepository repository;

	@Override
	public User process(User user) throws Exception {
		Optional<User> userFromDb = repository.findById(user.getUserId());
		if (userFromDb.isPresent()) {
			user.setAccount(user.getAccount().add(userFromDb.get().getAccount()));
		}
		user.setName(user.getName().toUpperCase());
		return user;
	}

}
