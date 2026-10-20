package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.acme.dto.InventoryDTO;
import org.acme.dto.PageResponse;
import org.acme.service.InventoryService;

@Path("/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    @Inject
    InventoryService service;

    @GET
    public List<InventoryDTO> findAll() {
        return service.findAll();
    }

    @GET
    @Path("/find-all-page")
    public PageResponse<InventoryDTO> findAllPage(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") @Max(100) int size) {
        return service.findAllPage(page, size);
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        InventoryDTO dto = service.findById(id);
        return dto == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(dto).build();
    }

    @POST
    public Response create(InventoryDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, InventoryDTO dto) {
        InventoryDTO updated = service.update(id, dto);
        return updated == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return service.delete(id) ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
