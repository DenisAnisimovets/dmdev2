package com.danis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Value
@Builder
@AllArgsConstructor
public class GoodReadDto {
    Long id;
    @Size(min = 3, max = 64)
    String goodName;
    @Min(value = 1)
    Integer price;
    Integer quantity;
    String image;
}
