package com.citiustech.HospitalManagement.controller;

import com.citiustech.HospitalManagement.dto.DoctorDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.service.DoctorService;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorDto> create(@Validated @RequestBody DoctorDto dto) {
        DoctorDto created = doctorService.createDoctor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<DoctorDto> getAll() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDto getById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/{id}")
    public DoctorDto update(@PathVariable Long id, @Validated @RequestBody DoctorDto dto) {
        return doctorService.updateDoctor(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/specialization/{spec}")
    public List<DoctorDto> getBySpecialization(@PathVariable("spec") String spec) {
        return doctorService.getBySpecialization(spec);
    }

    @GetMapping("/department/{dept}")
    public List<DoctorDto> getByDepartment(@PathVariable("dept") String dept) {
        return doctorService.getByDepartment(dept);
    }

    @GetMapping("/available")
    public List<DoctorDto> getAvailable() {
        return doctorService.getAvailableDoctors();
    }

    @GetMapping("/{id}/appointments")
    public List<Appointment> getAppointments(@PathVariable Long id) {
        return doctorService.getDoctorAppointments(id);
    }

    @PatchMapping("/{id}/availability")
    public DoctorDto toggleAvailability(@PathVariable Long id) {
        return doctorService.toggleAvailability(id);
    }
}
