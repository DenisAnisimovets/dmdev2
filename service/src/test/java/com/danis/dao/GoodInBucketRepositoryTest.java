package com.danis.dao;

import com.danis.entity.Good;
import com.danis.entity.GoodInBucket;
import com.danis.entity.User;
import com.danis.util.EntityTestUtil;
import com.danis.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoodInBucketRepositoryTest {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;
    private static UserRepository userRepository;
    private static GoodRepository goodRepository;
    private static GoodInBucketRepository goodInBucketRepository;

    @BeforeAll
    static void beforeAll() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        entityManager = HibernateTestUtil.buildSessionProxy(sessionFactory);
        userRepository = new UserRepository(entityManager);
        goodRepository = new GoodRepository(entityManager);
        goodInBucketRepository = new GoodInBucketRepository(entityManager);
    }

    @BeforeEach
    void setUp() {
        goodInBucketRepository.getEntityManager().getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        goodInBucketRepository.getEntityManager().getTransaction().rollback();
    }

    @Test
    void createGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInBucket");
        Good good = EntityTestUtil.createGood("GoodForGoodInOBucket");
        userRepository.save(user);
        goodRepository.save(good);

        GoodInBucket goodInBucket = EntityTestUtil.createGoodInBucket(user, good);
        goodInBucketRepository.save(goodInBucket);

        assertNotNull(goodInBucket.getId());
    }

    @Test
    void readGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInBucket");
        Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
        userRepository.save(user);
        goodRepository.save(good);

        GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
        goodInBucketRepository.save(expectedGoodInBucket);
        entityManager.clear();

        Optional<GoodInBucket> actualGoodInBucket = goodInBucketRepository.findById(expectedGoodInBucket.getId());
        assertThat(actualGoodInBucket).isNotEmpty();
        assertThat(actualGoodInBucket.get()).isEqualTo(expectedGoodInBucket);
    }

    @Test
    void updateGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInBucket");
        Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
        userRepository.save(user);
        goodRepository.save(good);

        GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
        goodInBucketRepository.save(expectedGoodInBucket);
        Integer expectedQuantity = Integer.valueOf(1000000);
        expectedGoodInBucket.setQuantity(expectedQuantity);
        entityManager.flush();
        entityManager.clear();

        Optional<GoodInBucket> actualGoodInBucket = goodInBucketRepository.findById(expectedGoodInBucket.getId());
        assertThat(actualGoodInBucket).isNotEmpty();
        assertThat(actualGoodInBucket.get().getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    void deleteGoodInOrder() {
        User user = EntityTestUtil.createUser("UserForGoodInBucket");
        Good good = EntityTestUtil.createGood("GoodForGoodInBucket");
        userRepository.save(user);
        goodRepository.save(good);

        GoodInBucket expectedGoodInBucket = EntityTestUtil.createGoodInBucket(user, good);
        goodInBucketRepository.save(expectedGoodInBucket);
        goodInBucketRepository.delete(expectedGoodInBucket.getId());
        entityManager.flush();
        entityManager.clear();

        Optional<GoodInBucket> actualGoodInBucket = goodInBucketRepository.findById(expectedGoodInBucket.getId());
        assertThat(actualGoodInBucket).isEmpty();
    }

    @Test
    void findAll() {
        User user = EntityTestUtil.createUser("UserForGoodInBucket");
        Good good1 = EntityTestUtil.createGood("GoodForGoodInBucket1");
        Good good2 = EntityTestUtil.createGood("GoodForGoodInBucket2");
        userRepository.save(user);
        goodRepository.save(good1);
        goodRepository.save(good2);
        GoodInBucket goodInBucket1 = EntityTestUtil.createGoodInBucket(user, good1);
        GoodInBucket goodInBucket2 = EntityTestUtil.createGoodInBucket(user, good2);
        goodInBucketRepository.save(goodInBucket1);
        goodInBucketRepository.save(goodInBucket2);

        List<GoodInBucket> allGoodsInBucket = goodInBucketRepository.findAll();

        assertThat(allGoodsInBucket).hasSize(2);
        assertTrue(allGoodsInBucket.contains(goodInBucket1));
        assertTrue(allGoodsInBucket.contains(goodInBucket2));
    }

}