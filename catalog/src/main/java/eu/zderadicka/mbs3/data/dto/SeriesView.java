package eu.zderadicka.mbs3.data.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import eu.zderadicka.mbs3.data.entity.Series;

@EntityView(Series.class)
public interface SeriesView {

    @IdMapping
    Long getId();

    String getTitle();

}
