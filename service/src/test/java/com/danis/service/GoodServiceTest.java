package com.danis.service;

import com.danis.dto.GoodReadUpdateDto;
import com.danis.entity.Good;
import com.danis.mapper.GoodCreateMapper;
import com.danis.mapper.GoodReadUpdateMapper;
import com.danis.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
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
    private GoodReadUpdateMapper goodReadUpdateMapper;

    @Mock
    private GoodCreateMapper goodCreateMapper;

    @Test
    public void findById() {
        // Arrange
        long goodId = 1L;
        Good good = Good.builder()
                .goodName("Good")
                .price(10)
                .quantity(35)
                .build();
        GoodReadUpdateDto goodReadUpdateDto = GoodReadUpdateDto.builder()
                .goodName("Good")
                .price(10)
                .quantity(35).build();
        when(goodRepository.findById(goodId)).thenReturn(Optional.of(good));
        doReturn(goodReadUpdateDto).when(goodReadUpdateMapper).map(good);

        // Act
        Optional<GoodReadUpdateDto> maybeGoodReadDto = goodService.findById(goodId);

        // Assert
        assertThat(maybeGoodReadDto.get());
        GoodReadUpdateDto actualGoodReadUpdateDto = maybeGoodReadDto.get();
        assertEquals(good.getId(), actualGoodReadUpdateDto.getId());
        assertEquals(good.getGoodName(), actualGoodReadUpdateDto.getGoodName());
        assertEquals(good.getPrice(), actualGoodReadUpdateDto.getPrice());
        assertEquals(good.getQuantity(), actualGoodReadUpdateDto.getQuantity());

        verify(goodRepository, times(1)).findById(goodId);
    }
}