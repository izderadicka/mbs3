package eu.zderadicka.mbs3;

import java.util.concurrent.CompletionStage;

import eu.zderadicka.mbs3.data.MetaRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/conversion")
public class ConversionResource {

    @Inject
    ConversionService conversionService;

    @POST
    @Path("metadata")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> metadata(MetaRequest request) {
        return conversionService.extractMetadata(request.file());
    }
}
