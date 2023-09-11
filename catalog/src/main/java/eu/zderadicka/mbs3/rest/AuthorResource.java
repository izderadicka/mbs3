package eu.zderadicka.mbs3.rest;

import static eu.zderadicka.mbs3.rest.Util.throwNoTFoundOnNull;

import java.util.List;

import eu.zderadicka.mbs3.common.Page;
import eu.zderadicka.mbs3.data.dto.AuthorView;
import eu.zderadicka.mbs3.data.entity.Author;
import eu.zderadicka.mbs3.data.repository.AuthorRepository;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("api/v1/authors")
public class AuthorResource extends BaseResource {

    @Inject
    private AuthorRepository repository;

    @GET
    public List<AuthorView> listPaged(@QueryParam("page") Integer pageNumber) {
        return repository.listProjected(new Page(pageNumber));
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Author getById(@PathParam("id") Long id) {
        Log.info("GET Author id " + id);

        return throwNoTFoundOnNull(repository.findById(id));
    }

    @POST
    @Transactional
    public Response create(Author author) {
        repository.persist(author);
        return Response.status(Status.CREATED).entity(author).build();

    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (repository.deleteById(id)) {
            return Response.status(204).build();
        } else {
            return Response.status(404).build();
        }

    }
}
