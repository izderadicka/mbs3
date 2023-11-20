package eu.zderadicka.mbs3.data;

import java.io.Serializable;

public record MetaRequest(String file, boolean extractCover) implements Serializable { 
}
