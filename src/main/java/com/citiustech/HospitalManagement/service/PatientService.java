package com.citiustech.HospitalManagement.service;

import com.citiustech.HospitalManagement.dto.PatientDto;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.Insurance;
import java.util.List;

public interface PatientService {

    PatientDto createPatient(PatientDto dto);

    List<PatientDto> getAllPatients();

    PatientDto getPatientById(Long id);

    PatientDto updatePatient(Long id, PatientDto dto);

    void deletePatient(Long id);

    List<PatientDto> searchByName(String name);

    List<PatientDto> getByBloodGroup(String bloodGroup);

    List<PatientDto> getActivePatients();

    List<Appointment> getPatientAppointments(Long patientId);

    List<Bill> getPatientBills(Long patientId);

    Insurance getPatientInsurance(Long patientId);
}
