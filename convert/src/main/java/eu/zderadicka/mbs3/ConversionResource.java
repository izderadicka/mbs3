package eu.zderadicka.mbs3;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import eu.zderadicka.mbs3.client.UploadServiceClient;
import eu.zderadicka.mbs3.data.Meta;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/conversion")
public class ConversionResource {

    @Inject
    @RestClient
    UploadServiceClient uploadService;

    @Inject
    ConversionService conversionService;

    @Inject
    @Channel("meta-requests")
    Emitter<Meta.Job> metaEmitter;

    @POST
    @Path("metadata")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> metadata(Meta.Request request) {
        return conversionService.extractMetadata(request.file());
    }

    @POST
    @Path("metadata2")
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking
    public Meta.Confirmation metadata2(Meta.Request request) {
        var jobId = UUID.randomUUID().toString();

        metaEmitter.send(new Meta.Job(jobId, request));
        return new Meta.Confirmation(jobId);


        
    }
}
