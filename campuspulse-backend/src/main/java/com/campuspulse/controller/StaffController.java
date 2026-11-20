package com.campuspulse.controller;

import com.campuspulse.service.ComplaintService;
import com.campuspulse.dto.request.Requests;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
	private final ComplaintService service;

	public StaffController(ComplaintService service) {
		this.service = service;
	}

	@GetMapping("/complaints")
	public Object assigned(@RequestParam(defaultValue = "") String search,
			@RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return service.list("staff", search, status, page, size);
	}

	@PatchMapping("/complaints/{id}/status")
	public Object progress(@PathVariable Long id, @Valid @RequestBody Requests.SetStatus r) {
		return service.progress(id, r.status(), r.note());
	}
}
