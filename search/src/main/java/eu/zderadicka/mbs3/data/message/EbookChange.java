package eu.zderadicka.mbs3.data.message;

import java.time.Instant;
import java.util.List;

import eu.zderadicka.mbs3.data.Ebook;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class EbookChange {

    public List<Ebook> ebooks;
    public boolean created;
    public Instant timestamp;

}
