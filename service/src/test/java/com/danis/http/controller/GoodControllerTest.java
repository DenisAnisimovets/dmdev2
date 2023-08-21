package com.danis.http.controller;

import com.danis.util.TestBase;
import lombok.RequiredArgsConstructor;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class GoodControllerTest extends TestBase {

    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/goods"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("good/goods"))
                .andExpect(model().attributeExists("goods"))
                .andExpect(model().attribute("goods", IsCollectionWithSize.hasSize(5)));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/goods")
                        .param("goodName", "Salt")
                        .param("price", String.valueOf(10))
                        .param("quantity", String.valueOf(100))
                        .param("img", "")

                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/goods/{\\d+}")
                );
    }
}