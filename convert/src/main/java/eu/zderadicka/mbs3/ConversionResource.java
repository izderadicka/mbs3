package eu.zderadicka.mbs3;

import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import eu.zderadicka.mbs3.client.UploadServiceClient;
import eu.zderadicka.mbs3.data.MetaRequest;
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
    public CompletionStage<String> metadata2(MetaRequest request) {
        var resp = uploadService.downloadTemporaryFile(request.file());
        var bodyStream = resp.readEntity(InputStream.class);
        var ext = Utils.getFileExtension(request.file());
        var file = conversionService.extractMetadata(bodyStream, ext, resp.getHeaderString("Content-Type"));
       // save resp to file

        // conversionService.extractMetadata(null)

        //return downloadedFile.onItem().transform(path -> path.toString());
        return file;
    }
}
