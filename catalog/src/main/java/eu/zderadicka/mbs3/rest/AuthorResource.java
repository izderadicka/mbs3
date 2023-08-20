package eu.zderadicka.mbs3.rest;

import java.util.List;

import eu.zderadicka.mbs3.orm.Author;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

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
        return Author.findByIdOrThrow(id);
    }
}
