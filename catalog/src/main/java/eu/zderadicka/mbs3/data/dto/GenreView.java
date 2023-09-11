package eu.zderadicka.mbs3.data.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import eu.zderadicka.mbs3.data.value.Genre;

@EntityView(Genre.class)
public interface GenreView {
    @IdMapping
    Long getId();

    String getName();
}
