package eu.zderadicka.mbs3;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import eu.zderadicka.mbs3.client.UploadService;
import eu.zderadicka.mbs3.data.MetaRequest;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/conversion")
public class ConversionResource {

    @RestClient
    UploadService uploadService;

    @Inject
    ConversionService conversionService;

    @POST
    @Path("metadata")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> metadata(MetaRequest request) {
        return conversionService.extractMetadata(request.file());
    }

    @POST
    @Path("metadata2")
    @Produces(MediaType.TEXT_PLAIN)
    @Blocking
    public String metadata2(MetaRequest request) {
        var resp = uploadService.downloadTemporaryFile(request.file());
        return String.valueOf(resp.getLength());
    }
}
