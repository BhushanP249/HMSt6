package com.citiustech.HospitalManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PatientDto {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(max = 20)
    private String gender;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{7,20}$")
    private String phoneNumber;

    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 500)
    private String address;

    @Size(max = 5)
    private String bloodGroup;

    @Size(max = 1000)
    private String medicalHistory;

    @Size(max = 500)
    private String allergies;

    @Size(max = 500)
    private String emergencyContactName;

    @Size(max = 20)
    private String emergencyContactPhone;
}
