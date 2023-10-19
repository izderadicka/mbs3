package eu.zderadicka.mbs3.rest;

import static eu.zderadicka.mbs3.rest.Util.throwNoTFoundOnNull;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.message.EbookChange;
import eu.zderadicka.mbs3.data.repository.EbookRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/api/v1/tools")
public class ManagementResource {

    @Channel("ebook-updates")
    Emitter<EbookChange> updateChannel;

    @Inject
    private EbookRepository repository;

    @POST
    @Path("reindex/ebook/{id}")
    public void reindexEbook(@PathParam("id") Long id) {
        Ebook ebook = throwNoTFoundOnNull(repository.findById(id));
        updateChannel.send(EbookChange.fromEbook(ebook, false));

    }

}
