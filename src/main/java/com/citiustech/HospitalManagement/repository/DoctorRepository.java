package com.citiustech.HospitalManagement.repository;

import com.citiustech.HospitalManagement.entity.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecializationIgnoreCase(String specialization);

    List<Doctor> findByDepartmentIgnoreCase(String department);

    List<Doctor> findByAvailableTrue();
}
