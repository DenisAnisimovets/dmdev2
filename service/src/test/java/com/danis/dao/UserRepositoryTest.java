package com.danis.dao;

import com.danis.entity.User;
import com.danis.util.EntityTestUtil;
import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserRepositoryTest extends TestBase {

    private final UserRepository userRepository;

    @Test
    void createUser() {
        User user = EntityTestUtil.createUser("User for test");
        userRepository.save(user);

        assertNotNull(user.getId());
    }

    @Test
    void readUser() {
        User expectedUser = EntityTestUtil.createUser("User for test");

        userRepository.save(expectedUser);
        userRepository.getEntityManager().clear();

        Optional<User> actualUser = userRepository.findById(expectedUser.getId());
        assertTrue(actualUser.isPresent());
        assertThat(actualUser.get()).isEqualTo(expectedUser);
    }

    @Test
    void updateUser() {
        User expectedUser = EntityTestUtil.createUser("User for test");
        userRepository.save(expectedUser);

        expectedUser.setUsername("User after test");
        userRepository.getEntityManager().flush();
        userRepository.getEntityManager().clear();

        Optional<User> actualUser = userRepository.findById(expectedUser.getId());
        assertTrue(actualUser.isPresent());
        assertEquals("User after test", actualUser.get().getUsername());
    }

    @Test
    void deleteUser() {
        User expectedUser = EntityTestUtil.createUser("User for test");
        userRepository.save(expectedUser);

        userRepository.delete(expectedUser);
        userRepository.getEntityManager().flush();
        userRepository.getEntityManager().clear();

        assertTrue(userRepository.findById(expectedUser.getId()).isEmpty());
    }

    @Test
    void findAll() {
        User user1 = EntityTestUtil.createUser("User 1");
        User user2 = EntityTestUtil.createUser("User 2");
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(2);
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }
}