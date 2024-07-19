package eu.zderadicka.mbs3.client;

import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/upload")
@RegisterRestClient(configKey = "upload-api")
public interface UploadServiceClient {

    @Path("/temporary/{file}")
    @GET
    public Response downloadTemporaryFile(@PathParam("file") String file);

    @DELETE
    @Path("temporary/{file}")
    public Response deleteTemporaryFile(String file);
    
}
