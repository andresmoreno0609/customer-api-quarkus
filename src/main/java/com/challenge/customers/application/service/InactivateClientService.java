// InactivateClientService.java
package com.challenge.customers.application.service;

import com.challenge.customers.application.port.InactivateClientUseCase;
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
public class InactivateClientService implements InactivateClientUseCase {

    @Inject ClientRepository repo;
    @Inject EntityManager em;

    @Override
    @Transactional
    public ClientResponse handle(Long id) {
        Client client = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        CatalogStatus inactive = em.createQuery("select s from CatalogStatus s where upper(s.name)='INACTIVE'", CatalogStatus.class)
                .getResultStream().findFirst()
                .orElseThrow(() -> new NotFoundException("Status INACTIVE no existe"));

        client.setStatus(inactive);
        client.setActivatedDate(null);
        client.setInactivatedDate(Instant.now());
        return ClientMapper.toResponse(repo.save(client));
    }
}

