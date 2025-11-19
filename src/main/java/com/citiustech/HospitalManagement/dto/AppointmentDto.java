package com.citiustech.HospitalManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AppointmentDto {

    private Long id;

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private LocalDateTime appointmentDateTime;

    @NotBlank
    @Size(max = 500)
    private String reason;

    @Size(max = 1000)
    private String notes;
}
