package com.citiustech.HospitalManagement.service.impl;

import com.citiustech.HospitalManagement.dto.BillDto;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.PaymentStatus;
import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.Patient;
import com.citiustech.HospitalManagement.exception.BusinessException;
import com.citiustech.HospitalManagement.exception.ResourceNotFoundException;
import com.citiustech.HospitalManagement.repository.AppointmentRepository;
import com.citiustech.HospitalManagement.repository.BillRepository;
import com.citiustech.HospitalManagement.repository.PatientRepository;
import com.citiustech.HospitalManagement.service.BillService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    /**
     * Creates a new {@link Bill} from the provided {@link BillDto}.
     * <p>
     * The method resolves the associated {@link Patient} (and optional {@link Appointment}),
     * maps all DTO fields to a new Bill entity, calculates net and balance amounts, derives
     * the appropriate {@link PaymentStatus}, assigns a default due date of 30 days from the
     * bill date when none is provided, and finally persists the Bill.
     *
     * @param dto the request payload containing patient/appointment references and billing details
     * @return the persisted Bill entity with calculated financial fields and payment status
     */
    public Bill createBill(BillDto dto) {
        Patient patient = getPatient(dto.getPatientId());
        Appointment appointment = null;
        if (dto.getAppointmentId() != null) {
            appointment = getAppointment(dto.getAppointmentId());
        }
        Bill bill = toEntity(dto, patient, appointment);
        calculateAmounts(bill);
        updatePaymentStatus(bill);
        if (bill.getDueDate() == null) {
            bill.setDueDate(bill.getBillDate().plusDays(30));
        }
        return billRepository.save(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Bill getBillById(Long id) {
        return getBill(id);
    }

    @Override
    public Bill updateBill(Long id, BillDto dto) {
        Bill existing = getBill(id);
        Patient patient = getPatient(dto.getPatientId());
        Appointment appointment = null;
        if (dto.getAppointmentId() != null) {
            appointment = getAppointment(dto.getAppointmentId());
        }
        updateBillFromDto(existing, dto, patient, appointment);
        calculateAmounts(existing);
        updatePaymentStatus(existing);
        return billRepository.save(existing);
    }

    @Override
    public void deleteBill(Long id) {
        Bill bill = getBill(id);
        billRepository.delete(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getBillsByPatient(Long patientId) {
        getPatient(patientId);
        return billRepository.findByPatientId(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getBillsByStatus(PaymentStatus status) {
        return billRepository.findByPaymentStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getOverdueBills() {
        return billRepository.findByDueDateBeforeAndPaymentStatusNot(LocalDate.now(), PaymentStatus.PAID);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bill> getBillsByDateRange(LocalDate start, LocalDate end) {
        return billRepository.findByBillDateBetween(start, end);
    }

    @Override
    public Bill recordPayment(Long id, double amount, String method) {
        if (amount <= 0) {
            throw new BusinessException("Payment amount must be positive");
        }
        Bill bill = getBill(id);
        bill.setPaidAmount(bill.getPaidAmount() + amount);
        bill.setPaymentMethod(method);
        calculateAmounts(bill);
        updatePaymentStatus(bill);
        return billRepository.save(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public Bill getByBillNumber(String billNumber) {
        return billRepository.findByBillNumber(billNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with number " + billNumber));
    }

    private Bill getBill(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id " + id));
    }

    private Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + id));
    }

    private Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + id));
    }

    private Bill toEntity(BillDto dto, Patient patient, Appointment appointment) {
        Bill bill = new Bill();
        updateBillFromDto(bill, dto, patient, appointment);
        return bill;
    }

    private void updateBillFromDto(Bill bill, BillDto dto, Patient patient, Appointment appointment) {
        bill.setPatient(patient);
        bill.setAppointment(appointment);
        bill.setBillNumber(dto.getBillNumber());
        bill.setBillDate(dto.getBillDate());
        bill.setTotalAmount(dto.getTotalAmount());
        bill.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : 0.0);
        bill.setTax(dto.getTax() != null ? dto.getTax() : 0.0);
        bill.setDescription(dto.getDescription());
        bill.setPaymentMethod(dto.getPaymentMethod());
        if (bill.getDueDate() == null) {
            bill.setDueDate(dto.getBillDate().plusDays(30));
        }
    }

    private void calculateAmounts(Bill bill) {
        double net = bill.getTotalAmount() - bill.getDiscount() + bill.getTax();
        bill.setNetAmount(net);
        double balance = net - bill.getPaidAmount();
        bill.setBalanceAmount(balance);
    }

    private void updatePaymentStatus(Bill bill) {
        if (bill.getBalanceAmount() <= 0) {
            bill.setPaymentStatus(PaymentStatus.PAID);
        } else if (bill.getPaidAmount() > 0 && bill.getBalanceAmount() > 0) {
            bill.setPaymentStatus(PaymentStatus.PARTIAL);
        } else {
            bill.setPaymentStatus(PaymentStatus.PENDING);
        }
        if (bill.getBalanceAmount() > 0 && bill.getDueDate() != null && bill.getDueDate().isBefore(LocalDate.now())) {
            bill.setPaymentStatus(PaymentStatus.OVERDUE);
        }
    }
}
