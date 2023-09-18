package eu.zderadicka.mbs3.rest;

import eu.zderadicka.mbs3.data.value.Language;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.MethodProperties;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/api/v1/languages", paged = false)
public interface LanguageResource extends PanacheEntityResource<Language, Long> {

    @Override
    @MethodProperties(exposed = false)
    boolean delete(Long id);

}
