// infra/persistence/ClientPanacheRepository.java
package com.challenge.customers.infra.persistence;

import com.challenge.customers.domain.model.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClientPanacheRepository implements PanacheRepository<Client> {
    public Optional<Client> findByNumCta(String numCta) {
        return find("lower(numCta) = ?1", numCta.toLowerCase()).firstResultOptional();
    }
    public boolean existsByNumCta(String numCta) {
        return count("lower(numCta) = ?1", numCta.toLowerCase()) > 0;
    }
    public List<Client> search(int page, int size, String nameLike, Long countryId, Long statusId) {
        var where = new StringBuilder("1=1");
        var params = new java.util.ArrayList<>();
        if (nameLike != null && !nameLike.isBlank()) { where.append(" and lower(name) like ?").append(params.size()+1); params.add("%"+nameLike.toLowerCase()+"%"); }
        if (countryId != null) { where.append(" and country.id = ?").append(params.size()+1); params.add(countryId); }
        if (statusId  != null) { where.append(" and status.id  = ?").append(params.size()+1); params.add(statusId); }
        return find(where.toString(), params.toArray()).page(page, size).list();
    }
}

