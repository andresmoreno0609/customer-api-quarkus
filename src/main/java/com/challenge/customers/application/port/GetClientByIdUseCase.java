package com.challenge.customers.application.port;
import com.challenge.customers.application.web.dto.ClientResponse;

public interface GetClientByIdUseCase {
    ClientResponse handle(Long id);
}
