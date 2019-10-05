package pl.coderstrust.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class ModelsMapper {

    static Invoice mapInvoice(pl.coderstrust.domain.Invoice domainInvoice) {
        Company buyer = mapBuyer(domainInvoice);
        Company seller = mapSeller(domainInvoice);
        List<InvoiceEntry> listOfEntries = mapEntries(domainInvoice);

        return new Invoice.InvoiceBuilder()
            .id(domainInvoice.getId())
            .date(domainInvoice.getDate())
            .buyer(buyer)
            .seller(seller)
            .entries(listOfEntries)
            .build();
    }

    static Company mapBuyer(pl.coderstrust.domain.Invoice domainInvoice) {
        return new Company.CompanyBuilder()
            .name(domainInvoice.getBuyer().getName())
            .address(domainInvoice.getBuyer().getAddress())
            .taxIdentificationNumber(domainInvoice.getBuyer().getTaxIdentificationNumber())
            .build();
    }

    static Company mapSeller(pl.coderstrust.domain.Invoice domainInvoice) {
        return new Company.CompanyBuilder()
            .name(domainInvoice.getSeller().getName())
            .address(domainInvoice.getSeller().getAddress())
            .taxIdentificationNumber(domainInvoice.getSeller().getTaxIdentificationNumber())
            .build();
    }

    static List<InvoiceEntry> mapEntries(pl.coderstrust.domain.Invoice domainInvoice) {
        List<InvoiceEntry> listOfEntries = new ArrayList<>();
        for (int i = 0; i < domainInvoice.getEntries().size(); i++) {
            Vat vat;
            switch (domainInvoice.getEntries().get(i).getVat()) {
                case VAT_0:
                    vat = Vat.VAT_0;
                    break;
                case VAT_5:
                    vat = Vat.VAT_5;
                    break;
                case VAT_8:
                    vat = Vat.VAT_8;
                    break;
                case VAT_23:
                    vat = Vat.VAT_23;
                    break;
                case VAT_ZW:
                    vat = Vat.VAT_ZW;
                    break;
                default:
                    vat = null;
            }
            InvoiceEntry entry = new InvoiceEntry.InvoiceEntryBuilder()
                .id(domainInvoice.getEntries().get(i).getId())
                .title(domainInvoice.getEntries().get(i).getTitle())
                .value(domainInvoice.getEntries().get(i).getValue())
                .vat(vat)
                .amount(domainInvoice.getEntries().get(i).getAmount())
                .build();
            listOfEntries.add(entry);
        }
        return listOfEntries;
    }

    static Collection<Invoice> mapInvoices(
        Collection<pl.coderstrust.domain.Invoice> domainInvoices) {
        return domainInvoices.stream()
            .map(ModelsMapper::mapInvoice)
            .collect(Collectors.toList());
    }

    static pl.coderstrust.domain.Invoice mapInvoiceToDomain(Invoice invoice) {
        pl.coderstrust.domain.Company buyerFromDomain = mapBuyerToDomain(invoice);
        pl.coderstrust.domain.Company sellerFromDomain = mapSellerToDomain(invoice);
        List<pl.coderstrust.domain.InvoiceEntry> listOfEntriesFromDomain = mapEntriesToDomain(
            invoice);

        return new pl.coderstrust.domain.Invoice
            .InvoiceBuilder().id(invoice.getId()).date(invoice.getDate()).buyer(buyerFromDomain)
            .seller(sellerFromDomain).entries(listOfEntriesFromDomain).build();
    }

    static pl.coderstrust.domain.Company mapBuyerToDomain(Invoice invoice) {
        return new pl.coderstrust.domain.Company
            .CompanyBuilder().name(invoice.getBuyer().getName())
            .address(invoice.getBuyer().getAddress())
            .taxIdentificationNumber(invoice.getBuyer().getTaxIdentificationNumber()).build();
    }

    static pl.coderstrust.domain.Company mapSellerToDomain(Invoice invoice) {
        return new pl.coderstrust.domain.Company
            .CompanyBuilder().name(invoice.getSeller().getName())
            .address(invoice.getSeller().getAddress())
            .taxIdentificationNumber(invoice.getSeller().getTaxIdentificationNumber()).build();
    }

    static List<pl.coderstrust.domain.InvoiceEntry> mapEntriesToDomain(Invoice invoice) {
        List<pl.coderstrust.domain.InvoiceEntry> listOfEntriesFromDomain = new ArrayList<>();
        for (int i = 0; i < invoice.getEntries().size(); i++) {
            pl.coderstrust.domain.Vat vatFromDomain;
            switch (invoice.getEntries().get(i).getVat()) {
                case VAT_0:
                    vatFromDomain = pl.coderstrust.domain.Vat.VAT_0;
                    break;
                case VAT_5:
                    vatFromDomain = pl.coderstrust.domain.Vat.VAT_5;
                    break;
                case VAT_8:
                    vatFromDomain = pl.coderstrust.domain.Vat.VAT_8;
                    break;
                case VAT_23:
                    vatFromDomain = pl.coderstrust.domain.Vat.VAT_23;
                    break;
                case VAT_ZW:
                    vatFromDomain = pl.coderstrust.domain.Vat.VAT_ZW;
                    break;
                default:
                    vatFromDomain = null;
            }
            pl.coderstrust.domain.InvoiceEntry entryFromDomain = new pl.coderstrust.domain
                .InvoiceEntry.InvoiceEntryBuilder()
                .id(invoice.getEntries().get(i).getId())
                .title(invoice.getEntries().get(i).getTitle())
                .amount(invoice.getEntries().get(i).getAmount())
                .value(invoice.getEntries().get(i).getValue())
                .vat(vatFromDomain).build();
            listOfEntriesFromDomain.add(entryFromDomain);
        }
        return listOfEntriesFromDomain;
    }

}
