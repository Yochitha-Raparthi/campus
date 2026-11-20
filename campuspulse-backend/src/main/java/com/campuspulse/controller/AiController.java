package com.campuspulse.controller;

import com.campuspulse.service.ClassifierService;
import com.campuspulse.dto.request.Requests;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
	private final ClassifierService service;

	public AiController(ClassifierService service) {
		this.service = service;
	}

	@PostMapping("/analyze")
	public Object analyze(@Valid @RequestBody Requests.Analyze r) {
		return service.analyze(r.title(), r.description());
	}
}
