package com.citiustech.HospitalManagement.controller;

import com.citiustech.HospitalManagement.dto.BillDto;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.PaymentStatus;
import com.citiustech.HospitalManagement.service.BillService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping
    public ResponseEntity<Bill> create(@Valid @RequestBody BillDto dto) {
        Bill created = billService.createBill(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Bill> getAll() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public Bill getById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @PutMapping("/{id}")
    public Bill update(@PathVariable Long id, @Valid @RequestBody BillDto dto) {
        return billService.updateBill(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public List<Bill> getByPatient(@PathVariable Long patientId) {
        return billService.getBillsByPatient(patientId);
    }

    @GetMapping("/status/{status}")
    public List<Bill> getByStatus(@PathVariable("status") PaymentStatus status) {
        return billService.getBillsByStatus(status);
    }

    @GetMapping("/overdue")
    public List<Bill> getOverdue() {
        return billService.getOverdueBills();
    }

    @GetMapping("/date-range")
    public List<Bill> getByDateRange(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                     @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return billService.getBillsByDateRange(start, end);
    }

    @PatchMapping("/{id}/payment")
    public Bill recordPayment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        return billService.recordPayment(id, request.getAmount(), request.getMethod());
    }

    @GetMapping("/number/{billNumber}")
    public Bill getByBillNumber(@PathVariable String billNumber) {
        return billService.getByBillNumber(billNumber);
    }

    @Data
    public static class PaymentRequest {
        private double amount;
        private String method;
    }
}
