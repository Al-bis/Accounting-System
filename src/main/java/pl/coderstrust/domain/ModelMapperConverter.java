package pl.coderstrust.domain;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper
public interface ModelMapperConverter {

    ModelMapperConverter modelMapperConverter = Mappers.getMapper(ModelMapperConverter.class);

    Invoice convert(pl.coderstrust.persistence.Invoice persistenceInvoice);

    pl.coderstrust.persistence.Invoice convertToPersistenceInvoice(Invoice invoice);

    Collection<Invoice> convertAll(
        Collection<pl.coderstrust.persistence.Invoice> persistenceInvoices);

}
