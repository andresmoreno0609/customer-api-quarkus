package com.challenge.customers.domain.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DefaultAccountValidationTest {
    @Test
    void default_does_not_validate_prefix() {
        var s = new DefaultAccountValidation();
        assertDoesNotThrow(() -> s.validate("CUALQUIERA"));
        assertDoesNotThrow(() -> s.validate(null)); // si prefieres que null sea válido aquí
    }
}
