package com.spring.batch.writers;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.spring.batch.models.User;
import com.spring.batch.repositories.UserRepository;

@Component
public class Writer implements ItemWriter<User> {

	@Autowired
	UserRepository repository;
	
	@Override
	@Transactional
	public void write(List<? extends User> users) throws Exception {
		repository.saveAll(users);
	}

}
