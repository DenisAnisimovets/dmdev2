package com.danis.service;

import com.danis.dto.GoodCreateDto;
import com.danis.dto.GoodReadDto;
import com.danis.dto.GoodUpdateDto;
import com.danis.entity.Good;
import com.danis.mapper.GoodCreateMapper;
import com.danis.mapper.GoodReadMapper;
import com.danis.mapper.GoodUpdateMapper;
import com.danis.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepository goodRepository;
    private final GoodReadMapper goodReadMapper;
    private final GoodUpdateMapper goodUpdateMapper;
    private final GoodCreateMapper goodCreateMapper;
    private final ImageService imageService;

    public List<GoodReadDto> findAll() {
        return goodRepository.findAll()
                .stream()
                .map(goodReadMapper::map).collect(toList());
    }

//    public Page<GoodReadDto> findAll(GoodFilter filter, Pageable pageable) {
//        var predicate = QPredicate.builder()
//                .add(filter.goodName(), good.goodName::containsIgnoreCase)
//                .buildOr();
//
//        return goodRepository.findAll(predicate, pageable)
//                .map(goodReadMapper::map);
//    }

    public Optional<GoodReadDto> findById(Long id) {
        return goodRepository.findById(id)
                .map(goodReadMapper::map);
    }

    @Transactional
    public Optional<GoodReadDto> update(Long id, GoodUpdateDto goodUpdateDto) {
        return goodRepository.findById(id)
                .map(good -> {
                    uploadImage(goodUpdateDto.getImage());
                    return goodUpdateMapper.map(goodUpdateDto, good);
                })
                .map(goodRepository::saveAndFlush)
                .map(goodReadMapper::map);
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    @Transactional
    public boolean delete(Long id) {
        return goodRepository.findById(id)
                .map(entity -> {
                    goodRepository.delete(entity);
                    goodRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public GoodReadDto create(GoodCreateDto goodCreateDto) {
        return Optional.of(goodCreateDto)
                .map(it -> {
                    uploadImage(goodCreateDto.getImage());
                    return goodCreateMapper.map(goodCreateDto);})
                .map(goodRepository::save)
                .map(goodReadMapper::map)
                .orElseThrow();
    }

    public Optional<byte[]> findAvatar(Long id) {
        return goodRepository.findById(id)
                .map(Good::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }
}
