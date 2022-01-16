package no.bjornhjelle.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;


import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class SampleFlywayTest {

    @Autowired
    private JdbcTemplate template;

    @Test
    void testDefaultSettings() {
        assertThat(this.template.queryForObject("SELECT COUNT(*) from authdb.USERS", Integer.class)).isGreaterThan(0);
    }

}