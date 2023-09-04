package eu.zderadicka.mbs3.rest;

import java.util.List;

import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.repository.EbookRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/api/v1/ebooks")
public class EbookResource extends BaseResource {

    @Inject
    private EbookRepository repository;

    @GET
    public Uni<List<Ebook>> listPaged(@QueryParam("page") @DefaultValue("0") int pageNumber) {
        var query = repository.findAllPrefetched(Sort.by("title"))
                .page(pageNumber, pageSize);

        return query.list();
    }
}
