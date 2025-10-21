package com.challenge.customers.domain.strategy;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("DEFAULT")
public class DefaultAccountValidation implements AccountValidationStrategy {
    @Override
    public void validate(String numCta) {}
}

