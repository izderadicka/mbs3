package eu.zderadicka.mbs3.data.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import eu.zderadicka.mbs3.data.value.Language;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@EntityView(Language.class)
public interface LanguageView {
    @IdMapping
    Long getId();

    String getName();
}
