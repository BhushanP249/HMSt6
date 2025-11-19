package com.citiustech.HospitalManagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class InsuranceDto {

    private Long id;

    @NotNull
    private Long patientId;

    @NotBlank
    @Size(max = 255)
    private String provider;

    @NotBlank
    @Size(max = 100)
    private String policyNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotBlank
    @Size(max = 100)
    private String coverageType;

    @NotNull
    @DecimalMin("0.0")
    private Double coverageAmount;

    @NotNull
    @DecimalMin("0.0")
    private Double premiumAmount;

    @Size(max = 50)
    private String paymentFrequency;

    @DecimalMin("0.0")
    private Double coPayAmount;

    @DecimalMin("0.0")
    private Double deductibleAmount;

    @Size(max = 1000)
    private String coverageDetails;

    @Size(max = 500)
    private String exclusions;
}
