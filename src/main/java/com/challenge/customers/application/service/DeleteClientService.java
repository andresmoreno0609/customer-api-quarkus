// DeleteClientService.java
package com.challenge.customers.application.service;

import com.challenge.customers.application.port.DeleteClientUseCase;
import com.challenge.customers.domain.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class DeleteClientService implements DeleteClientUseCase {

    @Inject ClientRepository repo;

    @Override
    @Transactional
    public void handle(Long id) {
        if (!repo.deleteById(id)) throw new NotFoundException("Cliente no encontrado");
    }
}

