package eu.zderadicka.mbs3.data.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import eu.zderadicka.mbs3.data.value.Language;

@EntityView(Language.class)
public interface LanguageView {
    @IdMapping
    Long getId();

    String getName();
}
