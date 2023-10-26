package eu.zderadicka.mbs3.data.message;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class EbookChange {

    public static class Ebook {

        public Long id;
        public String title;
        public String series;
        public Integer seriesIndex;
        public Set<String> authors;
        public Set<String> genres;
        public String language;
        public String description;

    }

    public List<EbookChange.Ebook> ebooks;
    public boolean created;
    public Instant timestamp;

    private static EbookChange empty(boolean created) {
        var ebookChange = new EbookChange();
        ebookChange.created = created;
        ebookChange.timestamp = Instant.now();
        return ebookChange;
    }

    public static EbookChange fromEbook(eu.zderadicka.mbs3.data.entity.Ebook ebook, boolean created) {
        var ebookChange = empty(created);
        var e = mapEbook(ebook);
        ebookChange.ebooks = List.of(e);
        return ebookChange;
    }

    public static EbookChange fromEbooks(List<eu.zderadicka.mbs3.data.entity.Ebook> ebooks, boolean created) {
        var ebookChange = empty(created);
        var ebooksMapped = ebooks.stream().map(EbookChange::mapEbook).collect(Collectors.toList());
        ebookChange.ebooks = ebooksMapped;
        return ebookChange;
    }

    private static EbookChange.Ebook mapEbook(eu.zderadicka.mbs3.data.entity.Ebook ebook) {
        var e = new EbookChange.Ebook();
        e.id = ebook.getId();
        e.title = ebook.getTitle();
        e.series = ebook.getSeries() != null ? ebook.getSeries().getTitle() : null;
        e.seriesIndex = ebook.getSeriesIndex();
        e.authors = ebook.getAuthors().stream().map(author -> author.toString()).collect(Collectors.toSet());
        e.genres = ebook.getGenres().stream().map(genre -> genre.name).collect(Collectors.toSet());
        e.language = ebook.getLanguage().name;
        e.description = ebook.getDescription();
        return e;
    }
}
