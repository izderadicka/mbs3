package eu.zderadicka.mbs3.rest;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.NotFoundException;

public class Util {

    public static <T> Uni<T> throwNoTFoundOnNull(Uni<T> future) {
        return future.onItem().ifNull().failWith(() -> new NotFoundException("Not Found"));
    }
    
}
