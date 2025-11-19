package com.citiustech.HospitalManagement.service.impl;

import com.citiustech.HospitalManagement.dto.InsuranceDto;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.Insurance;
import com.citiustech.HospitalManagement.entity.InsuranceStatus;
import com.citiustech.HospitalManagement.entity.Patient;
import com.citiustech.HospitalManagement.exception.BusinessException;
import com.citiustech.HospitalManagement.exception.DuplicateResourceException;
import com.citiustech.HospitalManagement.exception.ResourceNotFoundException;
import com.citiustech.HospitalManagement.repository.BillRepository;
import com.citiustech.HospitalManagement.repository.InsuranceRepository;
import com.citiustech.HospitalManagement.repository.PatientRepository;
import com.citiustech.HospitalManagement.service.InsuranceService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;
    private final BillRepository billRepository;

    @Override
    public Insurance createInsurance(InsuranceDto dto) {
        Patient patient = getPatient(dto.getPatientId());
        insuranceRepository.findByPatientId(patient.getId()).ifPresent(i -> {
            throw new BusinessException("Patient already has an insurance policy");
        });
        insuranceRepository.findAll().stream()
                .filter(i -> i.getPolicyNumber().equals(dto.getPolicyNumber()))
                .findAny()
                .ifPresent(i -> { throw new DuplicateResourceException("Policy number already exists"); });

        Insurance insurance = toEntity(dto, patient);
        updateStatus(insurance);
        return insuranceRepository.save(insurance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Insurance> getAllPolicies() {
        return insuranceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Insurance getById(Long id) {
        return getInsurance(id);
    }

    @Override
    public Insurance updateInsurance(Long id, InsuranceDto dto) {
        Insurance existing = getInsurance(id);
        Patient patient = getPatient(dto.getPatientId());
        if (!existing.getPolicyNumber().equals(dto.getPolicyNumber())) {
            insuranceRepository.findAll().stream()
                    .filter(i -> i.getPolicyNumber().equals(dto.getPolicyNumber()))
                    .findAny()
                    .ifPresent(i -> { throw new DuplicateResourceException("Policy number already exists"); });
        }
        updateFromDto(existing, dto, patient);
        updateStatus(existing);
        return insuranceRepository.save(existing);
    }

    @Override
    public void deleteInsurance(Long id) {
        Insurance insurance = getInsurance(id);
        List<Bill> bills = billRepository.findByPatientId(insurance.getPatient().getId());
        boolean linkedToBills = bills.stream()
                .anyMatch(b -> b.getInsuranceClaim() != null && !b.getInsuranceClaim().isBlank());
        if (linkedToBills) {
            throw new BusinessException("Cannot delete insurance linked to bills");
        }
        insuranceRepository.delete(insurance);
    }

    @Override
    @Transactional(readOnly = true)
    public Insurance getByPatient(Long patientId) {
        getPatient(patientId);
        return insuranceRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found for patient id " + patientId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Insurance> getByProvider(String provider) {
        return insuranceRepository.findByProviderIgnoreCase(provider);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Insurance> getActivePolicies() {
        return insuranceRepository.findByStatus(InsuranceStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Insurance> getExpiringSoon(int days) {
        LocalDate now = LocalDate.now();
        LocalDate end = now.plusDays(days);
        return insuranceRepository.findByEndDateBetween(now, end).stream()
                .peek(this::updateStatus)
                .filter(i -> i.getStatus() == InsuranceStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public Insurance renewPolicy(Long id, InsuranceDto dto) {
        Insurance insurance = getInsurance(id);
        if (dto.getEndDate().isBefore(insurance.getEndDate())) {
            throw new BusinessException("New end date must be after current end date");
        }
        insurance.setEndDate(dto.getEndDate());
        updateStatus(insurance);
        return insuranceRepository.save(insurance);
    }

    private Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    private Insurance getInsurance(Long id) {
        return insuranceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance not found with id " + id));
    }

    private Insurance toEntity(InsuranceDto dto, Patient patient) {
        Insurance insurance = new Insurance();
        updateFromDto(insurance, dto, patient);
        return insurance;
    }

    private void updateFromDto(Insurance insurance, InsuranceDto dto, Patient patient) {
        insurance.setPatient(patient);
        insurance.setProvider(dto.getProvider());
        insurance.setPolicyNumber(dto.getPolicyNumber());
        insurance.setStartDate(dto.getStartDate());
        insurance.setEndDate(dto.getEndDate());
        insurance.setCoverageType(dto.getCoverageType());
        insurance.setCoverageAmount(dto.getCoverageAmount());
        insurance.setPremiumAmount(dto.getPremiumAmount());
        insurance.setPaymentFrequency(dto.getPaymentFrequency());
        insurance.setCoPayAmount(dto.getCoPayAmount());
        insurance.setDeductibleAmount(dto.getDeductibleAmount());
        insurance.setCoverageDetails(dto.getCoverageDetails());
        insurance.setExclusions(dto.getExclusions());
    }

    private void updateStatus(Insurance insurance) {
        LocalDate today = LocalDate.now();
        if (insurance.getEndDate().isBefore(today)) {
            insurance.setStatus(InsuranceStatus.EXPIRED);
            insurance.setActive(false);
        } else if (insurance.getStartDate().isAfter(today)) {
            insurance.setStatus(InsuranceStatus.PENDING);
            insurance.setActive(false);
        } else {
            insurance.setStatus(InsuranceStatus.ACTIVE);
            insurance.setActive(true);
        }
    }
}
