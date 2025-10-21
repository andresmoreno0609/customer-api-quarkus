package com.challenge.customers.application.service;

import com.challenge.customers.application.port.UpdateClientUseCase;
import com.challenge.customers.application.web.dto.ClientResponse;
import com.challenge.customers.application.web.dto.UpdateClientRequest;
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
public class UpdateClientService implements UpdateClientUseCase {

    @Inject ClientRepository repo;
    @Inject EntityManager em;
    @Inject AccountValidationProcessor accountValidation;

    @Override
    @Transactional
    public ClientResponse handle(Long id, UpdateClientRequest req) {
        Client client = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        if (req.name() != null && !req.name().isBlank()) client.setName(req.name());
        if (req.birthDate() != null) client.setBirthDate(req.birthDate());
        if (req.genderId() != null) {
            CatalogGender catalogGender = em.find(CatalogGender.class, req.genderId());
            ensureNotNull(catalogGender, "genderId");
            client.setGender(catalogGender);
        }
        if (req.statusId() != null) {
            CatalogStatus catalogStatus = em.find(CatalogStatus.class, req.statusId());
            ensureNotNull(catalogStatus, "statusId");
            client.setStatus(catalogStatus);
        }

        if (req.countryId() != null) {
            CatalogCountry catalogCountry = em.find(CatalogCountry.class, req.countryId());
            ensureNotNull(catalogCountry, "countryId");
            client.setCountry(catalogCountry);

            String countryKey = catalogCountry.getName() == null ? "DEFAULT" : catalogCountry.getName().toUpperCase();
            accountValidation.validate(countryKey, client.getNumCta());
        }

        if (req.numCta() != null && !req.numCta().isBlank()) {
            repo.findByNumCta(req.numCta())
                    .filter(other -> !other.getId().equals(id))
                    .ifPresent(x -> { throw new WebApplicationException("numCta ya existe", Response.Status.CONFLICT); });

            CatalogCountry country = client.getCountry();
            String countryKey = (country == null || country.getName() == null)
                    ? "DEFAULT"
                    : country.getName().toUpperCase();
            accountValidation.validate(countryKey, req.numCta());

            client.setNumCta(req.numCta());
        }

        if (client.isActive()) {
            if (client.getActivatedDate() == null) client.setActivatedDate(Instant.now());
            client.setInactivatedDate(null);
        } else {
            if (client.getInactivatedDate() == null) client.setInactivatedDate(Instant.now());
            client.setActivatedDate(null);
        }

        return ClientMapper.toResponse(repo.save(client));
    }

    private static void ensureNotNull(Object ref, String field) {
        if (ref == null) throw new NotFoundException("Catálogo no encontrado: " + field);
    }
}
