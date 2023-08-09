package com.cyphers.game.RecordSearch.controller;

import com.cyphers.game.RecordSearch.model.CrsUser;
import com.cyphers.game.RecordSearch.service.user.CrsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final CrsUserService userService;

    @GetMapping("/add/{userId}")
    public String add(@PathVariable("userId") String userId) {
    	log.info("/add/{}, {}, {}",userId, userId, userId);
    	System.out.println("/add/"+userId+", "+userId+", "+userId);
        userService.register(userId, "test");
        return "사용자 생성이 완료되었습니다. ";
    }

    @GetMapping("/update/{userId}/{nickname}")
    public String update(@PathVariable("userId") String userId,
                         @PathVariable("nickname") String nickname) {
        Optional<CrsUser> user = userService.getUser(userId);
        if (user.isEmpty()) {
            return "사용자가 없습니다";
        }
        CrsUser crsUser = user.get();
        crsUser.setNickname(nickname);
        userService.upsertUser(crsUser);
        return "닉네임 설정이 완료되었습니다." ;
    }

    @GetMapping("/remove/{userId}")
    public String remove(@PathVariable("userId") String userId) {
        Optional<CrsUser> user = userService.getUser(userId);
        if (user.isEmpty()) {
            return "사용자가 없습니다";
        }
        CrsUser crsUser = user.get();
        userService.delete(crsUser);
        return "사용자 삭제가 완료되었습니다.";
    }

    @GetMapping("/all")
    public List<CrsUser> all() {
        return userService.getUserList();
    }

    @GetMapping("/get/{userId}")
    public CrsUser get(@PathVariable("userId") String userId) {
        Optional<CrsUser> user = userService.getUser(userId);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }
}
