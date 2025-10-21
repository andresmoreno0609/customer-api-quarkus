package com.challenge.customers.application.service;

import com.challenge.customers.application.web.dto.CreateClientRequest;
import com.challenge.customers.domain.model.CatalogCountry;
import com.challenge.customers.domain.model.CatalogGender;
import com.challenge.customers.domain.model.CatalogStatus;
import com.challenge.customers.domain.model.Client;
import com.challenge.customers.domain.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class CreateClientServiceTest {

    @Mock ClientRepository repo;
    @Mock EntityManager em;
    @Mock AccountValidationProcessor validator;

    @InjectMocks CreateClientService service;

    @Test
    void create_non_chile_success() {
        var gender = new CatalogGender(); gender.setId(1L); gender.setName("MALE");
        var status = new CatalogStatus(); status.setId(1L); status.setName("ACTIVE");
        var country = new CatalogCountry(); country.setId(1L); country.setName("COLOMBIA");

        when(em.find(CatalogGender.class, 1L)).thenReturn(gender);
        when(em.find(CatalogStatus.class, 1L)).thenReturn(status);
        when(em.find(CatalogCountry.class, 1L)).thenReturn(country);
        when(repo.existsByNumCta("CT999888777")).thenReturn(false);
        when(repo.save(any(Client.class))).thenAnswer(inv -> {
            Client c = inv.getArgument(0);
            c.setId(100L);
            return c;
        });

        var req = new CreateClientRequest("Maria Gomez",
                LocalDate.parse("1995-03-22"),
                1L, 1L, 1L,
                "CT999888777");

        var res = service.handle(req);

        verify(validator).validate("COLOMBIA", "CT999888777");
        assertThat(res.id(), is(100L));
        assertThat(res.name(), is("Maria Gomez"));
        assertThat(res.status(), is("ACTIVE"));
        verifyNoMoreInteractions(validator, repo, em);
    }

    @Test
    void create_chile_invalid_prefix_should_throw_400() {
        var gender = new CatalogGender(); gender.setId(1L); gender.setName("MALE");
        var status = new CatalogStatus(); status.setId(1L); status.setName("ACTIVE");
        var chile  = new CatalogCountry(); chile.setId(4L); chile.setName("CHILE");

        when(em.find(CatalogGender.class, 1L)).thenReturn(gender);
        when(em.find(CatalogStatus.class, 1L)).thenReturn(status);
        when(em.find(CatalogCountry.class, 4L)).thenReturn(chile);
        when(repo.existsByNumCta("ABC")).thenReturn(false);

        doThrow(new WebApplicationException("Para Chile...", 400))
                .when(validator).validate("CHILE", "ABC");

        var req = new CreateClientRequest("Pedro",
                LocalDate.parse("1990-01-01"),
                1L, 1L, 4L,
                "ABC");

        var ex = assertThrows(WebApplicationException.class, () -> service.handle(req));
        assertThat(ex.getResponse().getStatus(), is(400));
        verify(repo, never()).save(any());
    }

    @Test
    void duplicate_account_should_throw_409() {
        var gender = new CatalogGender(); gender.setId(1L); gender.setName("MALE");
        var status = new CatalogStatus(); status.setId(1L); status.setName("ACTIVE");
        var country = new CatalogCountry(); country.setId(1L); country.setName("COLOMBIA");

        when(em.find(CatalogGender.class, 1L)).thenReturn(gender);
        when(em.find(CatalogStatus.class, 1L)).thenReturn(status);
        when(em.find(CatalogCountry.class, 1L)).thenReturn(country);
        when(repo.existsByNumCta("DUP")).thenReturn(true);

        var req = new CreateClientRequest("Ana",
                LocalDate.parse("1992-02-02"),
                1L, 1L, 1L,
                "DUP");

        var ex = assertThrows(WebApplicationException.class, () -> service.handle(req));
        assertThat(ex.getResponse().getStatus(), is(409));
        verify(validator, never()).validate(anyString(), anyString());
        verify(repo, never()).save(any());
    }
}

