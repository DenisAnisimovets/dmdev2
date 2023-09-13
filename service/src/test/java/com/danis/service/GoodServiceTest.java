package com.danis.service;

import com.danis.dto.GoodReadDto;
import com.danis.entity.Good;
import com.danis.mapper.GoodCreateMapper;
import com.danis.mapper.GoodReadMapper;
import com.danis.repository.GoodRepository;
import com.danis.util.EntityTestUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class GoodServiceTest {

    @InjectMocks
    private GoodService goodService;

    @Mock
    private GoodRepository goodRepository;

    @Mock
    private GoodReadMapper goodReadMapper;

    @Mock
    private GoodCreateMapper goodCreateMapper;

    long goodId = 1L;

    @Test
    void findById() {
        // Given
        Good good = Good.builder()
                .goodName("Good")
                .price(10)
                .quantity(35)
                .build();
        GoodReadDto goodReadDto = GoodReadDto.builder()
                .goodName("Good")
                .price(10)
                .quantity(35).build();
        when(goodRepository.findById(goodId)).thenReturn(Optional.of(good));
        doReturn(goodReadDto).when(goodReadMapper).map(good);

        // When
        Optional<GoodReadDto> maybeGoodReadDto = goodService.findById(goodId);

        // Then
        assertThat(maybeGoodReadDto.get());
        GoodReadDto actualGoodReadDto = maybeGoodReadDto.get();
        assertEquals(good.getId(), actualGoodReadDto.getId());
        assertEquals(good.getGoodName(), actualGoodReadDto.getGoodName());
        assertEquals(good.getPrice(), actualGoodReadDto.getPrice());
        assertEquals(good.getQuantity(), actualGoodReadDto.getQuantity());

        verify(goodRepository, times(1)).findById(goodId);
    }

    @Test
    void findAll() {
        // Given
        Good good1 = EntityTestUtil.createGood("Good1");
        GoodReadDto goodReadDto1 = EntityTestUtil.createGoodReadDto(good1);
        Good good2 = EntityTestUtil.createGood("Good2");
        GoodReadDto goodReadDto2 = EntityTestUtil.createGoodReadDto(good2);

        when(goodRepository.findAll()).thenReturn(List.of(good1, good2));
        doReturn(goodReadDto1).when(goodReadMapper).map(good1);
        doReturn(goodReadDto2).when(goodReadMapper).map(good2);

        // When
        List<GoodReadDto> allGoods = goodService.findAll();

        // Then
        assertThat(allGoods).hasSize(2);
        assertThat(allGoods).contains(goodReadDto1, goodReadDto2);

        verify(goodRepository, times(1)).findAll();
    }

    @Test
    void delete() {
        // Given
        Good good = EntityTestUtil.createGood("Good");
        good.setId(goodId);

        doReturn(Optional.of(good)).when(goodRepository).findById(good.getId());

        // When
        boolean deletedGood  = goodService.delete(good.getId());

        // Then
        assertTrue(deletedGood);
        verify(goodRepository).delete(good);
        verify(goodRepository).flush();
    }
}