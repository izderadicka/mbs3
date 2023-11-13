package eu.zderadicka.mbs3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import eu.zderadicka.mbs3.data.ConfirmUpload;
import eu.zderadicka.mbs3.data.FinalFileInfo;
import eu.zderadicka.mbs3.data.TmpFileInfo;
import io.quarkus.logging.Log;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/v1/upload")
public class UploadResource {

    @Inject
    @Location("upload.html")
    Template uploadTemplate;

    @Inject
    FileService fileService;

    @GET
    @Path("form")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance uploadForm() {
        return uploadTemplate.instance();
    }

    @POST
    @Path("form")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public TmpFileInfo uploadFileFromForm(@RestForm FileUpload file) {
        var size = file.size();
        var originalFileName = file.fileName();
        var contentType = file.contentType();
        String tmpFile;
        try {
            tmpFile = fileService.moveToOurTemporaryFolder(file.uploadedFile(), originalFileName);
        } catch (IOException e) {
            Log.error("Error moving uploaded file", e);
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        return new TmpFileInfo(size, originalFileName, contentType, tmpFile);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public TmpFileInfo uploadFile(File file, @HeaderParam("Content-Type") String contentType,
            @QueryParam("file-name") String originalFileName) {
        var filePath = file.toPath();
        long size;
        String tmpFile;
        try {
            size = Files.size(filePath);
            tmpFile = fileService.moveToOurTemporaryFolder(filePath, originalFileName);
        } catch (IOException e) {
            Log.error("Error moving uploaded file", e);
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }

        return new TmpFileInfo(size, originalFileName, contentType, tmpFile);
    }

    @POST
    @Path("confirm")
    @Produces(MediaType.APPLICATION_JSON)
    public FinalFileInfo confirmUpload(ConfirmUpload confirmation) {
        try {
            return fileService.moveToPermanentLocation(confirmation);
        } catch (IOException e) {
            Log.error("Error when confirming upload", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    @GET
    @Path("temporary/{file}")
    public Response getTemporaryFile(String file) {

        var filePath = fileService.getTemporaryPath(file);
        if (!Files.exists(filePath)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(filePath).build();

    }

}
