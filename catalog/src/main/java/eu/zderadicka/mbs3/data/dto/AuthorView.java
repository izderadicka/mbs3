package eu.zderadicka.mbs3.data.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import eu.zderadicka.mbs3.data.entity.Author;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@EntityView(Author.class)
public interface AuthorView {

    @IdMapping
    Long getId();

    String getFirstName();

    String getLastName();

}
