package eu.zderadicka.mbs3;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class FileUploadTest {

    @Test
    public void testUploadPage() {
        given()
          .when().get("/api/v1/files/upload")
          .then()
             .statusCode(200)
             .contentType("text/html");
    }

}