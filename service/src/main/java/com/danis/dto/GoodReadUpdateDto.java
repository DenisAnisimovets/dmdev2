package com.danis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class GoodReadUpdateDto {
    Long id;
    String goodName;
    Integer price;
    Integer quantity;
    String img;
}
