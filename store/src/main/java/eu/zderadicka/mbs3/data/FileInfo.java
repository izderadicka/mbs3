package eu.zderadicka.mbs3.data;

import java.io.Serializable;

public record FileInfo(long size, String originalFileName, String contentType, String tmpFile)  implements Serializable{}
