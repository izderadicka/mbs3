package eu.zderadicka.mbs3.rest;

import static eu.zderadicka.mbs3.rest.Util.throwNoTFoundOnNull;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import eu.zderadicka.mbs3.common.Page;
import eu.zderadicka.mbs3.data.dto.EbookView;
import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.message.EbookChange;
import eu.zderadicka.mbs3.data.repository.EbookRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/v1/ebooks")
public class EbookResource {

    @Channel("ebook-updates")
    Emitter<EbookChange> updateChannel;

    @Inject
    private EbookRepository repository;

    @GET
    public List<EbookView> listPaged(@QueryParam("page") Integer pageNumber) {
        return repository.listAllProjected(new Page(pageNumber));
    }

    @GET
    @Path("{id}")
    public Ebook getOne(@PathParam("id") Long id) {
        return throwNoTFoundOnNull(repository.findById(id));
    }

    @POST
    @Transactional
    public Response create(@Valid Ebook ebook) {

        repository.persist(ebook);
        updateChannel.send(EbookChange.fromEbook(ebook));
        return Response.status(Status.CREATED).entity(ebook).build();

    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response modify(@PathParam("id") Long id, @Valid Ebook ebook) {

        if (id == null || !id.equals(ebook.getId())) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        throwNoTFoundOnNull(repository.findById(id));

        var existingEbook = repository.getEntityManager().merge(ebook);

        existingEbook.setModified(LocalDateTime.now());
        updateChannel.send(EbookChange.fromEbook(ebook));
        return Response.status(Status.NO_CONTENT).build();
    }
}
