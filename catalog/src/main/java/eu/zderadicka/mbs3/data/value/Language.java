package eu.zderadicka.mbs3.data.value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "language", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Language extends BaseValueObject {
    @Column(name = "code", unique = true, nullable = false, length = 6)
    public String code;
    @Column(name = "name", nullable = false, length = 64)
    public String name;
}
