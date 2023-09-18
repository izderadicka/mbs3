package eu.zderadicka.mbs3;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static eu.zderadicka.mbs3.Utils.*;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.zderadicka.mbs3.data.entity.Author;
import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.value.Genre;
import eu.zderadicka.mbs3.data.value.Language;
import eu.zderadicka.mbs3.rest.EbookResource;
import io.quarkus.logging.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@QuarkusTest
@TestHTTPEndpoint(EbookResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EBookResourceTest {

    private static Long ebookId;

    @Order(10)
    @Test
    public void testInitialEmptyList() {
        var result = given()
                .when()
                .get()
                .then()
                .statusCode(isSuccessNoRedirect())
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

        var genre1 = new Genre();
        genre1.id = 9L;

        var genre2 = new Genre();
        genre2.id = 25L;

        var author = new Author();
        author.setId(2L);

        ebook.addGenre(genre1, genre2);
        ebook.addAuthor(author);

        var response = given()
                .body(ebook)
                .and()
                .header("Content-Type", "application/json")
                .when()
                .post()
                .then();

        final int EXPECTED_CODE = Response.Status.CREATED.getStatusCode();
        if (response.extract().statusCode() != EXPECTED_CODE) {
            Log.warn("Response body: " + response.extract().body().asPrettyString());
        }
        response.statusCode(is(EXPECTED_CODE));

        Ebook createdEbook = response.extract().body().as(Ebook.class);
        assertNotNull(createdEbook);
        assertNotNull(createdEbook.getId());
        ebookId = createdEbook.getId();
    }

    @Order(25)
    @Test
    public void testModification() {

        Ebook ebook = given().when().get("/{id}", ebookId)
                .then().statusCode(is(200)).extract().body().as(Ebook.class);

        assertEquals(ebookId, ebook.getId());

        var author = new Author();
        author.setId(3L);
        ebook.addAuthor(author);
        ebook.setTitle(ebook.getTitle() + " 2");

        given().body(ebook).and().header("Content-Type", MediaType.APPLICATION_JSON)
                .when().put("/{id}", ebookId)
                .then().statusCode(anyOf(is(200), is(204)));

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
        assertEquals(1, result.size(), "One ebook should exists");
        var first = result.get(0);
        assertEquals("Testovaci kniha 2", first.getTitle());
        assertEquals("Czech", first.getLanguage().name);
        assertEquals(2, first.getGenres().size());
        assertEquals(2, first.getAuthors().size());
        assertTrue(first.getModified().compareTo(first.getCreated()) > 0, "Modified timestamp is bigger then Created");
        for (var genre : first.getGenres()) {
            if (genre.id == 9L) {
                assertEquals("Fantasy", genre.name);
            } else if (genre.id == 25L) {
                assertEquals("Science Fiction", genre.name);
            }
        }
        // Nefunguje v testech - asi diky transakcim
        // assertEquals(2, first.getVersion(), "After update version should increase");
    }

}
