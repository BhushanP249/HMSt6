package com.citiustech.HospitalManagement.config;

import com.citiustech.HospitalManagement.entity.Appointment;
import com.citiustech.HospitalManagement.entity.AppointmentStatus;
import com.citiustech.HospitalManagement.entity.Bill;
import com.citiustech.HospitalManagement.entity.Doctor;
import com.citiustech.HospitalManagement.entity.Insurance;
import com.citiustech.HospitalManagement.entity.Patient;
import com.citiustech.HospitalManagement.entity.PaymentStatus;
import com.citiustech.HospitalManagement.repository.AppointmentRepository;
import com.citiustech.HospitalManagement.repository.BillRepository;
import com.citiustech.HospitalManagement.repository.DoctorRepository;
import com.citiustech.HospitalManagement.repository.InsuranceRepository;
import com.citiustech.HospitalManagement.repository.PatientRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final InsuranceRepository insuranceRepository;

    @Override
    public void run(String... args) {
        if (patientRepository.count() > 0) {
            return;
        }

        Random random = new Random();

        List<Patient> patients = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Patient p = new Patient();
            p.setFirstName("Patient" + i);
            p.setLastName("Test");
            p.setDateOfBirth(LocalDate.now().minusYears(20 + i));
            p.setGender(i % 2 == 0 ? "Male" : "Female");
            p.setPhoneNumber("+9112345678" + String.format("%02d", i));
            p.setEmail("patient" + i + "@example.com");
            p.setAddress("123 Test Street City " + i);
            p.setBloodGroup(i % 2 == 0 ? "O+" : "A+");
            p.setMedicalHistory(i % 3 == 0 ? "Hypertension" : null);
            p.setAllergies(i % 4 == 0 ? "Penicillin" : null);
            p.setEmergencyContactName("Contact " + i);
            p.setEmergencyContactPhone("+9111122233" + String.format("%02d", i));
            patients.add(p);
        }
        patients = patientRepository.saveAll(patients);

        List<Doctor> doctors = new ArrayList<>();
        String[] specs = {"Cardiology", "Neurology", "Orthopedics", "Pediatrics", "General Medicine"};
        String[] departments = {"Cardiology", "Neurology", "Orthopedics", "Pediatrics", "OPD"};
        for (int i = 1; i <= 8; i++) {
            Doctor d = new Doctor();
            d.setFirstName("Doctor" + i);
            d.setLastName("Test");
            d.setSpecialization(specs[i % specs.length]);
            d.setLicenseNumber("LIC" + String.format("%05d", i));
            d.setPhoneNumber("+9199998888" + String.format("%02d", i));
            d.setEmail("doctor" + i + "@hospital.com");
            d.setYearsOfExperience(5 + i);
            d.setQualification("MBBS, MD");
            d.setDepartment(departments[i % departments.length]);
            d.setConsultationHours("Mon-Fri: 9AM-5PM");
            d.setConsultationFee(1000.0 + i * 100);
            d.setAvailable(true);
            doctors.add(d);
        }
        doctors = doctorRepository.saveAll(doctors);

        List<Insurance> insurances = new ArrayList<>();
        String[] providers = {"Star Health", "Max Bupa", "ICICI Lombard", "HDFC Ergo", "Religare"};
        for (int i = 0; i < 5; i++) {
            Patient p = patients.get(i);
            Insurance ins = new Insurance();
            ins.setPatient(p);
            ins.setProvider(providers[i]);
            ins.setPolicyNumber("POL" + String.format("%06d", i + 1));
            ins.setStartDate(LocalDate.now().minusMonths(6));
            ins.setEndDate(LocalDate.now().plusMonths(i - 2));
            ins.setCoverageType("Individual");
            ins.setCoverageAmount(500000.0);
            ins.setPremiumAmount(15000.0);
            ins.setPaymentFrequency("Annually");
            ins.setCoPayAmount(1000.0);
            ins.setDeductibleAmount(5000.0);
            ins.setCoverageDetails("Standard health coverage");
            ins.setExclusions("Cosmetic procedures");
            insurances.add(ins);
        }
        insurances = insuranceRepository.saveAll(insurances);

        List<Appointment> appointments = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Patient p = patients.get(random.nextInt(patients.size()));
            Doctor d = doctors.get(random.nextInt(doctors.size()));
            Appointment a = new Appointment();
            a.setPatient(p);
            a.setDoctor(d);
            LocalDateTime dateTime = LocalDateTime.now().minusDays(10 - i);
            a.setAppointmentDateTime(dateTime);
            a.setReason("Checkup " + i);
            a.setNotes("Sample notes for appointment " + i);
            AppointmentStatus status;
            switch (i % 5) {
                case 0 -> status = AppointmentStatus.SCHEDULED;
                case 1 -> status = AppointmentStatus.CONFIRMED;
                case 2 -> status = AppointmentStatus.COMPLETED;
                case 3 -> status = AppointmentStatus.CANCELLED;
                default -> status = AppointmentStatus.IN_PROGRESS;
            }
            a.setStatus(status);
            if (status == AppointmentStatus.COMPLETED) {
                a.setDiagnosis("Diagnosis for appointment " + i);
                a.setPrescription("Prescription for appointment " + i);
            }
            appointments.add(a);
        }
        appointments = appointmentRepository.saveAll(appointments);

        List<Bill> bills = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Patient p = patients.get(random.nextInt(patients.size()));
            // Ensure each seeded bill is linked to a unique appointment to satisfy the one-to-one constraint
            Appointment a = appointments.get(i - 1);
            Bill b = new Bill();
            b.setPatient(p);
            b.setAppointment(a);
            b.setBillNumber("BILL-2025-" + String.format("%03d", i));
            b.setBillDate(LocalDate.now().minusDays(i));
            b.setTotalAmount(3000.0 + i * 500);
            b.setDiscount(i % 3 == 0 ? 300.0 : 0.0);
            b.setTax(250.0);
            b.setDescription("Consultation and tests " + i);
            b.setPaymentMethod(i % 2 == 0 ? "Card" : "Cash");
            b.setPaidAmount(i % 4 == 0 ? b.getTotalAmount() : (i % 2 == 0 ? b.getTotalAmount() / 2 : 0.0));
            b.setDueDate(b.getBillDate().plusDays(30));
            double net = b.getTotalAmount() - b.getDiscount() + b.getTax();
            b.setNetAmount(net);
            double balance = net - b.getPaidAmount();
            b.setBalanceAmount(balance);
            if (balance <= 0) {
                b.setPaymentStatus(PaymentStatus.PAID);
            } else if (b.getPaidAmount() > 0) {
                b.setPaymentStatus(PaymentStatus.PARTIAL);
            } else {
                b.setPaymentStatus(PaymentStatus.PENDING);
            }
            if (b.getDueDate().isBefore(LocalDate.now()) && b.getBalanceAmount() > 0) {
                b.setPaymentStatus(PaymentStatus.OVERDUE);
            }
            if (i % 3 == 0) {
                b.setInsuranceClaim("Insurance claim ref " + i);
            }
            bills.add(b);
        }
        billRepository.saveAll(bills);
    }
}
