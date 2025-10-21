package com.challenge.customers.application.web;

import com.challenge.customers.application.port.*;
import com.challenge.customers.application.web.dto.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    @Inject CreateClientUseCase createUC;
    @Inject UpdateClientUseCase updateUC;
    @Inject GetClientByIdUseCase getByIdUC;
    @Inject ListClientsUseCase listUC;
    @Inject DeleteClientUseCase deleteUC;
    @Inject ActivateClientUseCase activateUC;
    @Inject InactivateClientUseCase inactivateUC;

    @POST
    public Response create(@Valid CreateClientRequest req) {
        var res = createUC.handle(req);
        return Response.created(URI.create("/clients/" + res.id())).entity(res).build();
    }

    @GET @Path("/{id}")
    public ClientResponse get(@PathParam("id") Long id) {
        return getByIdUC.handle(id);
    }

    @GET
    public List<ClientResponse> list(@QueryParam("page") @DefaultValue("0") int page,
                                     @QueryParam("size") @DefaultValue("20") int size,
                                     @QueryParam("name") String nameLike,
                                     @QueryParam("countryId") Long countryId,
                                     @QueryParam("statusId") Long statusId) {
        return listUC.handle(page, size, nameLike, countryId, statusId);
    }

    @PATCH @Path("/{id}")
    public ClientResponse update(@PathParam("id") Long id, @Valid UpdateClientRequest req) {
        return updateUC.handle(id, req);
    }

    @DELETE @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        deleteUC.handle(id);
        return Response.noContent().build();
    }

    @POST @Path("/{id}/activate")
    public ClientResponse activate(@PathParam("id") Long id) {
        return activateUC.handle(id);
    }

    @POST @Path("/{id}/inactivate")
    public ClientResponse inactivate(@PathParam("id") Long id) {
        return inactivateUC.handle(id);
    }
}
