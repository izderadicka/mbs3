package eu.zderadicka.mbs3.data;

import java.io.Serializable;

public record ConfirmUpload(String tempFile, String finalFile) implements Serializable{
    
}
