package com.challenge.customers.application.service;

import com.challenge.customers.application.port.CreateClientUseCase;
import com.challenge.customers.application.web.dto.ClientResponse;
import com.challenge.customers.application.web.dto.CreateClientRequest;
import com.challenge.customers.application.web.mapper.ClientMapper;
import com.challenge.customers.domain.model.CatalogCountry;
import com.challenge.customers.domain.model.CatalogGender;
import com.challenge.customers.domain.model.CatalogStatus;
import com.challenge.customers.domain.model.Client;
import com.challenge.customers.domain.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.time.Instant;

@ApplicationScoped
public class CreateClientService implements CreateClientUseCase {

    @Inject ClientRepository repo;
    @Inject EntityManager em;
    @Inject AccountValidationProcessor accountValidation;

    @Override
    @Transactional
    public ClientResponse handle(CreateClientRequest req) {

        // aqui esto validano de que la cuenta no exista
        if (repo.existsByNumCta(req.numCta())) {
            throw new WebApplicationException("numCta ya existe", Response.Status.CONFLICT);
        }

        // cargar catálogos
        CatalogGender gender = em.find(CatalogGender.class, req.genderId());
        CatalogStatus status = em.find(CatalogStatus.class, req.statusId());
        CatalogCountry country = em.find(CatalogCountry.class, req.countryId());
        ensureNotNull(gender, "genderId");
        ensureNotNull(status, "statusId");
        ensureNotNull(country, "countryId");

        // Validación por strategy
        String countryKey = country.getName() == null ? "DEFAULT" : country.getName().toUpperCase();
        accountValidation.validate(countryKey, req.numCta());

        Client client = Client.builder()
                .name(req.name())
                .birthDate(req.birthDate())
                .gender(gender)
                .status(status)
                .country(country)
                .numCta(req.numCta())
                .build();

        // fechas según estado
        if ("ACTIVE".equalsIgnoreCase(status.getName())) {
            client.setActivatedDate(Instant.now());
            client.setInactivatedDate(null);
        } else {
            client.setActivatedDate(null);
            client.setInactivatedDate(Instant.now());
        }

        return ClientMapper.toResponse(repo.save(client));
    }

    private static void ensureNotNull(Object ref, String field) {
        if (ref == null) throw new NotFoundException("Catálogo no encontrado: " + field);
    }
}
