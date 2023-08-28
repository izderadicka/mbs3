package eu.zderadicka.mbs3.data.value;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Version;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseValueObject extends PanacheEntity {

    @Column(name = "version_id", nullable = false)
    @Version
    public int version = 1;

}
