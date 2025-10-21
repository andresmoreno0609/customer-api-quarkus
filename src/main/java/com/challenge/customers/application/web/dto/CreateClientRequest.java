// application/web/dto/CreateClientRequest.java
package com.challenge.customers.application.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateClientRequest(
        @NotBlank String name,
        @NotNull LocalDate birthDate,
        @NotNull Long genderId,
        @NotNull Long statusId,
        @NotNull Long countryId,
        @NotBlank @Size(max=15) String numCta
) {}

