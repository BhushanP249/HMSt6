package com.citiustech.HospitalManagement.service;

import com.citiustech.HospitalManagement.dto.DoctorDto;
import com.citiustech.HospitalManagement.entity.Appointment;
import java.util.List;

public interface DoctorService {

    DoctorDto createDoctor(DoctorDto dto);

    List<DoctorDto> getAllDoctors();

    DoctorDto getDoctorById(Long id);

    DoctorDto updateDoctor(Long id, DoctorDto dto);

    void deleteDoctor(Long id);

    List<DoctorDto> getBySpecialization(String specialization);

    List<DoctorDto> getByDepartment(String department);

    List<DoctorDto> getAvailableDoctors();

    List<Appointment> getDoctorAppointments(Long doctorId);

    DoctorDto toggleAvailability(Long doctorId);
}
