package eu.zderadicka.mbs3.rest;

import eu.zderadicka.mbs3.data.value.Genre;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.MethodProperties;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/api/v1/genres", paged = false)
public interface GenreResource extends PanacheEntityResource<Genre, Long> {

    @Override
    @MethodProperties(exposed = false)
    boolean delete(Long id);

}
