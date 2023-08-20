package eu.zderadicka.mbs3.orm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "author")
public class Author extends BaseEntity {
    @Column(name = "first_name", length = 128)
    public String firstName;

    @Column(name = "last_name", length = 128, nullable = false)
    public String lastName;

}
