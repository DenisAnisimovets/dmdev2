package com.danis.mapper;

import com.danis.dto.GoodUpdateDto;
import com.danis.entity.Good;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
public class GoodUpdateMapper implements Mapper<GoodUpdateDto, Good> {

    @Override
    public Good map(GoodUpdateDto object) {
        Good good = new Good();
        copy(object, good);
        return good;
    }

    @Override
    public Good map(GoodUpdateDto fromObject, Good toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(GoodUpdateDto object, Good good) {
        good.setId(object.getId());
        good.setGoodName(object.getGoodName());
        good.setPrice(object.getPrice());
        good.setQuantity(object.getQuantity());
        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> good.setImage(image.getOriginalFilename()));
    }
}
