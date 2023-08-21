package com.danis.dto;

import lombok.Value;

@Value
public class GoodReadDto {
    Long id;
    String goodName;
    Integer price;
    Integer quantity;
    String img;
}
