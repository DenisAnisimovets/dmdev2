package com.danis.mapper;

import com.danis.dto.GoodCreateDto;
import com.danis.entity.Good;
import com.danis.util.EntityTestUtil;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class GoodCreateMapperTest {

    private final GoodCreateMapper goodCreateMapper = new GoodCreateMapper();

    @Test
    void map() {
        // Given
        GoodCreateDto createDto = EntityTestUtil.createGoodCreateDto(EntityTestUtil.createGood("GoodForTest"));

        // When
        Good actualGood = goodCreateMapper.map(createDto);

        // Then
        Good expectedGood = Good.builder()
                .goodName(createDto.getGoodName())
                .quantity(createDto.getQuantity())
                .price(createDto.getPrice())
                .build();

        Assertions.assertThat(actualGood).isEqualTo(expectedGood);
    }


}