package eu.zderadicka.mbs3.data.dto;

import java.util.List;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import eu.zderadicka.mbs3.data.entity.Ebook;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@EntityView(Ebook.class)
public interface EbookView {

    @IdMapping
    Long getId();

    String getTitle();

    LanguageView getLanguage();

    SeriesView getSeries();

    Integer getSeriesIndex();

    String getCover();

    Double getRating();

    List<AuthorView> getAuthors();

    List<GenreView> getGenres();
}
