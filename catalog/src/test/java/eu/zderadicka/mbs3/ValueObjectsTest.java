package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import eu.zderadicka.mbs3.data.value.Genre;
import io.quarkus.hibernate.reactive.panache.common.runtime.SessionOperations;
import io.quarkus.test.hibernate.reactive.panache.TransactionalUniAsserter;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.quarkus.test.vertx.UniAsserterInterceptor;
import io.smallrye.mutiny.Uni;

@QuarkusTest
public class ValueObjectsTest {

    private final class UniAsserterWithTransactions extends UniAsserterInterceptor {
        private UniAsserterWithTransactions(UniAsserter asserter) {
            super(asserter);
        }

        @Override
        protected <T> Supplier<Uni<T>> transformUni(Supplier<Uni<T>> uniSupplier) {
            return () -> SessionOperations.withTransaction(() -> uniSupplier.get());
        }
    }

    @Test
    @RunOnVertxContext
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testGenre(UniAsserter asserterIn) {

        var asserter = new UniAsserterWithTransactions(asserterIn);
        asserter.assertTrue(() -> Genre.count().map(n -> n > 50));
        asserter.assertFalse(() -> Genre.count().map(n -> n <= 50));

    }

    @Test
    @RunOnVertxContext
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testGenreProblematic(TransactionalUniAsserter asserter) {
        asserter.assertTrue(() -> Genre.count().map(n -> n < 50));
        asserter.assertTrue(() -> Uni.createFrom().item(false));

    }

    @Test
    @RunOnVertxContext
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testPersitGenre(UniAsserter asserterIn) {
        var asserter = new UniAsserterWithTransactions(asserterIn);

        asserter.assertEquals(() -> Genre.count(), 57L)
        .execute(() -> {
            var genre = new Genre();
            genre.name = "Test";
            return genre.persist();
        })
        .assertEquals(() -> Genre.count(), 58L);
    }

    @Test
    public void testCreation() {

        var g = new Genre();
        g.name = "Test";
        assertNotNull(g.version, "version is defaulted");

    }

}
