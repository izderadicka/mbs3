package eu.zderadicka.mbs3.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.tika.Tika;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;

public class Utils {

    private static Tika tika = new Tika();

    public static Response filResponse(Path filePath) {

        if (!Files.exists(filePath)) {
            Log.error("Requested file " + filePath + " does not exist");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        var downloadName = filePath.getFileName().toString();
        String contentType = null;
        try {
            // this is unreliable, because depends on particular OS implementation - have problem with ubi8/openjdk17 container for instance
            // contentType = Files.probeContentType(filePath);
            contentType = tika.detect(filePath.toFile());
            Log.debug("Detected content type for " + filePath + " detected as " + contentType);
        } catch (IOException e) {
            Log.error("Could not determine content type for " + filePath, e);
        }

        var resp = Response.ok(filePath)
                .header("Content-Disposition", String.format("attachment;filename=\"%s\"", downloadName));
        if (contentType != null) {
            resp = resp.header("Content-Type", contentType);
        }

        return resp.build();

    }
    
}
