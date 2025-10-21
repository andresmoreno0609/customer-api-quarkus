// ListClientsService.java
package com.challenge.customers.application.service;

import com.challenge.customers.application.port.ListClientsUseCase;
import com.challenge.customers.application.web.dto.ClientResponse;
import com.challenge.customers.application.web.mapper.ClientMapper;
import com.challenge.customers.domain.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ListClientsService implements ListClientsUseCase {

    @Inject ClientRepository repo;

    @Override
    public List<ClientResponse> handle(int page, int size, String nameLike, Long countryId, Long statusId) {
        return repo.list(page, size, nameLike, countryId, statusId)
                .stream().map(ClientMapper::toResponse).toList();
    }
}

