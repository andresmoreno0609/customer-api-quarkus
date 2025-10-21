// GetClientByIdService.java
package com.challenge.customers.application.service;

import com.challenge.customers.application.port.GetClientByIdUseCase;
import com.challenge.customers.application.web.dto.ClientResponse;
import com.challenge.customers.application.web.mapper.ClientMapper;
import com.challenge.customers.domain.model.Client;
import com.challenge.customers.domain.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class GetClientByIdService implements GetClientByIdUseCase {

    @Inject ClientRepository repo;

    @Override
    public ClientResponse handle(Long id) {
        Client client = repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return ClientMapper.toResponse(client);
    }
}

