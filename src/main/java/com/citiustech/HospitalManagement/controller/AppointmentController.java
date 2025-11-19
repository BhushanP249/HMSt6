package com.citiustech.HospitalManagement.controller;

import com.citiustech.HospitalManagement.dto.AppointmentDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.AppointmentStatus;
import com.citiustech.HospitalManagement.service.AppointmentService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> create(@Valid @RequestBody AppointmentDto dto) {
        Appointment created = appointmentService.createAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @Valid @RequestBody AppointmentDto dto) {
        return appointmentService.updateAppointment(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getByPatient(@PathVariable Long patientId) {
        return appointmentService.getByPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getByDoctor(@PathVariable Long doctorId) {
        return appointmentService.getByDoctor(doctorId);
    }

    @GetMapping("/status/{status}")
    public List<Appointment> getByStatus(@PathVariable("status") AppointmentStatus status) {
        return appointmentService.getByStatus(status);
    }

    @GetMapping("/date-range")
    public List<Appointment> getByDateRange(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return appointmentService.getByDateRange(start, end);
    }

    @PatchMapping("/{id}/status")
    public Appointment updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        return appointmentService.updateStatus(id, request.getStatus());
    }

    @PatchMapping("/{id}/complete")
    public Appointment complete(@PathVariable Long id, @RequestBody CompleteAppointmentRequest request) {
        return appointmentService.completeAppointment(id, request.getDiagnosis(), request.getPrescription());
    }

    @Data
    public static class StatusUpdateRequest {
        private AppointmentStatus status;
    }

    @Data
    public static class CompleteAppointmentRequest {
        private String diagnosis;
        private String prescription;
    }
}
