package eu.zderadicka.mbs3;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.value.Language;
import eu.zderadicka.mbs3.rest.EbookResource;
import io.quarkus.logging.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.internal.http.Status;

@QuarkusTest
@TestHTTPEndpoint(EbookResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EBookResourceTest {

    @Order(10)
    @Test
    public void testInitialEmptyList() {
        var result = given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().as(List.class);

        assertEquals(0, result.size());
    }

    @Order(20)
    @Test
    public void testCreateEbook() {

        Language language = new Language();
        language.code = "cs";
        language.name = "Czech";
        language.id = 1L;
        language.version = 1;

        Ebook ebook = new Ebook();
        ebook.setTitle("Testovaci kniha");
        ebook.setBaseDir("Testovaci kniha");
        ebook.setLanguage(language);

        var response = given()
                .body(ebook)
                .and()
                .header("Content-Type", "application/json")
                .when()
                .post()
                .then();

        final int EXPECTED_CODE = jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode();
        if (response.extract().statusCode() != EXPECTED_CODE) {
            Log.warn("Response body: " + response.extract().body().asPrettyString());
        }
        response.statusCode(is(EXPECTED_CODE));

        Ebook createdEbook = response.extract().body().as(Ebook.class);
        assertNotNull(createdEbook);
        assertNotNull(createdEbook.getId());
    }

    @Order(30)
    @Test
    public void testExists() {
        List<Ebook> result = given()
                .when()
                .get()
                .then()
                .extract().body().as(new TypeRef<List<Ebook>>() {
                });

        assertEquals(1, result.size());
        assertEquals(result.get(0).getTitle(), "Testovaci kniha");
        assertEquals(result.get(0).getLanguage().code, "cs");
    }

}
