package com.danis.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@WithMockUser(username = "test@gmail.com", password = "test", authorities = "ADMIN")
public class TestBase {

    @DynamicPropertySource
    private static void SetProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> TestUtil.postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> TestUtil.postgres.getUsername());
        registry.add("spring.datasource.password", () -> TestUtil.postgres.getPassword());
        registry.add("app.image.bucket", () -> System.getProperty("java.io.tmpdir"));
    }
}
