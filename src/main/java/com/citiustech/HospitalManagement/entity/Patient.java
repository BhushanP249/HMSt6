package com.citiustech.HospitalManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.Insurance;

@Entity
@Table(
        name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_patient_phone", columnNames = "phone_number"),
                @UniqueConstraint(name = "uk_patient_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull
    @Past
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(max = 20)
    @Column(name = "gender", nullable = false, length = 20)
    private String gender;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{7,20}$")
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Email
    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;

    @NotBlank
    @Size(max = 500)
    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Size(max = 5)
    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Size(max = 1000)
    @Column(name = "medical_history", length = 1000)
    private String medicalHistory;

    @Size(max = 500)
    @Column(name = "allergies", length = 500)
    private String allergies;

    @Size(max = 500)
    @Column(name = "emergency_contact_name", length = 500)
    private String emergencyContactName;

    @Size(max = 20)
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    @Default
    private LocalDate registrationDate = LocalDate.now();

    @Column(name = "active", nullable = false)
    @Default
    private boolean active = true;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @Default
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @Default
    private List<Bill> bills = new ArrayList<>();

    @OneToOne(mappedBy = "patient", fetch = FetchType.LAZY)
    private Insurance insurance;
}
