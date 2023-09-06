package eu.zderadicka.mbs3.rest;

import java.util.List;

import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.repository.EbookRepository;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/v1/ebooks")
public class EbookResource extends BaseResource {

    @Inject
    private EbookRepository repository;

    @GET
    public List<Ebook> listPaged(@QueryParam("page") @DefaultValue("0") int pageNumber) {
        var query = repository.findAllPrefetched(Sort.by("title"))
                .page(pageNumber, pageSize);

        return query.list();
    }

    @POST
    @Transactional
    public Response create(@Valid Ebook ebook) {

        repository.persist(ebook);
        return Response.status(Status.CREATED).entity(ebook).build();

    }
}
