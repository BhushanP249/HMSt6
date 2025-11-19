package com.citiustech.HospitalManagement.service;

import com.citiustech.HospitalManagement.dto.InsuranceDto;
import com.citiustech.HospitalManagement.entity.Insurance;
import java.util.List;

public interface InsuranceService {

    Insurance createInsurance(InsuranceDto dto);

    List<Insurance> getAllPolicies();

    Insurance getById(Long id);

    Insurance updateInsurance(Long id, InsuranceDto dto);

    void deleteInsurance(Long id);

    Insurance getByPatient(Long patientId);

    List<Insurance> getByProvider(String provider);

    List<Insurance> getActivePolicies();

    List<Insurance> getExpiringSoon(int days);

    Insurance renewPolicy(Long id, InsuranceDto dto);
}
