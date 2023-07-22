package com.danis.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class OrderFilter {
    List<Long> ids;
    List<Long> userIds;
    Long minSum;
    Long maxSum;
}
