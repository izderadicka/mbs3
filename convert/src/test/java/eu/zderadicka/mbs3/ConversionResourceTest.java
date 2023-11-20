package eu.zderadicka.mbs3;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import eu.zderadicka.mbs3.data.MetaRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestHTTPEndpoint(ConversionResource.class)
public class ConversionResourceTest {

    @Test
    public void testMetaEndpoint() {
        given()
            .body(new MetaRequest("doyle.epub",false))
            .header("Content-Type", "application/json")
          .when().post("/metadata")
          .then()
             .statusCode(200)
             .body(containsString("Sherlock Holmes"));
    }

}