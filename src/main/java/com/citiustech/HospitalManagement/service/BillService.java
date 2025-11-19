package com.citiustech.HospitalManagement.service;

import com.citiustech.HospitalManagement.dto.BillDto;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.PaymentStatus;
import java.time.LocalDate;
import java.util.List;

public interface BillService {

    Bill createBill(BillDto dto);

    List<Bill> getAllBills();

    Bill getBillById(Long id);

    Bill updateBill(Long id, BillDto dto);

    void deleteBill(Long id);

    List<Bill> getBillsByPatient(Long patientId);

    List<Bill> getBillsByStatus(PaymentStatus status);

    List<Bill> getOverdueBills();

    List<Bill> getBillsByDateRange(LocalDate start, LocalDate end);

    Bill recordPayment(Long id, double amount, String method);

    Bill getByBillNumber(String billNumber);
}
