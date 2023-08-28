package com.danis.dto;

import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class GoodUpdateDto {
    Long id;
    @NotEmpty
    @Size(min = 3, max = 64)
    String goodName;
    @Min(value = 1)
    Integer price;
    Integer quantity;
    MultipartFile image;
}
