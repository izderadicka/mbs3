package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.lucene.store.ByteBuffersDirectory;
import org.junit.jupiter.api.Test;

import eu.zderadicka.mbs3.data.message.EbookChange.Ebook;

public class BasicSearchServiceTest {

    @Test
    public void addAndSearch() {

        var service = new SearchService(new ByteBuffersDirectory());
        Ebook ebook = new Ebook();
        ebook.title = "To zatracene vedro";
        ebook.series = "Utery";
        service.addDocument(ebook);
        var res = service.search("vedro");
        assertEquals(1, res.size());
        assertEquals("Utery", res.get(0).series);

        res = service.search("tlacholap");
        assertEquals(0, res.size());

    }

}
