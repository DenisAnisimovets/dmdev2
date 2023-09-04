package com.danis.mapper;

import com.danis.dto.UserUpdateDto;
import com.danis.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class UserUpdateMapper implements Mapper<UserUpdateDto, User>{

    @Override
    public User map(UserUpdateDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    @Override
    public User map(UserUpdateDto fromObject, User toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(UserUpdateDto object, User user) {
        user.setUsername(object.getUsername());
        user.setId(object.getId());
        user.setRole(object.getRole());
        user.setFirstname(object.getFirstname());
        user.setLastname(object.getLastname());
        user.setBirthDate(object.getBirthDate());

        Optional.ofNullable(object.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> user.setImage(image.getOriginalFilename()));
    }
}
