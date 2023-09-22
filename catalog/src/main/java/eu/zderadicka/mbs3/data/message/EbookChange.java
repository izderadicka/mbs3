package eu.zderadicka.mbs3.data.message;

import eu.zderadicka.mbs3.data.entity.Ebook;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class EbookChange {

    public Long id;
    public String title;

    public static EbookChange fromEbook(Ebook ebook) {
        var ebookChange = new EbookChange();
        ebookChange.id = ebook.getId();
        ebookChange.title = ebook.getTitle();
        return ebookChange;
    }
}
