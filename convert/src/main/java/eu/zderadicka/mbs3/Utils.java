package eu.zderadicka.mbs3;

import java.util.Optional;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import io.quarkus.logging.Log;

public  class Utils {

   public static Optional<String> getFileExtension (String fileName) {

    var pos = fileName.lastIndexOf('.');
    if (pos == -1 || pos == fileName.length() - 1 || pos == 0) {
        return Optional.empty();
    } else{
        return Optional.of(fileName.substring(pos + 1));
    }
       
   } 

   public static Optional<String> guessExtension(String mimeTypeName) {

        MimeType mimeType = null;
        try {
            mimeType = new MimeTypes().forName(mimeTypeName);
        } catch (MimeTypeException e) {
            Log.warn("Couldn't Detect Mime Type for type: " + mimeTypeName, e);
        }

        if (mimeType != null) {
            String extension = mimeType.getExtension();
            if (extension == null || extension.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(extension);
        } else {
            return Optional.empty();
        }

    }
    
}
