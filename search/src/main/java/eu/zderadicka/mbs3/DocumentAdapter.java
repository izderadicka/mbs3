package eu.zderadicka.mbs3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;

import eu.zderadicka.mbs3.data.Ebook;

public class DocumentAdapter {

    public static Document fromEbook(Ebook ebook) {
        var document = new Document();
        var text = new ArrayList<String>();

        if (ebook.id == null) {
            throw new RuntimeException("Ebook ID is mandatory");
        }
        document.add(new LongPoint("id", ebook.id));
        document.add(new StoredField("id", ebook.id));

        if (ebook.authors != null) {
            for (var author : ebook.authors) {
                document.add(new TextField("author", author, Store.YES));
                text.add(author);
            }
        }

        document.add(new TextField("title", ebook.title, Store.YES));
        text.add(ebook.title);

        if (ebook.series != null) {
            document.add(new TextField("series", ebook.series, Store.YES));
            text.add(ebook.series);
            if (ebook.seriesIndex != null) {
                document.add(new StoredField("series_index", ebook.seriesIndex));
                text.add(Integer.toString(ebook.seriesIndex));
            }
        }

        if (ebook.genres != null) {
            for (var genre : ebook.genres) {
                document.add(new TextField("genre", genre, Store.YES));
                text.add(genre);
            }
        }
        if (ebook.language != null) {
            document.add(new StringField("language", ebook.language, Store.YES));
            text.add(ebook.language);
        }
        if (ebook.description != null) {
            document.add(new TextField("description", ebook.description, Store.NO));
        }

        document.add(new TextField("text", String.join(" ", text), Store.NO));
        return document;

    }

    private static Set<String> getAllValues(Document doc, String name) {
        var values = new HashSet<String>();
        for (var field : doc.getFields(name)) {
            values.add(field.stringValue());
        }
        return values;
    }

    public static Ebook toEbook(Document doc) {

        Ebook ebook = new Ebook();
        var id = doc.getField("id");
        if (id == null) {
            throw new RuntimeException("Invalid document without id");
        }
        ebook.id = id.storedValue().getLongValue();
        ebook.title = doc.get("title");
        ebook.series = doc.get("series");
        ebook.language = doc.get("language");
        var seriesIndex = doc.getField("series_index");
        ebook.seriesIndex = seriesIndex != null ? seriesIndex.storedValue().getIntValue() : null;

        ebook.authors = getAllValues(doc, "author");
        ebook.genres = getAllValues(doc, "genre");

        return ebook;
    }

}
