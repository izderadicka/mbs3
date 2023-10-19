package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.apache.lucene.store.ByteBuffersDirectory;
import org.junit.jupiter.api.Test;

import eu.zderadicka.mbs3.data.message.EbookChange.Ebook;

public class BasicSearchServiceTest {

    @Test
    public void addAndSearch() {

        var service = new SearchService(new ByteBuffersDirectory());
        Ebook ebook = new Ebook();
        ebook.id = 1L;
        ebook.title = "To zatracene vedro";
        ebook.series = "Utery";
        var authors = new HashSet<String>();
        authors.add("Jarka Metelka");
        authors.add("Mirek Dusin");
        ebook.authors = authors;
        var genres = new HashSet<String>();
        genres.add("Horror");
        ebook.genres = genres;
        ebook.language = "Czech";
        service.addOrUpdateDocument(ebook);
        var res = service.search("vedro");
        assertEquals(1, res.size());
        assertEquals("Utery", res.get(0).series);

        res = service.search("Metelka Dusin");
        assertEquals(1, res.size());

        res = service.search("Czech Horror");
        assertEquals(1, res.size());

        res = service.search("tlacholap");
        assertEquals(0, res.size());

    }

}
