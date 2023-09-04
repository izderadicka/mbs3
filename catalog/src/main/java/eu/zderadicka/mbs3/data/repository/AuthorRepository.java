package eu.zderadicka.mbs3.data.repository;

import eu.zderadicka.mbs3.data.entity.Author;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthorRepository implements PanacheRepository<Author> {

}
