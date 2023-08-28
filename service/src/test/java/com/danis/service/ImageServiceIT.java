package com.danis.service;

import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertArrayEquals;

@RequiredArgsConstructor
class ImageServiceIT extends TestBase {

    private final ImageService imageService;

    @Test
    public void uploadSuccessful() throws IOException {
        // Arrange
        String bucket = imageService.getBucket();
        String imagePath = "test";
        byte[] imageData = new byte[]{1};
        InputStream inputStream = new ByteArrayInputStream(imageData);
        Path imagePathOnDisk1 = Path.of(bucket, imagePath);
        MockMultipartFile multipartFile = new MockMultipartFile(imagePathOnDisk1.toString(), imagePath, MediaType.APPLICATION_OCTET_STREAM_VALUE, imageData);

        // Act
        imageService.upload(multipartFile.getOriginalFilename(), inputStream);

        // Assert
        Path imagePathOnDisk = Path.of(bucket, imagePath);
        byte[] uploadedImageData = Files.readAllBytes(imagePathOnDisk);
        assertArrayEquals(imageData, uploadedImageData);
    }
}