package eu.zderadicka.mbs3;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.zderadicka.mbs3.data.entity.Author;
import eu.zderadicka.mbs3.rest.AuthorResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.common.mapper.TypeRef;

@QuarkusTest
@TestHTTPEndpoint(AuthorResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestSecurity(user = "ivan@zderadicka.eu", roles = "admin")
public class AuthorResourceTest {

    private static Long authorId;
    private static int numberOfAuthors;

    private int countRecords() {
        List<Author> authors = given()
                .when()
                .get()
                .then()
                .statusCode(is(200))
                .extract().body().as(new TypeRef<List<Author>>() {
                });
        return authors.size();
    }

    @Test
    @Order(1)
    public void testSomeRecorsInitially() {
        numberOfAuthors = countRecords();
        assertTrue(numberOfAuthors > 0);
    }

    @Test
    @Order(10)
    public void testCreate() {
        Author author = new Author();
        author.setFirstName("Usak");
        author.setLastName("Kulisak");

        assertNotNull(author.getVersion());

        Author authorCreated = given()
                .header("Content-Type", "application/json")
                .and()
                .body(author)
                .when()
                .post()
                .then()
                .statusCode(is(201))
                .extract().body().as(Author.class);

        assertNotNull(authorCreated.getId());

        authorId = authorCreated.getId();

    }

    @Test
    @Order(15)
    public void testOneRecordAfterCreate() {
        assertEquals(numberOfAuthors + 1, countRecords());
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

        assertEquals(author.getFirstName(), "Usak");
        assertEquals(author.getLastName(), "Kulisak");

    }

    @Test
    @Order(25)
    @TestSecurity(user = "usak@kulisak.net", roles = "user")
    public void testDeleteUnauthorized() {
        assertNotNull(authorId);
        given()
                .when()
                .delete("/{id}", authorId)
                .then()
                .statusCode(is(403));
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

    @Test
    @Order(45)
    public void testNoRecorsFinally() {
        assertEquals(numberOfAuthors, countRecords());
    }
}
