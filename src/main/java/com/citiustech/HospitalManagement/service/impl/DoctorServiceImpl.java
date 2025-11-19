package com.citiustech.HospitalManagement.service.impl;

import com.citiustech.HospitalManagement.dto.DoctorDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.AppointmentStatus;
import com.citiustech.HospitalManagement.entity.Doctor;
import com.citiustech.HospitalManagement.exception.BusinessException;
import com.citiustech.HospitalManagement.exception.DuplicateResourceException;
import com.citiustech.HospitalManagement.exception.ResourceNotFoundException;
import com.citiustech.HospitalManagement.repository.AppointmentRepository;
import com.citiustech.HospitalManagement.repository.DoctorRepository;
import com.citiustech.HospitalManagement.service.DoctorService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public DoctorDto createDoctor(DoctorDto dto) {
        doctorRepository.findByLicenseNumber(dto.getLicenseNumber())
                .ifPresent(d -> { throw new DuplicateResourceException("License number already in use"); });
        Doctor doctor = toEntity(dto);
        Doctor saved = doctorRepository.save(doctor);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {
        return toDto(getDoctorEntity(id));
    }

    @Override
    public DoctorDto updateDoctor(Long id, DoctorDto dto) {
        Doctor doctor = getDoctorEntity(id);
        if (!doctor.getLicenseNumber().equals(dto.getLicenseNumber())) {
            doctorRepository.findByLicenseNumber(dto.getLicenseNumber())
                    .ifPresent(d -> { throw new DuplicateResourceException("License number already in use"); });
        }
        updateDoctorFromDto(doctor, dto);
        return toDto(doctorRepository.save(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorEntity(id);
        List<Appointment> appointments = appointmentRepository.findByDoctorId(id);
        boolean hasScheduled = appointments.stream().anyMatch(a ->
                a.getStatus() == AppointmentStatus.SCHEDULED ||
                        a.getStatus() == AppointmentStatus.CONFIRMED ||
                        a.getStatus() == AppointmentStatus.IN_PROGRESS);
        if (hasScheduled) {
            throw new BusinessException("Cannot delete doctor with scheduled appointments");
        }
        doctorRepository.delete(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationIgnoreCase(specialization)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getByDepartment(String department) {
        return doctorRepository.findByDepartmentIgnoreCase(department)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAvailableDoctors() {
        return doctorRepository.findByAvailableTrue().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        getDoctorEntity(doctorId);
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    public DoctorDto toggleAvailability(Long doctorId) {
        Doctor doctor = getDoctorEntity(doctorId);
        doctor.setAvailable(!doctor.isAvailable());
        return toDto(doctorRepository.save(doctor));
    }

    private Doctor getDoctorEntity(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + id));
    }

    private DoctorDto toDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setLicenseNumber(doctor.getLicenseNumber());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setEmail(doctor.getEmail());
        dto.setYearsOfExperience(doctor.getYearsOfExperience());
        dto.setQualification(doctor.getQualification());
        dto.setDepartment(doctor.getDepartment());
        dto.setConsultationHours(doctor.getConsultationHours());
        dto.setConsultationFee(doctor.getConsultationFee());
        return dto;
    }

    private Doctor toEntity(DoctorDto dto) {
        Doctor doctor = new Doctor();
        updateDoctorFromDto(doctor, dto);
        return doctor;
    }

    private void updateDoctorFromDto(Doctor doctor, DoctorDto dto) {
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setEmail(dto.getEmail());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setQualification(dto.getQualification());
        doctor.setDepartment(dto.getDepartment());
        doctor.setConsultationHours(dto.getConsultationHours());
        doctor.setConsultationFee(dto.getConsultationFee());
    }
}
