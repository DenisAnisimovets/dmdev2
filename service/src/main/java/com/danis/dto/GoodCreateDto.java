package com.danis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
@Builder
@AllArgsConstructor
public class GoodCreateDto {
    @NotEmpty
    @Size(min = 3, max = 64)
    String goodName;
    @Min(value = 1)
    Integer price;
    Integer quantity;
    MultipartFile image;
}
