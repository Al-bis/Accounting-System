package pl.coderstrust.persistence;

import pl.coderstrust.domain.InvoiceNotFoundException;

import java.time.LocalDate;

class DatabaseValidator {

    static void validateDatesIfNullOrInIncorrectOrder(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) {
            throw new IllegalArgumentException("Given fromDate cannot be null");
        }
        if (toDate == null) {
            throw new IllegalArgumentException("Given toDate cannot be null");
        }
        if (toDate.compareTo(fromDate) <= -1) {
            throw new IllegalArgumentException("Given fromDate must be earlier than toDate");
        }
    }

    static void validateInvoiceIfNull(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Given invoice cannot be null");
        }
    }

    static void validateInvoiceIdIfNull(Long id, boolean idIsNotExistInDatabase) {
        if (idIsNotExistInDatabase) {
            throw new InvoiceNotFoundException("Invoice for id = {" + id + "} is not exists.");
        }
    }

}
