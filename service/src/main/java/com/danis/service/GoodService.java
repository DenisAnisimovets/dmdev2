package com.danis.service;

import com.danis.dto.GoodCreateEditDto;
import com.danis.dto.GoodReadDto;
import com.danis.mapper.GoodCreateEditMapper;
import com.danis.mapper.GoodReadMapper;
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
    private final GoodReadMapper goodReadMapper;
    private final GoodCreateEditMapper goodCreateEditMapper;

    public List<GoodReadDto> findAll() {
        return goodRepository.findAll().stream()
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
        return goodRepository.findById(id).map(goodReadMapper::map);
    }

    @Transactional
    public Optional<GoodReadDto> update(Long id, GoodCreateEditDto goodCreateEditDto) {
        return goodRepository.findById(id)
                .map(entity -> goodCreateEditMapper.map(goodCreateEditDto, entity))
                .map(goodRepository::saveAndFlush)
                .map(goodReadMapper::map);
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
    public GoodReadDto create(GoodCreateEditDto good) {
        return Optional.of(good)
                .map(goodCreateEditMapper::map)
                .map(goodRepository::save)
                .map(goodReadMapper::map)
                .orElseThrow();
    }
}
