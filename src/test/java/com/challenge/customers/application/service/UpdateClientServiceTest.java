package com.challenge.customers.application.service;

import com.challenge.customers.application.web.dto.UpdateClientRequest;
import com.challenge.customers.domain.model.CatalogCountry;
import com.challenge.customers.domain.model.CatalogGender;
import com.challenge.customers.domain.model.CatalogStatus;
import com.challenge.customers.domain.model.Client;
import com.challenge.customers.domain.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class UpdateClientServiceTest {

    @Mock ClientRepository repo;
    @Mock EntityManager em;
    @Mock AccountValidationProcessor validator;

    @InjectMocks UpdateClientService service;

    @Test
    void update_numCta_should_validate_with_current_country() {
        var existing = Client.builder()
                .id(10L).name("Maria").birthDate(LocalDate.parse("1995-03-22"))
                .numCta("CT999").build();

        var co = new CatalogCountry(); co.setId(1L); co.setName("COLOMBIA");
        var active = new CatalogStatus(); active.setId(1L); active.setName("ACTIVE");
        var male = new CatalogGender(); male.setId(1L); male.setName("MALE");

        existing.setCountry(co); existing.setStatus(active); existing.setGender(male);

        when(repo.findById(10L)).thenReturn(Optional.of(existing));
        when(repo.findByNumCta("NEWNUM")).thenReturn(Optional.empty());
        when(repo.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

        var req = new UpdateClientRequest(null, null, null, null, null, "NEWNUM");
        var res = service.handle(10L, req);

        verify(validator).validate("COLOMBIA", "NEWNUM");
        assertThat(res.numCta(), is("NEWNUM"));
    }

    @Test
    void switch_to_chile_and_invalid_numCta_should_throw_400() {
        var existing = Client.builder()
                .id(10L).name("Maria").birthDate(LocalDate.parse("1995-03-22"))
                .numCta("CT999").build();

        var co = new CatalogCountry(); co.setId(1L); co.setName("COLOMBIA");
        var cl = new CatalogCountry(); cl.setId(4L); cl.setName("CHILE");
        var active = new CatalogStatus(); active.setId(1L); active.setName("ACTIVE");
        var male = new CatalogGender(); male.setId(1L); male.setName("MALE");

        existing.setCountry(co); existing.setStatus(active); existing.setGender(male);

        when(repo.findById(10L)).thenReturn(Optional.of(existing));
        when(em.find(CatalogCountry.class, 4L)).thenReturn(cl);
        when(repo.findByNumCta("ABC")).thenReturn(Optional.empty());

        doThrow(new WebApplicationException("Para Chile...", 400))
                .when(validator).validate("CHILE", "ABC");

        var req = new UpdateClientRequest(null, null, null, 4L, null, "ABC");
        var ex = assertThrows(WebApplicationException.class, () -> service.handle(10L, req));
        assertThat(ex.getResponse().getStatus(), is(400));

        verify(repo, never()).save(any());
    }

    @Test
    void not_found_client_should_throw_404() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        var req = new UpdateClientRequest(null, null, null, null, null, "ANY");
        assertThrows(NotFoundException.class, () -> service.handle(999L, req));
        verifyNoInteractions(validator);
    }

    @Test
    void duplicate_numCta_should_throw_409() {
        var existing = Client.builder()
                .id(10L).name("Maria").birthDate(LocalDate.parse("1995-03-22"))
                .numCta("CT999").build();
        var co = new CatalogCountry(); co.setId(1L); co.setName("COLOMBIA");
        var active = new CatalogStatus(); active.setId(1L); active.setName("ACTIVE");
        var male = new CatalogGender(); male.setId(1L); male.setName("MALE");
        existing.setCountry(co); existing.setStatus(active); existing.setGender(male);

        var other = Client.builder().id(999L).numCta("DUP").build();

        when(repo.findById(10L)).thenReturn(Optional.of(existing));
        when(repo.findByNumCta("DUP")).thenReturn(Optional.of(other));

        var req = new UpdateClientRequest(null, null, null, null, null, "DUP");
        var ex = assertThrows(WebApplicationException.class, () -> service.handle(10L, req));
        assertThat(ex.getResponse().getStatus(), is(409));
        verify(validator, never()).validate(anyString(), anyString());
        verify(repo, never()).save(any());
    }
}

