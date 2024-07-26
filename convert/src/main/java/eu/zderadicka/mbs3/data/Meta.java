package eu.zderadicka.mbs3.data;

import java.io.Serializable;

public final class Meta {

    private Meta() {}

    public static record Confirmation(String jobId) implements Serializable {}
    public static record Job(String jobId, Meta.Request request) implements Serializable {}
    public static record Request(String file, boolean extractCover) implements Serializable {}

    
}
