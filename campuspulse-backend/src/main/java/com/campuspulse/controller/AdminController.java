package com.campuspulse.controller;

import com.campuspulse.service.ComplaintService;
import com.campuspulse.repository.*;
import com.campuspulse.dto.request.Requests;
import com.campuspulse.dto.response.Responses;
import com.campuspulse.entity.User;
import com.campuspulse.enums.*;
import com.campuspulse.exception.ApiException;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	private final ComplaintService service;
	private final UserRepository users;
	private final ComplaintRepository complaints;
	private final PasswordEncoder encoder;

	public AdminController(ComplaintService service, UserRepository users, ComplaintRepository complaints,
			PasswordEncoder encoder) {
		this.service = service;
		this.users = users;
		this.complaints = complaints;
		this.encoder = encoder;
	}

	@GetMapping("/complaints")
	public Object all(@RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return service.list("admin", search, status, page, size);
	}

	@PatchMapping("/complaints/{id}/review")
	public Object review(@PathVariable Long id) {
		return service.review(id);
	}

	@PatchMapping("/complaints/{id}/reject")
	public Object reject(@PathVariable Long id, @Valid @RequestBody Requests.Reject r) {
		return service.reject(id, r.note());
	}

	@PostMapping("/complaints/{id}/assign")
	public Object assign(@PathVariable Long id, @Valid @RequestBody Requests.Assign r) {
		return service.assign(id, r.staffId());
	}

	@PatchMapping("/complaints/{id}/priority")
	public Object priority(@PathVariable Long id, @Valid @RequestBody Requests.SetPriority r) {
		return service.priority(id, r.priority());
	}

	@GetMapping("/users")
	public Object users() {
		return users.findAll().stream().map(Responses.UserView::of).toList();
	}

	@GetMapping("/staff")
	public Object staff() {
		return users.findByRole(Role.STAFF).stream().map(Responses.UserView::of).toList();
	}

	@PostMapping("/staff")
	public ResponseEntity<?> createStaff(@Valid @RequestBody Requests.Signup r) {
		if (users.existsByEmailIgnoreCase(r.email()))
			throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
		User u = new User();
		u.name = r.name().trim();
		u.email = r.email().trim().toLowerCase();
		u.password = encoder.encode(r.password());
		u.role = Role.STAFF;
		return ResponseEntity.status(201).body(Responses.UserView.of(users.save(u)));
	}

	@GetMapping("/stats")
	public Object stats() {
		var all = complaints.findAll();
		long resolved = all.stream().filter(c -> c.status == Status.RESOLVED).count(),
				progress = all.stream().filter(c -> c.status == Status.IN_PROGRESS).count(),
				open = all.stream().filter(c -> c.status != Status.RESOLVED && c.status != Status.REJECTED).count();
		return Map.of("total", all.size(), "resolved", resolved, "inProgress", progress, "open", open);
	}
}
