package eu.zderadicka.mbs3.data.repository;

import eu.zderadicka.mbs3.data.entity.Ebook;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EbookRepository implements PanacheRepository<Ebook> {

    public PanacheQuery<Ebook> findAllPrefetched(Sort sort) {
        sort.getColumns().forEach((col) -> col.setName("e." + col.getName()));
        return find(
                "FROM Ebook e LEFT JOIN FETCH e.language LEFT JOIN FETCH e.series", sort);
    }

}
