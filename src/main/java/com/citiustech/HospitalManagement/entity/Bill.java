package com.citiustech.HospitalManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.PaymentStatus;

@Entity
@Table(
        name = "bills",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_bill_number", columnNames = "bill_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @NotBlank
    @Size(max = 100)
    @Column(name = "bill_number", nullable = false, length = 100)
    private String billNumber;

    @NotNull
    @Column(name = "bill_date", nullable = false)
    @Default
    private LocalDate billDate = LocalDate.now();

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @DecimalMin("0.0")
    @Column(name = "discount", nullable = false)
    @Default
    private Double discount = 0.0;

    @DecimalMin("0.0")
    @Column(name = "tax", nullable = false)
    @Default
    private Double tax = 0.0;

    @DecimalMin("0.0")
    @Column(name = "net_amount", nullable = false)
    private Double netAmount;

    @DecimalMin("0.0")
    @Column(name = "paid_amount", nullable = false)
    @Default
    private Double paidAmount = 0.0;

    @DecimalMin("0.0")
    @Column(name = "balance_amount", nullable = false)
    private Double balanceAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 50)
    @Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Size(max = 50)
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Size(max = 500)
    @Column(name = "insurance_claim", length = 500)
    private String insuranceClaim;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
}
