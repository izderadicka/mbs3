package eu.zderadicka.mbs3.data.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Version;
import jakarta.ws.rs.NotFoundException;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    @SuppressWarnings("unchecked")
    public static <T extends BaseEntity> Uni<T> findByIdOrThrow(Object id) {
        return T.findById(id).onItem().ifNull().failWith(() -> new NotFoundException("Not Found"))
                .map(entity -> (T) entity);

    }

}
