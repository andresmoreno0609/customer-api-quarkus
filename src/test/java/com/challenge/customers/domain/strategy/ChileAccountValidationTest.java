package com.challenge.customers.domain.strategy;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ChileAccountValidationTest {

    @Test
    void should_accept_accounts_starting_with_003() {
        var s = new ChileAccountValidation();
        assertDoesNotThrow(() -> s.validate("003123456"));
    }

    @Test
    void should_reject_accounts_without_003_prefix() {
        var s = new ChileAccountValidation();
        var ex = assertThrows(WebApplicationException.class, () -> s.validate("111999"));
        assertEquals(400, ex.getResponse().getStatus());
        assertTrue(ex.getMessage().contains("003"));
    }

    @Test
    void should_reject_null_account() {
        var s = new ChileAccountValidation();
        var ex = assertThrows(WebApplicationException.class, () -> s.validate(null));
        assertEquals(400, ex.getResponse().getStatus());
    }
}

