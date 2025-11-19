package com.citiustech.HospitalManagement.repository;

import com.citiustech.HospitalManagement.entity.Patient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPhoneNumber(String phoneNumber);

    Optional<Patient> findByEmail(String email);

    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    List<Patient> findByBloodGroupIgnoreCase(String bloodGroup);

    List<Patient> findByActiveTrue();
}
