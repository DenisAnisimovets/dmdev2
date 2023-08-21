package com.danis.service;

import com.danis.dto.GoodReadDto;
import com.danis.entity.Good;
import com.danis.mapper.GoodCreateEditMapper;
import com.danis.mapper.GoodReadMapper;
import com.danis.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    private GoodCreateEditMapper goodCreateEditMapper;

    @Test
    public void testFindById() {
        // Arrange
        long goodId = 1L;
        Good good = Good.builder().
                goodName("Good")
                .price(10)
                .quantity(35)
                .build();
        Mockito.when(goodRepository.findById(goodId)).thenReturn(Optional.of(good));

        // Act
        Optional<GoodReadDto> maybeGoodReadDto = goodService.findById(goodId);

        // Assert
        Assertions.assertThat(maybeGoodReadDto.get());
        GoodReadDto actualGoodReadDto = maybeGoodReadDto.get();
        Assert.assertEquals(good.getId(), actualGoodReadDto.getId());
        Assert.assertEquals(good.getGoodName(), actualGoodReadDto.getGoodName());
        Assert.assertEquals(good.getPrice(), actualGoodReadDto.getPrice());
        Assert.assertEquals(good.getQuantity(), actualGoodReadDto.getQuantity());

        Mockito.verify(goodRepository, Mockito.times(1)).findById(goodId);
    }
}