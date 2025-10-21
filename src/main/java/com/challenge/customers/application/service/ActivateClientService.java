// ActivateClientService.java
package com.challenge.customers.application.service;

import com.challenge.customers.application.port.ActivateClientUseCase;
import com.challenge.customers.application.web.dto.ClientResponse;
import com.challenge.customers.application.web.mapper.ClientMapper;
import com.challenge.customers.domain.model.CatalogStatus;
import com.challenge.customers.domain.model.Client;
import com.challenge.customers.domain.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.Instant;

@ApplicationScoped
public class ActivateClientService implements ActivateClientUseCase {

    @Inject ClientRepository repo;
    @Inject EntityManager em;

    @Override
    @Transactional
    public ClientResponse handle(Long id) {
        Client client = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        var active = em.createQuery("select s from CatalogStatus s where upper(s.name)='ACTIVE'", CatalogStatus.class)
                .getResultStream().findFirst()
                .orElseThrow(() -> new NotFoundException("Status ACTIVE no existe"));
        client.setStatus(active);
        client.setActivatedDate(Instant.now());
        client.setInactivatedDate(null);
        return ClientMapper.toResponse(repo.save(client));
    }
}

