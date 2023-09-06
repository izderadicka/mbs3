package eu.zderadicka.mbs3.data.value;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class BaseValueObject extends PanacheEntity {

    @Column(name = "version_id", nullable = false)
    @Version
    public int version = 1;

}
