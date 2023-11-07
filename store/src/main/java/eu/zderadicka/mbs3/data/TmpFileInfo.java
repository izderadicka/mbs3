package eu.zderadicka.mbs3.data;

import java.io.Serializable;

public record TmpFileInfo(long size, String originalFileName, String contentType, String tmpFile)  implements Serializable{
}
