package com.danis.http.controller;

import com.danis.dto.GoodReadDto;
import com.danis.service.ImageService;
import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class GoodControllerIT extends TestBase {

    private final MockMvc mockMvc;
    @MockBean
    private final ImageService imageService;
    private static final long goodId = 1L;
    private static final long goodIdNotExisting = 10000000000L;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/goods"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("good/goods"))
                .andExpect(model().attributeExists("goods"))
                .andExpect(model().attribute("goods", IsCollectionWithSize.hasSize(5)));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/goods/" + goodId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("good/good"))
                .andExpect(model().attributeExists("good"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/goods")
                        .param("goodName", "Salt")
                        .param("price", String.valueOf(10))
                        .param("quantity", String.valueOf(100))
                        .param("image", "")

                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/goods/{\\d+}")
                );
    }

    @Test
    void update() throws Exception {
        doNothing().when(imageService).upload(any(String.class), any());
        GoodReadDto expectedGoodReadDto = GoodReadDto.builder()
                .id(goodId)
                .image("\\goods\\" + goodId)
                .price(999)
                .quantity(555)
                .goodName("New GoodName")
                .build();
        var multipartFile = new MockMultipartFile(expectedGoodReadDto.getImage(),
                expectedGoodReadDto.getImage(),
                "content",
                "InputStream".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/goods/" + goodId + "/update")
                        .file("image", multipartFile.getBytes())
                        .param("goodName", expectedGoodReadDto.getGoodName())
                        .param("price", expectedGoodReadDto.getPrice().toString())
                        .param("quantity",  expectedGoodReadDto.getQuantity().toString())
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/goods/{\\d?}"),
                        view().name("redirect:/goods/{id}")
                );

        mockMvc.perform(get("/goods/" + goodId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("good/good"))
                .andExpect(model().attributeExists("good"))
                .andExpect(model().attribute("good", expectedGoodReadDto)
                );

    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/" + goodId + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/goods")
                );
    }

    @Test
    void deleteNotExisting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/" + goodIdNotExisting + "/delete"))
                .andExpect(status().is4xxClientError()
                );
    }
}