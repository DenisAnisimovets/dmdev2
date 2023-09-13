package com.danis.service;

import com.danis.dto.UserCreateDto;
import com.danis.dto.UserReadDto;
import com.danis.dto.UserUpdateDto;
import com.danis.mapper.UserCreateMapper;
import com.danis.mapper.UserReadMapper;
import com.danis.mapper.UserUpdateMapper;
import com.danis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserCreateMapper userCreateMapper;
    private final ImageService imageService;
    @Value("${app.image.bucket-for-users}")
    private final String bucket;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::map)
                .collect(toList());
    }

    public Optional<UserReadDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserUpdateDto userUpdateDto) {
        return userRepository.findById(id)
                .map(user -> userUpdateMapper.map(userUpdateDto, user))
                .map(userRepository::saveAndFlush)
                .map(it -> {
                    String pathToSave = bucket + it.getId();
                    uploadImage(userUpdateDto.getImage(), pathToSave);
                    it.setImage(pathToSave);
                    return userReadMapper.map(it);
                });
    }

    @Transactional
    public UserReadDto create(UserCreateDto userDto) {
        return Optional.of(userDto)
                .map(dto -> userCreateMapper.map(dto))
                .map(userRepository::save)
                .map(it -> {
                    String pathToSave = bucket + it.getId();
                    uploadImage(userDto.getImage(), pathToSave);
                    it.setImage(pathToSave);
                    return userReadMapper.map(it);
                })
                .orElseThrow();
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image, String pathToSave) {
        if(!image.isEmpty()) {
            imageService.upload(pathToSave, image.getInputStream());
        }
    }

    public Optional<byte[]> findAvatar(Long id) {
        return userRepository.findById(id)
                .map(com.danis.entity.User::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }
}
