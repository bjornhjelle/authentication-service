package no.bjornhjelle.authentication.repositories;

import no.bjornhjelle.authentication.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    public Optional<UserEntity> findByEmail(String email);

    @Query(value = "select count(u.*) from authdb.users u where u.email = :email", nativeQuery = true)
    Integer findCountByEmail(String email);
}
