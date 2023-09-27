package eu.zderadicka.mbs3;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;

import eu.zderadicka.mbs3.data.message.EbookChange.Ebook;

public class DocumentAdapter {

    public static Document fromEbook(Ebook ebook) {
        var document = new Document();
        document.add(new TextField("title", ebook.title, Store.YES));
        if (ebook.series != null) {
            document.add(new TextField("series", ebook.series, Store.YES));
        }

        return document;

    }

    public static Ebook toEbook(Document doc) {

        Ebook ebook = new Ebook();
        ebook.title = doc.get("title");
        ebook.series = doc.get("series");

        return ebook;
    }

}
