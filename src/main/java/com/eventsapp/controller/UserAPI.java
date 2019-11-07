package com.eventsapp.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.eventsapp.persistence.UserRepository;
import com.eventsapp.valueobjects.User;

@RestController
@RequestMapping("/users")
public class UserAPI {
	
	@Autowired
	UserRepository repo;
	
	@GetMapping
	public Iterable<User> getAll() {
		return repo.findAll();
	}
	
	@GetMapping("/{userName}")
	public Optional<User> getUserByName(@PathVariable String userName) {
		Optional<User> optionalUser = Optional.of(repo.findByName(userName));
		return optionalUser;
	}
	
	@PostMapping
	public ResponseEntity<?> addUser(@RequestBody User newUser, UriComponentsBuilder uri) {
		if (newUser.getName() == null || newUser.getPassword() == null || newUser.getEmail() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		newUser = repo.save(newUser);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/userName").buildAndExpand(newUser.getName()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		
		return response;
	}
	
	@PutMapping("/{userName}")
	public ResponseEntity<?> putUser(@RequestBody User newUser, @PathVariable("userName") String userName) {
		User user = repo.findByName(userName);
		
		if (user == null || newUser.getName() == null || newUser.getPassword() == null || newUser.getEmail() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		user.setName(newUser.getName());
		user.setPassword(newUser.getPassword());
		user.setEmail(newUser.getEmail());
		repo.save(user);

		return ResponseEntity.ok().build();
	}
	
	@Transactional
	@DeleteMapping("/{userName}")
	public ResponseEntity<?> deleteUser(@PathVariable("userName") String userName) {
		long usersDeleted = repo.deleteByName(userName);
		if (usersDeleted == 0) {
			return ResponseEntity.badRequest().build();
		}
		//assertThat(usersDeleted).isEqualTo(1);
		
		return ResponseEntity.ok().build();	
	}
}
