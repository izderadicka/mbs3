package eu.zderadicka.mbs3;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DebugQueue {

    @Incoming("meta-responses")
    public void debug(String meta) {
        Log.info("Meta: " + meta);
    }
    
}
