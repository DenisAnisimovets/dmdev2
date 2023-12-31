package com.danis.mapper;

import com.danis.dto.GoodCreateDto;
import com.danis.entity.Good;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
public class GoodCreateMapper implements Mapper<GoodCreateDto, Good> {

    @Override
    public Good map(GoodCreateDto object) {
        Good good = new Good();
        copy(object, good);
        return good;
    }

    @Override
    public Good map(GoodCreateDto fromObject, Good toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(GoodCreateDto object, Good good) {
        good.setGoodName(object.getGoodName());
        good.setPrice(object.getPrice());
        good.setQuantity(object.getQuantity());
        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> good.setImage(image.getOriginalFilename()));

    }
}
