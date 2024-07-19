package eu.zderadicka.mbs3.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import eu.zderadicka.mbs3.data.ConfirmUpload;
import eu.zderadicka.mbs3.data.FinalFileInfo;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileService {

    @ConfigProperty(name = "files.temporary_dir")
    Path tempDirectory;

    @ConfigProperty(name = "files.final_dir")
    Path finalDirectory;

    public String moveToOurTemporaryFolder(Path sourcePath, String originalFileName) throws IOException {

        if (!Files.exists(tempDirectory)) {
            Files.createDirectories(tempDirectory);
        }
        var name = UUID.randomUUID().toString();
        var ext = getExtension(originalFileName);
        var targetFile = name + ext;
        var targetPath = tempDirectory.resolve(targetFile);
        Files.move(sourcePath, targetPath);
        return targetFile;

    }

    public FinalFileInfo moveToPermanentLocation(ConfirmUpload info) throws IOException {
        var res = moveWithUniqueName(info.tempFile(), info.finalFile());
        if (res.isEmpty()) {
            return new FinalFileInfo(info.finalFile(), false);
        } else {
            return new FinalFileInfo(res.get(), true);
        }
    }

    private Optional<String> moveWithUniqueName(String fromFile, String toFile) throws IOException {

        var fullTarget = getFullFinalPath(toFile);
        var fullTargetDirectory = fullTarget.getParent();
        var fullSource = getTemporaryPath(fromFile);
        var idx = 1;
        Optional<String> changedName = Optional.empty();
        synchronized (this) {
            while (Files.exists(fullTarget)) {
                var ext = getExtension(toFile);
                var newName = toFile.substring(0, toFile.length() - ext.length()) + String.format("(%d)", idx) + ext;
                fullTarget = finalDirectory.resolve(newName);
                changedName = Optional.of(newName);
                idx++;

            }
            if (fullTargetDirectory != null && !Files.exists(fullTargetDirectory)) {
                Files.createDirectories(fullTargetDirectory);
            }
            Files.move(fullSource, fullTarget);
        }

        return changedName;

    }

    public static class InsecurePathException extends IllegalArgumentException {
        InsecurePathException(String msg) {
            super(msg);
        }
    }

    public Path getFullFinalPath(String toFile) {
        var toPath = Path.of(toFile);
        checkPath(toPath);
        var fullPath = finalDirectory.resolve(toFile);
        return fullPath;
    }

    public Path getTemporaryPath(String file) {
        var path = Path.of(file);
        if (path.getParent() != null || file.startsWith(".")) {
            throw new InsecurePathException("Invalid name of temporary file");
        }
        return tempDirectory.resolve(path);
    }

    private void checkPath(Path p) {
        if (p.isAbsolute()) {
            throw new InsecurePathException("Absolute paths not allowed");
        }
        Iterable<Path> segments = () -> p.iterator();
        for (Path segment : segments) {
            if (segment.toString().startsWith(".")) {
                throw new InsecurePathException("Path segments must not start with .");
            }
        }
    }

    private String getExtension(String path) {
        if (path == null) {
            return "";
        }
        var pos = path.lastIndexOf(".");
        if (pos > 0) {
            return path.substring(pos);
        } else {
            return "";
        }
    }

    @ConfigProperty(name = "files.keep.temporary.days", defaultValue = "5")
    int keepTemporaryFilesDays;

    @Scheduled(every = "{files.cleanup.schedule}")
    void cleanup() {
        Log.debug("Cleaning up temporary uploaded files");
        try {
            deleteFilesOlderThan(tempDirectory, keepTemporaryFilesDays);
        } catch (IOException e) {
            Log.error("Error while deleting temporary files", e);
        }

    }

    private void deleteFilesOlderThan(Path dir, int daysOld) throws IOException {
        if (!Files.exists(dir)) {
            return;
        }
        Instant oldDate = Instant.now().minus(daysOld, ChronoUnit.DAYS);
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (attrs.creationTime().toInstant().isBefore(oldDate)) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public boolean deleteTemporaryFile(String file) {
       var path = getTemporaryPath(file);
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            Log.error("Error deleting temporary file", e);
            return false;
        }
    }

}
