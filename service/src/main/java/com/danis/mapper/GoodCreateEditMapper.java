package com.danis.mapper;

import com.danis.dto.GoodCreateEditDto;
import com.danis.entity.Good;
import org.springframework.stereotype.Component;

@Component
public class GoodCreateEditMapper implements Mapper<GoodCreateEditDto, Good> {

    @Override
    public Good map(GoodCreateEditDto object) {
        Good good = new Good();
        copy(object, good);
        return good;
    }

    @Override
    public Good map(GoodCreateEditDto fromObject, Good toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(GoodCreateEditDto object, Good good) {
        good.setGoodName(object.getGoodName());
        good.setPrice(object.getPrice());
        good.setQuantity(object.getQuantity());
        good.setImg(object.getImg());
    }
}
