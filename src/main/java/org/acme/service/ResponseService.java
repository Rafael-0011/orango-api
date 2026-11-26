package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ResponseService {

    public static Response responseStatus(String text, int numErro) {
      return Response.status(numErro)
                .entity(text)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    public static Response responseStatus(String text, int numErro, Object o) {
        return Response.status(numErro)
                .entity(text + o)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

}
