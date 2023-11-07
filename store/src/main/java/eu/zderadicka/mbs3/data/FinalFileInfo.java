package eu.zderadicka.mbs3.data;

import java.io.Serializable;

public record FinalFileInfo(String filePath, boolean pathWasModified) implements Serializable {

}
