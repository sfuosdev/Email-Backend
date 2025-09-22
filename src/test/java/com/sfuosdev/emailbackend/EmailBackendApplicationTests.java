package com.sfuosdev.emailbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.mail.host=",
    "spring.mail.username=",
    "spring.mail.password="
})
class EmailBackendApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the Spring Boot application context loads successfully
    }
}