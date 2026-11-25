package org.acme.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import org.acme.dto.req.ReportioReqDTO;
import org.acme.service.ReportService;

@Path("/")
public class ReportResource {

    @Inject
    ReportService userService;


    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
   // @Produces("application/vnd.oasis.opendocument.text")
    public Response gerarRelatorioUsuarios(ReportioReqDTO request) throws JRException {
        var fill = userService.fill(request.params, request.dataSource, request.reportName);

        var pdfBytes = userService.createPdf(fill);
        return Response.ok(pdfBytes)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=usuarios.pdf")
                .header(HttpHeaders.CACHE_CONTROL, "must-revalidate, post-check=0, pre-check=0")

                //.header("Content-Disposition", "attachment; filename=\"relatorio.odt\"")
                .build();

    }

}
