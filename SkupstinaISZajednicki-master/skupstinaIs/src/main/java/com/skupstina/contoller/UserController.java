package com.skupstina.contoller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	/*
	 * If the “/user” resource is reachable then it will return the currently 
	 * authenticated user (an Authentication), 
	 * and otherwise Spring Security will intercept the request and send a 401 
	 * response through an AuthenticationEntryPoint.
	 */
	@RequestMapping("/")
	public Principal user(Principal user) {		//vraca logovanog usera
		return user;
	}
}
