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

import com.eventsapp.persistence.CustomerRepository;
import com.eventsapp.valueobjects.Customer;

@RestController
@RequestMapping("/customers")
public class CustomerAPI {
	
	@Autowired
	CustomerRepository repo;
	
	@GetMapping
	public Iterable<Customer> getAll() {
		return repo.findAll();
	}
	
	@GetMapping("/{customerName}")
	public Optional<Customer> getCustomerByName(@PathVariable String customerName) {
		Optional<Customer> optionalCustomer = Optional.of(repo.findByName(customerName));
		return optionalCustomer;
	}
	
	@PostMapping
	public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer, UriComponentsBuilder uri) {
		Customer existingCustomer = repo.findByName(newCustomer.getName());
		
		// Can't add a new customer if one already exists with this name
		if (existingCustomer != null) {
			return ResponseEntity.badRequest().build();
		}
		
		// If any of the new customer fields are blank, don't create a new customer
		if (newCustomer.getName() == null || newCustomer.getPassword() == null || newCustomer.getEmail() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		newCustomer = repo.save(newCustomer);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/customerName").buildAndExpand(newCustomer.getName()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		
		return response;
	}
	
	@PutMapping("/{customerName}")
	public ResponseEntity<?> putcustomer(@RequestBody Customer newCustomer, @PathVariable("customerName") String customerName) {
		Customer customer = repo.findByName(customerName);
		
		// If the customer doesn't exist, we can't update their info
		if (customer == null) {
			return ResponseEntity.badRequest().build();
		}
		
		// If any of the updated customer fields are blank, don't update the customer
		if (newCustomer.getName() == null || newCustomer.getPassword() == null || newCustomer.getEmail() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		customer.setName(newCustomer.getName());
		customer.setPassword(newCustomer.getPassword());
		customer.setEmail(newCustomer.getEmail());
		repo.save(customer);

		return ResponseEntity.ok().build();
	}
	
	@Transactional
	@DeleteMapping("/{customerName}")
	public ResponseEntity<?> deletecustomer(@PathVariable("customerName") String customerName) {
		long customersDeleted = repo.deleteByName(customerName);
		
		// if we didn't delete a user, return a bad request
		if (customersDeleted == 0) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok().build();	
	}
}