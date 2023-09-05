package eu.zderadicka.mbs3.rest;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.repository.EbookRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response.Status;

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

    @POST
    public Uni<RestResponse<Ebook>> create(@Valid Ebook ebook) {

        return Panache.withTransaction(() -> repository.persist(ebook))
                .replaceWith(() -> RestResponse.status(Status.CREATED, ebook));

    }
}
