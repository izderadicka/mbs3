package eu.zderadicka.mbs3.rest;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import eu.zderadicka.mbs3.service.FileService;
import jakarta.ws.rs.core.Response;

public class ErrorMapping {

    @ServerExceptionMapper
    public RestResponse<String> mapException(FileService.InsecurePathException e) {
        return RestResponse.status(Response.Status.BAD_REQUEST, "Invalid path used");
    }

}
