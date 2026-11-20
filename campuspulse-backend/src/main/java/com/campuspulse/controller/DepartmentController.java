package com.campuspulse.controller;

import com.campuspulse.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
	private final DepartmentRepository repo;

	public DepartmentController(DepartmentRepository repo) {
		this.repo = repo;
	}

	@GetMapping
	public Object all() {
		return repo.findAll().stream().map(d -> Map.of("id", d.id, "name", d.name)).toList();
	}
}
