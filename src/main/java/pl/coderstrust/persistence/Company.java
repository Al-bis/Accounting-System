package pl.coderstrust.persistence;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public final class Company {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String taxIdentificationNumber;
    private String address;

    private Company() {
    }

    public Company(Long id, String name, String taxIdentificationNumber, String address) {
        this.id = id;
        this.name = name;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }

    public Long getId() {
        return id;
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
        return Objects.equals(id, company.id)
            && Objects.equals(name, company.name)
            && Objects.equals(taxIdentificationNumber, company.taxIdentificationNumber)
            && Objects.equals(address, company.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, taxIdentificationNumber, address);
    }

    @Override
    public String toString() {
        return "Company{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", taxIdentificationNumber='" + taxIdentificationNumber + '\''
            + ", address='" + address + '\''
            + '}';
    }

    public static CompanyBuilder builder() {
        return new CompanyBuilder();
    }

    public static class CompanyBuilder {

        private Long id;
        private String name;
        private String taxIdentificationNumber;
        private String address;

        public CompanyBuilder id(Long id) {
            this.id = id;
            return this;
        }

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
            return new Company(id, name, taxIdentificationNumber, address);
        }
    }
}
