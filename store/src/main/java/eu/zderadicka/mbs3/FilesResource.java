package eu.zderadicka.mbs3;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.core.file.FileSystemException;
import io.vertx.core.file.OpenOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.file.AsyncFile;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/files")
public class FilesResource {

    @Inject
    FileService fileService;

    @Inject
    Vertx vertx;

    private record ResolvedFile(AsyncFile file, Long size) {
    }

    @GET
    @Path("/{file:.+}")
    public Response getFile(@PathParam("file") String file) {
        String decodedFile = URLDecoder.decode(file, StandardCharsets.UTF_8);
        var fullName = fileService.getFullFinalPath(decodedFile);
        if (!Files.exists(fullName)) {
            Log.error("Requested file is " + fullName + " does not exist");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var downloadName = fullName.getFileName().toString();
        String contentType = null;
        try {
            contentType = Files.probeContentType(fullName);
        } catch (IOException e) {

        }

        var resp = Response.ok(fullName)
                .header("Content-Disposition", String.format("attachment;filename=\"%s\"", downloadName));
        if (contentType != null) {
            resp = resp.header("Content-Type", contentType);
        }

        return resp.build();

    }

}
