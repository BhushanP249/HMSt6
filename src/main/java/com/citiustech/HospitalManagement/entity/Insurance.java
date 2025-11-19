package com.citiustech.HospitalManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.citiustech.HospitalManagement.entity.Patient;
import com.citiustech.HospitalManagement.entity.InsuranceStatus;

@Entity
@Table(
        name = "insurance",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_insurance_policy", columnNames = "policy_number"),
                @UniqueConstraint(name = "uk_insurance_patient", columnNames = "patient_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotBlank
    @Size(max = 255)
    @Column(name = "provider", nullable = false, length = 255)
    private String provider;

    @NotBlank
    @Size(max = 100)
    @Column(name = "policy_number", nullable = false, length = 100)
    private String policyNumber;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotBlank
    @Size(max = 100)
    @Column(name = "coverage_type", nullable = false, length = 100)
    private String coverageType;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "coverage_amount", nullable = false)
    private Double coverageAmount;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "premium_amount", nullable = false)
    private Double premiumAmount;

    @Size(max = 50)
    @Column(name = "payment_frequency", length = 50)
    private String paymentFrequency;

    @DecimalMin("0.0")
    @Column(name = "co_pay_amount")
    private Double coPayAmount;

    @DecimalMin("0.0")
    @Column(name = "deductible_amount")
    private Double deductibleAmount;

    @Size(max = 1000)
    @Column(name = "coverage_details", length = 1000)
    private String coverageDetails;

    @Size(max = 500)
    @Column(name = "exclusions", length = 500)
    private String exclusions;

    @Column(name = "active", nullable = false)
    @Default
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Default
    private InsuranceStatus status = InsuranceStatus.ACTIVE;
}
