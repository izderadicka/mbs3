package eu.zderadicka.mbs3.rest;

import static eu.zderadicka.mbs3.rest.Util.throwNoTFoundOnNull;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import eu.zderadicka.mbs3.data.entity.Ebook;
import eu.zderadicka.mbs3.data.message.EbookChange;
import eu.zderadicka.mbs3.data.repository.EbookRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/tools")
@ApplicationScoped
public class ManagementResource {

    @Channel("ebook-updates")
    Emitter<EbookChange> updateChannel;

    @Inject
    private EbookRepository repository;

    @Inject
    ManagedExecutor managedExecutor;

    private Reindexer reindexerRunning = null;

    private class Reindexer implements Runnable {

        private boolean running = true;

        @Override
        public void run() {

            var page = repository.findAll(Sort.by("id")).page(Page.ofSize(100));

            while (running) {
                var ebooks = page.list();
                if (ebooks.size() == 0) {
                    break;
                }

                updateChannel.send(EbookChange.fromEbooks(ebooks, false));
                page = page.nextPage();
            }

            reindexerRunning = null;

        }

        public void stop() {
            running = false;
        }

    }

    @POST
    @Path("reindex/ebook/{id}")
    public void reindexEbook(@PathParam("id") Long id) {
        Ebook ebook = throwNoTFoundOnNull(repository.findById(id));
        updateChannel.send(EbookChange.fromEbook(ebook, false));

    }

    private synchronized boolean startReindexer() {
        if (reindexerRunning == null) {
            reindexerRunning = new Reindexer();
            managedExecutor.execute(reindexerRunning);
            return true;

        } else {
            return false;
        }
    }

    private synchronized boolean stopReindexer() {
        if (reindexerRunning == null) {
            return false;
        } else {
            try {
                reindexerRunning.stop();
                return true;
            } catch (NullPointerException e) {
                return false;
            }
        }
    }

    @POST
    @Path("reindex/all")
    public Response reindexAll() {

        if (startReindexer()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }

    }

    @POST
    @Path("reindex/stop")
    public Response reindexSTOP() {

        if (stopReindexer()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }

    }

}
