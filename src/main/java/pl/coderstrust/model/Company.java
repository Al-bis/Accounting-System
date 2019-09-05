package pl.coderstrust.model;

import java.util.Objects;

public final class Company {

    private final String name;
    private final String taxIdentificationNumber;

    private Company(String name, String taxIdentificationNumber) {
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public String getName() {
        return name;
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Company company = (Company) object;
        return Objects.equals(name, company.name)
            && Objects.equals(taxIdentificationNumber, company.taxIdentificationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, taxIdentificationNumber);
    }

    @Override
    public String toString() {
        return "Company{"
            + "name='" + name + '\''
            + ", taxIdentificationNumber='" + taxIdentificationNumber + '\''
            + '}';
    }

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public static class CompanyBuilder {

        private String name;
        private String taxIdentificationNumber;

        public CompanyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompanyBuilder taxIdentificationNumber(String taxIdentificationNumber) {
            this.taxIdentificationNumber = taxIdentificationNumber;
            return this;
        }

        public Company build() {
            if (name == null) {
                throw new IllegalArgumentException("Name cannot be null");
            }
            if (taxIdentificationNumber == null) {
                throw new IllegalArgumentException("Tax ID number cannot be null");
            }
            return new Company(name, taxIdentificationNumber);
        }
    }
}
