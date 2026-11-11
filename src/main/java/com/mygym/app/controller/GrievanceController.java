package com.mygym.app.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mygym.app.models.Grievance;
import com.mygym.app.models.GrievanceWrapper;
import com.mygym.app.service.GrievanceService;
import com.mygym.app.validation.OnCreate;

@RestController
@RequestMapping("/api/v1/grievances") // 🎯 Fixed: Absolute, standardized string path
public class GrievanceController {

    private final GrievanceService service;

    public GrievanceController(GrievanceService service) {
        this.service = service;
    }
    
    // Path: POST /api/grievances
    @PostMapping
    public ResponseEntity<Object> create(@Validated({Default.class, OnCreate.class}) @RequestBody Grievance grievance) {
        service.createGrievance(grievance);
        return ResponseEntity.ok().build();
    }
    
    // Path: GET /api/grievances/allactive
    @GetMapping("/allactive") // 🎯 Fixed: Clean mapping structure
    public GrievanceWrapper getAllGrievances() {
        return service.getAllGrievances();
    }
    
    // Path: GET /api/grievances/allactive/{id}
    @GetMapping("/allactive/{id}") // 🎯 Fixed: Resolved dynamic string mapping collision
    public GrievanceWrapper getAllActiveGrievancesInBranch(@PathVariable("id") String branchId) {
        return service.getAllActiveGrievancesInBranch(branchId);
    }
    
    // Path: GET /api/grievances/allactive/member/{id}
    @GetMapping("/allactive/member/{id}")
    public GrievanceWrapper getAllActiveGrievancesForMember(@PathVariable("id") String memberId) {
        return service.getAllActiveGrievancesForMember(memberId);
    }
    
    // Path: GET /api/grievances/allinactive/member/{id}
    @GetMapping("/allinactive/member/{id}")
    public GrievanceWrapper getAllInactiveGrievancesInBranch(@PathVariable("id") String memberId) {
        return service.getAllInactiveGrievancesForMember(memberId);
    }
    
    // Path: PUT /api/grievances
    @PutMapping
    public ResponseEntity<Object> updateGrienvance(@Valid @RequestBody Grievance grievance) {
        if (service.updateGrienvance(grievance)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }
}
