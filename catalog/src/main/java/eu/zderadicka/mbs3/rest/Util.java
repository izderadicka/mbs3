package eu.zderadicka.mbs3.rest;

import jakarta.ws.rs.NotFoundException;

public class Util {

    public static <T> T throwNoTFoundOnNull(T object) {
        if (object == null) {
            throw new NotFoundException("Not Found");
        }
        return object;
    }

}
