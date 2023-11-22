package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class ConversionServiceTest {

    @Inject
    ConversionService service;

    @Test
    public void testExtractMetadata() {

        var result = service.extractMetadata("doyle.epub").join();
        assertTrue(result.contains("Sherlock Holmes"));
    }
    
}
