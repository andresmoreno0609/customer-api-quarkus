package com.challenge.customers.application.port;
import com.challenge.customers.application.web.dto.*;

public interface UpdateClientUseCase {
    ClientResponse handle(Long id, UpdateClientRequest req);
}
