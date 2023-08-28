package eu.zderadicka.mbs3;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import eu.zderadicka.mbs3.data.entity.Author;
import eu.zderadicka.mbs3.data.value.Genre;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.smallrye.mutiny.Uni;

@QuarkusTest
public class ValueObjectsTest {

    @Test
    @RunOnVertxContext
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testGenre(TransactionalUniAsserter asserter) {
        // asserter.execute(() -> Uni.createFrom().item(true));
        asserter.assertTrue(() -> Genre.count().map(n -> n > 50));
    }
}
