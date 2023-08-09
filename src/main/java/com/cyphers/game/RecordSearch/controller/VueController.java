package com.cyphers.game.RecordSearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VueController {

    @RequestMapping({ "/user/**", "/anotherPath/**", "/*" }) // 여기에 필요한 경로를 추가하세요.
    public String index() {
        return "forward:/index.html";
    }

}

