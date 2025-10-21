package com.challenge.customers.domain.repository;

import com.challenge.customers.domain.model.Client;
import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Long id);
    Optional<Client> findByNumCta(String numCta);
    boolean existsByNumCta(String numCta);
    List<Client> list(int page, int size, String nameLike, Long countryId, Long statusId);
    boolean deleteById(Long id);
}

