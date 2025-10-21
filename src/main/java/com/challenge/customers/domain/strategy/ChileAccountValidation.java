package com.challenge.customers.domain.strategy;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Named("CHILE")
public class ChileAccountValidation implements AccountValidationStrategy {
    @Override
    public void validate(String numCta) {
        if (numCta == null || !numCta.startsWith("003")) {
            throw new WebApplicationException(
                    "Para Chile, el número de cuenta debe iniciar con '003'",
                    Response.Status.BAD_REQUEST
            );
        }
    }
}

