package com.citiustech.HospitalManagement.repository;

import com.citiustech.HospitalManagement.entity.Insurance;
import com.citiustech.HospitalManagement.entity.InsuranceStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    Optional<Insurance> findByPatientId(Long patientId);

    List<Insurance> findByProviderIgnoreCase(String provider);

    List<Insurance> findByStatus(InsuranceStatus status);

    List<Insurance> findByEndDateBetween(LocalDate start, LocalDate end);
}
