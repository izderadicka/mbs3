package eu.zderadicka.mbs3;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;

import java.util.Set;

@QuarkusTest
@TestHTTPEndpoint(Authenticator.class)
public class AutheticatorResourceTest {

    @Test
    public void testJwtAuthentication() {
        var token = new TokenInfo();
        token.upn = "usak@kulisak.net";
        token.validSeconds = 60L;
        token.groups = Set.of("user", "admin");
        String jwt = given()
                .header("Content-Type", "application/json")
                .body(token)
                .when().post("/generate")
                .then()
                .statusCode(200)
                .extract().asString();

        given()
                .when().get("/test-secured")
                .then()
                .statusCode(is(401));

        given()
                .header("Authorization", "Bearer " + jwt)
                .when().get("/test-secured")
                .then()
                .statusCode(is(200))
                .and()
                .body(containsString(token.upn));

    }

}