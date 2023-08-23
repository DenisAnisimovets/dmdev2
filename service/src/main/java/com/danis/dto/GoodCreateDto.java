package com.danis.dto;

import lombok.Value;

@Value
public class GoodCreateDto {
    String goodName;
    Integer price;
    Integer quantity;
    String img;
}
