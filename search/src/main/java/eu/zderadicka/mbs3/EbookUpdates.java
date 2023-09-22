package eu.zderadicka.mbs3;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EbookUpdates {

    @Incoming("ebook-updates")
    public void processUpdate(Object msg) {

        Log.info(String.format("Got message payload type %s content %s", msg.getClass().getName(), msg));

    }
}
