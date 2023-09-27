package eu.zderadicka.mbs3;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import eu.zderadicka.mbs3.data.message.EbookChange;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EbookUpdates {

    @Inject
    SearchService search;

    @Incoming("ebook-updates")
    public void processUpdate(JsonObject msg) {

        Log.info(String.format("Got message payload type %s content %s", msg.getClass().getName(), msg));
        EbookChange data = msg.mapTo(EbookChange.class);
        search.addDocument(data.ebook);

    }
}
