package eu.zderadicka.mbs3.data;

import java.io.Serializable;
import java.util.List;

public class SearchResults implements Serializable {

    public static class OneResult implements Serializable {

        public float score;
        public Ebook ebook;
    }

    public long approximateTotal;
    public List<OneResult> results;

}
