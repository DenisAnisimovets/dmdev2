package com.danis.dto;

import com.danis.entity.Role;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@Builder
public class UserUpdateDto {
    Long id;

    @Size(min = 3)
    String rawPassword;

    @Email
    String username;

    @Size(min = 3)
    String firstname;

    String lastname;
    LocalDate birthDate;
    MultipartFile image;
    Role role;
}
