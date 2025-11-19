package com.citiustech.HospitalManagement.service;

import com.citiustech.HospitalManagement.dto.AppointmentDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    Appointment createAppointment(AppointmentDto dto);

    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(Long id);

    Appointment updateAppointment(Long id, AppointmentDto dto);

    void cancelAppointment(Long id);

    List<Appointment> getByPatient(Long patientId);

    List<Appointment> getByDoctor(Long doctorId);

    List<Appointment> getByStatus(AppointmentStatus status);

    List<Appointment> getByDateRange(LocalDateTime start, LocalDateTime end);

    Appointment updateStatus(Long id, AppointmentStatus status);

    Appointment completeAppointment(Long id, String diagnosis, String prescription);
}
