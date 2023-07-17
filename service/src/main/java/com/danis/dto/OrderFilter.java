package com.danis.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class OrderFilter {
    List<Long> idList;
    List<Long> userIdList;
    Long minValue;
    Long maxValue;
}
