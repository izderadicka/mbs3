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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import eu.zderadicka.mbs3.client.UploadServiceClient;
import eu.zderadicka.mbs3.data.Meta;
import io.quarkus.logging.Log;
import io.quarkus.virtual.threads.VirtualThreads;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConversionService {

    @ConfigProperty(name = "files.work-dir")
    private Path workDir;

    @Inject
    @VirtualThreads
    private ExecutorService executor;

    @Inject
    @RestClient
    private UploadServiceClient uploadService;

    public String createTmpFile(InputStream data, Optional<String> maybeExt, String mimeType) throws IOException {
        var name = UUID.randomUUID().toString();
        Optional<String> ext = maybeExt.or(() -> guessExtension(mimeType));
        final String fileName;
        if (ext.isPresent()) {
            fileName = name + "." + ext.get();
        } else {
            fileName = name;
        }
        var tmpFile = workDir.resolve(fileName);

        Files.copy(data, tmpFile, StandardCopyOption.REPLACE_EXISTING);
        return fileName;

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
                    // var asyncStream = new OutputTo
                    var contentType = resp.getHeaderString("Content-Type");
                    return new Resolvedresponse(bodyStream, contentType);
                })
                .chain(resp -> {
                    var bodyStream = resp.body();
                    var contentType = resp.contenType();
                    var metaFuture = extractMetadata(bodyStream, ext, contentType);
                    return metaFuture;
                });
    }

    public Uni<String> extractMetadata(InputStream dataStream, Optional<String> maybeExt,
            String mimeType) {

        new CompletableFuture<String>();
        var future = executor.submit(() -> createTmpFile(dataStream, maybeExt, mimeType));

        return Uni.createFrom().future(future)
                .chain(file -> {
                    return runEbookMeta(file)
                            .invoke(() -> {
                                try {
                                    Files.delete(workDir.resolve(file));
                                } catch (IOException e) {
                                    Log.error("Cannot delete file ", e);
                                }
                            });
                });

    }

    public Uni<String> runEbookMeta(String file) {
        String[] command = new String[] { "ebook-meta", file };
        var future = executeCommandAsync(command);
        return Uni.createFrom().future(future);
    }

    private Future<String> executeCommandAsync(String[] command) {
        return executor.submit(() -> runProcess(command));
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

        public int exitCode() {
            return exitCode;
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
        var future = executor.submit(() -> {
            try {
                readStream(errorOutput, errorResult);
            } catch (IOException e) {
                Log.error("Cannot read error stream", e);
            }
        });
        readStream(output, result);
        try {
            int exitCode = process.waitFor();
            future.get();

            if (exitCode != 0) {
                var errorLog = errorResult.toString();
                Log.error("Process failed with exit code " + exitCode + "\n" + errorLog);
                throw new ProcessError("Process failed with exit code " + exitCode, exitCode, errorLog);
            }
        } catch (InterruptedException e) {
            process.destroy();
            Log.error("Process interrupted", e);
        } catch (ExecutionException e) {
            Log.error("Error in reader of stderr", e);
        }

        return result.toString();

    }

}
