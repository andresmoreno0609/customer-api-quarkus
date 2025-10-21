// application/web/dto/ClientResponse.java
package com.challenge.customers.application.web.dto;

import java.time.*;
public record ClientResponse(
        Long id, String name, LocalDate birthDate,
        String gender, String status, String country,
        String numCta, Instant activatedDate, Instant inactivatedDate
) {}

