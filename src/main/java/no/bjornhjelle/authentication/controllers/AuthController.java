package no.bjornhjelle.authentication.controllers;

import lombok.extern.slf4j.Slf4j;
import no.bjornhjelle.authentication.api.AuthenticationApi;
import no.bjornhjelle.authentication.entities.UserEntity;
import no.bjornhjelle.authentication.exceptions.InvalidRefreshTokenException;
import no.bjornhjelle.authentication.models.RefreshToken;
import no.bjornhjelle.authentication.models.SignInReq;
import no.bjornhjelle.authentication.models.SignedInUser;
import no.bjornhjelle.authentication.models.User;
import no.bjornhjelle.authentication.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController implements AuthenticationApi {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<SignedInUser> getAccessToken(@Valid RefreshToken refreshToken) {
        return ResponseEntity.ok(service.getAccessToken(refreshToken).orElseThrow(InvalidRefreshTokenException::new));
    }

    @Override
    public ResponseEntity<SignedInUser> signIn(@Valid SignInReq signInReq) {
        UserEntity userEntity = service.findUserByEmail(signInReq.getUsername());
        if (passwordEncoder.matches(signInReq.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.ok(service.getSignedInUser(userEntity));
        }
        throw new InsufficientAuthenticationException("Unauthorized.");
    }

    @Override
    public ResponseEntity<Void> signOut(@Valid RefreshToken refreshToken) {
        // We are using removeToken API for signout.
        // Ideally you would like to get the user ID from Logged in user's request
        // and remove the refresh token based on retrieved user id from request.
        service.removeRefreshToken(refreshToken);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<SignedInUser> signUp(@Valid User user) {
        // Have a validation for all required fields.
        SignedInUser signedInUser = service.createUser(user).get();
        return ResponseEntity.status(HttpStatus.CREATED).body(signedInUser);
    }

}
