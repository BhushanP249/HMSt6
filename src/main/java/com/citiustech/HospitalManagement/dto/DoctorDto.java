package com.citiustech.HospitalManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DoctorDto {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Size(max = 100)
    private String specialization;

    @NotBlank
    @Size(max = 100)
    private String licenseNumber;

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @Email
    @Size(max = 255)
    private String email;

    @NotNull
    @Min(0)
    private Integer yearsOfExperience;

    @NotBlank
    @Size(max = 500)
    private String qualification;

    @Size(max = 100)
    private String department;

    @Size(max = 500)
    private String consultationHours;

    @NotNull
    @Min(0)
    private Double consultationFee;
}
