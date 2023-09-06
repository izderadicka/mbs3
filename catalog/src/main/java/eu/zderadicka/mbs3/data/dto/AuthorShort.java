package eu.zderadicka.mbs3.data.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AuthorShort {

    public Long id;
    public String firstName;
    public String lastName;

    public AuthorShort(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
