package com.citiustech.HospitalManagement.util;

import java.time.LocalDate;
import java.time.Period;

import lombok.extern.slf4j.Slf4j;

/**
 * Author: CitiDeveloper
 * Version: V1
 *
 * AgeValidationClass — validates a patient's age against the allowed hospital range.
 *
 * The accepted age range is {@link #MIN_AGE} (5) to {@link #MAX_AGE} (100) years,
 * inclusive: a patient's age must not be less than 5 nor greater than 100. The class
 * offers both boolean checks and guard methods that throw {@link IllegalArgumentException}
 * with a clear message when validation fails, and works with either an explicit age in
 * years or a date of birth.
 */
@Slf4j
public final class AgeValidationClass {

    /** Minimum allowed patient age in years (inclusive). */
    public static final int MIN_AGE = 5;

    /** Maximum allowed patient age in years (inclusive). */
    public static final int MAX_AGE = 100;

    /**
     * Prevents instantiation of this utility class.
     */
    private AgeValidationClass() {
        throw new AssertionError("Do not instantiate utility class");
    }

    /**
     * Checks whether the given age (in years) is within the allowed range.
     *
     * @param age the age in years to check
     * @return true when age is non-null and between {@link #MIN_AGE} and {@link #MAX_AGE} inclusive
     */
    public static boolean isValidAge(Integer age) {
        return age != null && age >= MIN_AGE && age <= MAX_AGE;
    }

    /**
     * Ensures the given age (in years) is within the allowed range.
     *
     * @param age the age in years to validate
     * @throws IllegalArgumentException when age is null or outside the allowed range
     */
    public static void validateAge(Integer age) {
        if (!isValidAge(age)) {
            String message = String.format("Age must be between %d and %d years", MIN_AGE, MAX_AGE);
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Calculates a patient's age in years from their date of birth.
     *
     * @param dateOfBirth the patient's date of birth
     * @return the age in completed years
     * @throws IllegalArgumentException when dateOfBirth is null or in the future
     */
    public static int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            String message = "Date of birth must be a valid date in the past";
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Checks whether the age derived from the given date of birth is within the allowed range.
     *
     * @param dateOfBirth the patient's date of birth
     * @return true when the resulting age is between {@link #MIN_AGE} and {@link #MAX_AGE} inclusive
     */
    public static boolean isValidAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            return false;
        }
        return isValidAge(Period.between(dateOfBirth, LocalDate.now()).getYears());
    }

    /**
     * Ensures the age derived from the given date of birth is within the allowed range.
     *
     * @param dateOfBirth the patient's date of birth
     * @throws IllegalArgumentException when dateOfBirth is null, in the future,
     *         or yields an age outside the allowed range
     */
    public static void validateAge(LocalDate dateOfBirth) {
        validateAge(calculateAge(dateOfBirth));
    }
}

/*
 * ---- Summary ----
 * AgeValidationClass: validates a patient's age, enforcing an inclusive range of
 * MIN_AGE (5) to MAX_AGE (100) years — age must not be less than 5 nor greater than 100.
 * Methods:
 *   isValidAge(Integer)      - boolean check on an explicit age in years
 *   validateAge(Integer)     - guard that throws when an explicit age is out of range
 *   calculateAge(LocalDate)  - derives age in completed years from a date of birth
 *   isValidAge(LocalDate)    - boolean check on the age derived from a date of birth
 *   validateAge(LocalDate)   - guard that throws when the date-of-birth age is out of range
 */
