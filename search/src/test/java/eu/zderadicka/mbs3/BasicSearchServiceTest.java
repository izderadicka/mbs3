package eu.zderadicka.mbs3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;

import org.apache.lucene.store.ByteBuffersDirectory;
import org.junit.jupiter.api.Test;

import eu.zderadicka.mbs3.data.Ebook;

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
        service.addOrUpdateDocuments(List.of(ebook));
        var res = service.search("vedro");
        assertEquals(1, res.results.size());
        assertEquals("Utery", res.results.get(0).ebook.series);

        res = service.search("Metelka Dusin");
        assertEquals(1, res.results.size());

        res = service.search("Czech Horror");
        assertEquals(1, res.results.size());

        res = service.search("tlacholap");
        assertEquals(0, res.results.size());

    }

}
