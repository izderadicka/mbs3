package eu.zderadicka.mbs3.data.repository;

import java.util.List;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;

import eu.zderadicka.mbs3.common.Page;
import eu.zderadicka.mbs3.data.dto.EbookView;
import eu.zderadicka.mbs3.data.entity.Ebook;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class EbookRepository implements PanacheRepository<Ebook> {

    @Inject
    EntityViewManager evm;
    @Inject
    EntityManager em;
    @Inject
    CriteriaBuilderFactory cbf;

    public PanacheQuery<Ebook> findAllPrefetched(Sort sort) {
        sort.getColumns().forEach((col) -> col.setName("e." + col.getName()));
        return find(
                "FROM Ebook e LEFT JOIN FETCH e.language LEFT JOIN FETCH e.series", sort);
    }

    public List<EbookView> listAllProjected(Page pageNumber) {
        var cb = cbf.create(em, Ebook.class)
                .orderByAsc("title")
                .orderByAsc("id");

        return evm
                .applySetting(
                        EntityViewSetting.create(EbookView.class, pageNumber.getOffset(), pageNumber.getPageSize()), cb)
                .getResultList();
    }
}
