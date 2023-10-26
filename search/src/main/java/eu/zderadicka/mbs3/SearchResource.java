package eu.zderadicka.mbs3;

import eu.zderadicka.mbs3.data.SearchResults;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.DefaultValue;
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
    public SearchResults search(@QueryParam("q") @NotEmpty String query,
            @QueryParam("size") @DefaultValue("10") Integer resultsSize) {
        return searchService.search(query, resultsSize);
    }
}
