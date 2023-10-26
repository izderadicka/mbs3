package eu.zderadicka.mbs3.data;

import java.io.Serializable;
import java.util.Set;

public class Ebook implements Serializable {

    public Long id;
    public String title;
    public String series;
    public Integer seriesIndex;
    public Set<String> authors;
    public Set<String> genres;
    public String language;
    public String description;

}
