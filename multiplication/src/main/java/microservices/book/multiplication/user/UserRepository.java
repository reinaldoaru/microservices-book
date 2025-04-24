package microservices.book.multiplication.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByAlias(final String alias);

}
