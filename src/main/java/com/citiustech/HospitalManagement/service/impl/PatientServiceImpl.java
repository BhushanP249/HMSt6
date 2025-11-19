package com.citiustech.HospitalManagement.service.impl;

import com.citiustech.HospitalManagement.dto.PatientDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.Patient;
import com.citiustech.HospitalManagement.entity.PaymentStatus;
import com.citiustech.HospitalManagement.entity.AppointmentStatus;
import com.citiustech.HospitalManagement.entity.Insurance;
import com.citiustech.HospitalManagement.exception.BusinessException;
import com.citiustech.HospitalManagement.exception.DuplicateResourceException;
import com.citiustech.HospitalManagement.exception.ResourceNotFoundException;
import com.citiustech.HospitalManagement.repository.AppointmentRepository;
import com.citiustech.HospitalManagement.repository.BillRepository;
import com.citiustech.HospitalManagement.repository.InsuranceRepository;
import com.citiustech.HospitalManagement.repository.PatientRepository;
import com.citiustech.HospitalManagement.service.PatientService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final InsuranceRepository insuranceRepository;

    @Override
    public PatientDto createPatient(PatientDto dto) {
        patientRepository.findByPhoneNumber(dto.getPhoneNumber())
                .ifPresent(p -> { throw new DuplicateResourceException("Phone number already in use"); });
        if (dto.getEmail() != null) {
            patientRepository.findByEmail(dto.getEmail())
                    .ifPresent(p -> { throw new DuplicateResourceException("Email already in use"); });
        }
        Patient patient = toEntity(dto);
        patient.setActive(true);
        Patient saved = patientRepository.save(patient);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getAllPatients() {
        return patientRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long id) {
        Patient patient = getPatientEntity(id);
        return toDto(patient);
    }

    @Override
    public PatientDto updatePatient(Long id, PatientDto dto) {
        Patient patient = getPatientEntity(id);
        if (!patient.getPhoneNumber().equals(dto.getPhoneNumber())) {
            patientRepository.findByPhoneNumber(dto.getPhoneNumber())
                    .ifPresent(p -> { throw new DuplicateResourceException("Phone number already in use"); });
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(patient.getEmail())) {
            patientRepository.findByEmail(dto.getEmail())
                    .ifPresent(p -> { throw new DuplicateResourceException("Email already in use"); });
        }
        updatePatientFromDto(patient, dto);
        Patient saved = patientRepository.save(patient);
        return toDto(saved);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = getPatientEntity(id);
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        boolean hasPendingAppointments = appointments.stream().anyMatch(a ->
                a.getStatus() == AppointmentStatus.SCHEDULED ||
                        a.getStatus() == AppointmentStatus.CONFIRMED ||
                        a.getStatus() == AppointmentStatus.IN_PROGRESS);
        if (hasPendingAppointments) {
            throw new BusinessException("Cannot delete patient with pending appointments");
        }
        List<Bill> bills = billRepository.findByPatientId(id);
        boolean hasUnpaidBills = bills.stream().anyMatch(b ->
                b.getPaymentStatus() != PaymentStatus.PAID &&
                        b.getPaymentStatus() != PaymentStatus.CANCELLED);
        if (hasUnpaidBills) {
            throw new BusinessException("Cannot delete patient with unpaid bills");
        }
        patient.setActive(false);
        patientRepository.save(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> searchByName(String name) {
        return patientRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getByBloodGroup(String bloodGroup) {
        return patientRepository.findByBloodGroupIgnoreCase(bloodGroup)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> getActivePatients() {
        return patientRepository.findByActiveTrue().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getPatientAppointments(Long patientId) {
        getPatientEntity(patientId);
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getPatientBills(Long patientId) {
        getPatientEntity(patientId);
        return billRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Insurance getPatientInsurance(Long patientId) {
        getPatientEntity(patientId);
        return insuranceRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found for patient id " + patientId));
    }

    private Patient getPatientEntity(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    private PatientDto toDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setAllergies(patient.getAllergies());
        dto.setEmergencyContactName(patient.getEmergencyContactName());
        dto.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        return dto;
    }

    private Patient toEntity(PatientDto dto) {
        Patient patient = new Patient();
        updatePatientFromDto(patient, dto);
        return patient;
    }

    private void updatePatientFromDto(Patient patient, PatientDto dto) {
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setAllergies(dto.getAllergies());
        patient.setEmergencyContactName(dto.getEmergencyContactName());
        patient.setEmergencyContactPhone(dto.getEmergencyContactPhone());
    }
}
