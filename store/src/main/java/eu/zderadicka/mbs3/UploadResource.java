package eu.zderadicka.mbs3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import eu.zderadicka.mbs3.data.FileInfo;
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
import jakarta.ws.rs.core.Response.Status;

@Path("/api/v1/files")
public class UploadResource {

    @Inject
    @Location("upload.html")
    Template uploadTemplate;

    @ConfigProperty(name="files.temporary_dir")
    String tempDirectory;

    @GET
    @Path("upload")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance uploadForm() {
        return uploadTemplate.instance();
    }

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public FileInfo uploadFileFromForm(@RestForm FileUpload file) {
        var size = file.size();
        var originalFileName = file.fileName();
        var contentType = file.contentType();
        String tmpFile;
        try {
            tmpFile = moveToOurTemporaryFolder(file.uploadedFile(), originalFileName);
        } 
            catch (IOException e){
                Log.error("Error moving uploaded file", e);
                throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
            }
        
        return new FileInfo(size,originalFileName,contentType,tmpFile);
    }

    @POST
    @Path("upload2")
    @Produces(MediaType.APPLICATION_JSON)
    public FileInfo uploadFile(File file, @HeaderParam("Content-Type") String contentType, @QueryParam("file-name") String originalFileName) {
        var filePath = file.toPath();
        long size;
        String tmpFile;
        try {
            size = Files.size(filePath);
            tmpFile = moveToOurTemporaryFolder(filePath, originalFileName);
        } 
            catch (IOException e){
                Log.error("Error moving uploaded file", e);
                throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
            }
        
        return new FileInfo(size,originalFileName,contentType,tmpFile);
    }

    private String moveToOurTemporaryFolder(java.nio.file.Path sourcePath, String originalFileName) throws IOException {

            var targetDirectoryPath = Paths.get(tempDirectory);
            if (!Files.exists(targetDirectoryPath)) {
                Files.createDirectories(targetDirectoryPath);
            }
            var name = UUID.randomUUID().toString();
            var ext = getExtension(originalFileName);
            var targetFile = name+ext;
            var targetPath = targetDirectoryPath.resolve(targetFile);
            Files.move(sourcePath, targetPath);
            return targetFile; 

    }

    private String getExtension(String path) {
        if (path == null) {
            return "";
        } 
        var pos = path.lastIndexOf(".");
        if (pos >0) {
            return path.substring(pos);
        } else {
            return "";
        }
    }
}
