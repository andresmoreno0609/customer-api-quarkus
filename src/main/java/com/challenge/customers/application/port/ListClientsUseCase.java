package com.challenge.customers.application.port;
import com.challenge.customers.application.web.dto.ClientResponse;
import java.util.List;

public interface ListClientsUseCase {
    List<ClientResponse> handle(int page, int size, String nameLike, Long countryId, Long statusId);
}
