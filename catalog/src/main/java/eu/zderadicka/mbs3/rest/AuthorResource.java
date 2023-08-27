package eu.zderadicka.mbs3.rest;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import eu.zderadicka.mbs3.orm.Author;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
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

    @GET
    public Uni<List<Author>> getAll(@QueryParam("page") @DefaultValue("0") int pageNumber) {
        var query = Author.findAll(Sort.by("lastName").and("firstName").ascending())
                .page(pageNumber, pageSize);

        return query.list();
    }

    @GET
    @Path("/{id}")
    public Uni<Author> getById(@PathParam("id") Long id) {
        Log.info("GET Author id " + id);

        return Author.findByIdOrThrow(id);
    }

    @POST
    public Uni<RestResponse<Author>> create(Author author) {
        return Panache.withTransaction(author::persist).replaceWith(RestResponse.status(Status.CREATED, author));

    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return Panache.withTransaction(() -> Author.deleteById(id)).map((wasDeleted) -> {
            if (wasDeleted) {
                return Response.status(204).build();
            } else {
                return Response.status(404).build();
            }
        });
    }
}
