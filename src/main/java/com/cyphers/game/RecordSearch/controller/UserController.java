package com.cyphers.game.RecordSearch.controller;

import com.cyphers.game.RecordSearch.cyphers.CyphersApiService;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersWordType;
import com.cyphers.game.RecordSearch.model.CrsUser;
import com.cyphers.game.RecordSearch.service.user.CrsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final CrsUserService userService;
    private final CyphersApiService cyphersApiService;

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
    public CrsUser get(@PathVariable("userId") String userId) throws URISyntaxException, IOException {
        Optional<CrsUser> user = userService.getUser(userId);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    @GetMapping("/find/cyphers/{nickname}")
    public CyphersPlayerResponse findCyphersUsers(@PathVariable("nickname") String nickname) throws Exception {
        CyphersPlayerResponse cyphersPlayerResponse = cyphersApiService.searchPlayers(nickname, null, null);
        CyphersPlayerResponse cyphersPlayerResponse2 = cyphersApiService.searchPlayers(nickname, CyphersWordType.MATCH, 100);
        CyphersPlayerResponse cyphersPlayerResponse3 = cyphersApiService.searchPlayers(nickname, CyphersWordType.FULL, 100);
        CyphersPlayerResponse cyphersPlayerResponse4 = cyphersApiService.searchPlayers(nickname, CyphersWordType.FULL, 1);
        return cyphersPlayerResponse;
    }

    @GetMapping("/find/id/{playerId}")
    public CyphersPlayerInfo findCyphersUserInfo(@PathVariable("playerId") String playerId) throws Exception {
        CyphersPlayerInfo res = cyphersApiService.searchPlayerInfo(playerId);
        return res;
    }
}
