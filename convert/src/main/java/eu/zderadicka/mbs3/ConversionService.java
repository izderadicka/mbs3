package eu.zderadicka.mbs3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConversionService {
    
    @ConfigProperty(name = "files.work-dir")
    private Path workDir;

    @Inject
    private ManagedExecutor executor;

    public CompletableFuture<String> extractMetadata(String file) {
        String[] command = new String[] { "ebook-meta",  file};
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
        }});
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
        processBuilder.environment().put("EBOOKS_DIR", workDir.toAbsolutePath().toString());
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
