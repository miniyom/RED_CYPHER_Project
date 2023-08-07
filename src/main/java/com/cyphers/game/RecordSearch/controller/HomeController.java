package com.cyphers.game.RecordSearch.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class HomeController {


    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
