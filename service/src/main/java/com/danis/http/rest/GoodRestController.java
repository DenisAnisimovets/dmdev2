package com.danis.http.rest;

import com.danis.service.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/goods")
@RequiredArgsConstructor
public class GoodRestController {

    private final GoodService goodService;

//    @GetMapping(value = "/id/avatar")
//    public ResponseEntity<byte[]> findAvatar(@PathVariable("id") Long id) {
//        return goodService.findAvatar(id)
//                .map(content -> ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
//                        .contentLength(content.length)
//                        .body(content))
//                .orElseGet(notFound()::build);
//    }

    @GetMapping(value = "/{id}/avatar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findAvatar(@PathVariable("id") Long id) {
        return goodService.findAvatar(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
