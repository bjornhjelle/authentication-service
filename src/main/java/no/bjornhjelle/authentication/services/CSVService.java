package no.bjornhjelle.authentication.services;

import no.bjornhjelle.authentication.models.User;
import no.bjornhjelle.authentication.utils.CSVHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class CSVService {

    private UserService userService;

    public CSVService(UserService userService) {
        this.userService = userService;
    }

    public ByteArrayInputStream load() {
        List<User> users = userService.getAllUsers();

        ByteArrayInputStream in = CSVHelper.usersToCSV(users);
        return in;
    }
}
