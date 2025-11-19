package com.citiustech.HospitalManagement.controller;

import com.citiustech.HospitalManagement.dto.InsuranceDto;
import com.citiustech.HospitalManagement.entity.Insurance;
import com.citiustech.HospitalManagement.service.InsuranceService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping
    public ResponseEntity<Insurance> create(@Valid @RequestBody InsuranceDto dto) {
        Insurance created = insuranceService.createInsurance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Insurance> getAll() {
        return insuranceService.getAllPolicies();
    }

    @GetMapping("/{id}")
    public Insurance getById(@PathVariable Long id) {
        return insuranceService.getById(id);
    }

    @PutMapping("/{id}")
    public Insurance update(@PathVariable Long id, @Valid @RequestBody InsuranceDto dto) {
        return insuranceService.updateInsurance(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public Insurance getByPatient(@PathVariable Long patientId) {
        return insuranceService.getByPatient(patientId);
    }

    @GetMapping("/provider/{provider}")
    public List<Insurance> getByProvider(@PathVariable String provider) {
        return insuranceService.getByProvider(provider);
    }

    @GetMapping("/active")
    public List<Insurance> getActive() {
        return insuranceService.getActivePolicies();
    }

    @GetMapping("/expiring")
    public List<Insurance> getExpiring(@RequestParam(name = "days", defaultValue = "30") int days) {
        return insuranceService.getExpiringSoon(days);
    }

    @PatchMapping("/{id}/renew")
    public Insurance renew(@PathVariable Long id, @Valid @RequestBody InsuranceDto dto) {
        return insuranceService.renewPolicy(id, dto);
    }
}
