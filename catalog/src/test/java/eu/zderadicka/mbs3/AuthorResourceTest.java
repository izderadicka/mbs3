package eu.zderadicka.mbs3;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.zderadicka.mbs3.orm.Author;
import eu.zderadicka.mbs3.rest.AuthorResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(AuthorResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorResourceTest {

    private static Long authorId;

    @Test
    @Order(10)
    public void testCreate() {
        Author author = new Author();
        author.firstName = "Usak";
        author.lastName = "Kulisak";

        Author authorCreated = given()
                .header("Content-Type", "application/json")
                .and()
                .body(author)
                .when()
                .post()
                .then()
                .statusCode(is(201))
                .extract().body().as(Author.class);

        assertNotNull(authorCreated.id);

        authorId = authorCreated.id;

    }

    @Test
    @Order(20)
    public void testGet() {
        assertNotNull(authorId);
        Author author = given()
                .when()
                .get("/" + authorId)
                .then()
                .statusCode(is(200))
                .extract().body().as(Author.class);

        assertEquals(author.firstName, "Usak");
        assertEquals(author.lastName, "Kulisak");

    }

    @Test
    @Order(30)
    public void testDelete() {
        assertNotNull(authorId);
        given()
                .when()
                .delete("/{id}", authorId)
                .then()
                .statusCode(is(204));
    }

    @Test
    @Order(40)
    public void testWasDeleted() {
        assertNotNull(authorId);
        given()
                .when()
                .get("/{id}", authorId)
                .then()
                .statusCode(is(404));
    }
}
