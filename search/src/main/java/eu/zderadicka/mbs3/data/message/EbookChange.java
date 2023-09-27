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

    }

    public EbookChange.Ebook ebook;
    public boolean created;
    public Instant timestamp;

}
