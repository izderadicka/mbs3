package eu.zderadicka.mbs3.data.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import eu.zderadicka.mbs3.data.value.Genre;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@EntityView(Genre.class)
public interface GenreView {
    @IdMapping
    Long getId();

    String getName();
}
