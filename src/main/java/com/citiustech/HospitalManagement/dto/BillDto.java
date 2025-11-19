package com.citiustech.HospitalManagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BillDto {

    private Long id;

    @NotNull
    private Long patientId;

    private Long appointmentId;

    @NotBlank
    @Size(max = 100)
    private String billNumber;

    @NotNull
    private LocalDate billDate;

    @NotNull
    @DecimalMin("0.0")
    private Double totalAmount;

    @DecimalMin("0.0")
    private Double discount;

    @DecimalMin("0.0")
    private Double tax;

    @Size(max = 1000)
    private String description;

    @Size(max = 50)
    private String paymentMethod;
}
