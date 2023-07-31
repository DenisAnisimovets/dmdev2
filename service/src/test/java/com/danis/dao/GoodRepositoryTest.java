package com.danis.dao;

import com.danis.entity.Good;
import com.danis.util.EntityTestUtil;
import com.danis.util.TestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoodRepositoryTest extends TestBase {

    private static GoodRepository goodRepository;

    @BeforeAll
    static void beforeAllTest() {
        goodRepository = new GoodRepository(entityManager);
    }

    @Test
    void createGood() {
        Good good = EntityTestUtil.createGood("Good for test");
        goodRepository.save(good);

        assertNotNull(good.getId());
    }

    @Test
    void readGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");

        goodRepository.save(expectedGood);
        goodRepository.getEntityManager().clear();

        Optional<Good> actualGood = goodRepository.findById(expectedGood.getId());
        assertTrue(actualGood.isPresent());
        assertThat(actualGood.get()).isEqualTo(expectedGood);
    }

    @Test
    void updateGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");
        goodRepository.save(expectedGood);

        expectedGood.setGoodName("Good after test");
        goodRepository.getEntityManager().flush();
        goodRepository.getEntityManager().clear();

        Optional<Good> actualGood = goodRepository.findById(expectedGood.getId());
        assertTrue(actualGood.isPresent());
        assertEquals("Good after test", actualGood.get().getGoodName());
    }

    @Test
    void deleteGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");
        goodRepository.save(expectedGood);

        goodRepository.delete(expectedGood);
        goodRepository.getEntityManager().flush();
        goodRepository.getEntityManager().clear();

        assertTrue(goodRepository.findById(expectedGood.getId()).isEmpty());
    }

    @Test
    void findAll() {
        Good good1 = EntityTestUtil.createGood("Good for test 1");
        Good good2 = EntityTestUtil.createGood("Good for test 2");
        goodRepository.save(good1);
        goodRepository.save(good2);

        List<Good> allGoods = goodRepository.findAll();

        assertThat(allGoods).hasSize(2);
        assertTrue(allGoods.contains(good1));
        assertTrue(allGoods.contains(good2));
    }
}