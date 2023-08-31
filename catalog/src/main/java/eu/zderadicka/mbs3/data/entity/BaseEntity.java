package eu.zderadicka.mbs3.data.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntity {

    @Column(name = "created", nullable = false)
    public LocalDateTime created = LocalDateTime.now();

    @Column(name = "modified", nullable = false)
    public LocalDateTime modified = LocalDateTime.now();

    @Column(name = "version_id", nullable = false)
    @Version
    public int version = 1;

    @Column(name = "created_by_id")
    public Long createdById;

    @Column(name = "modified_by_id")
    public Long modifiedById;

}
