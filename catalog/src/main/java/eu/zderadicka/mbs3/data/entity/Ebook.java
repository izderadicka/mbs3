package eu.zderadicka.mbs3.data.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import eu.zderadicka.mbs3.data.value.Genre;
import eu.zderadicka.mbs3.data.value.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ebook")
public class Ebook extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false)
    @NotNull(message = "language is mandatory")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(name = "title", nullable = false, length = 256)
    @NotBlank(message = "Title is mandatory")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "series_index")
    private Integer seriesIndex;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "rating_count")
    private Integer ratingCount;

    @Column(name = "downloads")
    private Integer downloads;

    @Column(name = "cover", length = 512)
    private String cover;

    @Column(name = "base_dir", nullable = false, length = 512)
    private String baseDir;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ebook_authors", schema = "public", joinColumns = {
            @JoinColumn(name = "ebook_id", nullable = false, updatable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "author_id", nullable = false, updatable = false) })
    private Set<Author> authors = new HashSet<Author>(0);

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ebook_genres", schema = "public", joinColumns = {
            @JoinColumn(name = "ebook_id", nullable = false, updatable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "genre_id", nullable = false, updatable = false) })
    private Set<Genre> genres = new HashSet<Genre>(0);

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSeriesIndex() {
        return this.seriesIndex;
    }

    public void setSeriesIndex(Integer seriesIndex) {
        this.seriesIndex = seriesIndex;
    }

    public Double getRating() {
        return this.rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return this.ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Integer getDownloads() {
        return this.downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBaseDir() {
        return this.baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Series getSeries() {
        return this.series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author... authors) {
        this.authors.addAll(Arrays.asList(authors));
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public void addGenre(Genre... genres) {
        this.genres.addAll(Arrays.asList(genres));
    }

}
