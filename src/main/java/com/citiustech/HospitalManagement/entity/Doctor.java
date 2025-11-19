package com.citiustech.HospitalManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.citiustech.HospitalManagement.entity.Appointment;

@Entity
@Table(
        name = "doctors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_doctor_license", columnNames = "license_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;

    @NotBlank
    @Size(max = 100)
    @Column(name = "license_number", nullable = false, length = 100)
    private String licenseNumber;

    @NotBlank
    @Size(max = 20)
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Email
    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;

    @NotNull
    @Min(0)
    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @NotBlank
    @Size(max = 500)
    @Column(name = "qualification", nullable = false, length = 500)
    private String qualification;

    @Size(max = 100)
    @Column(name = "department", length = 100)
    private String department;

    @Size(max = 500)
    @Column(name = "consultation_hours", length = 500)
    private String consultationHours;

    @NotNull
    @Min(0)
    @Column(name = "consultation_fee", nullable = false)
    private Double consultationFee;

    @NotNull
    @Column(name = "joining_date", nullable = false)
    @Default
    private LocalDate joiningDate = LocalDate.now();

    @Column(name = "available", nullable = false)
    @Default
    private boolean available = true;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    @Default
    private List<Appointment> appointments = new ArrayList<>();
}
