package com.danis.http.controller;

import com.danis.dto.GoodCreateEditDto;
import com.danis.dto.GoodFilter;
import com.danis.dto.GoodReadDto;
import com.danis.dto.PageResponse;
import com.danis.service.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodController {
    private final GoodService goodService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("goods", goodService.findAll());
        return "good/goods";
    }

//    @GetMapping
//    public String findAll(Model model, GoodFilter filter, Pageable pageable) {
//        Page<GoodReadDto> page = goodService.findAll(filter, pageable);
//        model.addAttribute("goods", PageResponse.of(page));
//        model.addAttribute("filter", filter);
//        return "good/goods";
//    }

    @GetMapping({"/{id}"})
    public String findById(@PathVariable("id") Long id, Model model) {
        return goodService.findById(id).map(goodReadDto -> {
            model.addAttribute("good", goodReadDto);
            return "good/good";
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, @ModelAttribute GoodCreateEditDto goodCreateEditDto) {
        return goodService.update(id, goodCreateEditDto)
                .map(it -> "redirect:/goods/{id}")
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if(!goodService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/goods";
    }

    @PostMapping
    public String create(@ModelAttribute GoodCreateEditDto good, RedirectAttributes redirectAttributes) {
        return "redirect:/goods/" + goodService.create(good).getId();
    }
}
