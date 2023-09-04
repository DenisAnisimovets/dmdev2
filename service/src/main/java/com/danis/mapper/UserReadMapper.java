package com.danis.mapper;

import com.danis.dto.UserReadDto;
import com.danis.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User object) {
        return UserReadDto.builder()
                .id(object.getId())
                .birthDate(object.getBirthDate())
                .username(object.getUsername())
                .firstname(object.getFirstname())
                .lastname(object.getLastname())
                .image(object.getImage())
                .role(object.getRole())
                .build();
    }
}
