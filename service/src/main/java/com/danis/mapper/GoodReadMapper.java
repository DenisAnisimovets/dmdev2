package com.danis.mapper;

import com.danis.dto.GoodReadDto;
import com.danis.entity.Good;
import org.springframework.stereotype.Component;

@Component
public class GoodReadMapper implements Mapper<Good, GoodReadDto> {

    @Override
    public GoodReadDto map(Good good) {
        return new GoodReadDto(
                good.getId(),
                good.getGoodName(),
                good.getPrice(),
                good.getQuantity(),
                good.getImage()
        );
    }


    public Good merge (GoodReadDto fromObject, Good toObject) {
        return new Good(
                fromObject.getId(),
                fromObject.getGoodName(),
                fromObject.getPrice(),
                fromObject.getQuantity(),
                fromObject.getImage()
        );
    }
}
