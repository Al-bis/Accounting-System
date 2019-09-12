package pl.coderstrust.service.model;

import java.util.Objects;

public final class Company {

    private final String name;
    private final String taxIdentificationNumber;
    private final String address;

    public Company(String name, String taxIdentificationNumber, String address) {
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public String getAddress() {
        return address;
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
            && Objects.equals(taxIdentificationNumber, company.taxIdentificationNumber)
            && Objects.equals(address, company.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, taxIdentificationNumber, address);
    }

    @Override
    public String toString() {
        return "Company{"
            + "name='" + name + '\''
            + ", taxIdentificationNumber='" + taxIdentificationNumber + '\''
            + ", address='" + address + '\''
            + '}';
    }

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public static class CompanyBuilder {

        private String name;
        private String taxIdentificationNumber;
        private String address;

        public CompanyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompanyBuilder taxIdentificationNumber(String taxIdentificationNumber) {
            this.taxIdentificationNumber = taxIdentificationNumber;
            return this;
        }

        public CompanyBuilder address(String address) {
            this.address = address;
            return this;
        }

        public Company build() {
            if (name == null) {
                throw new IllegalArgumentException("Name cannot be null");
            }
            if (taxIdentificationNumber == null) {
                throw new IllegalArgumentException("Tax ID number cannot be null");
            }
            if (address == null) {
                throw new IllegalArgumentException("Address cannot be null");
            }
            return new Company(name, taxIdentificationNumber, address);
        }
    }
}
