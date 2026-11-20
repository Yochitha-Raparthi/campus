package com.campuspulse.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import com.campuspulse.dto.request.Requests;
import com.campuspulse.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService service;

	public AuthController(AuthService service) {
		this.service = service;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody Requests.Signup r) {
		return ResponseEntity.status(201).body(service.signup(r));
	}

	@PostMapping("/login")
	public Object login(@Valid @RequestBody Requests.Login r) {
		return service.login(r);
	}

	@GetMapping("/me")
	public Object me() {
		return com.campuspulse.dto.response.Responses.UserView.of(service.current());
	}

	@PutMapping("/profile")
	public Object update(@Valid @RequestBody Requests.Profile r) {
		return service.update(r);
	}
}
