package eu.zderadicka.mbs3.data.message;

import java.time.Instant;
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

    public EbookChange.Ebook ebook;
    public boolean created;
    public Instant timestamp;

    public static EbookChange fromEbook(eu.zderadicka.mbs3.data.entity.Ebook ebook, boolean created) {
        var ebookChange = new EbookChange();
        ebookChange.created = created;
        ebookChange.timestamp = Instant.now();
        var e = new EbookChange.Ebook();
        e.id = ebook.getId();
        e.title = ebook.getTitle();
        e.series = ebook.getSeries() != null ? ebook.getSeries().getTitle() : null;
        e.seriesIndex = ebook.getSeriesIndex();
        e.authors = ebook.getAuthors().stream().map(author -> author.toString()).collect(Collectors.toSet());
        e.genres = ebook.getGenres().stream().map(genre -> genre.name).collect(Collectors.toSet());
        e.language = ebook.getLanguage().name;
        e.description = ebook.getDescription();
        ebookChange.ebook = e;
        return ebookChange;
    }
}
