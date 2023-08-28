package com.danis.http.rest;

import com.danis.service.ImageService;
import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class GoodRestControllerIT extends TestBase {

    private final MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    void findAvatar() throws Exception {
        long goodId = 1L;
        byte[] avatarData = new byte[]{1};
        String path = "/api/v1/goods/" + goodId + "/avatar";
        Mockito.doReturn(Optional.of(avatarData)).when(imageService).get(path);

        mockMvc.perform(get(path))
                .andExpect(status().is2xxSuccessful())
                .andExpect(result -> result.equals(avatarData))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE));

        verify(imageService, times(1)).get(path);
    }
}