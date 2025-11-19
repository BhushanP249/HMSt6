package com.citiustech.HospitalManagement.service.impl;

import com.citiustech.HospitalManagement.dto.AppointmentDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.AppointmentStatus;
import com.citiustech.HospitalManagement.entity.Doctor;
import com.citiustech.HospitalManagement.entity.Patient;
import com.citiustech.HospitalManagement.exception.BusinessException;
import com.citiustech.HospitalManagement.exception.ResourceNotFoundException;
import com.citiustech.HospitalManagement.repository.AppointmentRepository;
import com.citiustech.HospitalManagement.repository.DoctorRepository;
import com.citiustech.HospitalManagement.repository.PatientRepository;
import com.citiustech.HospitalManagement.service.AppointmentService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public Appointment createAppointment(AppointmentDto dto) {
        Patient patient = getPatient(dto.getPatientId());
        Doctor doctor = getDoctor(dto.getDoctorId());
        validateAppointmentBusinessRules(patient, doctor, dto.getAppointmentDateTime());
        Appointment appointment = toEntity(dto, patient, doctor);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment getAppointmentById(Long id) {
        return getAppointment(id);
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentDto dto) {
        Appointment existing = getAppointment(id);
        Patient patient = getPatient(dto.getPatientId());
        Doctor doctor = getDoctor(dto.getDoctorId());
        validateAppointmentBusinessRules(patient, doctor, dto.getAppointmentDateTime());

        existing.setPatient(patient);
        existing.setDoctor(doctor);
        existing.setAppointmentDateTime(dto.getAppointmentDateTime());
        existing.setReason(dto.getReason());
        existing.setNotes(dto.getNotes());
        return appointmentRepository.save(existing);
    }

    @Override
    public void cancelAppointment(Long id) {
        Appointment appointment = getAppointment(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getByPatient(Long patientId) {
        getPatient(patientId);
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getByDoctor(Long doctorId) {
        getDoctor(doctorId);
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
    }

    @Override
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = getAppointment(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment completeAppointment(Long id, String diagnosis, String prescription) {
        Appointment appointment = getAppointment(id);
        appointment.setDiagnosis(diagnosis);
        appointment.setPrescription(prescription);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    private Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
    }

    private Patient getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
        if (!patient.isActive()) {
            throw new BusinessException("Patient must be active to book appointment");
        }
        return patient;
    }

    private Doctor getDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + id));
        if (!doctor.isAvailable()) {
            throw new BusinessException("Doctor is not available");
        }
        return doctor;
    }

    private void validateAppointmentBusinessRules(Patient patient, Doctor doctor, LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot book appointment in the past");
        }
        boolean exists = appointmentRepository.existsByDoctorIdAndAppointmentDateTime(doctor.getId(), dateTime);
        if (exists) {
            throw new BusinessException("Doctor already has an appointment at this time");
        }
    }

    private Appointment toEntity(AppointmentDto dto, Patient patient, Doctor doctor) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());
        return appointment;
    }
}
