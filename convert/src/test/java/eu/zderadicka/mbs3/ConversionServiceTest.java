package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;

@QuarkusTest
public class ConversionServiceTest {

    @Inject
    ConversionService service;

    @Test
    public void testExtractMetadata() {

        var future = service.runEbookMeta("doyle.epub");
        var subscriber = future.subscribe().withSubscriber(UniAssertSubscriber.create());
        var result = subscriber.awaitItem().getItem();
        assertTrue(result.contains("Sherlock Holmes"));
    }

}
