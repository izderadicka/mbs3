package eu.zderadicka.mbs3.rest;

import static eu.zderadicka.mbs3.rest.Util.throwNoTFoundOnNull;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import eu.zderadicka.mbs3.data.dto.AuthorShort;
import eu.zderadicka.mbs3.data.entity.Author;
import eu.zderadicka.mbs3.data.repository.AuthorRepository;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("api/v1/authors")
public class AuthorResource extends BaseResource {

    @Inject
    private AuthorRepository repository;

    @GET
    public Uni<List<AuthorShort>> listPaged(@QueryParam("page") @DefaultValue("0") int pageNumber) {
        var query = repository.findAll(Sort.by("lastName").and("firstName").ascending())
                .project(AuthorShort.class)
                .page(pageNumber, pageSize);

        return query.list();
    }

    @GET
    @Path("/{id}")
    public Uni<Author> getById(@PathParam("id") Long id) {
        Log.info("GET Author id " + id);

        return throwNoTFoundOnNull(repository.findById(id));
    }

    @POST
    public Uni<RestResponse<Author>> create(Author author) {
        return Panache.withTransaction(() -> repository.persist(author))
                .replaceWith(RestResponse.status(Status.CREATED, author));

    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Panache.withTransaction(() -> repository.deleteById(id)).map((wasDeleted) -> {
            if (wasDeleted) {
                return Response.status(204).build();
            } else {
                return Response.status(404).build();
            }
        });
    }
}
