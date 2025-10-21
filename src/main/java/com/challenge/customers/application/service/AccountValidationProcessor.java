package com.challenge.customers.application.service;

import com.challenge.customers.domain.strategy.AccountValidationStrategy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.inject.Inject;

@ApplicationScoped
public class AccountValidationProcessor {

    @Inject @Any
    Instance<AccountValidationStrategy> strategies;

    public void validate(String countryKey, String numCta) {
        var key = (countryKey == null || countryKey.isBlank()) ? "DEFAULT" : countryKey;

        var selected = strategies.select(NamedLiteral.of(key));
        if (selected.isUnsatisfied()) {
            // Fallback si no hay bean con ese nombre
            selected = strategies.select(NamedLiteral.of("DEFAULT"));
        }
        // selected.get() lanza si sigue insatisfecho (lo cual no debería si existe DEFAULT)
        selected.get().validate(numCta);
    }
}
