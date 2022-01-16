package no.bjornhjelle.authentication.services;

import no.bjornhjelle.authentication.entities.UserEntity;
import no.bjornhjelle.authentication.models.RefreshToken;
import no.bjornhjelle.authentication.models.SignedInUser;
import no.bjornhjelle.authentication.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    UserEntity findUserByEmail(String email);
    Optional<SignedInUser> createUser(User user);
    SignedInUser getSignedInUser(UserEntity userEntity);
    Optional<SignedInUser> getAccessToken(RefreshToken refreshToken);
    void removeRefreshToken(RefreshToken refreshToken);

}
