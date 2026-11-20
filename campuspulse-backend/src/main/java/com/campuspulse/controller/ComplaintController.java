package com.campuspulse.controller;

import com.campuspulse.service.ComplaintService;
import com.campuspulse.dto.request.Requests;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {
	private final ComplaintService service;

	public ComplaintController(ComplaintService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Requests.CreateComplaint r) {
		return ResponseEntity.status(201).body(service.create(r));
	}

	@GetMapping("/mine")
	public Object mine(@RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String status,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return service.list("mine", search, status, page, size);
	}

	@GetMapping("/{id}")
	public Object get(@PathVariable Long id) {
		return service.get(id);
	}

	@GetMapping("/{id}/history")
	public Object history(@PathVariable Long id) {
		return service.history(id);
	}
}
