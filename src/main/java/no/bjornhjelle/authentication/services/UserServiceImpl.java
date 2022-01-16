package no.bjornhjelle.authentication.services;
import no.bjornhjelle.authentication.components.JwtManager;
import no.bjornhjelle.authentication.entities.UserEntity;
import no.bjornhjelle.authentication.entities.UserTokenEntity;
import no.bjornhjelle.authentication.exceptions.GenericAlreadyExistsException;
import no.bjornhjelle.authentication.exceptions.InvalidRefreshTokenException;
import no.bjornhjelle.authentication.models.RefreshToken;
import no.bjornhjelle.authentication.models.SignedInUser;
import no.bjornhjelle.authentication.models.User;
import no.bjornhjelle.authentication.repositories.UserRepository;
import no.bjornhjelle.authentication.repositories.UserTokenRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Streamable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtManager tokenManager;


    public UserServiceImpl(UserRepository userRepository, UserTokenRepository userTokenRepository, PasswordEncoder bCryptPasswordEncoder, JwtManager tokenManager) {
        this.repository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenManager = tokenManager;
    }

    public List<User> getAllUsers() {

        List<User> list = Streamable.of(repository.findAll())
                .map(u -> u.toUser()).stream()
                .collect(Collectors.toCollection(ArrayList::new));
        return list;

    }

    @Override
    public UserEntity findUserByEmail(String email) {
        if (Strings.isBlank(email)) {
            throw new UsernameNotFoundException("Invalid user.");
        }
        final String uname = email.trim();
        Optional<UserEntity> oUserEntity = repository.findByEmail(uname);
        UserEntity userEntity = oUserEntity.orElseThrow(
                () -> new UsernameNotFoundException(String.format("Given user(%s) not found.", uname)));
        return userEntity;
    }

    @Override
    @Transactional
    public Optional<SignedInUser> createUser(User user) {
        Integer count = repository.findCountByEmail(user.getEmail());
        if (count > 0) {
            throw new GenericAlreadyExistsException("Use email.");
        }
        UserEntity userEntity = repository.save(toEntity(user));
        return Optional.of(createSignedUserWithRefreshToken(userEntity));
    }

    private SignedInUser createSignedUserWithRefreshToken(UserEntity userEntity) {
        return createSignedInUser(userEntity).refreshToken(createRefreshToken(userEntity));
    }

    private String createRefreshToken(UserEntity user) {
        String token = RandomHolder.randomKey(128);
        userTokenRepository.save(new UserTokenEntity().setRefreshToken(token).setUser(user));
        return token;
    }

    private SignedInUser createSignedInUser(UserEntity userEntity) {
        String token = tokenManager.create(org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(Objects.nonNull(userEntity.getRole()) ? userEntity.getRole().name() : "")
                .build());
        return new SignedInUser().username(userEntity.getUsername()).accessToken(token)
                .userId(userEntity.getId().toString());
    }

    @Override
    @Transactional
    public SignedInUser getSignedInUser(UserEntity userEntity) {
        userTokenRepository.deleteByUserId(userEntity.getId());
        return createSignedUserWithRefreshToken(userEntity);
    }

    @Override
    public Optional<SignedInUser> getAccessToken(RefreshToken refreshToken) {
        // You may add an additional validation for time that would remove/invalidate the refresh token
        return userTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
                .map(ut -> Optional.of(createSignedInUser(ut.getUser()).refreshToken(refreshToken.getRefreshToken())))
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid token."));
    }

    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
        userTokenRepository.findByRefreshToken(refreshToken.getRefreshToken())
                .ifPresentOrElse(userTokenRepository::delete, () -> {
                    throw new InvalidRefreshTokenException("Invalid token.");
                });
    }

    private UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userEntity;
    }

    // https://stackoverflow.com/a/31214709/109354
    // or can use org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(n)
    private static class RandomHolder {
        static final Random random = new SecureRandom();
        public static String randomKey(int length) {
            return String.format("%"+length+"s", new BigInteger(length*5/*base 32,2^5*/, random)
                    .toString(32)).replace('\u0020', '0');
        }
    }
}
