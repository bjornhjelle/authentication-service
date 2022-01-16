package no.bjornhjelle.authentication.repositories;

import java.util.Optional;
import java.util.UUID;

import no.bjornhjelle.authentication.entities.UserTokenEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter06 - Modern API Development with Spring and Spring Boot
 **/
public interface UserTokenRepository extends CrudRepository<UserTokenEntity, UUID> {

  Optional<UserTokenEntity> findByRefreshToken(String refreshToken);
  Optional<UserTokenEntity> deleteByUserId(UUID userId);

}

