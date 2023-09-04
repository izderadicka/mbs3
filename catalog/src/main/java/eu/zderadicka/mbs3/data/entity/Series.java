package eu.zderadicka.mbs3.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "series")
public class Series extends BaseEntity {

    @Column(name = "title", nullable = false, length = 256)
    private String title;
    @Column(name = "rating")
    private Double rating;
    @Column(name = "rating_count")
    private Integer ratingCount;
    @Column(name = "description")
    private String description;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
