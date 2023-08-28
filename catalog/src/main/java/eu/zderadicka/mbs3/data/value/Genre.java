package eu.zderadicka.mbs3.data.value;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(name = "genre", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Genre extends BaseValueObject {

    @Column(name = "name", unique = true, nullable = false, length = 64)
    public String name;

}
