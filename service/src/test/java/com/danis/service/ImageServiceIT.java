package com.danis.service;

import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@RequiredArgsConstructor
class ImageServiceIT extends TestBase {

    @Value("${app.image.bucket}")
    private final String bucket;

    private final ImageService imageService;

    @Test
    void uploadSuccessful() throws IOException {
        // Given
        String imagePath = "test";
        byte[] imageData = new byte[]{1};
        InputStream inputStream = new ByteArrayInputStream(imageData);

        // When
        imageService.upload(imagePath, inputStream);

        // Then
        Path imagePathOnDisk = Path.of(bucket, imagePath);
        byte[] uploadedImageData = Files.readAllBytes(imagePathOnDisk);
        assertArrayEquals(imageData, uploadedImageData);
    }
}