package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.acme.dto.req.InventoryTransactionReqDTO;
import org.acme.dto.PageResponse;
import org.acme.dto.res.InventoryTransactionResDTO;
import org.acme.service.InventoryTransactionService;

@Path("/inventory-transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryTransactionResource {

    @Inject
    InventoryTransactionService service;

    @GET
    public Response findAll() {
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok(service.findById(id)).build();
    }

    @GET
    @Path("/find-all-page")
    public PageResponse<InventoryTransactionResDTO> findAllPage(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") @Max(100) int size) {
        return service.findAllPage(page, size);
    }

    @POST
    @Path("/inflow")
    public Response createInFlow(InventoryTransactionReqDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.createInFlow(dto)).build();
    }
    @POST
    @Path("/outflow")
    public Response createOutFlow(InventoryTransactionReqDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.createOutFlow(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, InventoryTransactionReqDTO dto) {
        return Response.ok(service.update(id, dto)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return service.delete(id) ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
