package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import org.junit.jupiter.api.Test;

import eu.zderadicka.mbs3.data.ConfirmUpload;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class FileServiceTest extends BaseTest {

    @Inject
    FileService fileService;

    @Test
    public void testFileService() throws IOException {
        var tmpFile = uploadFile("test_file.txt", 1024);
        var finalName = "Author/Book/Test.txt";
        var res = fileService.moveToPermanentLocation(new ConfirmUpload(tmpFile, finalName));
        assertEquals(finalName, res.filePath());
        tmpFile = uploadFile("other_test.txt", 512);
        res = fileService.moveToPermanentLocation(new ConfirmUpload(tmpFile, finalName));
        var modifiedPath = res.filePath();
        assertNotEquals(finalName, modifiedPath);
        var idx = finalName.lastIndexOf(".");
        var commonPart = finalName.substring(0, idx);
        var shouldBePath = commonPart + "(1).txt";
        assertEquals(shouldBePath, modifiedPath);
    }

    private String uploadFile(String name, int length) throws IOException {
        var testFile = dataDirectory.resolve(name);
        randomContent(testFile, length);
        var tmpFile = fileService.moveToOurTemporaryFolder(testFile, name);
        return tmpFile;
    }

}
