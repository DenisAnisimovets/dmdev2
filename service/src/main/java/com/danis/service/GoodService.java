package com.danis.service;

import com.danis.dto.GoodCreateDto;
import com.danis.dto.GoodReadUpdateDto;
import com.danis.mapper.GoodCreateMapper;
import com.danis.mapper.GoodReadUpdateMapper;
import com.danis.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepository goodRepository;
    private final GoodReadUpdateMapper goodReadUpdateMapper;
    private final GoodCreateMapper goodCreateMapper;

    public List<GoodReadUpdateDto> findAll() {
        return goodRepository.findAll()
                .stream()
                .map(goodReadUpdateMapper::map).collect(toList());
    }

//    public Page<GoodReadDto> findAll(GoodFilter filter, Pageable pageable) {
//        var predicate = QPredicate.builder()
//                .add(filter.goodName(), good.goodName::containsIgnoreCase)
//                .buildOr();
//
//        return goodRepository.findAll(predicate, pageable)
//                .map(goodReadMapper::map);
//    }

    public Optional<GoodReadUpdateDto> findById(Long id) {
        return goodRepository.findById(id)
                .map(goodReadUpdateMapper::map);
    }

    @Transactional
    public Optional<GoodReadUpdateDto> update(Long id, GoodCreateDto goodCreateEditDto) {
        return goodRepository.findById(id)
                .map(entity -> goodCreateMapper.map(goodCreateEditDto, entity))
                .map(goodRepository::saveAndFlush)
                .map(goodReadUpdateMapper::map);
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
    public GoodReadUpdateDto create(GoodCreateDto good) {
        return Optional.of(good)
                .map(goodCreateMapper::map)
                .map(goodRepository::save)
                .map(goodReadUpdateMapper::map)
                .orElseThrow();
    }
}
