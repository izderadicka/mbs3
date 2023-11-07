package eu.zderadicka.mbs3;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import eu.zderadicka.mbs3.data.ConfirmUpload;
import eu.zderadicka.mbs3.data.FinalFileInfo;
import eu.zderadicka.mbs3.data.TmpFileInfo;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

@QuarkusTest
// @TestHTTPEndpoint(UploadResource.class)
public class FileUploadTest extends BaseTest {

    final static String PATH_PREFIX = "/api/v1/";

    @Test
    public void testUploadPage() {
        given()
                .when().get(PATH_PREFIX + "upload/form")
                .then()
                .statusCode(200)
                .contentType("text/html");
    }

    @Test
    public void testFileUpload() throws IOException {
        var fileName = "my-test-file-123.txt";
        var test_file = dataDirectory.resolve(fileName);
        randomContent(test_file, 1000);

        var tmpFileInfo = given()
                .body(test_file.toFile())
                .header("Content-Type", "text/plain")
                .when()
                .post(PATH_PREFIX + "upload/?file-name=" + fileName)
                .then()
                .statusCode(200)
                .and()
                .extract()
                .as(TmpFileInfo.class);

        assertEquals(fileName, tmpFileInfo.originalFileName());
        assertEquals(1000, tmpFileInfo.size());
        var contentType = tmpFileInfo.contentType().split(";")[0].trim();
        assertEquals("text/plain", contentType);
        assertNotNull(tmpFileInfo.tmpFile());

        var finalFile = "book/author/" + fileName;
        var confirm = new ConfirmUpload(tmpFileInfo.tmpFile(), finalFile);
        var fileInfo = given()
                .body(confirm)
                .header("Content-Type", "application/json")
                .when()
                .post(PATH_PREFIX + "upload/confirm")
                .then()
                .statusCode(200)
                .extract()
                .as(FinalFileInfo.class);

        assertEquals(finalFile, confirm.finalFile());
        assertFalse(fileInfo.pathWasModified());

        given()
                .when()
                .get(PATH_PREFIX + "files/" + finalFile)
                .then()
                .statusCode(200);

    }

}