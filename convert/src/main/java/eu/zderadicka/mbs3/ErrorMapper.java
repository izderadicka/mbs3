package eu.zderadicka.mbs3;

import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.ws.rs.core.Response;

public class ErrorMapper {
    @ServerExceptionMapper
    public Response mapNotFoundException(ClientWebApplicationException e) {

        if (e.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
        return Response.status(Response.Status.NOT_FOUND).entity("Not Found").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
    }
    
}
