package com.microservice.mobileapp.controller;

import com.microservice.mobileapp.model.UpdateUserDetailsRequestModel;
import com.microservice.mobileapp.model.UserDetailsRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservice.mobileapp.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

	Map<String, User> userMap;


	@PostMapping
	public User createUser(@Valid @RequestBody UserDetailsRequestModel model){
		User user = new User();
		user.setFirstName(model.getFirstName());
		user.setLastName(model.getLastName());
		user.setEmail(model.getEmail());
		String userID = UUID.randomUUID().toString();
		user.setUserId(userID);

		if(userMap == null) userMap = new HashMap<>();
		userMap.put(userID, user);

		return  user;
	}


	@GetMapping
	public String getUsers(@RequestParam(value = "page", defaultValue ="2") int page, @RequestParam(value = "limit", required = false) String limit){
		
		return "Get Users was called .... with page "+ page + " and limit : "+ limit;
	}
	
	@GetMapping(path= "{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> gerUserById(@Valid @PathVariable String userId) {

		String firstName = null;
		System.out.println(firstName.length());

		if(userMap.containsKey(userId)){
			return new ResponseEntity<User>(userMap.get(userId), HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}

	}

	@PutMapping(path="/{userId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDetailsRequestModel model) {
		User user = userMap.get(userId);
		if(userMap.containsKey(userId)) {
			user.setFirstName(model.getFirstName());
			user.setLastName(model.getLastName());
			userMap.put(userId, user);
		}
		return new ResponseEntity<User>(userMap.get(userId), HttpStatus.OK);
	}

	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable String userId) {
		userMap.remove(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
