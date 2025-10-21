// infra/repository/ClientRepositoryImpl.java
package com.challenge.customers.infra.repository;

import com.challenge.customers.domain.model.Client;
import com.challenge.customers.domain.repository.ClientRepository;
import com.challenge.customers.infra.persistence.ClientPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClientRepositoryImpl implements ClientRepository {

    @Inject ClientPanacheRepository panache;

    @Override @Transactional
    public Client save(Client client) {
        if (client.getId() == null) { panache.persist(client); return client; }
        return panache.getEntityManager().merge(client);
    }

    @Override public Optional<Client> findById(Long id) { return Optional.ofNullable(panache.findById(id)); }
    @Override public Optional<Client> findByNumCta(String numCta) { return panache.findByNumCta(numCta); }
    @Override public boolean existsByNumCta(String numCta) { return panache.existsByNumCta(numCta); }
    @Override public List<Client> list(int page,int size,String nameLike,Long countryId,Long statusId){ return panache.search(page,size,nameLike,countryId,statusId); }
    @Override @Transactional public boolean deleteById(Long id) { return panache.deleteById(id); }
}

