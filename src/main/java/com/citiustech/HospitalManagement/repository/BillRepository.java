package com.citiustech.HospitalManagement.repository;

import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.PaymentStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByPatientId(Long patientId);

    List<Bill> findByPaymentStatus(PaymentStatus status);

    List<Bill> findByBillDateBetween(LocalDate start, LocalDate end);

    List<Bill> findByDueDateBeforeAndPaymentStatusNot(LocalDate date, PaymentStatus status);

    Optional<Bill> findByBillNumber(String billNumber);
}
