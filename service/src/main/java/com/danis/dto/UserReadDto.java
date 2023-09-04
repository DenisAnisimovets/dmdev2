package com.danis.dto;

import com.danis.entity.Role;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@Builder
public class UserReadDto {

    Long id;
    @Email
    String username;
    @Size(min = 3)
    String firstname;
    String lastname;
    LocalDate birthDate;
    String image;
    Role role;
}
