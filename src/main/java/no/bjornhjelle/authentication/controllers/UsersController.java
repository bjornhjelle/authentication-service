package no.bjornhjelle.authentication.controllers;

import lombok.extern.slf4j.Slf4j;
import no.bjornhjelle.authentication.api.UsersApi;
import no.bjornhjelle.authentication.entities.UserEntity;
import no.bjornhjelle.authentication.models.User;
import no.bjornhjelle.authentication.services.CSVService;
import no.bjornhjelle.authentication.services.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UsersController implements UsersApi {

    private UserService userService;
    private CSVService csvService;

    public UsersController(UserService userService, CSVService csvService) {
        this.userService = userService;
        this.csvService = csvService;
    }

    @Override
    public ResponseEntity<User> getUser(String email) {
        log.info("Request for User with email address: {}", email);
        UserEntity userEntity = userService.findUserByEmail(email);
        return ResponseEntity.ok(userEntity.toUser());
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<Object> getUsersCsv() {
        String filename = "users.csv";
        InputStreamResource file = new InputStreamResource(csvService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

}
