package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import eu.zderadicka.mbs3.data.value.Genre;
import eu.zderadicka.mbs3.data.value.Language;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

@QuarkusTest
public class ValueObjectsTest {

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testValues() {

        assertTrue(Genre.count() > 50);
        assertTrue(Language.count() >= 4);

    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @Transactional
    public void testPersitGenre() {
        var count = Genre.count();
        var genre = new Genre();
        genre.name = "Test";
        genre.persist();
        assertEquals(count + 1, Genre.count());
    }

    @Test
    public void testCreation() {

        var g = new Genre();
        g.name = "Test";
        assertNotNull(g.version, "version is defaulted");

    }

}
