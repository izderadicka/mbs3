package eu.zderadicka.mbs3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    @ConfigProperty(name = "files.prefix")
    Path dataDirectory;

    @BeforeEach
    public void prepareDir() throws IOException {
        if (!Files.exists(dataDirectory)) {
            Files.createDirectories(dataDirectory);
        }
    }

    @AfterEach
    public void removeDir() throws IOException {
        FileUtils.deleteDirectory(dataDirectory.toFile());
    }

    protected void randomContent(Path file, int length) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            var rng = new Random();
            var minChar = (int) ' ';
            var maxChar = (int) 'Z';
            var rangeInclusive = maxChar - minChar + 1;
            for (int i = 0; i < length; i++) {
                char c = (char) (rng.nextInt(rangeInclusive) + minChar);
                writer.write(c);
            }
        }
    }

}
