package com.danis.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class TestBase {

    @DynamicPropertySource
    private static void SetProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> TestUtil.postgres.getJdbcUrl());
        registry.add("spring.datasource.username", () -> TestUtil.postgres.getUsername());
        registry.add("spring.datasource.password", () -> TestUtil.postgres.getPassword());
    }
}
