package eu.zderadicka.mbs3;

import java.util.List;

import eu.zderadicka.mbs3.data.message.EbookChange.Ebook;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/search")
public class SearchResource {

    @Inject
    private SearchService searchService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ebook> search(@QueryParam("q") @NotEmpty String query) {
        return searchService.search(query);
    }
}
