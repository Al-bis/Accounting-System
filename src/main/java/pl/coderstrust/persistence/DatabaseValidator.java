package pl.coderstrust.persistence;

import java.time.LocalDate;

class DatabaseValidator {

    static void checkRangeOfDates(LocalDate fromDate, LocalDate toDate) {
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

    static void checkInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Given invoice cannot be null");
        }
    }

}
