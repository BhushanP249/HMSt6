package com.citiustech.HospitalManagement.controller;

import com.citiustech.HospitalManagement.dto.PatientDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.Insurance;
import com.citiustech.HospitalManagement.service.PatientService;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Validated
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDto> create(@Validated @RequestBody PatientDto dto) {
        PatientDto created = patientService.createPatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<PatientDto> getAll() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public PatientDto getById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public PatientDto update(@PathVariable Long id, @Validated @RequestBody PatientDto dto) {
        return patientService.updatePatient(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<PatientDto> searchByName(@RequestParam("name") @NotBlank String name) {
        return patientService.searchByName(name);
    }

    @GetMapping("/blood-group/{group}")
    public List<PatientDto> getByBloodGroup(@PathVariable("group") String group) {
        return patientService.getByBloodGroup(group);
    }

    @GetMapping("/active")
    public List<PatientDto> getActive() {
        return patientService.getActivePatients();
    }

    @GetMapping("/{id}/appointments")
    public List<Appointment> getAppointments(@PathVariable Long id) {
        return patientService.getPatientAppointments(id);
    }

    @GetMapping("/{id}/bills")
    public List<Bill> getBills(@PathVariable Long id) {
        return patientService.getPatientBills(id);
    }

    @GetMapping("/{id}/insurance")
    public Insurance getInsurance(@PathVariable Long id) {
        return patientService.getPatientInsurance(id);
    }
}
