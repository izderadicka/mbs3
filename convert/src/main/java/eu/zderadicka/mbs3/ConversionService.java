package eu.zderadicka.mbs3;

import static eu.zderadicka.mbs3.Utils.guessExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import eu.zderadicka.mbs3.client.UploadServiceClient;
import eu.zderadicka.mbs3.data.Meta;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConversionService {

    @ConfigProperty(name = "files.work-dir")
    private Path workDir;

    @Inject
    private ManagedExecutor executor;

    @Inject
    private Vertx vertx;

    @Inject
    @RestClient
    private UploadServiceClient uploadService;

    public CompletableFuture<String> createTmpFile(InputStream data, Optional<String> maybeExt, String mimeType) {
        var name = UUID.randomUUID().toString();
        Optional<String> ext = maybeExt.or(() -> guessExtension(mimeType));
        final String fileName;
        if (ext.isPresent()) {
            fileName = name + "." + ext.get();
        } else {
            fileName = name;
        }

        CompletableFuture<String> future = executor.newIncompleteFuture();
        executor.execute(() -> {
            try {
                Path tmpFile = workDir.resolve(fileName);
                Files.copy(data, tmpFile, StandardCopyOption.REPLACE_EXISTING);
                future.complete(fileName);
            } catch (Exception e) {
                Log.error("Error", e);
                future.completeExceptionally(e);
            }
        });

        return future;

    }


    private static record Resolvedresponse(InputStream body, String contenType) {
    }

    @Incoming("meta-requests")
    @Outgoing("meta-responses")
    public Uni<String> extractMetadata(Meta.Job job) {
        var file = job.request().file();
        Log.info("Got metadata request: " + file);
        var ext = Utils.getFileExtension(file);
        return uploadService.downloadTemporaryFile(file)
        .map(resp -> {
        var bodyStream = resp.readEntity(InputStream.class);
        var contentType = resp.getHeaderString("Content-Type");
        return new Resolvedresponse(bodyStream, contentType);})
        .chain(resp -> {
            var bodyStream = resp.body();
            var contentType = resp.contenType();
            var metaFuture = extractMetadata(bodyStream, ext, contentType);
            return Uni.createFrom().completionStage(metaFuture);
        });


        
        
        
       // save resp to file

        // conversionService.extractMetadata(null)

        //return downloadedFile.onItem().transform(path -> path.toString());
        // return file;
    }

    public CompletableFuture<String> extractMetadata(InputStream dataStream, Optional<String> maybeExt,
            String mimeType) {

        return createTmpFile(dataStream, maybeExt, mimeType)
                .thenCompose(file -> {
                    var future = extractMetadata(file);
                    future.thenRun(() -> {
                        try {
                            Files.delete(workDir.resolve(file));
                        } catch (IOException e) {
                            Log.error("Cannot delete file ", e);
                        }
                    });
                    return future;
                })

        ;

    }

    public CompletableFuture<String> extractMetadata(String file) {
        String[] command = new String[] { "ebook-meta", file };
        return executeCommandAsync(command);
    }

    private CompletableFuture<String> executeCommandAsync(String[] command) {
        var future = new CompletableFuture<String>();
        executor.execute(() -> {
            try {
                var result = runProcess(command);
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    private static void readStream(InputStream output, StringBuilder result) throws IOException {
        var reader = new BufferedReader(new java.io.InputStreamReader(output));
        var buffer = new char[4096];
        int read;
        while ((read = reader.read(buffer)) > 0) {
            result.append(buffer, 0, read);
        }

    }

    private static class ProcessError extends Exception {
        private int exitCode;
        private String errorOutput;

        public ProcessError(String message, int exitCode, String errorOutput) {
            super(message);
            this.exitCode = exitCode;
            this.errorOutput = errorOutput;
        }

        public String errorOutput() {
            return errorOutput;
        }
    }

    private String runProcess(String[] command) throws IOException, ProcessError {

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        // processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        processBuilder.directory(workDir.toFile());
        // HACK: this is used only in development
        processBuilder.environment().put("CONVERTED_DATA_DIR", workDir.getFileName().toString());
        var process = processBuilder.start();
        var output = process.getInputStream();
        var errorOutput = process.getErrorStream();
        var result = new StringBuilder();
        // read output

        var errorResult = new StringBuilder();
        var errorThread = new Thread(() -> {
            try {
                readStream(errorOutput, errorResult);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        readStream(output, result);
        errorThread.start();
        try {
            int exitCode = process.waitFor();
            errorThread.join();

            if (exitCode != 0) {
                var errorLog = errorResult.toString();
                Log.error("Process failed with exit code " + exitCode + "\n" + errorLog);
                throw new ProcessError("Process failed with exit code " + exitCode, exitCode, errorLog);
            }
        } catch (InterruptedException e) {
            process.destroy();
            throw new IOException("Interrupted while waing for process", e);
        }

        return result.toString();

    }

}
