package com.danis.dto;

import lombok.Value;

@Value
public class GoodCreateEditDto {
    Long id;
    String goodName;
    Integer price;
    Integer quantity;
    String img;
}
