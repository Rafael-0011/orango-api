package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.acme.dto.req.InventoryReqDTO;
import org.acme.dto.PageResponse;
import org.acme.dto.res.InventoryResDTO;
import org.acme.service.InventoryService;

@Path("/inventory")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InventoryResource {

    @Inject
    InventoryService service;

    @GET
    public List<InventoryResDTO> findAll() {
        return service.findAll();
    }

    @GET
    @Path("/find-all-page")
    public PageResponse<InventoryResDTO> findAllPage(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") @Max(100) int size) {
        return service.findAllPage(page, size);
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        InventoryResDTO dto = service.findById(id);
        return dto == null ? Response.status(Response.Status.NOT_FOUND).build() : Response.ok(dto).build();
    }

    @POST
    public Response create(InventoryReqDTO dto) {
        return Response.status(Response.Status.CREATED).entity(service.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, InventoryReqDTO dto) {
        return Response.ok(service.update(id, dto)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return service.delete(id) ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
