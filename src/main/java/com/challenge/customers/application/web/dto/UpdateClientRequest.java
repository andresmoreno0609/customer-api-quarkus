// application/web/dto/UpdateClientRequest.java
package com.challenge.customers.application.web.dto;

import java.time.LocalDate;

public record UpdateClientRequest(
        String name,
        LocalDate birthDate,
        Long genderId,
        Long statusId,
        Long countryId,
        String numCta
) {}

