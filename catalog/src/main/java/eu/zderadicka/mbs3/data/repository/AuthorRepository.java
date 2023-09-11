package eu.zderadicka.mbs3.data.repository;

import java.util.List;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;

import eu.zderadicka.mbs3.common.Page;
import eu.zderadicka.mbs3.data.dto.AuthorView;
import eu.zderadicka.mbs3.data.entity.Author;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class AuthorRepository implements PanacheRepository<Author> {

    @Inject
    EntityViewManager evm;
    @Inject
    EntityManager em;
    @Inject
    CriteriaBuilderFactory cbf;

    public List<AuthorView> listProjected(Page page) {
        CriteriaBuilder<Author> cb = cbf.create(em, Author.class)
                .orderByAsc("lastName")
                .orderByAsc("id");
        return evm.applySetting(EntityViewSetting.create(AuthorView.class, page.getOffset(), page.getPageSize()), cb)
                .getResultList();

    }
}
