// application/web/mapper/ClientMapper.java
package com.challenge.customers.application.web.mapper;

import com.challenge.customers.application.web.dto.ClientResponse;
import com.challenge.customers.domain.model.Client;

public final class ClientMapper {
    private ClientMapper(){}
    public static ClientResponse toResponse(Client c) {
        return new ClientResponse(
                c.getId(), c.getName(), c.getBirthDate(),
                c.getGender()!=null ? c.getGender().getName():null,
                c.getStatus()!=null ? c.getStatus().getName():null,
                c.getCountry()!=null? c.getCountry().getName():null,
                c.getNumCta(), c.getActivatedDate(), c.getInactivatedDate()
        );
    }
}

