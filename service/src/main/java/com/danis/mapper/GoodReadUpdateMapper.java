package com.danis.mapper;

import com.danis.dto.GoodReadUpdateDto;
import com.danis.entity.Good;
import org.springframework.stereotype.Component;

@Component
public class GoodReadUpdateMapper implements Mapper<Good, GoodReadUpdateDto> {

    @Override
    public GoodReadUpdateDto map(Good good) {
        return new GoodReadUpdateDto(
                good.getId(),
                good.getGoodName(),
                good.getPrice(),
                good.getQuantity(),
                good.getImg()
        );
    }

}
