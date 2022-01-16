package no.bjornhjelle.authentication.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter06 - Modern API Development with Spring and Spring Boot
 **/
@Data
@Accessors(chain = true)
@Entity
@Table(name = "user_tokens")
@EqualsAndHashCode(callSuper=false)
public class UserTokenEntity extends EntityWithUUID {

  @NotNull(message = "Refresh token is required.")
  @Basic(optional = false)
  @Column(name = "refresh_token")
  private String refreshToken;

  @ManyToOne(fetch = FetchType.LAZY)
  private UserEntity user;

  public UserTokenEntity setId(UUID id, UserEntity user, String refreshToken) {
    this.id = id;
    this.refreshToken = refreshToken;
    this.user = user;
    return this;
  }

}
