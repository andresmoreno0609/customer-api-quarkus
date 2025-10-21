package com.challenge.customers.application.port;
import com.challenge.customers.application.web.dto.*;

public interface CreateClientUseCase {
    ClientResponse handle(CreateClientRequest req);
}
