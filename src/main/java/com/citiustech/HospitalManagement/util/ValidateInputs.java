package com.citiustech.HospitalManagement.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class providing common input validation helpers used across the application.
 *
 * This class offers static methods to validate strings, emails, phone numbers,
 * identifiers, numeric ranges, date formats, lengths, and regex patterns. It also
 * includes guard methods that throw {@link IllegalArgumentException} with clear messages
 * when validations fail.
 *
 * @author Bhushan P
 * @version 1.0
 * @since 2024-11-24
 */
@Slf4j
public final class ValidateInputs {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9 .\\n\\t\\-()]{7,20}$");

    private ValidateInputs() {
        throw new AssertionError("Do not instantiate utility class");
    }

    /**
     * Checks whether the provided string is non-null and contains non-whitespace characters.
     *
     * @param value the string to validate
     * @return true when the string is non-null and non-blank
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Ensures the provided string is non-null and non-blank.
     *
     * @param value the string to validate
     * @param fieldName field name for error context
     * @throws IllegalArgumentException when the value is null or blank
     */
    public static void requireNotBlank(String value, String fieldName) {
        if (!isNotBlank(value)) {
            String message = fieldName + " must not be blank";
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates an email string using a conservative regex pattern.
     *
     * @param email the email to check
     * @return true when the email appears valid
     */
    public static boolean isEmail(String email) {
        return isNotBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Ensures the provided email string is valid.
     *
     * @param email the email to validate
     * @param fieldName field name for error context
     * @throws IllegalArgumentException when the email is invalid
     */
    public static void requireEmail(String email, String fieldName) {
        if (!isEmail(email)) {
            String message = fieldName + " must be a valid email address";
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates a phone number allowing digits, spaces, dashes, parentheses, and optional leading plus.
     *
     * @param phone the phone number to validate
     * @return true when the phone number appears valid
     */
    public static boolean isPhone(String phone) {
        return isNotBlank(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Ensures the provided phone string is valid.
     *
     * @param phone the phone number to validate
     * @param fieldName field name for error context
     * @throws IllegalArgumentException when the phone is invalid
     */
    public static void requirePhone(String phone, String fieldName) {
        if (!isPhone(phone)) {
            String message = fieldName + " must be a valid phone number";
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks that an identifier is non-null and strictly positive.
     *
     * @param id the id to validate
     * @return true when id is non-null and > 0
     */
    public static boolean isPositiveId(Long id) {
        return id != null && id > 0L;
    }

    /**
     * Ensures an identifier is non-null and strictly positive.
     *
     * @param id the id to validate
     * @param fieldName field name for error context
     * @throws IllegalArgumentException when id is null or not > 0
     */
    public static void requirePositiveId(Long id, String fieldName) {
        if (!isPositiveId(id)) {
            String message = fieldName + " must be a positive identifier";
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if the string length is within an inclusive range.
     *
     * @param value the string to check
     * @param min minimum length (inclusive)
     * @param max maximum length (inclusive)
     * @return true when within range; false on null or outside
     */
    public static boolean hasLengthBetween(String value, int min, int max) {
        if (value == null) return false;
        int len = value.length();
        return len >= min && len <= max;
    }

    /**
     * Ensures the string length is within an inclusive range.
     *
     * @param value the string to validate
     * @param min minimum length (inclusive)
     * @param max maximum length (inclusive)
     * @param fieldName field name for error context
     * @throws IllegalArgumentException when string is null or outside range
     */
    public static void requireLengthBetween(String value, int min, int max, String fieldName) {
        if (!hasLengthBetween(value, min, max)) {
            String message = String.format("%s length must be between %d and %d", fieldName, min, max);
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates a string against a regex.
     *
     * @param value the string value
     * @param regex the regex pattern
     * @return true when value matches regex; false on null
     */
    public static boolean matches(String value, String regex) {
        return value != null && Pattern.compile(regex).matcher(value).matches();
    }

    /**
     * Ensures a string matches the given regex pattern.
     *
     * @param value the string value
     * @param regex the regex pattern
     * @param fieldName field name for error context
     * @throws IllegalArgumentException when value doesn't match
     */
    public static void requireMatches(String value, String regex, String fieldName) {
        if (!matches(value, regex)) {
            String message = fieldName + " is invalid format";
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks if a numeric value lies within an inclusive range.
     *
     * @param value number to check
     * @param min minimum inclusive
     * @param max maximum inclusive
     * @return true when value is non-null and within range
     */
    public static boolean isWithinRange(Number value, double min, double max) {
        if (value == null) return false;
        double v = value.doubleValue();
        return v >= min && v <= max;
    }

    /**
     * Ensures that a numeric value is within an inclusive range.
     *
     * @param value number to validate
     * @param min minimum inclusive
     * @param max maximum inclusive
     * @param fieldName field name for error context
     * @throws IllegalArgumentException when value is null or outside range
     */
    public static void requireWithinRange(Number value, double min, double max, String fieldName) {
        if (!isWithinRange(value, min, max)) {
            String message = String.format("%s must be between %s and %s", fieldName, min, max);
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Validates that a date string conforms to a provided pattern.
     *
     * @param dateStr the date string
     * @param pattern the expected pattern (e.g., yyyy-MM-dd)
     * @return true when the string can be parsed to a {@link LocalDate}
     */
    public static boolean isValidDate(String dateStr, String pattern) {
        if (!isNotBlank(dateStr) || !isNotBlank(pattern)) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    /**
     * Parses a date string using the provided pattern.
     *
     * @param dateStr the date string
     * @param pattern the expected pattern (e.g., yyyy-MM-dd)
     * @return parsed {@link LocalDate}
     * @throws IllegalArgumentException when parsing fails
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException ex) {
            String message = "Invalid date format. Expected pattern: " + pattern;
            log.warn(message);
            throw new IllegalArgumentException(message, ex);
        }
    }

    /**
     * Generic guard to assert a condition.
     *
     * @param condition condition that must be true
     * @param message message to use when condition is false
     * @throws IllegalArgumentException when condition is false
     */
    public static void requireTrue(boolean condition, String message) {
        if (!condition) {
            log.warn(Objects.requireNonNullElse(message, "Validation failed"));
            throw new IllegalArgumentException(Objects.requireNonNullElse(message, "Validation failed"));
        }
    }

    // End of ValidateInputs - Provides reusable validation utilities across layers
}
