package com.danis.dao;

import com.danis.entity.Good;
import com.danis.util.EntityTestUtil;
import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class GoodRepositoryTest extends TestBase {

    private final EntityManager entityManager;
    private final GoodRepository goodRepository;

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
        entityManager.clear();

        Optional<Good> actualGood = goodRepository.findById(expectedGood.getId());
        assertTrue(actualGood.isPresent());
        assertThat(actualGood.get()).isEqualTo(expectedGood);
    }

    @Test
    void updateGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");
        goodRepository.save(expectedGood);

        expectedGood.setGoodName("Good after test");
        entityManager.flush();
        entityManager.clear();

        Optional<Good> actualGood = goodRepository.findById(expectedGood.getId());
        assertTrue(actualGood.isPresent());
        assertEquals("Good after test", actualGood.get().getGoodName());
    }

    @Test
    void deleteGood() {
        Good expectedGood = EntityTestUtil.createGood("Good for test");
        goodRepository.save(expectedGood);

        goodRepository.delete(expectedGood);
        entityManager.flush();
        entityManager.clear();

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